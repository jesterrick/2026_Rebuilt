// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.ClimberConstants;
import frc.robot.subsystems.ClimberSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class HomeClimber extends Command {
  private ClimberSubsystem m_climber;
  
  /** Creates a new HomeClimber. */
  public HomeClimber(ClimberSubsystem climber) {
    this.m_climber = climber;
    addRequirements(this.m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_climber.prepareForHoming();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.m_climber.setHomingVoltages(ClimberConstants.kHomingVoltage);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.stop();

    if (!interrupted)
    {
      m_climber.resetEncoders();
      m_climber.setIsHomed(true);
    }

    m_climber.enableSoftLimits();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_climber.isAtBottom();
  }
}
