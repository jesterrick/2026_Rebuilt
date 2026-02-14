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
    public void updateHardwarePID(double p, double i, double d, double kV, double kS) {
        TalonFXConfiguration config = new TalonFXConfiguration();
        m_motor.getConfigurator().refresh(config); // Get current config to modify
        config.Slot0.kP = p;
        config.Slot0.kI = i;
        config.Slot0.kD = d;
        config.Slot0.kV = kV;
        config.Slot0.kS = kS; // Phoenix6 uses kS for static feedforward, k V for velocity feedforward, this
                              // is a rough mapping

        m_motor.getConfigurator().apply(config);
    }

    @Override
    public void setMaxAccel(double accel) {
        // Phoenix6 has motion magic for this (MotionMagicVoltage, MotionMagicVelocity)
        // Implementing this would involve setting motion magic configs.
        // For now, we'll log a warning.
        System.err.println("RealKraken does not directly support setMaxAccel without full motion magic setup.");
    }

    @Override
    public SparkMax getSparkMax() {
        // This method is SparkMax specific. RealKraken returns its TalonFX.
        return null; // Or throw UnsupportedOperationException
    }

    public TalonFX getTalonFX() {
        return this.m_motor;
    }

    @Override
    public void follow(MotorControllerWrapper leader, boolean invert) {
        if (leader instanceof RealSparkMax) {
            // Following a SparkMax from a Kraken is not directly supported by Phoenix6
            // Follower control
            // This would require custom logic or a more advanced wrapper
            System.err.println("RealKraken cannot directly follow a RealSparkMax.");
        } else if (leader instanceof RealKraken) {
            int leaderID = leader.getTalonFX().getDeviceID();
            MotorAlignmentValue align = MotorAlignmentValue.Aligned;
            if(invert)
            {
                align = MotorAlignmentValue.Opposed;
            }
            // This is the "Nuclear Option" if the constructor is being difficult:
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
        m_motor.setNeutralMode(NeutralModeValue.Brake); // Assuming we want brake when stopped
    }
}
