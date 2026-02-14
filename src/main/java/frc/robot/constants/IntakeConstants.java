// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

import frc.robot.util.TunableNumber;
import frc.robot.util.TuningManager;

/**
 * The IntakeConstants class stores all constant values related to the robot's intake mechanism.
 * These constants define motor speeds and other operational parameters for the intake.
 */
public class IntakeConstants {
   
    /*
     ****** TUNABLE VARIABLES ******
     */

    /** The target velocity for the rollers (e.g., in RPM or 0-1 duty cycle). */
    public static final TunableNumber kIntakeMotorSpeed = TuningManager.register("Intake/MotorSpeed", 0.5);

    /** kV: The voltage required to sustain a given velocity. Units: Volts/(Unit of Speed). */
    public static final TunableNumber kIntakeFF = TuningManager.register("Intake/FF", 0.2);
    
    /** kS: The 'Oomph' required to break static friction. Units: Volts or Percent. */
    public static final TunableNumber kIntakeStatic = TuningManager.register("Intake/Static", 0.02);

    /*
     ****** NON TUNABLE VARIABLES ******
     */
    // direct motor - no gears, sprockets, chains etc.
    // measure with direct RPM
    public static final double kPositionFactor = 1.0;
    public static final double kVelocityFactor = 1.0;
    
}
