package frc.robot.util.hardware;

import java.util.HashMap;
import java.util.Map;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.constants.GlobalConstants;

public class HardwareFactory {
    private static final Map<Integer, Boolean> connectedDevices = new HashMap<>();
    private static final Map<Integer, String> discoveredBrands = new HashMap<>();
    private static boolean vitalHardwareMissing = false; // The "Kill Switch" flag

    /**
     * Checks if the robot should enter 'Global Shutdown' mode.
     */
    public static boolean isSystemFatal() {
        return vitalHardwareMissing;
    }

    /**
     * Checks if a device ID was found during the initial hardware scan.
     */
    public static boolean isDeviceConnected(int id) {
        return connectedDevices.getOrDefault(id, false);
    }

    public static void scanAndLogHardware(int[] vitalIds, int[] auxiliaryIds) {
        System.out.println("********** STARTING HIERARCHICAL INSPECTION **********");

        // Scan Vitals First
        for (int id : vitalIds) {
            boolean found = probeDevice(id);
            connectedDevices.put(id, found);
            if (!found) {
                vitalHardwareMissing = true; // If a vital motor is gone, we're a brick
                System.out.println("!!! FATAL ERROR: Vital ID " + id + " is MISSING.");
            }
        }

        // Scan Auxiliaries (These don't trip the kill switch)
        for (int id : auxiliaryIds) {
            boolean found = probeDevice(id);
            connectedDevices.put(id, found);
            if (!found) {
                System.out.println("[WARNING] Auxiliary ID " + id + " is missing. Functionality limited.");
            }
        }
        System.out.println("********** INSPECTION COMPLETE **********");
    }

    public static boolean probeDevice(int id) {

        boolean found = false;
        String brand = "Unknown";

        // 1. Try checking for a REV device (Spark Max/Flex)
        try (com.revrobotics.spark.SparkMax sparkProbe = new com.revrobotics.spark.SparkMax(id,
                com.revrobotics.spark.SparkLowLevel.MotorType.kBrushless)) {
            if (sparkProbe.getFirmwareVersion() > 0) {
                found = true;
                brand = "REV (Spark Max/Flex)";
            }
        } catch (Exception e) {
            /* Not a happy Spark */ }

        // 2. If no Spark was found, try checking for a CTRE device (TalonFX/Kraken)
        if (!found) {
            try (com.ctre.phoenix6.hardware.TalonFX talonProbe = new com.ctre.phoenix6.hardware.TalonFX(id)) {
                // We check the version or a signal to see if it's actually there
                var version = talonProbe.getVersion().getValue();
                if (version > 0) {
                    found = true;
                    brand = "CTRE (TalonFX/Kraken)";
                }
            } catch (Exception e) {
                /* Not a happy Kraken */ }
        }

        // 3. Log and store the results
        connectedDevices.put(id, found);
        edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putBoolean("Hardware/Status ID " + id, found);

        if (found) {
            System.out.println("[FOUND] ID " + id + ": " + brand);
        } else {
            System.out.println("[MISSING] ID " + id + ": No response from REV or CTRE.");
        }

        discoveredBrands.put(id, brand);
        return found;
    }

    public static MotorControllerWrapper createMotor(int id, MotorConstants constants) {
        return createMotor(id, constants, false);
    }

    public static MotorControllerWrapper createMotor(int id, MotorConstants constants, boolean isAbsolute) {
        MotorConfig config = MotorConfig.fromConstants(constants);
        String brand = discoveredBrands.getOrDefault(id, "Unknown");

        // 1. If the wire is unplugged, return a zombie motor
        if (!isDeviceConnected(id)) {
            return new MockMotor();
        }

        // 2. The Factory picks the right tool for the job based on the brand found
        if (brand.equals("REV (Spark Max/Flex)") && config.getSparkConfig() != null) {
            return new RealSparkMax(id, config.getSparkConfig(), isAbsolute);
        }

        if (brand.equals("CTRE (TalonFX/Kraken)") && config.getTalonConfig() != null) {
            return new RealKraken(id, config.getTalonConfig());
        }

        // 3. Fallback: If we found a motor but don't have a config for it
        System.out.println("!!! WARNING: ID " + id + " found as " + brand + " but no config provided!");
        return new MockMotor();
    }

    public static MotorControllerWrapper[] createMotorPair(int leaderId, int followId, MotorConstants constants,
            boolean inverted) {
        MotorConfig config = MotorConfig.fromConstants(constants);
        String brand = discoveredBrands.getOrDefault(leaderId, "Unknown");

        MotorControllerWrapper leader = createMotor(leaderId, constants);

        if (leader instanceof MockMotor) {
            System.out.println(
                    "!!! [HARDWARE ALERT] Leader " + leaderId + " is MOCK. Forcing Follower " + followId + " to MOCK.");
            return new MotorControllerWrapper[] { leader, new MockMotor() };
        }

        // --- PRE-BIRTH (REV) ---
        if (brand.equals("REV (Spark Max/Flex)")) {
            config.getSparkConfig().follow(leaderId, inverted);
        }

        MotorControllerWrapper follower = createMotor(followId, constants);

        // --- POST-BIRTH (CTRE) ---
        if (brand.equals("CTRE (TalonFX/Kraken)") && !(leader instanceof MockMotor)) {
            follower.follow(leader, inverted);
        }

        return new MotorControllerWrapper[] { leader, follower };
    }

    public static MotorControllerWrapper createSparkMax(int id, SparkMaxConfig config) {
        return createSparkMax(id, config, false);
    }

    public static MotorControllerWrapper createSparkMax(int id, SparkMaxConfig config, boolean isAbsolute) {
        if (GlobalConstants.IS_BENCHTOP) {
            boolean found = false;
            // 1. Open and immediately close the probe to check if hardware exists
            try (SparkMax probe = new SparkMax(id, com.revrobotics.spark.SparkLowLevel.MotorType.kBrushless)) {
                if (probe.getFirmwareVersion() != 0) {
                    found = true;
                }
                probe.close();
            } catch (Exception e) {
                found = false;
            }

            // 2. Now that the 'probe' is officially CLOSED, we can safely create the real
            // one
            if (found) {
                System.out.println("Benchtop: ID " + id + " found. Using Real.");
                return new RealSparkMax(id, config, isAbsolute);
            } else {
                System.out.println("Benchtop: ID " + id + " missing. Using Mock.");
                return new MockSparkMax();
            }
        } else {
            return new RealSparkMax(id, config, isAbsolute);
        }
    }
}