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

  public LauncherSubsystem() {
    this.m_LauncherMotor = new SparkMax(CanIdConstants.kLauncherMotor, MotorType.kBrushless);
    this.m_ClosedLoopController = m_LauncherMotor.getClosedLoopController();
    this.m_LauncherMotor.configure(LauncherConfigs.config, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    this.targetRPM = 0.0;
  }

  @Override
  public void periodic() {
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
}