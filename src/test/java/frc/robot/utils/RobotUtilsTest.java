package frc.robot.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RobotUtilsTest {

    @Test
    void testCalculateLinearFactor() {
        // Test case 1: simple values
        double gearRatio1 = 10.0;
        double drumDiameter1 = 2.0;
        double expected1 = (Math.PI * drumDiameter1) / gearRatio1;
        assertEquals(expected1, RobotUtils.calculateLinearFactor(gearRatio1, drumDiameter1), 0.0001);

        // Test case 2: different values
        double gearRatio2 = 5.0;
        double drumDiameter2 = 4.0;
        double expected2 = (Math.PI * drumDiameter2) / gearRatio2;
        assertEquals(expected2, RobotUtils.calculateLinearFactor(gearRatio2, drumDiameter2), 0.0001);

        // Test case 3: zero drum diameter
        double gearRatio3 = 10.0;
        double drumDiameter3 = 0.0;
        double expected3 = 0.0; // (Math.PI * 0.0) / 10.0 = 0.0
        assertEquals(expected3, RobotUtils.calculateLinearFactor(gearRatio3, drumDiameter3), 0.0001);

        // Test case 4: gear ratio of 1
        double gearRatio4 = 1.0;
        double drumDiameter4 = 3.0;
        double expected4 = (Math.PI * drumDiameter4) / gearRatio4;
        assertEquals(expected4, RobotUtils.calculateLinearFactor(gearRatio4, drumDiameter4), 0.0001);
    }

    @Test
    void testToVelocityPerSecond() {
        // Test case 1: simple positive value
        double positionFactor1 = 60.0;
        double expected1 = 1.0; // 60.0 / 60.0 = 1.0
        assertEquals(expected1, RobotUtils.toVelocityPerSecond(positionFactor1), 0.0001);

        // Test case 2: zero value
        double positionFactor2 = 0.0;
        double expected2 = 0.0; // 0.0 / 60.0 = 0.0
        assertEquals(expected2, RobotUtils.toVelocityPerSecond(positionFactor2), 0.0001);

        // Test case 3: negative value
        double positionFactor3 = -120.0;
        double expected3 = -2.0; // -120.0 / 60.0 = -2.0
        assertEquals(expected3, RobotUtils.toVelocityPerSecond(positionFactor3), 0.0001);

        // Test case 4: fractional value
        double positionFactor4 = 30.0;
        double expected4 = 0.5; // 30.0 / 60.0 = 0.5
        assertEquals(expected4, RobotUtils.toVelocityPerSecond(positionFactor4), 0.0001);
    }

    @Test
    void testCalculateDistance() {
        // Test case 1: simple positive values
        double velocity1 = 10.0;
        double time1 = 2.0;
        double expected1 = 20.0; // 10.0 * 2.0 = 20.0
        assertEquals(expected1, RobotUtils.calculateDistance(velocity1, time1), 0.0001);

        // Test case 2: zero velocity
        double velocity2 = 0.0;
        double time2 = 5.0;
        double expected2 = 0.0; // 0.0 * 5.0 = 0.0
        assertEquals(expected2, RobotUtils.calculateDistance(velocity2, time2), 0.0001);

        // Test case 3: zero time
        double velocity3 = 7.0;
        double time3 = 0.0;
        double expected3 = 0.0; // 7.0 * 0.0 = 0.0
        assertEquals(expected3, RobotUtils.calculateDistance(velocity3, time3), 0.0001);

        // Test case 4: negative velocity
        double velocity4 = -5.0;
        double time4 = 3.0;
        double expected4 = -15.0; // -5.0 * 3.0 = -15.0
        assertEquals(expected4, RobotUtils.calculateDistance(velocity4, time4), 0.0001);

        // Test case 5: negative time (though usually not physically meaningful, for completeness)
        double velocity5 = 8.0;
        double time5 = -1.0;
        double expected5 = -8.0; // 8.0 * -1.0 = -8.0
        assertEquals(expected5, RobotUtils.calculateDistance(velocity5, time5), 0.0001);

        // Test case 6: fractional values
        double velocity6 = 2.5;
        double time6 = 1.5;
        double expected6 = 3.75; // 2.5 * 1.5 = 3.75
        assertEquals(expected6, RobotUtils.calculateDistance(velocity6, time6), 0.0001);
    }
}
