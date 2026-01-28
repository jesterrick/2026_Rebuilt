package frc.robot.constants;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public class DriveConstants {
    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds

    // Speed reducers for real world - Let's keep things under control
    public static final double speedAdjustTeleOp = 0.9;
    public static final double speedAdjustAuto = 0.5;
            
    // Real World Adjusted (90% of theoretical)
    public static final double kAdjustedMaxSpeedMbpsTeleOp = NeoSwerveModuleConstants.kMaxSpeedMetersPerSecond * speedAdjustTeleOp;
    public static final double kAdjustedMaxSpeedMbpsAuto = NeoSwerveModuleConstants.kMaxSpeedMetersPerSecond * speedAdjustAuto;
    
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second
    
    // Chassis configuration
    public static final double kTrackWidth = Units.inchesToMeters(26);
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(26);

    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
            new Translation2d(kWheelBase / 2, kTrackWidth / 2),
            new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
            new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
            new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = -Math.PI / 2;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = Math.PI;
    public static final double kBackRightChassisAngularOffset = Math.PI / 2;

    public static final boolean kGyroReversed = false;

    public static final double kDriveP = 0.04;
    public static final double kDriveI = 0.0;
    public static final double kDriveD = 0.0;

    public static final double kTurnP = 1.0;
    public static final double kTurnI = 0.0;
    public static final double kTurnD = 0.0;
}