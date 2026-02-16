// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.FeederConstants;
import frc.robot.util.hardware.MotorControllerWrapper;

/**
 * The FeederSubsystem controls the robot's feeder mechanism, responsible for
 * moving game pieces (e.g., notes, balls) into the launcher or other systems.
 */
public class FeederSubsystem extends SubsystemBase {
  /** The motor responsible for driving the feeder mechanism. */
  private final MotorControllerWrapper m_FeederMotor;

  /**
   * Constructs a new FeederSubsystem.
   * Initializes the feeder motor and configures it with predefined settings.
   */
  public FeederSubsystem(MotorControllerWrapper motor) {
    this.m_FeederMotor = motor;
  }

  public void updateConfigs() {
    if (DriverStation.isDisabled()) {
      if (FeederConstants.kFeederP.hasChanged() || FeederConstants.kFeederI.hasChanged()
          || FeederConstants.kFeederD.hasChanged() || FeederConstants.kFeederFF.hasChanged()
          || FeederConstants.kFeederStatic.hasChanged()) {
        m_FeederMotor.setPID(FeederConstants.kFeederP.get(), FeederConstants.kFeederI.get(),
            FeederConstants.kFeederD.get(), FeederConstants.kFeederFF.get(), FeederConstants.kFeederStatic.get());
      }
    }
  }

  @Override
  public void periodic() {
  }

  /**
   * Engages the feeder motor at a specified speed.
   * 
   * @param speed The speed to set the feeder motor to, typically a value between
   *              -1.0 and 1.0.
   */
  public void engageFeeder(double speed) {
    this.m_FeederMotor.set(speed);
  }

  /**
   * Stops the feeder motor.
   */
  public void stopFeeder() {
    this.m_FeederMotor.stop();
  }
}
