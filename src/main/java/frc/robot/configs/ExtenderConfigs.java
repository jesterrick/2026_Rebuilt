package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.constants.ExtenderConstants;
import frc.robot.constants.GlobalConstants;

public class ExtenderConfigs {
    public static final SparkMaxConfig leaderConfig = new SparkMaxConfig();
    public static final SparkMaxConfig followConfig = new SparkMaxConfig();

    public static final SparkMaxConfig homingConfig = new SparkMaxConfig();
    private static boolean leaderInverted = false;

    static {
        leaderConfig.encoder
            .positionConversionFactor(ExtenderConstants.kPositionFactor)
            .velocityConversionFactor(ExtenderConstants.kVelocityFactor);

        leaderConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(ExtenderConstants.kExtenderP, ExtenderConstants.kExtenderI, ExtenderConstants.kExtenderD);

        leaderConfig.closedLoop.feedForward
            .kV(ExtenderConstants.kExtenderFF)
            .kS(ExtenderConstants.kExtenderStatic);
        
        leaderConfig.closedLoop.maxMotion
            .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal)
            .cruiseVelocity(ExtenderConstants.kExtenderCruiseVelocity)
            .maxAcceleration(ExtenderConstants.kExtAcceleration)
            .allowedProfileError(ExtenderConstants.kExtenderAllowedError);

        leaderConfig.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(ExtenderConstants.kExtenderMotorOut)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(ExtenderConstants.kExtenderMotorIn);

        leaderConfig.inverted(leaderInverted);
        leaderConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        leaderConfig.idleMode(IdleMode.kBrake); 

        followConfig.apply(leaderConfig);

        followConfig.inverted(!leaderInverted); 

        followConfig.softLimit
            .forwardSoftLimitEnabled(false)
            .reverseSoftLimitEnabled(false);
    
        homingConfig.apply(leaderConfig);

        homingConfig.softLimit
            .forwardSoftLimitEnabled(false)
            .reverseSoftLimitEnabled(false);
    }
}
