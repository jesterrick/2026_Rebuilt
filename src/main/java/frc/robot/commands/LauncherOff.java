// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LauncherSubsystem;

/**
 * The LauncherOff command is responsible for completely stopping the robot's launcher mechanism.
 * It continuously commands the LauncherSubsystem to stop its motor.
 */
public class LauncherOff extends Command {
  /** The LauncherSubsystem instance that this command will control. */
  LauncherSubsystem m_Launcher;
  
  /**
   * Creates a new LauncherOff command.
   *
   * @param launcher The LauncherSubsystem to be controlled by this command.
   */
  public LauncherOff(LauncherSubsystem launcher) {
    this.m_Launcher = launcher;
    addRequirements(this.m_Launcher);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Stop the launcher motor.
    this.m_Launcher.stopLauncher();
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
