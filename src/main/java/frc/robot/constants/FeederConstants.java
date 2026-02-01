// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

/**
 * The FeederConstants class stores all constant values related to the robot's feeder mechanism.
 * These constants define motor speeds and operational limits for the feeder.
 */
public class FeederConstants {

    /** The default operational speed for the feeder motor. */
    public static final double kFeederSpeed = 0.5;

    // direct motor - no gears, sprockets, chains etc.
    // measure with direct RPM
    public static final double kPositionFactor = 1.0;
    public static final double kVelocityFactor = 1.0;

    public static final double kFeederFF = 0.2;
    public static final double kFeederStatic = 0.02;
}
