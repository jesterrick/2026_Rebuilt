// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.IntakeConfigs;
import frc.robot.constants.CanIdConstants;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new Intake. */

  private final SparkMax m_FrontPickupMotor;

  public IntakeSubsystem() {
    this.m_FrontPickupMotor = new SparkMax(CanIdConstants.kIntakeMotor, MotorType.kBrushless);
    this.m_FrontPickupMotor.configure(IntakeConfigs.config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  /*
   * This will turn on the Intake motors.
   * Positive will cause the motors to pick up the coral
   * Negative will push the coral our of the intake
   */
  public void engageIntake(double speed) {
    this.m_FrontPickupMotor.set(speed);
  }

  // This will stop the intake motors
  public void stopIntake() {
    this.m_FrontPickupMotor.set(0);
  }
}
