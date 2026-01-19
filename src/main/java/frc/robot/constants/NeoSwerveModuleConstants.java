package frc.robot.constants;

import edu.wpi.first.math.util.Units;

public class NeoSwerveModuleConstants {

    /** Gearbox options for the REV MAXSwerve Module. */
    public enum GearboxConfig {
        LOW(5.50, 12),
        MEDIUM(5.08, 13),
        HIGH(4.71, 14);

        public final double ratio;
        public final int pinionTeeth;

        GearboxConfig(double ratio, int teeth) {
            this.ratio = ratio;
            this.pinionTeeth = teeth;
        }
    }

    // --- THE SETTINGS YOU CHANGE ---
    public static final GearboxConfig kCurrentGearbox = GearboxConfig.MEDIUM; //
    private static final double kWheelDiameterInches = 2.875; //

    // --- CALCULATIONS ---
    public static final double kWheelDiameterMeters = Units.inchesToMeters(kWheelDiameterInches); //
    private static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI; //

    // This is the line your DriveConstants was missing!
    // It pulls the theoretical max speed from the GearboxConfig table above.
    public static final double kMaxSpeedMetersPerSecond = ((NeoMotorConstants.kFreeSpeedRpm / 60) / kCurrentGearbox.ratio) * kWheelCircumferenceMeters;

    // DRIVE ENCODER FACTORS
    // Factor to turn motor rotations into meters
    public static final double kDriveEncoderPositionFactor = kWheelCircumferenceMeters / kCurrentGearbox.ratio; //
    
    // Factor to turn RPM into Meters Per Second
    public static final double kDriveEncoderVelocityFactor = kDriveEncoderPositionFactor / 60.0; //

    // TURNING FACTORS
    // The Absolute Encoder is 1:1 with the wheel, so its factor is just one full circle (2*PI)
    public static final double kTurningEncoderPositionFactor = 2 * Math.PI; //
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; //
}