package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType; 
import com.revrobotics.spark.SparkClosedLoopController; 

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.ExtenderConfigs;
import frc.robot.constants.CanIdConstants;
import frc.robot.constants.ExtenderConstants;

/**
 * The ExtenderSubsystem controls a two-motor extender mechanism on the robot.
 * It manages motor control, position tracking, synchronization between motors,
 * and implements safety features like skew error detection and homing.
 */
public class ExtenderSubsystem extends SubsystemBase {
  /** The leader motor for the extender mechanism. */
  private final SparkMax m_ExtenderLeaderMotor;
  /** The follower motor for the extender mechanism, synchronized with the leader. */
  private final SparkMax m_ExtenderFollowMotor;
  /** Closed-loop controller for the leader motor. */
  private final SparkClosedLoopController m_LeaderController;
  /** Closed-loop controller for the follower motor. */
  private final SparkClosedLoopController m_FollowController;  
  
  /** The global target position in inches for the extender mechanism. */
  private double m_globalTargetInches = 0.0;
  /** Flag indicating if the extender mechanism has been homed and is trusted for position control. */
  private boolean m_isHomed = false; 

  /**
   * Constructs a new ExtenderSubsystem.
   * Initializes the leader and follower motors, their closed-loop controllers,
   * and configures them with predefined settings.
   */
  public ExtenderSubsystem() {
    this.m_ExtenderLeaderMotor = new SparkMax(CanIdConstants.kExtenderMotor1, MotorType.kBrushless);
    this.m_ExtenderFollowMotor = new SparkMax(CanIdConstants.kExtenderMotor2, MotorType.kBrushless);

    this.m_LeaderController = m_ExtenderLeaderMotor.getClosedLoopController();
    this.m_FollowController = m_ExtenderFollowMotor.getClosedLoopController();

    // Configure both leader and follower motors with predefined configurations,
    // resetting safe parameters and persisting settings across power cycles.
    this.m_ExtenderLeaderMotor.configure(ExtenderConfigs.leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    this.m_ExtenderFollowMotor.configure(ExtenderConfigs.followConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double leaderPos = getPositionInInches(m_ExtenderLeaderMotor);
    double followPos = getPositionInInches(m_ExtenderFollowMotor);
    double error = leaderPos - followPos; // Calculate position difference (skew) between motors

    // Only synchronize motors if the system is homed and not in an error state
    if (m_isHomed) {
      // Calculate correction for follower motor based on skew error and proportional gain
      double correction = error * ExtenderConstants.kSyncP;

      // Command both motors independently using motion position control.
      // The Follower motor gets a "nudge" to correct for skew.
      m_LeaderController.setSetpoint(m_globalTargetInches, ControlType.kMAXMotionPositionControl);
      m_FollowController.setSetpoint(m_globalTargetInches + correction, ControlType.kMAXMotionPositionControl);

      // Implement a hard safety stop if the skew error exceeds a defined maximum difference.
      if (Math.abs(error) > ExtenderConstants.kMaxPositionDifference) {
        stop(); // Stop motors immediately
        m_isHomed = false; // System is no longer trusted
        SmartDashboard.putBoolean("Extender/SKEW_ERROR", true); // Indicate skew error on SmartDashboard
      }
    }

    // Update SmartDashboard with current motor positions and homing status for debugging
    SmartDashboard.putNumber("Extender/Leader Inches", leaderPos);
    SmartDashboard.putNumber("Extender/Follower Inches", followPos);
    SmartDashboard.putBoolean("Extender/Is Homed", m_isHomed);

    // Provide a warning on SmartDashboard if the robot is enabled but the extender is not homed
    if (!m_isHomed) {
      SmartDashboard.putString("Extender/Status", "RE-ZERO REQUIRED");
    } else {
      SmartDashboard.putString("Extender/Status", "READY");
    }
  }

  /**
   * Sets the global target position for the extender to its maximum outward position.
   * Movement will only occur if the extender is homed.
   */
  public void moveOut() {
    // Safety check: Do not move if the system is not homed, as position is untrusted
    if (!m_isHomed) return; 
    m_globalTargetInches = ExtenderConstants.kExtenderMotorOut;
  }

  /**
   * Sets the global target position for the extender to its maximum inward (retracted) position.
   * Movement will only occur if the extender is homed.
   */
  public void moveIn() {
    // Safety check: Do not move if the system is not homed
    if (!m_isHomed) return;
    m_globalTargetInches = ExtenderConstants.kExtenderMotorIn;
  }

  /**
   * Resets the encoders of both extender motors to zero and sets the system as homed.
   * This assumes the extender is in a known zero position (e.g., against a limit switch).
   */
  public void resetEncoders() {
    m_ExtenderLeaderMotor.getEncoder().setPosition(0);
    m_ExtenderFollowMotor.getEncoder().setPosition(0);
    this.m_globalTargetInches = 0; // Reset target to zero
    this.m_isHomed = true; // Extender is now square and trusted
  }

  /**
   * Stops both extender motors immediately.
   */
  public void stop() {
    m_ExtenderLeaderMotor.stopMotor();
    m_ExtenderFollowMotor.stopMotor();
  }

  /**
   * Retrieves the current position of a specified extender motor in inches.
   * @param motor The SparkMax motor to query.
   * @return The position of the motor's encoder in inches.
   */
  public double getPositionInInches(SparkMax motor) {
    return motor.getEncoder().getPosition();
  }

  /**
   * Checks if the extender mechanism is within a defined tolerance of its target position.
   * @param target The target position in inches to check against.
   * @return True if the leader motor's position is within `kAtTargetTolerance` of the target, false otherwise.
   */
  public boolean atTarget(double target) {
    // Checking leader is usually enough, but you can check both for extra safety
    return Math.abs(target - getPositionInInches(m_ExtenderLeaderMotor)) < ExtenderConstants.kAtTargetTolerance;
  }

  /**
   * Sets the raw voltage for both extender motors.
   * This method is typically used during homing procedures to bypass PID control
   * and apply direct motor power.
   * @param voltage The voltage to apply to the motors.
   */
  public void setHomingVoltages(double voltage) {
    m_ExtenderLeaderMotor.setVoltage(voltage);
    m_ExtenderFollowMotor.setVoltage(voltage);
  }

  public boolean isAtHome()
  {
    return m_ExtenderLeaderMotor.getOutputCurrent() > ExtenderConstants.kMaxHomingVoltage && m_ExtenderFollowMotor.getOutputCurrent() > ExtenderConstants.kMaxHomingVoltage;
  }

  public void setIsHomed(boolean homed)
  {
    this.m_isHomed = homed;
  }

  public void prepareForHoming()
  {
    m_ExtenderLeaderMotor.configure(ExtenderConfigs.homingConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  public void enableSoftLimits()
  {
    m_ExtenderLeaderMotor.configure(ExtenderConfigs.leaderConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
  }
}