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
    // if there isn't any other command for the launcher
    if (this.getCurrentCommand() == null) {
      // if there is a ball in the hopper
      if (isBallPresent()) {
        this.targetRPM = LauncherConstants.kLauncherMotorSpeedIdle;
        m_emptyTimer.reset();
        m_emptyTimer.stop();
      } else {
        m_emptyTimer.start();

        // If 3 seconds pass without a ball, shut down to save battery
        if (m_emptyTimer.hasElapsed(LauncherConstants.kWaitForEmptyTime)) {
          this.targetRPM = 0.0;
        }
      }
    }

    // Only use the PID controller if we have a target speed.
    // Otherwise, let the motor be still or coast.
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

      return (distance * LauncherConstants.kRPMPerInch) + LauncherConstants.kBaseRPM;
    } else {
      // IMPORTANT: Return IDLE if we lose the target.
      // This will cause atSpeed() to become FALSE immediately, stopping the feeder.
      return LauncherConstants.kLauncherMotorSpeedIdle;
    }
  }
}