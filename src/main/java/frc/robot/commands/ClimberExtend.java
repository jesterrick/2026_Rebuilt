// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;

/**
 * The ClimberExtend command is responsible for extending the robot's climber mechanism.
 * It continuously commands the climber subsystem to move upwards until the command is interrupted.
 */
public class ClimberExtend extends Command {
  /** The ClimberSubsystem instance that this command will control. */
  ClimberSubsystem m_climber;

  /**
   * Creates a new ClimberExtend command.
   *
   * @param climber The ClimberSubsystem to be controlled by this command.
   */
  public ClimberExtend(ClimberSubsystem climber) {
    this.m_climber = climber;
    // Declare that this command requires the ClimberSubsystem, ensuring exclusive access.
    addRequirements(this.m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Command the climber to move upwards (extend).
    SmartDashboard.putString("Current Command", this.getClass().getSimpleName());
    this.m_climber.climberUp();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the climber motors when the command ends or is interrupted.
    this.m_climber.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // This command is designed to run indefinitely until interrupted by another command
    // or a button release, so it never explicitly finishes on its own.
    return false;
  }
}
