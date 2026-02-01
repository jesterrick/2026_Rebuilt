// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Rollers;

/**
 * The RollerOff command is responsible for stopping the robot's roller mechanism.
 * It continuously commands the Rollers subsystem to turn its motors off.
 */
public class RollerOff extends Command {
  /** The Rollers subsystem instance that this command will control. */
  Rollers m_Rollers;
  
  /**
   * Creates a new RollerOff command.
   *
   * @param roller The Rollers subsystem to be controlled by this command.
   */
  public RollerOff(Rollers roller) {
    this.m_Rollers = roller;
    addRequirements(this.m_Rollers);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Turn the rollers off.
    this.m_Rollers.rollerOff();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // This command is designed to run indefinitely until interrupted,
    // so it never explicitly finishes on its own.
    return false;
  }
}
