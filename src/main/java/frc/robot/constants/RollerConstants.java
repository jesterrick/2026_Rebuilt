// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

/**
 * The RollerConstants class stores all constant values related to the robot's roller mechanism.
 * These constants define motor speeds and other operational parameters for the rollers.
 */
public class RollerConstants {
    /** The default operational speed for the roller motor. */
    public static final double kRollerSpeed = 0.05;

    // direct motor - no gears, sprockets, chains etc.
    // measure with direct RPM
    public static final double kPositionFactor = 1.0;
    public static final double kVelocityFactor = 1.0;

    public static final double kFeederStatic = 0.02;
    public static final double kFeederFF = 0.2;
}
