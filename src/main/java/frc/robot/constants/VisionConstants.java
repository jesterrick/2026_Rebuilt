package frc.robot.constants;

/**
 * The VisionConstants class stores all constant values related to the robot's vision system.
 * This includes configurations for Limelight cameras, such as network table keys,
 * pipeline IDs, and default values for vision-based calculations.
 */
public class VisionConstants {
/** The team number for the FRC team. */
public static final int TEAM_NUMBER = 5919;
/** The NetworkTables name for the Limelight camera. */
public static final String LIMELIGHT_NAME = "limelight";
/** The URL for accessing the Limelight's web interface. */
public static final String LIMELIGHT_URL = "http://limelight.local:5800";

/** The pipeline ID for driver camera feed. */
public static final int DRIVER_PIPELINE = 0;
/** The pipeline ID for vision processing (e.g., target tracking). */
public static final int VISION_PIPELINE = 1;

    /** NetworkTables key for checking if a valid target is present (`tv`). */
    public static final String kTargetValidKey = "tv";
    /** NetworkTables key for retrieving the target ID (`tid`). */
    public static final String kTargetIdKey = "tid";
    /** NetworkTables key for retrieving the vertical offset of the target (`ty`). */
    public static final String kTargetYKey = "ty";
    /** Default value for `kTargetValidKey` if no target is found. */
    public static final double kDefaultTargetValid = 0.0;
    /** Default value for `kTargetIdKey` if no target is found. */
    public static final int kDefaultTargetId = -1;
    /** Default value for `kTargetYKey` if no target is found. */
    public static final double kDefaultTargetY = 0.0;
}
