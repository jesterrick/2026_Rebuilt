package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.constants.GlobalConstants;
import frc.robot.constants.IntakeConstants;

import com.revrobotics.spark.config.SparkMaxConfig;

public class IntakeConfigs {
    public static final SparkMaxConfig config = new SparkMaxConfig();
    
    static {
        config.encoder
            .positionConversionFactor(IntakeConstants.kPositionFactor)
            .velocityConversionFactor(IntakeConstants.kVelocityFactor);

        config.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        config.closedLoop.feedForward
        .kS(IntakeConstants.kIntakeStatic)
        .kV(IntakeConstants.kIntakeFF);

        config.inverted(false);        
        config.idleMode(IdleMode.kCoast); 
        config.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);
    }
}