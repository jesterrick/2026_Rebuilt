package frc.robot.constants;

public class ClimberConstants {

    public static final double kClimberMotorSpeed = 0.05;
    // The robot cannot extend to a height greater than 30.0 inches
    // Max extend needs to be the highest the climber is allowed to reach
    public static final double kClimberMaxExtend = 12.0;
    public static final double kClimberZero = 0.0;
    public static final double kGearRatio = 1.0; // No gear reduction

    // pully/sprocket circumference = Diameter * PI
    public static final double pitchDiameter = 1.25;
    public static final double kInchesPerRotation = pitchDiameter * Math.PI;

    // factor: (1 / GearRatio) * InchesPerRotation
    public static final double kPositionFactor = (1.0 / kGearRatio) * kInchesPerRotation;

    public static final double kIntakeP = 4.0;
    public static final double kIntakeI = 0.0;
    public static final double kIntakeD = 0.03;

    public static final double kExtAcceleration = .10;

}
