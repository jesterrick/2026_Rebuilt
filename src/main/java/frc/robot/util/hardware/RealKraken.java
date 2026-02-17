package frc.robot.util.hardware;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkBase.ControlType;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.revrobotics.spark.SparkMax; // Required for getSparkMax() to compile, will be addressed

public class RealKraken implements MotorControllerWrapper {
    private final TalonFX m_motor;
    private final DutyCycleOut m_dutyCycleOut = new DutyCycleOut(0);
    private final VelocityVoltage m_velocityVoltage = new VelocityVoltage(0);
    private final PositionVoltage m_positionVoltage = new PositionVoltage(0);
    private final VoltageOut m_voltageOut = new VoltageOut(0);
    private final Follower m_followerRequest = new Follower(0, MotorAlignmentValue.Aligned);

    public RealKraken(int deviceId, TalonFXConfiguration config) {
        this.m_motor = new TalonFX(deviceId);
        this.m_motor.getConfigurator().apply(config);
    }

    @Override
    public void set(double speed) {
        // Simple speed control using duty cycle (percent output)
        m_motor.setControl(m_dutyCycleOut.withOutput(speed));
    }

    @Override
    public void setPosition(double position) {
        m_motor.setControl(m_positionVoltage.withPosition(position));
    }

    @Override
    public void setTargetValue(double value, ControlType controlType) {
        // This is a rough mapping and might need refinement based on actual usage.
        // ControlType is from REVLib, so we're making an assumption here.
        switch (controlType) {
            case kDutyCycle:
                m_motor.setControl(m_dutyCycleOut.withOutput(value));
                break;
            case kVelocity:
                // Assuming value is in RPM, need to convert to rotations/second for Phoenix6
                // For now, let's assume it's already in rotations/second for simplicity
                m_motor.setControl(m_velocityVoltage.withVelocity(value));
                break;
            case kPosition:
                m_motor.setControl(m_positionVoltage.withPosition(value));
                break;
            case kVoltage:
                m_motor.setControl(m_voltageOut.withOutput(value));
                break;
            default:
                System.err.println("Unsupported ControlType for RealKraken: " + controlType);
                // Fallback to duty cycle
                m_motor.setControl(m_dutyCycleOut.withOutput(0));
                break;
        }
    }

    @Override
    public double getPosition() {
        // Phoenix6 velocity is in rotations/second by default
        return m_motor.getPosition().getValueAsDouble();
    }

    @Override
    public double getVelocity() {
        // Phoenix6 velocity is in rotations/second by default
        return m_motor.getVelocity().getValueAsDouble();
    }

    @Override
    public void setOutputVoltage(double voltage) {
        m_motor.setControl(m_voltageOut.withOutput(voltage));
    }

    @Override
    public double getOutputCurrent() {
        // Phoenix6 has SupplyCurrent (from bus) and StatorCurrent (through motor
        // windings)
        // Stator current is usually what getOutputCurrent() implies for other
        // controllers
        return m_motor.getStatorCurrent().getValueAsDouble();
    }

    @Override
    public void applyConstants(MotorConstants constants) {
        TalonFXConfiguration config = new TalonFXConfiguration();
        m_motor.getConfigurator().refresh(config); // Get current config to modify

        // PIDF
        config.Slot0.kP = constants.getP();
        config.Slot0.kI = constants.getI();
        config.Slot0.kD = constants.getD();
        config.Slot0.kV = constants.getV();
        config.Slot0.kS = constants.getS();

        // Neutral Behavior
        config.MotorOutput.NeutralMode = (constants.getNeutralBehavior() == MotorSettings.NeutralBehavior.kBrake)
                ? NeutralModeValue.Brake
                : NeutralModeValue.Coast;

        // Current Limit
        config.CurrentLimits.SupplyCurrentLimit = constants.getCurrentLimit();
        config.CurrentLimits.SupplyCurrentLimitEnable = true;

        // Conversion Ratio
        config.Feedback.SensorToMechanismRatio = constants.getConversionRatio();

        // Motor Rotation
        config.MotorOutput.Inverted = (constants.getMotorRotation() == MotorSettings.MotorRotation.kCounterClockwise)
                ? com.ctre.phoenix6.signals.InvertedValue.CounterClockwise_Positive
                : com.ctre.phoenix6.signals.InvertedValue.Clockwise_Positive;

        // Motion Profiling (needs to be configured more deeply for Phoenix6)
        // For now, only set MaxAcceleration if it's explicitly enabled in constants
        if (constants.isMotionProfilingEnabled()) {
            // Phoenix6 Motion Magic requires setting a whole profile.
            // This is a simplification, may need dedicated MotionMagic configs
            config.MotionMagic.MotionMagicAcceleration = constants.getMaxAcceleration();
            config.MotionMagic.MotionMagicCruiseVelocity = constants.getCruiseVelocity();
        }

        // Soft Limits (needs more complete configuration for Phoenix6)
        // Phoenix6 uses "Feedback.SensorToMechanismRatio" and "SoftwareLimit" for soft
        // limits
        // This is a simplification.
        if (constants.isForwardLimitEnabled() || constants.isReverseLimitEnabled()) {
            var softLimits = config.SoftwareLimitSwitch;
            softLimits.ForwardSoftLimitEnable = constants.isForwardLimitEnabled();
            if (constants.isForwardLimitEnabled()) {
                softLimits.ForwardSoftLimitThreshold = constants.getForwardLimit();
            }
            softLimits.ForwardSoftLimitEnable = constants.isReverseLimitEnabled();
            if (constants.isReverseLimitEnabled()) {
                softLimits.ReverseSoftLimitThreshold = constants.getReverseLimit();
            }
        }

        m_motor.getConfigurator().apply(config);
    }

    @Override
    public SparkMax getSparkMax() {
        return null;
    }

    public TalonFX getTalonFX() {
        return this.m_motor;
    }

    @Override
    public void follow(MotorControllerWrapper leader, boolean invert) {
        if (leader instanceof RealSparkMax) {
            System.err.println("RealKraken cannot directly follow a RealSparkMax.");
        } else if (leader instanceof RealKraken) {
            int leaderID = leader.getTalonFX().getDeviceID();
            MotorAlignmentValue align = MotorAlignmentValue.Aligned;
            if (invert) {
                align = MotorAlignmentValue.Opposed;
            }
            m_followerRequest.LeaderID = leaderID;
            m_followerRequest.MotorAlignment = align;

            this.m_motor.setControl(m_followerRequest);
        } else {
            System.err.println("RealKraken cannot follow an unknown motor controller type.");
        }
    }

    @Override
    public void stop() {
        m_motor.setControl(m_dutyCycleOut.withOutput(0));
        m_motor.setNeutralMode(NeutralModeValue.Brake);
    }
}
