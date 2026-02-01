// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ExtenderSubsystem;

/**
 * The ExtenderIn command is responsible for retracting the robot's extender mechanism.
 * It continuously commands the ExtenderSubsystem to move to its inward (retracted) position.
 */
public class ExtenderIn extends Command {
  /** The ExtenderSubsystem instance that this command will control. */
  ExtenderSubsystem extenderSubsystem;

  /**
   * Creates a new ExtenderIn command.
   *
   * @param extendSub The ExtenderSubsystem to be controlled by this command.
   */
  public ExtenderIn(ExtenderSubsystem extendSub) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.extenderSubsystem = extendSub;
    addRequirements(extenderSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Command the extender to move to its inward (retracted) position.
    this.extenderSubsystem.moveIn();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the extender motors when the command ends or is interrupted.
    this.extenderSubsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // This command is designed to run indefinitely until interrupted by another command
    // or a button release, so it never explicitly finishes on its own.
    return false;
  }
}
