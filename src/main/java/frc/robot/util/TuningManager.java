package frc.robot.util;

public class TuningManager {
    // Set to false before competition to disable Shuffleboard overhead
    public static final boolean kTuningMode = true;

    public static TunableNumber register(String path, double defaultValue) {
        return new TunableNumber(path, defaultValue);
    }
}
