// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configs;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.constants.GlobalConstants;
import frc.robot.constants.LauncherConstants;

/** Add your docs here. */
public class TalonFxConfig {
    public static final TalonFXConfiguration config = new TalonFXConfiguration();
    static {
        config.Feedback.SensorToMechanismRatio = LauncherConstants.kPositionFactor;
        
        var slot0Lead = config.Slot0;
        slot0Lead.kP = LauncherConstants.kLauncherP.get();
        slot0Lead.kI = LauncherConstants.kLauncherI.get();
        slot0Lead.kD = LauncherConstants.kLauncherD.get();
        slot0Lead.kV = LauncherConstants.kLauncherkV.get(); // This is your Velocity Feedforward
        slot0Lead.kS = LauncherConstants.kLauncherStatic.get(); // This is your Static Feedforward (The "Oomph")

        config.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        config.CurrentLimits.SupplyCurrentLimit = GlobalConstants.kMediumCurrentLimit;
    }

    public static TalonFXConfiguration getConfig() {
        return config;
    }
}
