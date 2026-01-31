package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.constants.ExtenderConstants;
import frc.robot.constants.CanIdConstants;
import frc.robot.constants.GlobalConstants;

public class ExtenderConfigs {
    public static final SparkMaxConfig leaderConfig = new SparkMaxConfig();
    public static final SparkMaxConfig followConfig = new SparkMaxConfig();

    static {
        leaderConfig.encoder
            .positionConversionFactor(ExtenderConstants.kPositionFactor) // Set units to inches
            .velocityConversionFactor(ExtenderConstants.kPositionFactor / 60.0); // Set units to inches/sec

        leaderConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(ExtenderConstants.kIntakeP, ExtenderConstants.kIntakeI, ExtenderConstants.kIntakeD);
        
        leaderConfig.closedLoop.maxMotion
            .cruiseVelocity(5600 * ExtenderConstants.kExtenderMotorSpeed * (ExtenderConstants.kPositionFactor / 60.0))
            .maxAcceleration(ExtenderConstants.kExtAcceleration)
            .allowedProfileError(0.1);

        leaderConfig.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(ExtenderConstants.kExtenderMotorOut)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(ExtenderConstants.kExtenderMotorIn);

        leaderConfig.inverted(false);
        leaderConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        leaderConfig.idleMode(IdleMode.kBrake); 

        followConfig.apply(leaderConfig);

        followConfig.follow(CanIdConstants.kExtenderMotor1);
        followConfig.inverted(true); 

        followConfig.softLimit.forwardSoftLimitEnabled(false);
        followConfig.softLimit.reverseSoftLimitEnabled(false);
    
    }
}
