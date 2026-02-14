package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.constants.GlobalConstants;
import frc.robot.constants.IntakeConstants;

import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class IntakeConfigs {
    public static final SparkMaxConfig maxConfig = new SparkMaxConfig();
    public static final SparkFlexConfig flexConfig = new SparkFlexConfig();

    static {
        maxConfig.encoder
                .positionConversionFactor(IntakeConstants.kPositionFactor)
                .velocityConversionFactor(IntakeConstants.kVelocityFactor);

        maxConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        maxConfig.closedLoop.feedForward
                .kS(IntakeConstants.kIntakeStatic.get())
                .kV(IntakeConstants.kIntakeFF.get());

        maxConfig.inverted(false);
        maxConfig.idleMode(IdleMode.kCoast);
        maxConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);

        flexConfig.encoder
                .positionConversionFactor(IntakeConstants.kPositionFactor)
                .velocityConversionFactor(IntakeConstants.kVelocityFactor);
        flexConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        flexConfig.closedLoop.feedForward
                .kS(IntakeConstants.kIntakeStatic.get())
                .kV(IntakeConstants.kIntakeFF.get());

        flexConfig.inverted(false);
        flexConfig.idleMode(IdleMode.kCoast);
        flexConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);
    }

    public static SparkMaxConfig getMaxConfig()
    {
        return maxConfig;
    }

    public static SparkFlexConfig getFlexConfig(){
        return flexConfig;
    }
}