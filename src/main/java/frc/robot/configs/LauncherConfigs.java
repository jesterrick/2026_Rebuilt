// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configs;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.constants.GlobalConstants;
import frc.robot.constants.LauncherConstants;

/** Add your docs here. */
public class LauncherConfigs {
    public static final SparkMaxConfig config = new SparkMaxConfig();
    
    static {
        config.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pid(LauncherConstants.kLauncherP, LauncherConstants.kLauncherI, LauncherConstants.kLauncherD);
        
        config.closedLoop.feedForward
        .kV(LauncherConstants.kLauncherkV);

        config.inverted(false);        
        config.idleMode(IdleMode.kCoast); 
        config.smartCurrentLimit(GlobalConstants.kMediumCurrentLimit);
    }
}
