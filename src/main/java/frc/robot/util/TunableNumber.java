package frc.robot.util;

import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSubOption;

public class TunableNumber {
    private final DoubleEntry entry;
    private double lastValue;

    public TunableNumber(String path, double defaultValue) {
        this.entry = NetworkTableInstance.getDefault()
            .getDoubleTopic("Tuning/" + path)
            .getEntry(defaultValue, PubSubOption.periodic(0.02));
        this.lastValue = defaultValue;
    }

    public double get() { return entry.get(); }

    public boolean hasChanged() {
        double currentValue = get();
        if (currentValue != lastValue) {
            lastValue = currentValue;
            return true;
        }
        return false;
    }
}
