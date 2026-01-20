package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.constants.ExtenderConstants;
import frc.robot.constants.GlobalConstants;

public class ExtenderConfigs {
    public static final SparkMaxConfig config = new SparkMaxConfig();

    static {
        config.encoder
            .positionConversionFactor(ExtenderConstants.kPositionFactor) // Set units to inches
            .velocityConversionFactor(ExtenderConstants.kPositionFactor / 60.0); // Set units to inches/sec

        config.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(ExtenderConstants.kIntakeP, ExtenderConstants.kIntakeI, ExtenderConstants.kIntakeD);

        config.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(ExtenderConstants.kExtenderMotorOut)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(ExtenderConstants.kExtenderMotorIn);

        config.inverted(false);
        config.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        config.idleMode(IdleMode.kBrake); 
    }
}
