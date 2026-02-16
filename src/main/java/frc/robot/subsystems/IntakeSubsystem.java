// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IntakeConstants;
import frc.robot.util.hardware.MotorControllerWrapper;

/**
 * The IntakeSubsystem controls the robot's intake mechanism,
 * responsible for acquiring and manipulating game pieces.
 */
public class IntakeSubsystem extends SubsystemBase {
  /** The motor responsible for driving the front pickup mechanism of the intake. */
  private final MotorControllerWrapper m_FrontPickupMotor;

  /**
   * Constructs a new IntakeSubsystem.
   * Initializes the intake motor and configures it with predefined settings.
   */
  public IntakeSubsystem(MotorControllerWrapper motor) {
    this.m_FrontPickupMotor = motor;
  }

  public void updateConfigs() {
    if (DriverStation.isDisabled()) {
      if (IntakeConstants.kIntakeFF.hasChanged() || IntakeConstants.kIntakeStatic.hasChanged()) {
        m_FrontPickupMotor.setPID(0, 0, 0, IntakeConstants.kIntakeFF.get(), IntakeConstants.kIntakeStatic.get());
      }
    }
  }

  @Override
  public void periodic() {
  }

  /**
   * Engages the intake motors at a specified speed.
   * Positive speed typically causes the motors to pick up game pieces.
   * Negative speed typically pushes game pieces out of the intake.
   * @param speed The speed to set the intake motor to, typically a value between -1.0 and 1.0.
   */
  public void engageIntake(double speed) {
    this.m_FrontPickupMotor.set(speed);
  }

  /**
   * Stops the intake motors.
   */
  public void stopIntake() {
    this.m_FrontPickupMotor.set(0);
  }
}
