package frc.robot.configs;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.constants.GlobalConstants;
import com.revrobotics.spark.config.SparkMaxConfig;

public class RollerConfigs {
    public static final SparkMaxConfig config = new SparkMaxConfig();

    static{
        config.inverted(false);
        config.idleMode(IdleMode.kBrake);
        config.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);
    }

}


