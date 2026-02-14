// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;

import frc.robot.constants.FeederConstants;
import frc.robot.constants.GlobalConstants;

import com.revrobotics.spark.config.SparkMaxConfig;

/** Add your docs here. */
public class FeederConfigs {
    public static final SparkMaxConfig maxConfig = new SparkMaxConfig();
    public static final SparkFlexConfig flexConfig = new SparkFlexConfig();
    
    static {
        maxConfig.encoder
            .positionConversionFactor(FeederConstants.kPositionFactor)
            .velocityConversionFactor(FeederConstants.kPositionFactor);

        maxConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        maxConfig.closedLoop.feedForward
            .kS(FeederConstants.kFeederStatic.get())
            .kV(FeederConstants.kFeederFF.get());

        maxConfig.inverted(false);        
        maxConfig.idleMode(IdleMode.kBrake); 
        maxConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);

        flexConfig.encoder
            .positionConversionFactor(FeederConstants.kPositionFactor)
            .velocityConversionFactor(FeederConstants.kPositionFactor);

        flexConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        flexConfig.closedLoop.feedForward
            .kS(FeederConstants.kFeederStatic.get())
            .kV(FeederConstants.kFeederFF.get());

        flexConfig.inverted(false);        
        flexConfig.idleMode(IdleMode.kBrake); 
        flexConfig.smartCurrentLimit(GlobalConstants.kLowCurrentLimit);
    }

    public static SparkFlexConfig getFlexConfig() {
        return flexConfig;
    }

    public static SparkMaxConfig getMaxConfig() {
        return maxConfig;
    }
}
