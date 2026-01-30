package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.constants.ExtenderConstants;
import frc.robot.constants.GlobalConstants;

public class ExtenderConfigs {
    public static final SparkMaxConfig config = new SparkMaxConfig();
    public static final SparkMaxConfig config2 = new SparkMaxConfig();

    static {
        config.encoder
            .positionConversionFactor(ExtenderConstants.kPositionFactor) // Set units to inches
            .velocityConversionFactor(ExtenderConstants.kPositionFactor / 60.0); // Set units to inches/sec

        config.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(ExtenderConstants.kIntakeP, ExtenderConstants.kIntakeI, ExtenderConstants.kIntakeD);
        
        config.closedLoop.maxMotion
            .cruiseVelocity(5600 * ExtenderConstants.kExtenderMotorSpeed * (ExtenderConstants.kPositionFactor / 60.0))
            .maxAcceleration(ExtenderConstants.kExtAcceleration)
            .allowedProfileError(0.1);

        config.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(ExtenderConstants.kExtenderMotorOut)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(ExtenderConstants.kExtenderMotorIn);

        config.inverted(false);
        config.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        config.idleMode(IdleMode.kBrake); 

         config2.encoder
            .positionConversionFactor(ExtenderConstants.kPositionFactor) // Set units to inches
            .velocityConversionFactor(ExtenderConstants.kPositionFactor / 60.0); // Set units to inches/sec

        config2.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(ExtenderConstants.kIntakeP, ExtenderConstants.kIntakeI, ExtenderConstants.kIntakeD);
        
        config2.closedLoop.maxMotion
            .cruiseVelocity(5600 * ExtenderConstants.kExtenderMotorSpeed * (ExtenderConstants.kPositionFactor / 60.0))
            .maxAcceleration(ExtenderConstants.kExtAcceleration)
            .allowedProfileError(0.1);

        config2.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(ExtenderConstants.kExtenderMotorOut)
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(ExtenderConstants.kExtenderMotorIn);

        config2.inverted(false);
        config2.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        config2.idleMode(IdleMode.kBrake); 
    
    }
}
