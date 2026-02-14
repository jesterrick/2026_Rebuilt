# FRC Tunable Constants Refactor Plan

## Goal
Refactor the existing hardcoded constants into a "Tunable" system using NetworkTables 4 (NT4). This allows for real-time PID and speed tuning without redeploying code, while maintaining safety guards.

## 1. Core Utilities (Create these first)
Create these two files in `src/main/java/frc/robot/util/`.

### TunableNumber.java
```java
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

```

### TuningManager.java

```java
package frc.robot.util;

public class TuningManager {
    // Set to false before competition to disable Shuffleboard overhead
    public static final boolean kTuningMode = true;

    public static TunableNumber register(String path, double defaultValue) {
        return new TunableNumber(path, defaultValue);
    }
}

```

## 2. Refactoring Instructions for Constants Folder

Search all files in `src/main/java/frc/robot/constants/`.

* **DO NOT** change CAN IDs, Port numbers, or Physical dimensions (Inches/Meters).
* **DO** change variables related to **PID Gains (kP, kI, kD)**, **Max/Min Speeds**, and **RPMs**.
* **Conversion Pattern**:
Change `public static final double kDriveP = 0.04;`
to `public static final TunableNumber kDriveP = TuningManager.register("Drive/kP", 0.04);`

## 3. Subsystem Implementation Instructions

Update all Subsystems (e.g., `DriveSubsystem.java`) to use these new values.

* Update hardware references: Change `kDriveP` to `kDriveP.get()`.
* Add Update Logic: Inside the `periodic()` method, add a check:

```java
if (edu.wpi.first.wpilibj.DriverStation.isDisabled()) {
    if (DriveConstants.kDriveP.hasChanged()) {
        // Apply the new value to the motor controller
        m_frontLeft.getPIDController().setP(DriveConstants.kDriveP.get());
    }
}

```

## 4. Constraints

* Use `NetworkTableInstance.getDefault()` for NT4 compatibility.
* Ensure all imports are correctly handled.
* Provide a diff for `DriveConstants.java` and `DriveSubsystem.java` as a sample before proceeding with other files.
