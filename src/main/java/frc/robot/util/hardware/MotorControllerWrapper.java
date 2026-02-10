package frc.robot.util.hardware;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkMaxConfig;

public interface MotorControllerWrapper {
    /** Basic speed control (-1.0 to 1.0) used by all 7 subsystems. */
    void set(double speed);

    /**
     * Sets the closed-loop setpoint (Velocity, Position, etc.)
     */
    default void setTargetValue(double value, ControlType controlType) {
        // Mock behavior: just convert to a rough percentage for testing
        set(value / 5700.0); 
    }

    default void setPosition(double position) {
        // Mock does nothing, or you could update a internal 'fake' position variable
    }

    default void setConfiguration(SparkMaxConfig config){};

    default void setOutputVoltage(double voltage){}

    default double getOutputCurrent() { return 0.0; }

    /** Used by Climber and Extender. Default returns 0. */
    default double getPosition() { return 0.0; }

    /** Used by Launcher and Swerve. Default returns 0. */
    default double getVelocity() { return 0.0; }

    /** Used to stop any motor safely. */
    default void stop() { set(0); }

    default void setPID(double p, double i, double d, double ff){}

    default void setMaxAccel(double accel){}

    default double getPositionConversion(){return 0.0;}
}