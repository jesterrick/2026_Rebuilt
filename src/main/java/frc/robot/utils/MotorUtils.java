package frc.robot.utils;

public final class MotorUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MotorUtils() {
    }

    /**
     * Calculates the number of motor rotations required to move a mechanism a certain distance for a chain and sprocket system.
     *
     * @param distanceInches              The desired linear distance to travel in inches.
     * @param sprocketPitchDiameterInches The pitch diameter of the sprocket in inches.
     * @param gearRatio                   The gear ratio between the motor and the sprocket.
     * @return The required number of motor rotations.
     */
    public static double calculateRotationsForDistance(double distanceInches, double sprocketPitchDiameterInches,
            double gearRatio) {
        if (sprocketPitchDiameterInches <= 0) {
            return 0;
        }
        double sprocketCircumference = sprocketPitchDiameterInches * Math.PI;
        double rotationsOfSprocket = distanceInches / sprocketCircumference;
        double motorRotations = rotationsOfSprocket * gearRatio;
        return motorRotations;
    }

    /**
     * Calculates the number of motor rotations required to move a mechanism a
     * certain distance
     * for a lead screw system.
     *
     * @param distanceInches         The desired linear distance to travel in inches.
     * @param inchesPerMotorRotation The linear distance the mechanism travels for one rotation of the motor.
     * @param gearRatio              The gear ratio between the motor and the lead screw.
     * @return The required number of motor rotations.
     */
    public static double calculateRotationsForDistanceWithLeadScrew(double distanceInches,
            double inchesPerMotorRotation, double gearRatio) {
        if (inchesPerMotorRotation <= 0) {
            return 0;
        }
        double directMotorRotations = distanceInches / inchesPerMotorRotation;
        double gearedMotorRotations = directMotorRotations * gearRatio;
        return gearedMotorRotations;
    }

    /**
     * Converts degrees of the output mechanism to motor rotations.
     * Useful for arms or intake pivots.
     * * @param degrees The desired angle in degrees.
     * 
     * @param gearRatio The gear ratio (Motor Rotations / Output Rotations).
     * @return Required motor rotations.
     */
    public static double degreesToMotorRotations(double degrees, double gearRatio) {
        // (degrees / 360) gives us the fraction of a full circle
        return (degrees / 360.0) * gearRatio;
    }

    /**
     * Converts Motor Rotations back into Degrees of the output mechanism.
     * Useful for telemetry (showing the driver the arm angle).
     * * @param rotations Current motor rotations from encoder.
     * 
     * @param gearRatio The gear ratio used.
     * @return Current angle of mechanism in degrees.
     */
    public static double motorRotationsToDegrees(double rotations, double gearRatio) {
        return (rotations / gearRatio) * 360.0;
    }

    /**
     * Restricts a value between a minimum and maximum
     * If the value is greater than the max, return max
     * If the value is less than min, return min
     * Otherwise, return value
     * 
     * @param value The desired value to send to the motor
     * @param min   The minimum value allowed to be sent to the motor
     * @param max   The maximum value allowed to be sent to the motor
     * @return The value that will be sent to the motor
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Calculates the motor power needed to counteract gravity on an arm.
     *
     * @param relativeDeg The current angle of the arm in degrees, where 0 is the starting position of the encoder.
     * @param offsetDeg   The physical angle of the arm at encoder 0 (in degrees). 0° = Horizontal, 90° = Vertical Up.
     * @param kg          The "Gravity Constant" - the motor power (0.0 to 1.0) required to hold the arm perfectly horizontal.
     * @return A double between -1.0 and 1.0 representing the "Arbitrary Feedforward" motor output percentage.
     */
    public static double calculateArmGravityFF(double relativeDeg, double offsetDeg, double kg) {
        // Combine relative movement with the physical starting point
        double physicalDegrees = relativeDeg + offsetDeg;
        
        // Convert to Radians for Java's Math class
        double physicalRadians = Math.toRadians(physicalDegrees);
        
        // Return the percentage of power needed for this specific angle
        return kg * Math.cos(physicalRadians);
    }
}
