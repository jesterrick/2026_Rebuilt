package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.constants.CanIdConstants;
import frc.robot.constants.ClimberConstants;
import frc.robot.constants.GlobalConstants;


public class ClimberConfigs {
    public static final SparkMaxConfig leaderConfig = new SparkMaxConfig();
    public static final SparkMaxConfig followConfig = new SparkMaxConfig();

    static {
        leaderConfig.encoder
            .positionConversionFactor(ClimberConstants.kPositionFactor) // Set units to inches
            .velocityConversionFactor(ClimberConstants.kPositionFactor / 60.0); // Set units to inches/sec

        leaderConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(ClimberConstants.kIntakeP, ClimberConstants.kIntakeI, ClimberConstants.kIntakeD);
        
        leaderConfig.closedLoop.maxMotion
            .cruiseVelocity(5600 * ClimberConstants.kClimberMotorSpeed * (ClimberConstants.kPositionFactor / 60.0))
            .maxAcceleration(ClimberConstants.kExtAcceleration)
            .allowedProfileError(0.1);

        leaderConfig.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(ClimberConstants.kClimberMaxExtend)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(ClimberConstants.kClimberZero);

        leaderConfig.inverted(false);
        leaderConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        leaderConfig.idleMode(IdleMode.kBrake); 

        followConfig.apply(leaderConfig);
        followConfig.follow(CanIdConstants.kClimberMotor1);
        followConfig.inverted(true); 

        followConfig.softLimit.forwardSoftLimitEnabled(false);
        followConfig.softLimit.reverseSoftLimitEnabled(false);
    
    }
}
