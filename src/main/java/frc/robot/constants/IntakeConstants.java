// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

/**
 * The IntakeConstants class stores all constant values related to the robot's intake mechanism.
 * These constants define motor speeds and other operational parameters for the intake.
 */
public class IntakeConstants {

    /** The default operational speed for the intake motor. */
    public static final double kIntakeMotorSpeed = 0.5;

    public static final double kIntakeFF = 0.2;
    public static final double kIntakeStatic = 0.02;

    // direct motor - no gears, sprockets, chains etc.
    // measure with direct RPM
    public static final double kPositionFactor = 1.0;
    public static final double kVelocityFactor = 1.0;
}
