package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;

import frc.robot.constants.CanIdConstants;
import frc.robot.constants.ClimberConstants;
import frc.robot.constants.GlobalConstants;

/**
 * Configuration class for the Climber subsystem's SparkMax motor controllers.
 * Defines various settings for PID, feedforward, motion profiling, soft limits,
 * and current limits for leader, follower, and homing operational modes.
 */
public class ClimberConfigs {
    /** Configuration for the primary climber motor (leader). */
    public static final SparkMaxConfig leaderConfig = new SparkMaxConfig();
    /** Configuration for the secondary climber motor (follower). */
    public static final SparkMaxConfig followConfig = new SparkMaxConfig();
    /** Configuration specifically for the homing sequence of the climber. */
    public static final SparkMaxConfig homingConfig = new SparkMaxConfig();

    // Indicates if the leader motor's direction needs to be inverted.
    // This value will be applied to leaderConfig.
    private static boolean leaderInverted = false;

    static {
        // Configure the leader motor's encoder for position and velocity conversion factors.
        leaderConfig.encoder
            .positionConversionFactor(ClimberConstants.kPositionFactor) // Set units to inches
            .velocityConversionFactor(ClimberConstants.kVelocityFactor); // Set units to inches/sec

        // Configure PID constants for closed-loop control.
        leaderConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(ClimberConstants.kClimberP, ClimberConstants.kClimberI, ClimberConstants.kClimberD);
        
        // Configure Feedforward constants to improve control accuracy.
        leaderConfig.closedLoop.feedForward
            .kV(ClimberConstants.kClimberFF)
            .kS(ClimberConstants.kClimberStatic)
            .kG(ClimberConstants.kClimberGravity);

        // Configure motion profiling parameters for smooth movement.
        leaderConfig.closedLoop.maxMotion
            .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal)
            .cruiseVelocity(ClimberConstants.kClimberCruiseVelocity)
            .maxAcceleration(ClimberConstants.kClimberAcceleration)
            .allowedProfileError(ClimberConstants.kClimberAllowedError);

        // Configure soft limits to prevent physical over-extension or retraction.
        leaderConfig.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(ClimberConstants.kClimberMaxExtend)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(ClimberConstants.kClimberZero);

        // Apply the inverted setting to the leader motor.
        leaderConfig.inverted(leaderInverted);
        // Set a smart current limit to protect the motor and battery.
        leaderConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        // Set the idle mode to brake when the motor is not actively driven.
        leaderConfig.idleMode(IdleMode.kBrake); 

        // Apply the leader's configuration to the follower motor as a base.
        followConfig.apply(leaderConfig);
        // Configure the follower motor to follow the leader motor.
        // The 'true' argument indicates that the follower's direction should be inverted relative to the leader.
        followConfig.follow(CanIdConstants.kClimberMotor1, true);
        
        // Apply the leader's configuration to the homing motor config as a base.
        homingConfig.apply(leaderConfig);
        // Disable soft limits for homing, allowing the climber to reach a mechanical stop.
        homingConfig.softLimit.forwardSoftLimitEnabled(false);
        homingConfig.softLimit.reverseSoftLimitEnabled(false);
    }
}
