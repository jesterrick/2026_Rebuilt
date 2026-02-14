// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.FeederConstants;
import frc.robot.subsystems.FeederSubsystem;

/**
 * The FeederOn command is responsible for engaging the robot's feeder mechanism
 * at a predefined speed, typically to feed game pieces into the launcher.
 */
public class FeederOn extends Command {
  /** The FeederSubsystem instance that this command will control. */
  FeederSubsystem m_Feeder;
  /** The speed at which the feeder motor will operate (though it's currently unused as kFeederSpeed is used directly). */
  double motor_speed; 
  
  /**
   * Creates a new FeederOn command.
   *
   * @param feeder The FeederSubsystem to be controlled by this command.
   */
  public FeederOn(FeederSubsystem feeder) {
    this.m_Feeder = feeder;
    addRequirements(this.m_Feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Engage the feeder motor at the predefined constant speed.
    this.m_Feeder.engageFeeder(FeederConstants.kFeederSpeed.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the feeder motor when the command ends or is interrupted.
    this.m_Feeder.stopFeeder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // This command is designed to run indefinitely until interrupted,
    // for example, by releasing a button or another command taking over.
    return false;
  }
}
