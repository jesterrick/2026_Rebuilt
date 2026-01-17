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

    // Specific Hardware Specs
    public static final double kWheelDiameterInches = 2.875; // 2 7/8 inches
    public static final double kGearReduction = 5.08;        // "Medium" ratio
    public static final double kMaxMotorRPM = 5676.0;        // Max RPM for Neo Motor

    // The Calculation (Feet Per Second)
    // (RPM / 60) gives rotations per second
    // Multiply by (Diameter * PI) to get inches per second
    // Divide by 12 to get feet
    public static final double kMaxSpeedFeetPerSecond = (kMaxMotorRPM / 60.0) * (1.0 / kGearReduction) * (kWheelDiameterInches * Math.PI / 12.0);

    // The Calculation (Meters Per Second) - Needed for WPILib
    public static final double kMaxSpeedMetersPerSecond = Units.feetToMeters(kMaxSpeedFeetPerSecond);
            
    // Real World Adjusted (90% of theoretical)
    public static final double kAdjustedMaxSpeedMbpsTeleOp = kMaxSpeedMetersPerSecond * speedAdjustTeleOp;
    public static final double kAdjustedMaxSpeedMbpsAuto = kMaxSpeedMetersPerSecond * speedAdjustAuto;
    
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

    // SPARK MAX CAN IDs
    public static final int kFrontLeftDrivingCanId = 2;
    public static final int kRearLeftDrivingCanId = 4;
    public static final int kFrontRightDrivingCanId = 8;
    public static final int kRearRightDrivingCanId = 6;

    public static final int kFrontLeftTurningCanId = 1;
    public static final int kRearLeftTurningCanId = 3;
    public static final int kFrontRightTurningCanId = 7;
    public static final int kRearRightTurningCanId = 5;

    public static final boolean kGyroReversed = false;
}