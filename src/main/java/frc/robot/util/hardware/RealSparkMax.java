package frc.robot.util.hardware;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.ResetMode;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;

public class RealSparkMax implements MotorControllerWrapper {
    private final SparkMax m_motor;
    private final SparkClosedLoopController m_controller;
    private RelativeEncoder m_relativeEncoder;
    private AbsoluteEncoder m_absoluteEncoder;
    private boolean m_useAbsolute;
    private SparkMaxConfig currentConfig; // Store the current configuration

    public RealSparkMax(int deviceId, SparkMaxConfig config, boolean isAbsolute) {
        this.m_motor = new SparkMax(deviceId, MotorType.kBrushless);
        this.m_controller = this.m_motor.getClosedLoopController();
        this.currentConfig = config; // Store the initial config

        if (isAbsolute) {
            m_absoluteEncoder = this.m_motor.getAbsoluteEncoder();
        } else {
            m_relativeEncoder = this.m_motor.getEncoder();
        }

        this.m_motor.configure(currentConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public SparkClosedLoopController getPIDController() {
        return this.m_controller;
    }

    @Override
    public void set(double speed) {
        this.m_motor.set(speed);
    }

    @Override
    public void setPosition(double position) {
        this.m_motor.getEncoder().setPosition(position);
    }

    @Override
    public void setTargetValue(double value, ControlType controlType) {
        this.m_controller.setSetpoint(value, controlType);
    }

    @Override
    public double getPosition() {
        return m_useAbsolute ? m_absoluteEncoder.getPosition() : m_relativeEncoder.getPosition();
    }

    @Override
    public double getVelocity() {
        return m_useAbsolute ? m_absoluteEncoder.getVelocity() : m_relativeEncoder.getVelocity();
    }

    @Override
    public void setOutputVoltage(double voltage) {
        this.m_motor.setVoltage(voltage);
    }

    @Override
    public double getOutputCurrent() {
        return this.m_motor.getOutputCurrent();
    }

    @Override
    public void follow(MotorControllerWrapper leader, boolean invert) {
        SparkMaxConfig config = new SparkMaxConfig();
        config.follow(m_motor, invert);
        this.m_motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    @Override
    public void applyConstants(MotorConstants constants) {
        SparkMaxConfig config = new SparkMaxConfig();

        config.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(constants.getP(), constants.getI(), constants.getD());

        config.closedLoop.feedForward
                .kV(constants.getV())
                .kS(constants.getS());

        if (constants.isMotionProfilingEnabled()) {
            config.closedLoop.maxMotion
                    .maxAcceleration(constants.getMaxAcceleration())
                    .allowedProfileError(constants.getAllowedError())
                    .cruiseVelocity(constants.getCruiseVelocity())
                    .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal);
        }

        config.idleMode(constants.getNeutralBehavior() == MotorSettings.NeutralBehavior.kBrake ? IdleMode.kBrake
                : IdleMode.kCoast);

        if (constants.isForwardLimitEnabled()) {
            config.softLimit
                    .forwardSoftLimitEnabled(true)
                    .forwardSoftLimit(constants.getForwardLimit());
        }

        if (constants.isReverseLimitEnabled()) {
            config.softLimit
                    .reverseSoftLimitEnabled(true)
                    .reverseSoftLimit(constants.getReverseLimit());
        }

        // Motor Rotation
        config.inverted(constants.getMotorRotation() == MotorSettings.MotorRotation.kCounterClockwise);

        // Current Limit
        config.smartCurrentLimit(constants.getCurrentLimit());

        // Configure the leader motor's encoder for position and velocity conversion
        // factors.
        config.encoder
                .positionConversionFactor(constants.getConversionRatio()) // Set units to inches
                .velocityConversionFactor(constants.getConversionRatio()); // Set units to inches/sec

        this.m_motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public SparkMax getSparkMax() {
        return this.m_motor;
    }

    public void setPID(double p, double i, double d, double v, double s) {
        SparkMaxConfig config = new SparkMaxConfig();

        config.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(p, i, d);
        config.closedLoop.feedForward
                .kV(v)
                .kS(s);

        this.m_motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }
}