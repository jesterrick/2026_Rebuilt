package frc.robot.constants;

import frc.robot.util.RobotUtils;

/**
 * The ClimberConstants class stores constant values related to the robot's climber mechanism.
 * This includes motor speeds, extension limits, gear ratios, and PID controller gains
 * for controlling the climber's movement and position.
 */
public class ClimberConstants {

    /** Default speed for the climber motor during operation. */
    public static final double kClimberMotorSpeed = 0.05;
    /** Maximum allowed extension height for the climber, in inches. */
    public static final double kClimberMaxExtend = 12.0;
    /** The zero position for the climber, typically fully retracted. */
    public static final double kClimberZero = 0.0;
    /** Gear ratio of the climber mechanism. A value of 1.0 indicates no gear reduction. */
    public static final double kGearRatio = 1.0; 

    /** Pitch diameter of the pulley or sprocket used in the climber mechanism. */
    public static final double pitchDiameter = 1.25;
    /** Inches traveled per rotation of the climber motor, calculated from pitch diameter. */
    public static final double kInchesPerRotation = pitchDiameter * Math.PI;

    /**
     * Conversion factor to translate motor rotations into inches of extension.
     * Calculated as (1 / GearRatio) * InchesPerRotation.
     */
    public static final double kPositionFactor = RobotUtils.calculateLinearFactor(kGearRatio, pitchDiameter);;
    public static final double kVelocityFactor = RobotUtils.toVelocityPerSecond(kPositionFactor);

    public static final double kClimberFF = 0.5;
    public static final double kClimberGravity = 0.08;
    public static final double kClimberStatic = 0.2;

    /** Proportional gain for the climber's position PID controller. */
    public static final double kClimberP = 4.0;
    /** Integral gain for the climber's position PID controller. */
    public static final double kClimberI = 0.0;
    /** Derivative gain for the climber's position PID controller. */
    public static final double kClimberD = 0.03;

    public static final double kClimberAllowedError = 0.1;

    /** Acceleration constant for the extender, likely used in motion profiling or trapezoidal control. */
    public static final double kClimberAcceleration = .10;

    public static final double kClimberCruiseVelocity = NeoMotorConstants.kFreeSpeedRpm * kClimberMotorSpeed * kVelocityFactor;

    public static final double kHomingVoltage = -1.5;
    public static final double kMaxHomingVoltage = 10.0;
}
