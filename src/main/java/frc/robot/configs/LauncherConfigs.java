// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configs;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.InvertedValue;

import frc.robot.constants.GlobalConstants;
import frc.robot.constants.LauncherConstants;

/** Add your docs here. */
public class LauncherConfigs {
    public static final TalonFXConfiguration leaderConfig = new TalonFXConfiguration();
    public static final TalonFXConfiguration followConfig = new TalonFXConfiguration();
    
    static {
        leaderConfig.Feedback.SensorToMechanismRatio = LauncherConstants.kPositionFactor;
        
        var slot0Lead = leaderConfig.Slot0;
        slot0Lead.kP = LauncherConstants.kLauncherP.get();
        slot0Lead.kI = LauncherConstants.kLauncherI.get();
        slot0Lead.kD = LauncherConstants.kLauncherD.get();
        slot0Lead.kV = LauncherConstants.kLauncherkV.get(); // This is your Velocity Feedforward
        slot0Lead.kS = LauncherConstants.kLauncherStatic.get(); // This is your Static Feedforward (The "Oomph")

        leaderConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        leaderConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        leaderConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        leaderConfig.CurrentLimits.SupplyCurrentLimit = GlobalConstants.kMediumCurrentLimit;

        followConfig.Feedback.SensorToMechanismRatio = LauncherConstants.kPositionFactor;
        
        var slot0Foll = followConfig.Slot0;
        slot0Foll.kP = LauncherConstants.kLauncherP.get();
        slot0Foll.kI = LauncherConstants.kLauncherI.get();
        slot0Foll.kD = LauncherConstants.kLauncherD.get();
        slot0Foll.kV = LauncherConstants.kLauncherkV.get(); // This is your Velocity Feedforward
        slot0Foll.kS = LauncherConstants.kLauncherStatic.get(); // This is your Static Feedforward (The "Oomph")

        followConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        if (leaderConfig.MotorOutput.Inverted == InvertedValue.CounterClockwise_Positive)
        {
            followConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        } else {
            followConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        }

        followConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        followConfig.CurrentLimits.SupplyCurrentLimit = GlobalConstants.kMediumCurrentLimit;
    }

    public TalonFXConfiguration getTalonFXLeaderConfiguration() {
        return leaderConfig;
    }

    public TalonFXConfiguration getTalonFXFollowConfiguration() {
        return followConfig;
    }
}
