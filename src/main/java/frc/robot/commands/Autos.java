// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.AutoConstants;
//import edu.wpi.first.wpilibj2.command.Commands;

/**
 * The Autos class is a utility class for defining and generating autonomous commands.
 * It provides static factory methods to create predefined autonomous routines
 * that can be selected and run during the autonomous period of a match.
 */
public final class Autos {
  /**
   * An example static factory method that creates a simple autonomous command.
   * This command makes the robot drive straight forward at a specified speed
   * for a set duration.
   *
   * @param subsystem The DriveSubsystem instance that the command will require and operate on.
   * @return A {@link Command} that drives the robot straight.
   */
  public static Command exampleAuto(DriveSubsystem subsystem) {
    // Drive forward at a speed defined in AutoConstants for a duration specified in AutoConstants.
    return new DriveStraight(subsystem, AutoConstants.kExampleAutoSpeed).withTimeout(AutoConstants.kExampleAutoTimeout);
  }

  /**
   * Private constructor to prevent instantiation of this utility class.
   * All methods in this class are static.
   */
  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
