// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.FeederConfigs;
import frc.robot.constants.CanIdConstants;

/**
 * The FeederSubsystem controls the robot's feeder mechanism, responsible for
 * moving game pieces (e.g., notes, balls) into the launcher or other systems.
 */
public class FeederSubsystem extends SubsystemBase {
  /** The motor responsible for driving the feeder mechanism. */
  private final SparkMax m_FeederMotor;
  
  /**
   * Constructs a new FeederSubsystem.
   * Initializes the feeder motor and configures it with predefined settings.
   */
  public FeederSubsystem() {
    this.m_FeederMotor = new SparkMax(CanIdConstants.kFeederMotor, MotorType.kBrushless);
    // Configure the feeder motor with safe parameters and persist settings across power cycles.
    this.m_FeederMotor.configure(FeederConfigs.config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  /**
   * Engages the feeder motor at a specified speed.
   * @param speed The speed to set the feeder motor to, typically a value between -1.0 and 1.0.
   */
  public void engageFeeder(double speed) {
    this.m_FeederMotor.set(speed);
  }

  /**
   * Stops the feeder motor.
   */
  public void stopFeeder() {
    this.m_FeederMotor.set(0);
  }
}
