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
    public static final TunableNumber kMotorSpeed = TuningManager.register("Climber/MotorSpeed", 0.05);
    
    public static final TunableNumber kV = TuningManager.register("Climber/FF", 0.5);
    
    public static final TunableNumber kG = TuningManager.register("Climber/Gravity", 0.08);
    
    public static final TunableNumber kS = TuningManager.register("Climber/Static", 0.2);

    /** Proportional gain for the climber's position PID controller. */
    public static final TunableNumber kP = TuningManager.register("Climber/P", 4.0);
    
    /** Integral gain for the climber's position PID controller. */
    public static final TunableNumber kI = TuningManager.register("Climber/I", 0.0);
    
    /** Derivative gain for the climber's position PID controller. */
    public static final TunableNumber kD = TuningManager.register("Climber/D", 0.03);

    public static final TunableNumber kAllowedError = TuningManager.register("Climber/AllowedError", 0.1);

    /** Acceleration constant for the extender, likely used in motion profiling or trapezoidal control. */
    public static final TunableNumber kMaxAcceleration = TuningManager.register("Climber/Acceleration", .10);

    public static final TunableNumber kHomingVoltage = TuningManager.register("Climber/HomingVoltage", -1.5);
    
    public static final TunableNumber kMaxHomingVoltage = TuningManager.register("Climber/MaxHomingVoltage", 10.0);

    /*
     ****** NON TUNABLE VARIABLES ******
     */
    /** Maximum allowed extension height for the climber, in inches. */
    public static final double kMaxExtend = 12.0;
    /** The zero position for the climber, typically fully retracted. */
    public static final double kZero = 0.0;
    /** Gear ratio of the climber mechanism. A value of 1.0 indicates no gear reduction. */
    public static final double kGearRatio = 1.0; 

    /** Pitch diameter of the pulley or sprocket used in the climber mechanism. */
    public static final double kPitchDiameter = 1.25;
    /** Inches traveled per rotation of the climber motor, calculated from pitch diameter. */
    public static final double kInchesPerRotation = kPitchDiameter * Math.PI;

    /**
     * Conversion factor to translate motor rotations into inches of extension.
     * Calculated as (1 / GearRatio) * InchesPerRotation.
     */
    public static final double kPositionFactor = RobotUtils.calculateLinearFactor(kGearRatio, kPitchDiameter);;
    public static final double kVelocityFactor = RobotUtils.toVelocityPerSecond(kPositionFactor);
    
    public static final double kCruiseVelocity = NeoMotorConstants.kFreeSpeedRpm * kMotorSpeed.get() * kVelocityFactor;

    public static final int kCurrentLimit = GlobalConstants.kLowCurrentLimit;

    public static final MotorSettings.NeutralBehavior kNeutralMode = MotorSettings.NeutralBehavior.kCoast;

    public static final MotorSettings.MotorRotation kRotation = MotorSettings.MotorRotation.kCounterClockwise;

    public static final boolean isMotionProfilingEnabled = true;

    public static final boolean isForwardLimitEnabled = true;

    public static final boolean isReverseLimitEnabled = true;

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
    public MotorRotation getMotorRotation() { return kRotation; }

    @Override
    public double getConversionRatio() { return kPositionFactor; }

    @Override
    public boolean isMotionProfilingEnabled() { return isMotionProfilingEnabled; } // Climber uses motion profiling
    
    @Override
    public double getCruiseVelocity() { return kCruiseVelocity; }

    @Override
    public double getMaxAcceleration() { return kMaxAcceleration.get(); }

    @Override
    public double getAllowedError() { return kAllowedError.get(); }

    @Override
    public boolean isForwardLimitEnabled() { return isForwardLimitEnabled; }

    @Override
    public double getForwardLimit() { return kMaxExtend; }

    @Override
    public boolean isReverseLimitEnabled() { return isReverseLimitEnabled; }

    @Override
    public double getReverseLimit() { return kZero; }

    @Override
    public boolean hasChanged() {
        return kMotorSpeed.hasChanged() ||
               kV.hasChanged() ||
               kG.hasChanged() ||
               kS.hasChanged() ||
               kP.hasChanged() ||
               kI.hasChanged() ||
               kD.hasChanged() ||
               kAllowedError.hasChanged() ||
               kMaxAcceleration.hasChanged() ||
               kHomingVoltage.hasChanged() ||
               kMaxHomingVoltage.hasChanged();
    }
}
