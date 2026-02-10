// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.ClimberConstants;
import frc.robot.subsystems.ClimberSubsystem;

/**
 * The HomeClimber command is responsible for homing the robot's climber mechanism.
 * This typically involves driving the climber downwards at a set voltage until a stall
 * condition (indicated by high current draw) is met, then resetting the encoders
 * to establish a known zero position.
 */
/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class HomeClimber extends Command {
  /** The ClimberSubsystem instance that this command will control. */
  private ClimberSubsystem m_climber;
  
  /**
   * Creates a new HomeClimber command.
   *
   * @param climber The ClimberSubsystem to be controlled by this command.
   */
  public HomeClimber(ClimberSubsystem climber) {
    this.m_climber = climber;
    // Declare that this command requires the ClimberSubsystem, ensuring exclusive access.
    addRequirements(this.m_climber);
  }

  /**
   * Called when the command is initially scheduled.
   * This method prepares the climber subsystem for homing, typically by disabling soft limits.
   */
  @Override
  public void initialize() {
    m_climber.prepareForHoming();
  }

  /**
   * Called every time the scheduler runs while the command is scheduled.
   * This method continuously sets a homing voltage to drive the climber downward.
   */
  @Override
  public void execute() {
    SmartDashboard.putString("Current Command", this.getClass().getSimpleName());
    this.m_climber.setHomingVoltages(ClimberConstants.kHomingVoltage);
  }

  /**
   * Called once the command ends or is interrupted.
   * When homing is complete (or interrupted), the motors are stopped.
   * If the command was not interrupted, the encoders are reset to zero
   * and the climber's homed status is set to true. Soft limits are re-enabled.
   * @param interrupted True if the command was interrupted by another, false otherwise.
   */
  @Override
  public void end(boolean interrupted) {
    m_climber.stop();

    if (!interrupted)
    {
      m_climber.resetEncoders(); // Set the new 0 point after homing is complete.
      m_climber.setIsHomed(true);
    }

    m_climber.enableSoftLimits();
  }

  /**
   * Returns true when the command should end.
   * The command finishes when the climber detects a stall condition (e.g., by current draw)
   * indicating it has reached its mechanical bottom limit.
   * @return True if the climber is at its mechanical bottom, false otherwise.
   */
  @Override
  public boolean isFinished() {
    return m_climber.isAtBottom();
  }
}
