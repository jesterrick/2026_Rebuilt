package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.constants.GlobalConstants;
import frc.robot.constants.RollerConstants;

import com.revrobotics.spark.config.SparkMaxConfig;

public class RollerConfigs {
    public static final SparkMaxConfig config = new SparkMaxConfig();

    static{
        config.encoder
            .positionConversionFactor(RollerConstants.kPositionFactor)
            .velocityConversionFactor(RollerConstants.kPositionFactor);

        config.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        config.closedLoop.feedForward
            .kS(RollerConstants.kRollerStatic.get())
            .kV(RollerConstants.kRollerFF.get());

        config.inverted(false);
        config.idleMode(IdleMode.kCoast);
        config.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);
    }

}


