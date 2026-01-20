package frc.robot.configs;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.constants.NeoSwerveModuleConstants;

import frc.robot.constants.DriveConstants;
import frc.robot.constants.GlobalConstants;
import frc.robot.constants.ModuleConstants;

public final class DriveConfigs {
    public static final class MAXSwerveModule {
        public static final SparkMaxConfig drivingConfig = new SparkMaxConfig();
        public static final SparkMaxConfig turningConfig = new SparkMaxConfig();

        public static final IdleMode driveIdle = IdleMode.kCoast; //
        public static final IdleMode turnIdle = IdleMode.kBrake; //

        static {
            // --- Driving Motor Configuration ---
            drivingConfig
                    .idleMode(driveIdle)
                    .smartCurrentLimit(GlobalConstants.kHighCurrentLimit);
            drivingConfig.encoder
                    .positionConversionFactor(drivingFactor) // meters
                    .velocityConversionFactor(drivingFactor / 60.0); // meters per second
            drivingConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                    .pid(DriveConstants.kDriveP, DriveConstants.kDriveI, DriveConstants.kDriveD)
                    .outputRange(-1, 1);

            drivingConfig.encoder
                    .positionConversionFactor(NeoSwerveModuleConstants.kDriveEncoderPositionFactor) //
                    .velocityConversionFactor(NeoSwerveModuleConstants.kDriveEncoderVelocityFactor); //

            drivingConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kPrimaryEncoder) //
                    .pid(0.04, 0, 0) //
                    .outputRange(-1, 1); //

            // --- Turning Motor Configuration ---
            turningConfig
                    .idleMode(turnIdle) //
                    .smartCurrentLimit(GlobalConstants.kLowCurrentLimit);
            turningConfig.absoluteEncoder
                    .inverted(true) //
                    .positionConversionFactor(NeoSwerveModuleConstants.kTurningEncoderPositionFactor) //
                    .velocityConversionFactor(NeoSwerveModuleConstants.kTurningEncoderVelocityFactor); //

            turningConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kAbsoluteEncoder) //
                    .pid(DriveConstants.kTurnP, DriveConstants.kTurnI, DriveConstants.kTurnD)
                    .outputRange(-1, 1) //
                    .positionWrappingEnabled(true) //
                    .positionWrappingInputRange(0, NeoSwerveModuleConstants.kTurningEncoderPositionFactor); 
        }
    }
}