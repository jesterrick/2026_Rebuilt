package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;

import frc.robot.constants.CanIdConstants;
import frc.robot.constants.ClimberConstants;
import frc.robot.constants.GlobalConstants;


public class ClimberConfigs {
    public static final SparkMaxConfig leaderConfig = new SparkMaxConfig();
    public static final SparkMaxConfig followConfig = new SparkMaxConfig();

    private static boolean leaderInverted = false;

    static {
        leaderConfig.encoder
            .positionConversionFactor(ClimberConstants.kPositionFactor) // Set units to inches
            .velocityConversionFactor(ClimberConstants.kVelocityFactor); // Set units to inches/sec

        leaderConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(ClimberConstants.kClimberP, ClimberConstants.kClimberI, ClimberConstants.kClimberD);
        
        leaderConfig.closedLoop.feedForward
            .kV(ClimberConstants.kClimberFF)
            .kS(ClimberConstants.kClimberStatic)
            .kG(ClimberConstants.kClimberGravity);

        leaderConfig.closedLoop.maxMotion
            .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal)
            .cruiseVelocity(ClimberConstants.kClimberCruiseVelocity)
            .maxAcceleration(ClimberConstants.kClimberAcceleration)
            .allowedProfileError(ClimberConstants.kClimberAllowedError);

        leaderConfig.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(ClimberConstants.kClimberMaxExtend)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(ClimberConstants.kClimberZero);

        leaderConfig.inverted(leaderInverted);
        leaderConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        leaderConfig.idleMode(IdleMode.kBrake); 

        followConfig.apply(leaderConfig);
        followConfig.follow(CanIdConstants.kClimberMotor1,true);

        followConfig.softLimit.forwardSoftLimitEnabled(false);
        followConfig.softLimit.reverseSoftLimitEnabled(false);
    
    }
}
