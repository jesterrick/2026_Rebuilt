package frc.robot.util.hardware;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
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
        

    public RealSparkMax(int deviceId, SparkMaxConfig config, boolean isAbsolute) {
        m_motor = new SparkMax(deviceId, MotorType.kBrushless);
        m_controller = m_motor.getClosedLoopController();
        
        if (isAbsolute) {
            m_absoluteEncoder = m_motor.getAbsoluteEncoder();
        } else {
            m_relativeEncoder = m_motor.getEncoder();
        }

        // You can expand this config for PID or Current Limits later
        m_motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void set(double speed) {
        m_motor.set(speed);
    }

    @Override
    public void setPosition(double position) {
        m_motor.getEncoder().setPosition(position);
    }

    @Override
    public void setTargetValue(double value, ControlType controlType) {
        // This replaces the "m_ClosedLoopController.setReference" in your launcher
        m_controller.setSetpoint(value, controlType);
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
    public void setConfiguration(SparkMaxConfig config){
        m_motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    };

}