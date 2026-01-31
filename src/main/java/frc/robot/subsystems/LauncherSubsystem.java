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
    return (this.targetRPM > 0)
        && (Math.abs(this.targetRPM - getActualVelocity()) < LauncherConstants.kLauncherTolerance);
  }

  public boolean isBallPresent()
  {
    return m_hopperSensor.get();
  }

  public double calculateRPMFromLimeLight()
  {
    // 1. Get the vertical offset (ty) from the Limelight
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);

    // 2. Calculate distance using trigonometry
    // d = (targetHeight - cameraHeight) / tan(mountingAngle + ty)
    double distance = (LauncherConstants.kTargetHeight - LauncherConstants.kCameraHeight) / 
                      Math.tan(Math.toRadians(LauncherConstants.kMountAngle + ty));

    // 3. Convert distance to RPM using a simple formula or lookup table
    // Example: RPM = (Distance * 10) + 2000
    //double calculatedRPM = (distance * LauncherConstants.kRPMPerInch) + LauncherConstants.kBaseRPM;

    //return calculatedRPM;
    return 0.0;
  }
}