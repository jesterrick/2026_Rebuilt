package frc.robot.util.hardware;

import frc.robot.constants.GlobalConstants;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

public class HardwareFactory {

    public static MotorControllerWrapper createSparkMax(int id, SparkMaxConfig config, boolean isAbsolute) {
        // REMOVE the IF statement that checks IS_BENCHTOP here.
        // Instead, ALWAYS try to probe the motor first.

        try (SparkMax probe = new SparkMax(id, MotorType.kBrushless)) {
            // If firmware version is 0, the motor is definitely not there.
            if (probe.getFirmwareVersion() == 0) {
                System.out.println(">>> [HARDWARE] ID " + id + " MISSING. Using Mock.");
                return new MockSparkMax();
            }
            // If we found it, then we make the real one.
            return new RealSparkMax(id, config, isAbsolute);
        } catch (Exception e) {
            System.out.println(">>> [HARDWARE] ID " + id + " ERROR. Using Mock.");
            return new MockSparkMax();
        }
    }

    public static MotorControllerWrapper createSparkMax(int id, SparkMaxConfig config) {
        return createSparkMax(id, config, false);
    }

    public static MotorControllerWrapper createFollowerSparkMax(int leaderId, int followerId,
            SparkMaxConfig leaderConfig, boolean inverted) {
        MotorControllerWrapper leader = createSparkMax(leaderId, leaderConfig, false);

        SparkMaxConfig followerConfig = new SparkMaxConfig();
        followerConfig.follow(leaderId, inverted);

        // If the leader is Real, we check if the follower hardware is actually there.
        if (leader instanceof MockSparkMax) {
            System.out.println("HardwareFactory: Leader ID " + leaderId + " is Mock. Forcing Follower ID " + followerId
                    + " to Mock.");
            createSparkMax(followerId, followerConfig); // This will return a Mock anyway
        } else {
            // Leader is real, so we try to make a real follower
            createSparkMax(followerId, followerConfig);
        }

        return leader;
    }
}
