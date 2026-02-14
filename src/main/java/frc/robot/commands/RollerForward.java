// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.RollerConstants;
import frc.robot.subsystems.Rollers;

/**
 * The RollerForward command is responsible for running the robot's roller mechanism
 * in the forward direction at a predefined speed. This is typically used for intaking
 * or indexing game pieces.
 */
public class RollerForward extends Command {
  /** The Rollers subsystem instance that this command will control. */
  Rollers m_rollers;
  
  /**
   * Creates a new RollerForward command.
   *
   * @param roller The Rollers subsystem to be controlled by this command.
   */
  public RollerForward(Rollers roller) {
    this.m_rollers = roller;
    addRequirements(this.m_rollers);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Turn the rollers on at the predefined forward speed.
    m_rollers.rollerOn(RollerConstants.kRollerTargetSpeed.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Turn the rollers off when the command ends or is interrupted.
    this.m_rollers.rollerOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // This command is designed to run indefinitely until interrupted,
    // for example, by releasing a button or another command taking over.
    return false;
  }
}
