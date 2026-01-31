// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ExtenderSubsystem;
import frc.robot.constants.ExtenderConstants;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class HomeExtender extends Command {
  private final ExtenderSubsystem m_extender;
  /** Creates a new HomeExtender. */
  public HomeExtender(ExtenderSubsystem extender) {
    this.m_extender = extender;
    addRequirements(this.m_extender);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_extender.setHomingVoltages(ExtenderConstants.kHomingVoltage);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_extender.resetEncoders(); // Set the new 0 point
    m_extender.stop();          // Turn motors off
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_extender.getLeaderCurrent() > ExtenderConstants.kMaxHomingVoltage && m_extender.getFollowerCurrent() > ExtenderConstants.kMaxHomingVoltage;
  }
}
