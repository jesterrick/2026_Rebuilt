package frc.robot.util.hardware;

/**
 * Universal motor settings to prevent "Enum Soup" in your subsystems.
 */
public class MotorSettings {
    public enum NeutralBehavior {
        kBrake, kCoast
    }

    public enum MotorType {
        kBrushless, kBrushed
    }

    public enum MotorRotation {
        kClockwise, kCounterClockwise
    }
}