package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.constants.GlobalConstants;
import frc.robot.constants.RollerConstants;

import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class RollerConfigs {
    public static final SparkMaxConfig maxConfig = new SparkMaxConfig();
    public static final SparkFlexConfig flexConfig = new SparkFlexConfig();

    static{
        maxConfig.encoder
            .positionConversionFactor(RollerConstants.kPositionFactor)
            .velocityConversionFactor(RollerConstants.kPositionFactor);

        maxConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        maxConfig.closedLoop.feedForward
            .kS(RollerConstants.kRollerStatic.get())
            .kV(RollerConstants.kRollerFF.get());

        maxConfig.inverted(false);
        maxConfig.idleMode(IdleMode.kCoast);
        maxConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);

         flexConfig.encoder
            .positionConversionFactor(RollerConstants.kPositionFactor)
            .velocityConversionFactor(RollerConstants.kPositionFactor);

        flexConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        flexConfig.closedLoop.feedForward
            .kS(RollerConstants.kRollerStatic.get())
            .kV(RollerConstants.kRollerFF.get());

        flexConfig.inverted(false);
        flexConfig.idleMode(IdleMode.kCoast);
        flexConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);
    }

    public static SparkFlexConfig getFlexConfig() {
        return flexConfig;
    }

    public static SparkMaxConfig getMaxConfig() {
        return maxConfig;
    }

}


