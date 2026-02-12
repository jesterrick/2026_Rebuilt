package frc.robot.util.hardware;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkMaxConfig;

public class MockSparkMax implements MotorControllerWrapper {
    private double m_lastSpeed = 0;
    private double m_lastPos = 0;
    private double m_target = 0;
    private ControlType m_type;
    private SparkMaxConfig m_config;

    @Override
    public void set(double speed) {
        // We save the speed just for debugging, but we don't talk to CAN
        m_lastSpeed = speed;
    }

    @Override
    public double getPosition() {
        return m_lastPos; // The ghost never moves
    }

    @Override
    public double getVelocity() {
        return 0.0; // The ghost has no speed
    }

    @Override
    public void setPosition(double pos) {
        m_lastPos = pos;
    }

    @Override
    public void setTargetValue(double value, ControlType type) {
        m_target = value;
        m_type = type;
    }

    @Override
    public void setConfiguration(SparkMaxConfig config) {
        m_config = config;
    }
}