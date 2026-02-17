package frc.robot.util.hardware;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;

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

    default SparkClosedLoopController getPIDController() {
        return null;
    }

    default void setPosition(double position) {
        // Mock does nothing, or you could update a internal 'fake' position variable
    }

    default void setOutputVoltage(double voltage){}

    default double getOutputCurrent() { return 0.0; }

    /** Used by Climber and Extender. Default returns 0. */
    default double getPosition() { return 0.0; }

    /** Used by Launcher and Swerve. Default returns 0. */
    default double getVelocity() { return 0.0; }

    /** Used to stop any motor safely. */
    default void stop() { set(0); }

    default double getPositionConversion(){return 0.0;}

    default double getTarget(){return 0.0;}

    default double getSpeed(){return 0.0;}

    default SparkMaxConfig getSparkMaxConfig(){ return new SparkMaxConfig();}

    /**
     * @return The underlying SparkMax object, or null if not a SparkMax.
     */
    default SparkMax getSparkMax() { return null; }

     /**
     * @return The underlying SparkFlex object, or null if not a SparkFlex.
     */
    default SparkFlex getSparkFlex() { return null; }

    /**
     * @return The underlying TalonFX object, or null if not a TalonFX.
     */
    default TalonFX getTalonFX() { return null; }

    /**
     * Sets this motor controller to follow another.
     * @param leader The motor controller to follow.
     * @param invert True if this motor should be inverted relative to the leader.
     */
    default void follow(MotorControllerWrapper leader, boolean invert) {}

    /**
     * Applies new configuration settings from a MotorConstants object.
     * This is used for live tuning during disabled mode.
     * @param constants The MotorConstants object containing the new configuration.
     */
    void applyConstants(MotorConstants constants);

    default void setPID(double p, double i, double d, double v, double s) {};
}
