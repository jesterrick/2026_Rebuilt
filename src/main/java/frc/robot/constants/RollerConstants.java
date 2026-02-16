// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

import frc.robot.util.TunableNumber;
import frc.robot.util.TuningManager;
import frc.robot.util.hardware.MotorConstants;
import frc.robot.util.hardware.MotorSettings;
import frc.robot.util.hardware.MotorSettings.MotorRotation;
import frc.robot.util.hardware.MotorSettings.NeutralBehavior;

/**
 * The RollerConstants class stores all constant values related to the robot's
 * roller mechanism.
 * These constants define motor speeds and other operational parameters for the
 * rollers.
 */
public class RollerConstants implements MotorConstants {

    /*
     ****** TUNABLE VARIABLES ******
     */

    /** The target velocity for the rollers (e.g., in RPM or 0-1 duty cycle). */
    public static final TunableNumber kRollerTargetSpeed = TuningManager.register("Roller/TargetSpeed", 0.05);

    /** Proportional gain for the Roller's velocity PID controller. */
    public static final TunableNumber kRollerP = TuningManager.register("Roller/P", 0.0);
    
    /** Integral gain for the Roller's velocity PID controller. */
    public static final TunableNumber kRollerI = TuningManager.register("Roller/I", 0.0);
    
    /** Derivative gain for the Roller's velocity PID controller. */
    public static final TunableNumber kRollerD = TuningManager.register("Roller/D", 0.0);

    /** kV: The voltage required to sustain a given velocity. Units: Volts/(Unit of Speed). */
    public static final TunableNumber kRollerFF = TuningManager.register("Roller/FF", 0.2);
    
    /** kS: The 'Oomph' required to break static friction. Units: Volts or Percent. */
    public static final TunableNumber kRollerStatic = TuningManager.register("Roller/Static", 0.02);
    /*
     ****** NON TUNABLE VARIABLES ******
     */
    // direct motor - no gears, sprockets, chains etc.
    // measure with direct RPM
    public static final double kPositionFactor = 1.0;
    public static final double kVelocityFactor = 1.0;

    public static final int kCurrentLimit = GlobalConstants.kLowCurrentLimit;

    public static final MotorSettings.NeutralBehavior kNeutralMode = MotorSettings.NeutralBehavior.kCoast;

    public static final MotorSettings.MotorRotation kRotation = MotorSettings.MotorRotation.kCounterClockwise;

    @Override
    public double getP() { return kRollerP.get(); }

    @Override
    public double getI() { return kRollerI.get(); }

    @Override
    public double getD() { return kRollerD.get(); }

    @Override
    public double getV() { return kRollerFF.get(); }

    @Override
    public double getS() { return kRollerStatic.get(); }

    @Override
    public NeutralBehavior getNeutralBehavior() { return kNeutralMode; }

    @Override
    public int getCurrentLimit() { return kCurrentLimit; }
    
    @Override
    public MotorRotation getMotorRotation() { return kRotation; }

    @Override
    public double getConversionRatio() { return kPositionFactor; }
}
