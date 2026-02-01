// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FeederSubsystem;

/**
 * The FeederOff command is responsible for stopping the robot's feeder mechanism.
 * It continuously commands the FeederSubsystem to stop its motor.
 */
public class FeederOff extends Command {
  /** The FeederSubsystem instance that this command will control. */
  FeederSubsystem m_Feeder;

  /**
   * Creates a new FeederOff command.
   *
   * @param feeder The FeederSubsystem to be controlled by this command.
   */
  public FeederOff(FeederSubsystem feeder) {
    this.m_Feeder = feeder;
    addRequirements(this.m_Feeder);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Command the feeder to stop.
    this.m_Feeder.stopFeeder();
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
