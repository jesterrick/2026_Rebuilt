package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.IntakeSubsystem;

/**
 * The IntakeStop command is an {@link InstantCommand} that immediately stops
 * the robot's intake mechanism. It is designed to be called once to
 * halt any ongoing intake operations.
 */
public class IntakeStop extends InstantCommand {
  /**
   * Creates a new IntakeStop command.
   *
   * @param intake The {@link IntakeSubsystem} instance to be controlled by this command.
   */
  public IntakeStop(IntakeSubsystem intake) {
    // Calls the stopIntake() method of the provided IntakeSubsystem once when this command is initialized.
    // This command requires the IntakeSubsystem, ensuring exclusive access.
    super(intake::stopIntake, intake);
  }
}
