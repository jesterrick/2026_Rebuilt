// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

/**
 * The LauncherConstants class stores all constant values related to the robot's launcher mechanism.
 * This includes motor speeds, tolerances, PID gains, sensor IDs, timing parameters,
 * and physical measurements used for Limelight calculations.
 */
public class LauncherConstants {

    /** The target RPM for the launcher when it is in an idle state. */
    public static final double kLauncherMotorSpeedIdle = 500;
    /** The target RPM for the launcher motor when it should be completely stopped. */
    public static final double kLauncherMotorStop = 0.0;
    /** The acceptable tolerance for the launcher's actual RPM to be considered "at speed". */
    public static final double kLauncherTolerance = 50.0;
    /** A buffer value added to the idle speed to determine the minimum RPM for a "launch" speed. */
    public static final double kLaunchMinShotBuffer = 500;

    /** The minimum RPM the launcher should achieve for a successful launch. */
    public static final double kLaunchMinRPM = 1500;
    /** The maximum safe RPM the launcher can operate at. */
    public static final double kLaunchMaxRPM = 4500;

    /** The digital input port for the sensor that detects a ball in the launcher's hopper. */
    public static final int kLauncherIdleSensor = 0;
    /** The time in seconds to wait for the hopper to be empty before turning off the launcher. */
    public static final double kWaitForEmptyTime = 3.0;

    // direct motor - no gears, sprockets, chains etc.
    // measure with direct RPM
    public static final double kPositionFactor = 1.0;
    public static final double kVelocityFactor = 1.0;

    // Velocity Closed-Loop Constants (for variable speed)
    /**
     * Velocity feed-forward gain (kV) for the launcher motor.
     * Calculated as (max voltage / max RPM), e.g., 11.0 / 5676 (Max RPM of NEO).
     */
    public static final double kLauncherkV = .0021;

    public static final double kLauncherStatic = 0.002;
    
    /** Proportional gain for the launcher's velocity PID controller. */
    public static final double kLauncherP = 0.00038;
    /** Integral gain for the launcher's velocity PID controller. */
    public static final double kLauncherI = 0.0;
    /** Derivative gain for the launcher's velocity PID controller. */
    public static final double kLauncherD = 0.0;

    // values of the field components for the limelight calculations
    /** The height of the target on the field, in inches. */
    public static final double kTargetHeight = 72.0;
    /** The height of the AprilTag on the field, in inches. */
    public static final double kAprilTagHeight = 44.25;
    /** The height of the Limelight camera from the ground, in inches. */
    public static final double kCameraHeight = 12.0;
    /** The mounting angle of the Limelight camera, in degrees. */
    public static final double kMountAngle = 15.0;

    /** The base RPM for Limelight calculations, used as an offset. */
    public static final int kBaseRPM = 15;
    /** The RPM increase per inch of distance for Limelight calculations. */
    public static final int kRPMPerInch = 15;
}
