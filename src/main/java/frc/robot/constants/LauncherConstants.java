// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

/** Add your docs here. */
public class LauncherConstants {

    public static final double kLauncherMotorSpeedIdle = 0.5;
    public static final double kLauncherMotorStop = 0.0;
    public static final double kLauncherTolerance = 50.0;

    public static final int kLauncherIdleSensor = 0;
    public static final double kWaitForEmptyTime = 3.0;

    // Velocity Closed-Loop Constants (for variable speed)
    // kV = 11.0 / 5676 (Max RPM of NEO)
    public static final double kLauncherkV = 0.0019; 
    public static final double kLauncherP = 0.0001;
    public static final double kLauncherI = 0.0;
    public static final double kLauncherD = 0.0;

    // values of the field components for the limelight calculations
    public static final double kTargetHeight = 72.0;
    public static final double kCameraHeight = 12.0;
    public static final double kMountAngle = 15.0;

}
