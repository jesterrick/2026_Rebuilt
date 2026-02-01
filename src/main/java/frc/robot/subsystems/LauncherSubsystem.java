// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.MathUtil;
import frc.robot.configs.LauncherConfigs;
import frc.robot.constants.CanIdConstants;
import frc.robot.constants.LauncherConstants;
import frc.robot.utils.VisionUtils;

public class LauncherSubsystem extends SubsystemBase {
  /** Creates a new LauncherSubsystem. */
  private final SparkMax m_LauncherMotor;
  private final SparkClosedLoopController m_ClosedLoopController;
  private double targetRPM;
  private final Timer m_emptyTimer = new Timer();
  private final DigitalInput m_hopperSensor = new DigitalInput(LauncherConstants.kLauncherIdleSensor);

  public LauncherSubsystem() {
    this.m_LauncherMotor = new SparkMax(CanIdConstants.kLauncherMotor, MotorType.kBrushless);
    this.m_ClosedLoopController = m_LauncherMotor.getClosedLoopController();
    this.m_LauncherMotor.configure(LauncherConfigs.config, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    this.targetRPM = 0.0;

    m_emptyTimer.start();
  }

  @Override
  public void periodic() {
    boolean ballPresent = isBallPresent();

    // 1. Manage the Timer
    if (ballPresent) {
        m_emptyTimer.reset();
        m_emptyTimer.stop();
    } else {
        m_emptyTimer.start();
    }

    // 2. Decision Engine
    if (this.getCurrentCommand() != null) {
        // While the button is held, the command (LauncherOn) controls the speed.
        // The timer is ignored because the driver wants to shoot.
    } 
    else if (!ballPresent && m_emptyTimer.hasElapsed(LauncherConstants.kWaitForEmptyTime)) {
        // Scenario A: Hopper is empty AND 3 seconds have passed. 
        // SHUT DOWN completely.
        this.targetRPM = 0.0;
    } 
    else if (ballPresent) {
        // Scenario B: Ball is sitting in the hopper, but we aren't shooting.
        // Stay at IDLE.
        this.targetRPM = LauncherConstants.kLauncherMotorSpeedIdle;
    }
    // Scenario C: Ball just left (timer < 3s) and no command is running.
    // The targetRPM will naturally stay at whatever it was last (High Speed) 
    // until the timer hits 3 seconds, then it will hit Scenario A and shut off.

    // 3. Actuator Output
    if (this.targetRPM > 0) {
        this.m_ClosedLoopController.setSetpoint(this.targetRPM, ControlType.kVelocity);
    } else {
        this.m_LauncherMotor.stopMotor();
    }

    // This sends two lines to a graph so you can see them overlap
    SmartDashboard.putNumber("Launcher/Target RPM", this.targetRPM);
    SmartDashboard.putNumber("Launcher/Actual RPM", this.getActualVelocity());

    // True if spinning and within tolerance; False if bogged down or stopped
    SmartDashboard.putBoolean("Launcher/At Speed", this.atSpeed());

    // Display if there is a ball in the hopper
    SmartDashboard.putBoolean("Launcher/Ball Present", this.isBallPresent());
  }

  /**
   * Sets the launcher to a specific velocity.
   * 
   * @param setRPM The desired speed (e.g., calculated from vision distance)
   */
  public void setLauncherVelocity(double setRPM) {
    this.targetRPM = setRPM;
  }

  public double getActualVelocity() {
    return this.m_LauncherMotor.getEncoder().getVelocity();
  }

  public void stopLauncher() {
    this.targetRPM = 0.0;
    this.m_LauncherMotor.stopMotor();
  }

  public boolean atSpeed() {
    // 1. Calculate the difference between actual and target
    boolean isNearTarget = Math.abs(this.targetRPM - getActualVelocity()) < LauncherConstants.kLauncherTolerance;

    // 2. Ensure the target itself is a "Launch" speed, not an "Idle" speed
    boolean isNotIdle = this.targetRPM > (LauncherConstants.kLauncherMotorSpeedIdle + LauncherConstants.kLaunchMinShotBuffer);

    return isNearTarget && isNotIdle;
}

  public boolean isBallPresent() {
    return m_hopperSensor.get();
  }

  public double calculateRPMFromLimeLight() {
    var table = NetworkTableInstance.getDefault().getTable("limelight");

    // Check for target valid AND correct ID
    boolean hasTarget = table.getEntry("tv").getDouble(0) == 1.0;
    int tagID = (int) table.getEntry("tid").getInteger(-1);

    if (hasTarget && VisionUtils.isTargetingCorrectHoop(tagID)) {
      double ty = table.getEntry("ty").getDouble(0);

      double distance = (LauncherConstants.kAprilTagHeight - LauncherConstants.kCameraHeight) /
          Math.tan(Math.toRadians(LauncherConstants.kMountAngle + ty));

      double calculatedRPM = (distance * LauncherConstants.kRPMPerInch) + LauncherConstants.kBaseRPM;
      // Clamps the speed between your minimum viable shot and your maximum safe speed
      return MathUtil.clamp(calculatedRPM, LauncherConstants.kLaunchMinRPM, LauncherConstants.kLaunchMaxRPM);
    } else {
      // IMPORTANT: Return IDLE if we lose the target.
      // This will cause atSpeed() to become FALSE immediately, stopping the feeder.
      return LauncherConstants.kLauncherMotorSpeedIdle;
    }
  }
}