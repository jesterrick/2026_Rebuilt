// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ExtenderSubsystem;
import frc.robot.constants.ExtenderConstants;

/**
 * The HomeExtender command is responsible for homing the robot's extender mechanism.
 * This typically involves driving the extender inwards at a set voltage until a stall
 * condition (indicated by high current draw) is met, then resetting the encoders
 * to establish a known zero position.
 */
public class HomeExtender extends Command {
  /** The ExtenderSubsystem instance that this command will control. */
  private final ExtenderSubsystem m_extender;
  
  /**
   * Creates a new HomeExtender command.
   *
   * @param extender The ExtenderSubsystem to be controlled by this command.
   */
  public HomeExtender(ExtenderSubsystem extender) {
    this.m_extender = extender;
    // Declare that this command requires the ExtenderSubsystem, ensuring exclusive access.
    addRequirements(this.m_extender);
  }

  /**
   * Called when the command is initially scheduled.
   * No specific initialization is required for this command.
   */
  @Override
  public void initialize() {
    this.m_extender.prepareForHoming();
  }

  /**
   * Called every time the scheduler runs while the command is scheduled.
   * This method continuously sets a homing voltage to drive the extender inward.
   */
  @Override
  public void execute() {
    this.m_extender.setHomingVoltages(ExtenderConstants.kHomingVoltage);
  }

  /**
   * Called once the command ends or is interrupted.
   * When homing is complete (or interrupted), the encoders are reset to zero
   * and the motors are stopped.
   * @param interrupted True if the command was interrupted by another, false otherwise.
   */
  @Override
  public void end(boolean interrupted) {
    this.m_extender.stop();

    if (!interrupted)
    {
      this.m_extender.resetEncoders(); // Set the new 0 point after homing is complete.
      this.m_extender.setIsHomed(true);
    }
    this.m_extender.enableSoftLimits();
  }

  /**
   * Returns true when the command should end.
   * The command finishes when both the leader and follower motors of the extender
   * detect a current draw exceeding the maximum homing voltage, indicating a stall.
   * @return True if the extender is stalled (homed), false otherwise.
   */
  @Override
  public boolean isFinished() {
    return this.m_extender.isAtHome();
  }
}
