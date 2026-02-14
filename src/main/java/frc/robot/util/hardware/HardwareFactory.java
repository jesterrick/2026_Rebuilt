package frc.robot.util.hardware;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.constants.GlobalConstants;

public class HardwareFactory {

    public static MotorControllerWrapper createSparkMax(int id, SparkMaxConfig config, boolean isAbsolute) {
        if (GlobalConstants.IS_BENCHTOP) {
            boolean found = false;
            // 1. Open and immediately close the probe to check if hardware exists
            try (SparkMax probe = new SparkMax(id, MotorType.kBrushless)) {
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

    public static MotorControllerWrapper createSparkMax(int id, SparkMaxConfig config) {
        return createSparkMax(id, config, false);
    }

    public static MotorControllerWrapper createKraken(int id, TalonFXConfiguration config) {
        if (GlobalConstants.IS_BENCHTOP) {
            boolean found = false;
            // For Kraken, we can attempt to get the device ID or check for errors
            try {
                TalonFX probe = new TalonFX(id);
                // A version of 0 could indicate an issue, or no device.
                // A better check might be to see if getError() returns anything significant
                // or if the device ID matches. For now, check version.
                if (probe.getVersion().getValue() != 0) {
                    found = true;
                }
                probe.close();
            } catch (Exception e) {
                found = false;
            }

            if (found) {
                System.out.println("Benchtop: Kraken ID " + id + " found. Using RealKraken.");
                return new RealKraken(id, config);
            } else {
                System.out.println("Benchtop: Kraken ID " + id + " missing. Using MockSparkMax.");
                // We use MockSparkMax as a generic mock for now, as no specific MockKraken
                // exists.
                return new MockSparkMax();
            }
        } else {
            return new RealKraken(id, config);
        }
    }

    public static MotorControllerWrapper[] createSparkMaxPair(int leaderId, int followerId,
            SparkMaxConfig config, boolean inverted) {
        MotorControllerWrapper leader = createSparkMax(leaderId, config, false);

        if (leader instanceof MockSparkMax) {
            return new MotorControllerWrapper[] { new MockSparkMax(), new MockSparkMax() };
        }

        config.follow(leaderId, inverted);

        MotorControllerWrapper follower = createSparkMax(followerId, config, false);

        return new MotorControllerWrapper[] { leader, follower };
    }

    public static MotorControllerWrapper[] createKrakenPair(int leaderID, int followerID, TalonFXConfiguration config, boolean inverted) {
        MotorControllerWrapper leader = createKraken(leaderID, config);

        // We use the leader's actual hardware to set up the follow
        MotorControllerWrapper follower = createKraken(followerID, config);
        follower.follow(leader, true); // Or whatever inversion you need

        return new MotorControllerWrapper[] { leader, follower };
    }
}