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

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.LauncherConfigs;
import frc.robot.constants.CanIdConstants;

public class LauncherSubsystem extends SubsystemBase {
  /** Creates a new LauncherSubsystem. */
  private final SparkMax m_LauncherMotor;
  private final SparkClosedLoopController m_ClosedLoopController;

  public LauncherSubsystem() {
    this.m_LauncherMotor = new SparkMax(CanIdConstants.kLauncherMotor, MotorType.kBrushless);
    this.m_ClosedLoopController = m_LauncherMotor.getClosedLoopController();
    this.m_LauncherMotor.configure(LauncherConfigs.config, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
  }

  /**
   * Sets the launcher to a specific velocity.
   * 
   * @param targetRPM The desired speed (e.g., calculated from vision distance)
   */
  public void setLauncherVelocity(double targetRPM) {
    m_ClosedLoopController.setSetpoint(targetRPM, ControlType.kVelocity);
  }

  public double getActualVelocity(){
    return this.m_LauncherMotor.getEncoder().getVelocity();
  }

  public void stopLauncher() {
    this.m_LauncherMotor.stopMotor();
  }

}
