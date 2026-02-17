// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.IntakeConstants;
import frc.robot.subsystems.IntakeSubsystem;

/**
 * The IntakeReceive command is responsible for running the robot's intake mechanism
 * to receive game pieces. It runs the intake motor at a predefined positive speed.
 */
public class IntakeReceive extends Command {
    /** The IntakeSubsystem instance that this command will control. */
    IntakeSubsystem intakeSubsystem;
    
  /**
   * Creates a new IntakeReceive command.
   *
   * @param intSub The IntakeSubsystem to be controlled by this command.
   */
  public IntakeReceive(IntakeSubsystem intSub) {
    this.intakeSubsystem = intSub;
    // Declare that this command requires the IntakeSubsystem, ensuring exclusive access.
    addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Engage the intake motor at the predefined constant speed to receive game pieces.
    SmartDashboard.putString("Current Command", this.getClass().getSimpleName());
    this.intakeSubsystem.engageIntake(IntakeConstants.kMotorSpeed.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the intake motor when the command ends or is interrupted.
    this.intakeSubsystem.stopIntake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // This command is designed to run indefinitely until interrupted,
    // for example, by releasing a button or another command taking over.
    return false;
  }
}
