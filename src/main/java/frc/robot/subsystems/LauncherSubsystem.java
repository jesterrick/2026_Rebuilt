// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.MathUtil;
import frc.robot.configs.LauncherConfigs;
import frc.robot.constants.CanIdConstants;
import frc.robot.constants.LauncherConstants;
import frc.robot.constants.VisionConstants;
import frc.robot.utils.VisionUtils;

/**
 * The LauncherSubsystem controls the robot's launcher mechanism.
 * It manages the launcher motor's speed, regulates RPM using PID control,
 * tracks ball presence via a hopper sensor, and integrates with vision
 * (Limelight) for dynamic RPM calculation based on target distance.
 */
public class LauncherSubsystem extends SubsystemBase {
  /** The motor responsible for driving the launcher mechanism. */
  private final SparkMax m_LauncherMotor;
  /** Closed-loop controller for the launcher motor, used for velocity control. */
  private final SparkClosedLoopController m_ClosedLoopController;
  /** The desired target RPM for the launcher motor. */
  private double targetRPM;
  /** Timer used to track how long the hopper has been empty. */
  private final Timer m_emptyTimer = new Timer();
  /** Digital input sensor to detect if a ball is present in the hopper. */
  private final DigitalInput m_hopperSensor = new DigitalInput(LauncherConstants.kLauncherIdleSensor);

  /**
   * Constructs a new LauncherSubsystem.
   * Initializes the launcher motor, its closed-loop controller, and configures it
   * with predefined settings. The initial target RPM is set to 0.
   */
  public LauncherSubsystem() {
    this.m_LauncherMotor = new SparkMax(CanIdConstants.kLauncherMotor, MotorType.kBrushless);
    this.m_ClosedLoopController = m_LauncherMotor.getClosedLoopController();
    this.m_LauncherMotor.configure(LauncherConfigs.config, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    this.targetRPM = 0.0;

    m_emptyTimer.start();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    boolean ballPresent = isBallPresent();

    // 1. Manage the Timer:
    // If a ball is present, reset and stop the timer.
    // If no ball is present, start/continue the timer to track empty duration.
    if (ballPresent) {
        m_emptyTimer.reset();
        m_emptyTimer.stop();
    } else {
        m_emptyTimer.start();
    }

    // 2. Decision Engine: Determines the target RPM based on robot state and ball presence.
    if (this.getCurrentCommand() != null) {
        // If an explicit command (e.g., LauncherOn) is running, it dictates the speed.
        // The timer is ignored because the driver's intent to shoot overrides idle/off logic.
    } 
    else if (!ballPresent && m_emptyTimer.hasElapsed(LauncherConstants.kWaitForEmptyTime)) {
        // Scenario A: Hopper is empty for a prolonged period (e.g., 3 seconds).
        // SHUT DOWN the launcher completely to save power and prevent unnecessary spinning.
        this.targetRPM = LauncherConstants.kLauncherMotorStop;
    } 
    else if (ballPresent) {
        // Scenario B: A ball is detected in the hopper, but no explicit shoot command is active.
        // Maintain an IDLE speed to be ready for a quick launch.
        this.targetRPM = LauncherConstants.kLauncherMotorSpeedIdle;
    }
    // Scenario C: Ball just left (timer < kWaitForEmptyTime) and no command is running.
    // The targetRPM will naturally stay at whatever it was last (e.g., high speed after a shot)
    // until the timer hits kWaitForEmptyTime. Then, it transitions to Scenario A and shuts off.

    // 3. Actuator Output: Apply the calculated target RPM to the motor.
    if (this.targetRPM > 0) {
        // If a positive target RPM is set, use the closed-loop velocity controller.
        this.m_ClosedLoopController.setSetpoint(this.targetRPM, ControlType.kVelocity);
    } else {
        // If target RPM is 0 or negative, stop the motor.
        this.m_LauncherMotor.stopMotor();
    }

    // Update SmartDashboard with launcher status for debugging and monitoring
    SmartDashboard.putNumber("Launcher/Target RPM", this.targetRPM);
    SmartDashboard.putNumber("Launcher/Actual RPM", this.getActualVelocity());
    SmartDashboard.putBoolean("Launcher/At Speed", this.atSpeed());
    SmartDashboard.putBoolean("Launcher/Ball Present", this.isBallPresent());
  }

  /**
   * Sets the desired target RPM for the launcher motor.
   * This RPM might be determined by autonomous routines or vision calculations.
   * @param setRPM The desired speed in RPM.
   */
  public void setLauncherVelocity(double setRPM) {
    this.targetRPM = setRPM;
  }

  /**
   * Retrieves the actual current velocity of the launcher motor from its encoder.
   * @return The actual velocity of the launcher motor in RPM.
   */
  public double getActualVelocity() {
    return this.m_LauncherMotor.getEncoder().getVelocity();
  }

  /**
   * Stops the launcher motor immediately by setting the target RPM to zero
   * and explicitly commanding the motor to stop.
   */
  public void stopLauncher() {
    this.targetRPM = 0.0;
    this.m_LauncherMotor.stopMotor();
  }

  /**
   * Checks if the launcher motor is currently operating at its target speed within a defined tolerance,
   * and if that target speed is above the idle threshold.
   * @return True if the launcher is at a "launch" speed and within tolerance, false otherwise.
   */
  public boolean atSpeed() {
    // 1. Calculate if the actual RPM is within an acceptable tolerance of the target RPM.
    boolean isNearTarget = Math.abs(this.targetRPM - getActualVelocity()) < LauncherConstants.kLauncherTolerance;

    // 2. Ensure the target speed is a "launch" speed (i.e., significantly above idle),
    // to differentiate from idle state or a stopped state.
    boolean isNotIdle = this.targetRPM > (LauncherConstants.kLauncherMotorSpeedIdle + LauncherConstants.kLaunchMinShotBuffer);

    return isNearTarget && isNotIdle;
}

  /**
   * Checks if a ball is present in the hopper using the digital input sensor.
   * @return True if a ball is detected, false otherwise.
   */
  public boolean isBallPresent() {
    return m_hopperSensor.get();
  }

  /**
   * Calculates the optimal launcher RPM based on Limelight vision data.
   * This method retrieves target information (validity, ID, vertical offset)
   * and uses it to estimate distance, then computes the required RPM.
   * @return The calculated target RPM, clamped between minimum and maximum launch RPMs,
   *         or the idle speed if no valid target is found.
   */
  public double calculateRPMFromLimeLight() {
    var table = NetworkTableInstance.getDefault().getTable("limelight");

    // Check for target validity (tv == 1.0) and retrieve the target ID (tid).
    boolean hasTarget = table.getEntry(VisionConstants.kTargetValidKey).getDouble(VisionConstants.kDefaultTargetValid) == 1.0;
    int tagID = (int) table.getEntry(VisionConstants.kTargetIdKey).getInteger(VisionConstants.kDefaultTargetId);

    // If a valid target is found and it's the correct hoop, calculate RPM.
    if (hasTarget && VisionUtils.isTargetingCorrectHoop(tagID)) {
      // Get the vertical offset of the target from the camera's crosshair.
      double ty = table.getEntry(VisionConstants.kTargetYKey).getDouble(VisionConstants.kDefaultTargetY);

      // Calculate distance to target using trigonometry and known physical dimensions.
      double distance = (LauncherConstants.kAprilTagHeight - LauncherConstants.kCameraHeight) /
          Math.tan(Math.toRadians(LauncherConstants.kMountAngle + ty));

      // Calculate the required RPM based on distance and predefined constants.
      double calculatedRPM = (distance * LauncherConstants.kRPMPerInch) + LauncherConstants.kBaseRPM;
      
      // Clamps the calculated speed between your minimum viable shot and your maximum safe speed.
      return MathUtil.clamp(calculatedRPM, LauncherConstants.kLaunchMinRPM, LauncherConstants.kLaunchMaxRPM);
    } else {
      // IMPORTANT: If we lose the target or it's not the correct one, return IDLE speed.
      // This will cause the `atSpeed()` method to return FALSE, effectively stopping the feeder,
      // and signaling the robot is not ready to shoot.
      return LauncherConstants.kLauncherMotorSpeedIdle;
    }
  }
}