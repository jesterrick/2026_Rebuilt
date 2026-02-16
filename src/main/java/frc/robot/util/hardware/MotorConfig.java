// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util.hardware;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.constants.ExtenderConstants;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.InvertedValue;

/** Add your docs here. */
public class MotorConfig {
    private final SparkMaxConfig sparkConfig;
    private final TalonFXConfiguration talonConfig;

    public MotorConfig() {
        this.sparkConfig = new SparkMaxConfig();
        this.talonConfig = new TalonFXConfiguration();
    }

    public MotorConfig setNeutralBehavior(MotorSettings.NeutralBehavior behavior) {
        // Translate for REV
        sparkConfig.idleMode(behavior == MotorSettings.NeutralBehavior.kBrake
                ? SparkMaxConfig.IdleMode.kBrake
                : SparkMaxConfig.IdleMode.kCoast);

        // Translate for CTRE
        talonConfig.MotorOutput.NeutralMode = (behavior == MotorSettings.NeutralBehavior.kBrake)
                ? com.ctre.phoenix6.signals.NeutralModeValue.Brake
                : com.ctre.phoenix6.signals.NeutralModeValue.Coast;

        return this;
    }

    public MotorConfig setCurrentLimit(int amps) {
        sparkConfig.smartCurrentLimit(amps);
        talonConfig.CurrentLimits.SupplyCurrentLimit = amps;
        talonConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        return this;
    }

    public MotorConfig setPID(double p, double i, double d, double v, double s) {
        sparkConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(p, i, d);
        sparkConfig.closedLoop.feedForward
                .kV(v)
                .kS(s);

        var slot0 = talonConfig.Slot0;
        slot0.kP = p;
        slot0.kI = i;
        slot0.kD = i;
        slot0.kV = v;
        slot0.kS = s;

        return this;
    }

    public MotorConfig setPID(double p, double i, double d, double v) {
        return setPID(p, i, d, v, 0.0);
    }

    public MotorConfig setPID(double p, double i, double d) {
        return setPID(p, i, d, 0.0, 0.0);
    }

    public MotorConfig setConversionRatio(double ratio) {
        sparkConfig.encoder
                .positionConversionFactor(ratio)
                .velocityConversionFactor(ratio);

        talonConfig.Feedback.SensorToMechanismRatio = ratio;

        return this;
    }

    public MotorConfig setMotorRotation(MotorSettings.MotorRotation direction) {
        sparkConfig.inverted((direction == MotorSettings.MotorRotation.kCounterClockwise)
                ? false
                : true);

        talonConfig.MotorOutput.Inverted = (direction == MotorSettings.MotorRotation.kCounterClockwise)
                ? InvertedValue.CounterClockwise_Positive
                : InvertedValue.Clockwise_Positive;

        return this;
    }

    public MotorConfig setMotionConfigs(double cruiseVelocity, double maxAcceleration, double allowedError) {
        sparkConfig.closedLoop.maxMotion
                .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal)
                .cruiseVelocity(cruiseVelocity)
                .maxAcceleration(maxAcceleration)
                .allowedProfileError(allowedError);

        return this;
    }

    public MotorConfig setForwardLimit(double forwardLimit){
        sparkConfig.softLimit
            .forwardSoftLimitEnabled(true)
            .forwardSoftLimit(forwardLimit);
            
        return this;
    }

    public MotorConfig setReversLimit(double reversLimit) {
        sparkConfig.softLimit
            .reverseSoftLimitEnabled(true)
            .reverseSoftLimit(reversLimit);


        return this;
    }

    public SparkMaxConfig getSparkConfig() {
        return sparkConfig;
    }

    public TalonFXConfiguration getTalonConfig() {
        return talonConfig;
    }

    /**
     * Creates and configures a new MotorConfig object from a class that implements the MotorConstants interface.
     * This allows for a standardized and boilerplate-free way to define motor settings.
     *
     * @param constants An object that implements the MotorConstants interface.
     * @return A fully configured MotorConfig object.
     */
    public static MotorConfig fromConstants(MotorConstants constants) {
        MotorConfig config = new MotorConfig();

        // Apply all settings from the constants provider
        config.setPID(constants.getP(), constants.getI(), constants.getD(), constants.getV(), constants.getS());
        config.setNeutralBehavior(constants.getNeutralBehavior());
        config.setCurrentLimit(constants.getCurrentLimit());
        config.setConversionRatio(constants.getConversionRatio());
        config.setMotorRotation(constants.getMotorRotation());

        if (constants.isMotionProfilingEnabled()) {
            config.setMotionConfigs(constants.getCruiseVelocity(), constants.getMaxAcceleration(), constants.getAllowedError());
        }

        if (constants.isForwardLimitEnabled()) {
            config.setForwardLimit(constants.getForwardLimit());
        }

        if (constants.isReverseLimitEnabled()) {
            config.setReversLimit(constants.getReverseLimit());
        }

        return config;
    }
}
