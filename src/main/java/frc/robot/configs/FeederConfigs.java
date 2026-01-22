// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configs;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.constants.GlobalConstants;
import com.revrobotics.spark.config.SparkMaxConfig;

/** Add your docs here. */
public class FeederConfigs {
    public static final SparkMaxConfig config = new SparkMaxConfig();
    
    static {
        config.inverted(false);        
        config.idleMode(IdleMode.kBrake); 
        config.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);
    }

}
