package frc.robot.constants;

import frc.robot.util.RobotUtils;
import frc.robot.util.TunableNumber;
import frc.robot.util.TuningManager;
import frc.robot.util.hardware.MotorConstants;
import frc.robot.util.hardware.MotorSettings;
import frc.robot.util.hardware.MotorSettings.MotorRotation;
import frc.robot.util.hardware.MotorSettings.NeutralBehavior;

/**
 * The ClimberConstants class stores constant values related to the robot's climber mechanism.
 * This includes motor speeds, extension limits, gear ratios, and PID controller gains
 * for controlling the climber's movement and position.
 */
public class ClimberConstants implements MotorConstants {

    /*
     ****** TUNABLE VARIABLES ******
     */

    /** Default speed for the climber motor during operation. */
    public static final TunableNumber kClimberMotorSpeed = TuningManager.register("Climber/MotorSpeed", 0.05);
    
    public static final TunableNumber kClimberFF = TuningManager.register("Climber/FF", 0.5);
    
    public static final TunableNumber kClimberGravity = TuningManager.register("Climber/Gravity", 0.08);
    
    public static final TunableNumber kClimberStatic = TuningManager.register("Climber/Static", 0.2);

    /** Proportional gain for the climber's position PID controller. */
    public static final TunableNumber kClimberP = TuningManager.register("Climber/P", 4.0);
    
    /** Integral gain for the climber's position PID controller. */
    public static final TunableNumber kClimberI = TuningManager.register("Climber/I", 0.0);
    
    /** Derivative gain for the climber's position PID controller. */
    public static final TunableNumber kClimberD = TuningManager.register("Climber/D", 0.03);

    public static final TunableNumber kClimberAllowedError = TuningManager.register("Climber/AllowedError", 0.1);

    /** Acceleration constant for the extender, likely used in motion profiling or trapezoidal control. */
    public static final TunableNumber kClimberAcceleration = TuningManager.register("Climber/Acceleration", .10);

    public static final TunableNumber kHomingVoltage = TuningManager.register("Climber/HomingVoltage", -1.5);
    
    public static final TunableNumber kMaxHomingVoltage = TuningManager.register("Climber/MaxHomingVoltage", 10.0);

    /*
     ****** NON TUNABLE VARIABLES ******
     */
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
    
    public static final double kClimberCruiseVelocity = NeoMotorConstants.kFreeSpeedRpm * kClimberMotorSpeed.get() * kVelocityFactor;

    public static final int kCurrentLimit = GlobalConstants.kLowCurrentLimit;

    public static final MotorSettings.NeutralBehavior kNeutralMode = MotorSettings.NeutralBehavior.kCoast;

    public static final MotorSettings.MotorRotation kRotation = MotorSettings.MotorRotation.kCounterClockwise;

    @Override
    public double getP() { return kClimberP.get(); }

    @Override
    public double getI() { return kClimberI.get(); }

    @Override
    public double getD() { return kClimberD.get(); }

    @Override
    public double getV() { return kClimberFF.get(); }

    @Override
    public double getS() { return kClimberStatic.get(); }

    @Override
    public NeutralBehavior getNeutralBehavior() { return kNeutralMode; }

    @Override
    public int getCurrentLimit() { return kCurrentLimit; }
    
    @Override
    public MotorRotation getMotorRotation() { return kRotation; }

    @Override
    public double getConversionRatio() { return kPositionFactor; }

    @Override
    public boolean isMotionProfilingEnabled() { return true; } // Climber uses motion profiling
    
    @Override
    public double getCruiseVelocity() { return kClimberCruiseVelocity; }

    @Override
    public double getMaxAcceleration() { return kClimberAcceleration.get(); }

    @Override
    public double getAllowedError() { return kClimberAllowedError.get(); }

    @Override
    public boolean isForwardLimitEnabled() { return true; }

    @Override
    public double getForwardLimit() { return kClimberMaxExtend; }

    @Override
    public boolean isReverseLimitEnabled() { return true; }

    @Override
    public double getReverseLimit() { return kClimberZero; }
}
