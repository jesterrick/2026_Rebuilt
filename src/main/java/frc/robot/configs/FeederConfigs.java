// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configs;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.constants.GlobalConstants;

/** Add your docs here. */
public class FeederConfigs {
    public static final SparkMaxConfig config = new SparkMaxConfig();

    static {
        config.inverted(false);
        config.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);    
        config.idleMode(IdleMode.kCoast); 
    }
}
