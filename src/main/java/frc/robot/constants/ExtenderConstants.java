package frc.robot.constants;

public class ExtenderConstants {
    public static final int kExtenderMotor = 13;

    public static final double kExtenderMotorSpeed = 0.4;
    public static final double kExtenderMotorIn = 0.0;
    public static final double kExtenderMotorOut = 26.5;

    // -- PHYSICAL MATH --
    public static final double kGearRatio = 1.0; // No gear reduction

    // pully/sprocket circumference = Diameter * PI
    public static final double pitchDiameter = 1.75;
    public static final double kInchesPerRotation = pitchDiameter * Math.PI;

    // factor: (1 / GearRatio) * InchesPerRotation
    public static final double kPositionFactor = (1.0 / kGearRatio) * kInchesPerRotation;

    public static final double kIntakeP = 4.0;
    public static final double kIntakeI = 0.0;
    public static final double kIntakeD = 0.03;


}
