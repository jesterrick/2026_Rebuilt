package frc.robot.constants;

/**
 * The CanIdConstants class centralizes all CAN IDs for the robot's motors,
 * ensuring easy management and modification of hardware addresses.
 */
public class CanIdConstants {
    // CAN IDs for the Swerve Modules
    public static final int kFrontLeftTurningCanId = 1;
    public static final int kRearLeftTurningCanId = 3;
    public static final int kFrontRightTurningCanId = 7;
    public static final int kRearRightTurningCanId = 5;

    public static final int kFrontLeftDrivingCanId = 2;
    public static final int kRearLeftDrivingCanId = 4;
    public static final int kFrontRightDrivingCanId = 8;
    public static final int kRearRightDrivingCanId = 6;

   // CAN IDs for the Extender
    public static final int kExtenderMotor1 = 9;
    public static final int kExtenderMotor2 = 10;

    // CAN IDs for Climber
    public static final int kClimberMotor1 = 11;
    public static final int kClimberMotor2 = 12;

   // CAN IDs for the Intake
    public static final int kIntakeMotor = 13;

   // CAN IDs for the Rollers
    public static final int kRollerMotor = 14;

   // CAN IDs for the Feeder
    public static final int kFeederMotor = 16;

   // CAN IDs for the Launcher
    public static final int kLauncherMotor = 15;

}
