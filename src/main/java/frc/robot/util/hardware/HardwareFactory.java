package frc.robot.util.hardware;

import frc.robot.constants.GlobalConstants;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

public class HardwareFactory {

    public static MotorControllerWrapper createSparkMax(int id, SparkMaxConfig config, boolean isAbsolute) {
        if (!GlobalConstants.IS_BENCHTOP) {
            return new RealSparkMax(id, config, isAbsolute);
        }

        // Try-with-resources: 'probe' will be automatically closed at the end of the
        // block
        try (SparkMax probe = new SparkMax(id, MotorType.kBrushless)) {

            if (probe.getFirmwareVersion() == 0) {
                System.out.println("Benchtop: ID " + id + " missing. Using Mock.");
                return new MockSparkMax();
            } else {
                System.out.println("Benchtop: ID " + id + " found. Using Real.");
                return new RealSparkMax(id, config, isAbsolute);
            }
        } catch (Exception e) {
            // If the probe itself crashes, default to a Mock to stay safe
            System.out.println("Benchtop: Error probing ID " + id + ". Using Mock.");
            return new MockSparkMax();
        }
    }

    public static MotorControllerWrapper createSparkMax(int id, SparkMaxConfig config)
    {
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
