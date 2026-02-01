// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

/**
 * The DriveStraight command makes the robot drive straight forward at a specified speed.
 * This command is typically used in autonomous routines or for simple teleoperated movements.
 */
public class DriveStraight extends Command {
  /** The DriveSubsystem instance that this command will control. */
  private final DriveSubsystem m_drive;
  /** The speed at which the robot will drive forward (from 0.0 to 1.0). */
  private final double m_speed;

  /**
   * Creates a new DriveStraight command.
   *
   * @param drive The DriveSubsystem instance to be controlled by this command.
   * @param speed The speed at which the robot should drive forward. A value between 0.0 and 1.0.
   */
  public DriveStraight(DriveSubsystem drive, double speed) {
    m_drive = drive;
    m_speed = speed;
    // Declare that this command requires the DriveSubsystem, ensuring exclusive access.
    addRequirements(drive);
  }

  /**
   * Called when the command is initially scheduled.
   * No specific initialization is required for this command.
   */
  @Override
  public void initialize() {
    // Nothing needed
  }

  /**
   * Called every time the scheduler runs while the command is scheduled.
   * This method continuously commands the DriveSubsystem to drive straight forward
   * at the preset speed, without any strafing or rotation.
   */
  @Override
  public void execute() {
    // Drive forward at the specified speed (0 for Y-speed and rotation).
    m_drive.drive(m_speed, 0, 0, false);
  }

  /**
   * Called once the command ends or is interrupted.
   * Stops the robot's movement to prevent any lingering motion.
   * @param interrupted True if the command was interrupted by another, false otherwise.
   */
  @Override
  public void end(boolean interrupted) {
    // Stop the robot by commanding zero speeds in all directions.
    m_drive.drive(0, 0, 0, false);
  }

  /**
   * Returns true when the command should end.
   * This command is designed to run until it is explicitly interrupted or
   * until a timeout is reached (if configured with `.withTimeout()`).
   * @return Always false, as this command never finishes on its own.
   */
  @Override
  public boolean isFinished() {
    // This command runs until interrupted or timed out
    return false;
  }
}