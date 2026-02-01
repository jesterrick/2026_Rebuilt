package frc.robot.constants;

import edu.wpi.first.math.trajectory.TrapezoidProfile;

/**
 * The AutoConstants class stores configuration parameters for autonomous routines.
 * These constants define maximum speeds, accelerations, and PID controller gains
 * for various autonomous movements and actions.
 */
public class AutoConstants {
    /** Maximum linear speed in feet per second for autonomous movement. */
    public static final double kMaxFeetPerSecond = 3;
    /** Maximum linear acceleration in meters per second squared for autonomous movement. */
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    /** Maximum angular speed in radians per second for autonomous turning. */
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    /** Maximum angular acceleration in radians per second squared for autonomous turning. */
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    /** Proportional gain for the X position controller in autonomous. */
    public static final double kPXController = 1;
    /** Proportional gain for the Y position controller in autonomous. */
    public static final double kPYController = 1;
    /** Proportional gain for the Theta (angle) controller in autonomous. */
    public static final double kPThetaController = 1;

    /**
     * Constraints for the motion-profiled robot angle controller,
     * used to limit the maximum velocity and acceleration of angular movements.
     */
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);

    /** The speed at which the robot drives during the example autonomous command. */
    public static final double kExampleAutoSpeed = 0.5;
    /** The timeout duration in seconds for the example autonomous command. */
    public static final double kExampleAutoTimeout = 3.0;
}
