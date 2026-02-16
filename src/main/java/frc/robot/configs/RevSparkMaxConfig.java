// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configs;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.constants.ExtenderConstants;
import frc.robot.constants.GlobalConstants;

/** Add your docs here. */
public class RevSparkMaxConfig {
    public static final SparkMaxConfig config = new SparkMaxConfig();

    static {
        // Configure the motor's encoder for position and velocity conversion factors.
        config.encoder
                .positionConversionFactor(ExtenderConstants.kPositionFactor)
                .velocityConversionFactor(ExtenderConstants.kVelocityFactor);

        // Configure PID constants for closed-loop control.
        config.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(ExtenderConstants.kExtenderP.get(), ExtenderConstants.kExtenderI.get(), ExtenderConstants.kExtenderD.get());

        // Configure Feedforward constants to improve control accuracy.
        config.closedLoop.feedForward
                .kV(ExtenderConstants.kExtenderFF.get())
                .kS(ExtenderConstants.kExtenderStatic.get());

        // Configure motion profiling parameters for smooth movement.
        config.closedLoop.maxMotion
                .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal)
                .cruiseVelocity(ExtenderConstants.kExtenderCruiseVelocity)
                .maxAcceleration(ExtenderConstants.kExtAcceleration.get())
                .allowedProfileError(ExtenderConstants.kExtenderAllowedError.get());

        // Configure soft limits to prevent physical over-extension or retraction.
        config.softLimit
                .forwardSoftLimitEnabled(true)
                .forwardSoftLimit(ExtenderConstants.kExtenderMotorOut)
                .reverseSoftLimitEnabled(true)
                .reverseSoftLimit(ExtenderConstants.kExtenderMotorIn);

        // Apply the inverted setting to the leader motor.
        config.inverted(false);
        // Set a smart current limit to protect the motor and battery.
        config.smartCurrentLimit(GlobalConstants.kMediumCurrentLimit);
        // Set the idle mode to brake when the motor is not actively driven.
        config.idleMode(IdleMode.kBrake);
    }

    public static SparkMaxConfig getConfig() {
        return config;
    }
}
