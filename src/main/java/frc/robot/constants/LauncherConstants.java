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
 * The Constants class stores all constant values related to the robot's  mechanism.
 * This includes motor speeds, tolerances, PID gains, sensor IDs, timing parameters,
 * and physical measurements used for Limelight calculations.
 */
public class LauncherConstants implements MotorConstants {

    /*
     ****** TUNABLE VARIABLES ******
     */

    /** The target RPM for the  when it is in an idle state. */
    public static final TunableNumber kMotorSpeedIdle = TuningManager.register("Launcher/IdleSpeed", 500);

    /** The acceptable tolerance for the 's actual RPM to be considered "at speed". */
    public static final TunableNumber kTolerance = TuningManager.register("Launcher/Tolerance", 50.0);
    
    /** A buffer value added to the idle speed to determine the minimum RPM for a "launch" speed. */
    public static final TunableNumber kLaunchMinShotBuffer = TuningManager.register("Launcher/MinShotBuffer", 500);

    /** The minimum RPM the  should achieve for a successful launch. */
    public static final TunableNumber kLaunchMinRPM = TuningManager.register("Launcher/MinRPM", 1500);
    
    /** The maximum safe RPM the  can operate at. */
    public static final TunableNumber kLaunchMaxRPM = TuningManager.register("Launcher/MaxRPM", 4500);

    // Velocity Closed-Loop Constants (for variable speed)
    /**
     * Velocity feed-forward gain (kV) for the  motor.
     * Calculated as (max voltage / max RPM), e.g., 11.0 / 5676 (Max RPM of NEO).
     */
    public static final TunableNumber kV = TuningManager.register("Launcher/V", .0021);

    public static final TunableNumber kS = TuningManager.register("Launcher/S", 0.002);
    
    /** Proportional gain for the 's velocity PID controller. */
    public static final TunableNumber kP = TuningManager.register("Launcher/P", 0.00038);
    
    /** Integral gain for the 's velocity PID controller. */
    public static final TunableNumber kI = TuningManager.register("Launcher/I", 0.0);
    
    /** Derivative gain for the 's velocity PID controller. */
    public static final TunableNumber kD = TuningManager.register("Launcher/D", 0.0);

    /** The base RPM for Limelight calculations, used as an offset. */
    public static final TunableNumber kBaseRPM = TuningManager.register("Launcher/BaseRPM", 15);
    
    /** The RPM increase per inch of distance for Limelight calculations. */
    public static final TunableNumber kRPMPerInch = TuningManager.register("Launcher/RPMPerInch", 15);

    /*
     ****** NON TUNABLE VARIABLES ******
     */
    /** The target RPM for the  motor when it should be completely stopped. */
    public static final double kMotorStop = 0.0;

    /** The digital input port for the sensor that detects a ball in the 's hopper. */
    public static final int kIdleSensor = 0;
    
    /** The time in seconds to wait for the hopper to be empty before turning off the . */
    public static final double kWaitForEmptyTime = 3.0;

    // direct motor - no gears, sprockets, chains etc.
    // measure with direct RPM
    public static final double kPositionFactor = 1.0;
    public static final double kVelocityFactor = 1.0;

    // values of the field components for the limelight calculations
    /** The height of the target on the field, in inches. */
    public static final double kTargetHeight = 72.0;
    /** The height of the AprilTag on the field, in inches. */
    public static final double kAprilTagHeight = 44.25;
    /** The height of the Limelight camera from the ground, in inches. */
    public static final double kCameraHeight = 12.0;
    /** The mounting angle of the Limelight camera, in degrees. */
    public static final double kMountAngle = 15.0;

    /** The idle or neutral state of the motor when no power is applied */
    public static final MotorSettings.NeutralBehavior kNeutralMode = MotorSettings.NeutralBehavior.kCoast;

    /** Which direction does the motor spin? */
    public static final MotorSettings.MotorRotation kMotorRotation = MotorRotation.kClockwise;

    /** Max Current for the motor */
    public static final int kCurrentLimit = GlobalConstants.kMediumCurrentLimit;

    @Override
    public double getP() { return kP.get(); }

    @Override
    public double getI() { return kI.get(); }

    @Override
    public double getD() { return kD.get(); }

    @Override
    public double getV() { return kV.get(); }

    @Override
    public double getS() { return kS.get(); }

    @Override
    public NeutralBehavior getNeutralBehavior() { return kNeutralMode; }

    @Override
    public int getCurrentLimit() { return kCurrentLimit; }
    
    @Override
    public MotorRotation getMotorRotation() { return kMotorRotation; }

    @Override
    public double getConversionRatio() { return kPositionFactor; }

    @Override
    public boolean hasChanged() {
        return kMotorSpeedIdle.hasChanged() ||
               kTolerance.hasChanged() ||
               kLaunchMinShotBuffer.hasChanged() ||
               kLaunchMinRPM.hasChanged() ||
               kLaunchMaxRPM.hasChanged() ||
               kV.hasChanged() ||
               kS.hasChanged() ||
               kP.hasChanged() ||
               kI.hasChanged() ||
               kD.hasChanged() ||
               kBaseRPM.hasChanged() ||
               kRPMPerInch.hasChanged();
    }
}
