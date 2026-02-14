package frc.robot.util.hardware;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class MockSparkMax implements MotorControllerWrapper {
    private double m_lastSpeed = 0;
    private double m_lastPos = 0;
    private double m_target = 0;
    private ControlType m_type;
    // Removed m_config as setConfiguration is removed from interface

    @Override
    public void set(double speed) {
        // We save the speed just for debugging, but we don't talk to CAN
        this.m_lastSpeed = speed;
    }

    @Override
    public double getPosition() {
        return this.m_lastPos; // The ghost never moves
    }

    @Override
    public double getVelocity() {
        return 0.0; // The ghost has no speed
    }

    @Override
    public void setPosition(double pos) {
        this.m_lastPos = pos;
    }

    @Override
    public double getTarget() {
        return this.m_target;
    }

    @Override
    public void setTargetValue(double value, ControlType type) {
        this.m_target = value;
        this.m_type = type;
    }

    // setConfiguration(SparkMaxConfig config) is removed from interface, so remove from here too.

    @Override
    public void setOutputVoltage(double voltage) {
        // No-op for mock
    }

    @Override
    public double getOutputCurrent() {
        return 0.0; // No current for mock
    }

    @Override
    public void setPID(double p, double i, double d, double ff) {
        // No-op for mock
    }

    @Override
    public void setMaxAccel(double accel) {
        // No-op for mock
    }

    @Override
    public double getPositionConversion() {
        return 0.0; // No conversion for mock
    }

    @Override
    public SparkMax getSparkMax() {
        return null; // Mock does not have a real SparkMax
    }

    @Override
    public TalonFX getTalonFX() {
        return null; // Mock does not have a real TalonFX
    }

    @Override
    public void follow(MotorControllerWrapper leader, boolean invert) {
        // No-op for mock
    }

    // setConfiguration(SparkMaxConfig config) is removed from interface, so remove from here too.

    @Override
    public void setOutputVoltage(double voltage) {
        // No-op for mock
    }

    @Override
    public double getOutputCurrent() {
        return 0.0; // No current for mock
    }

    @Override
    public void setPID(double p, double i, double d, double ff) {
        // No-op for mock
    }

    @Override
    public void setMaxAccel(double accel) {
        // No-op for mock
    }

    @Override
    public double getPositionConversion() {
        return 0.0; // No conversion for mock
    }

    @Override
    public SparkMax getSparkMax() {
        return null; // Mock does not have a real SparkMax
    }

    @Override
    public TalonFX getTalonFX() {
        return null; // Mock does not have a real TalonFX
    }

    @Override
    public void follow(MotorControllerWrapper leader, boolean invert) {
        // No-op for mock
    }

    @Override
    public void setConfiguration(SparkMaxConfig config) {
        this.m_config = config;
    }

    @Override
    public void updateHardwarePID(double p, double i, double d, double kV , double kS){
        this.m_config.closedLoop.feedForward
        .kV(kV)
        .kS(kS);

        this.m_config.closedLoop.pid(p, i, d);
    }

    @Override
    public com.revrobotics.spark.SparkClosedLoopController getPIDController() {
        return null;
    }
}
