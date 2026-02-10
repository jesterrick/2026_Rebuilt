package frc.robot.constants;

import frc.robot.util.RobotUtils;

/**
 * The ExtenderConstants class centralizes all constant values related to the robot's extender mechanism.
 * This includes motor speeds, extension limits, physical conversion factors (e.g., inches per rotation),
 * PID gains for control, homing parameters, and various operational tolerances.
 */
public class ExtenderConstants {

    /** Default speed for the extender motor during operation. */
    public static final double kExtenderMotorSpeed = 0.05;
    /** The target position for the extender when fully retracted, in inches. */
    public static final double kExtenderMotorIn = 0.0;
    /** The target position for the extender when fully extended, in inches. */
    public static final double kExtenderMotorOut = 12.0;    // -- PHYSICAL MATH --
    /** Gear ratio of the extender mechanism. A value of 1.0 indicates no gear reduction. */
    public static final double kGearRatio = 1.0; 

    // pully/sprocket circumference = Diameter * PI
    /** Pitch diameter of the pulley or sprocket used in the extender mechanism. */
    public static final double pitchDiameter = 1.25;
    /** Inches traveled per rotation of the extender motor, calculated from pitch diameter. */
    public static final double kInchesPerRotation = pitchDiameter * Math.PI;

    // factor: (1 / GearRatio) * InchesPerRotation
    /**
     * Conversion factor to translate motor rotations into inches of extension.
     * Calculated as (1.0 / kGearRatio) * kInchesPerRotation.
     */
    public static final double kPositionFactor = RobotUtils.calculateLinearFactor(kGearRatio, pitchDiameter);;
    public static final double kVelocityFactor = RobotUtils.toVelocityPerSecond(kPositionFactor);

    /** Feedforward gain (kV) for the extender, compensating for motor voltage proportional to velocity. */
    public static final double kExtenderFF = 0.15;
    /** Static friction feedforward gain (kS) for the extender, compensating for static friction. */
    public static final double kExtenderStatic = 0.005;

    /** Proportional gain for the extender's position PID controller. */
    public static final double kExtenderP = 0.001;
    /** Integral gain for the extender's position PID controller. */
    public static final double kExtenderI = 0.0;
    /** Derivative gain for the extender's position PID controller. */
    public static final double kExtenderD = 0.0;
    
    public static final double kExtAcceleration = 25.0;
    
    /**
     * Allowed error (in inches) for the extender's closed-loop position control.
     * If the extender is within this tolerance of the target, it's considered "at target".
     */
    public static final double kExtenderAllowedError = 0.1;

    /**
     * Maximum acceleration for the extender mechanism, likely used in motion profiling or trapezoidal control
     * to ensure smooth and controlled movement.

    /** The voltage used for homing the extender mechanism. */
    public static final double kHomingVoltage = -1.5;
    /** Maximum allowed voltage during the homing process. */
    public static final double kMaxHomingVoltage = 10.0;
    
    /** Maximum allowed difference in position between the leader and follower motors before an error is flagged, in inches. */
    public static final double kMaxPositionDifference = 0.25;

    /** Proportional gain for synchronizing the extender's leader and follower motors. */
    public static final double kSyncP = 0.1;

    /** Tolerance for checking if the extender has reached its target position, in inches. */
    public static final double kAtTargetTolerance = 0.03;

    /**
     * Cruise velocity for the extender mechanism, used in motion profiling.
     * Calculated based on motor free speed, extender motor speed constant, and velocity conversion factor.
     */
    public static final double kExtenderCruiseVelocity = NeoMotorConstants.kFreeSpeedRpm * kExtenderMotorSpeed * kVelocityFactor;
}
