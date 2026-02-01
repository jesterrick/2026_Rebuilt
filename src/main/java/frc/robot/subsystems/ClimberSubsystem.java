// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.ClimberConfigs;
import frc.robot.constants.CanIdConstants;
import frc.robot.constants.ClimberConstants;

/**
 * The ClimberSubsystem controls the robot's climbing mechanism.
 * It manages two motors to extend and retract the climber, using PID control
 * to maintain target positions.
 */
public class ClimberSubsystem extends SubsystemBase {
  /** The lead motor for the climber mechanism. */
  private final SparkMax m_ClimberLeaderMotor;
  /** The follower motor for the climber mechanism, synchronized with the leader. */
  private final SparkMax m_ClimberFollowMotor;
  /** Closed-loop controller for the leader motor. */
  private final SparkClosedLoopController m_LeaderController;

  /** The target position for the climber, in encoder units (usually inches or rotations). */
  private double m_targetPosition = 0.0;
  private boolean m_isHomed = false;

  /** Creates a new ClimberSubsystem. */
  public ClimberSubsystem() {
    this.m_ClimberLeaderMotor = new SparkMax(CanIdConstants.kClimberMotor1, MotorType.kBrushless);
    this.m_ClimberFollowMotor = new SparkMax(CanIdConstants.kClimberMotor2, MotorType.kBrushless);

    this.m_LeaderController = m_ClimberLeaderMotor.getClosedLoopController();

    // Configure both leader and follower motors with predefined configurations
    this.m_ClimberLeaderMotor.configure(ClimberConfigs.leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    this.m_ClimberFollowMotor.configure(ClimberConfigs.followConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double climberPos = getClimberHeight();
    if (m_isHomed) {
      // Set the leader motor's target position using a motion position control loop
      this.m_LeaderController.setSetpoint(m_targetPosition, ControlType.kMAXMotionPositionControl);
    }
    // Update SmartDashboard with current climber height for debugging and monitoring
    SmartDashboard.putNumber("Climber/Height", climberPos);
    SmartDashboard.putBoolean("Climber/Is Homed", m_isHomed);

    // Provide a warning on SmartDashboard if the robot is enabled but the extender is not homed
    if (!m_isHomed) {
      SmartDashboard.putString("Climber/Status", "RE-ZERO REQUIRED");
    } else {
      SmartDashboard.putString("Climber/Status", "READY");
    }
  }

  /**
   * Sets the target position for the climber to its maximum extended position.
   * The actual movement will be handled in the `periodic()` method by the PID controller.
   */
  public void climberUp()
  {
    if (!m_isHomed) return; 
    this.m_targetPosition = ClimberConstants.kClimberMaxExtend;
  }

  /**
   * Sets the target position for the climber to its zero (retracted) position.
   * The actual movement will be handled in the `periodic()` method by the PID controller.
   */
  public void climberDown()
  {
    if (!m_isHomed) return; 
    this.m_targetPosition = ClimberConstants.kClimberZero;
  }

  /**
   * Retrieves the current height of the climber from the follower motor's encoder.
   * @return The current height of the climber in encoder units (e.g., inches).
   */
  private double getClimberHeight()
  {
    return this.m_ClimberFollowMotor.getEncoder().getPosition();
  }

  /**
   * Stops the climber motors and holds the current position.
   * The target position is updated to the current height to prevent further movement.
   */
  public void stop()
  {
    this.m_targetPosition = getClimberHeight();
    this.m_ClimberLeaderMotor.stopMotor();
  }

  public void resetEncoders()
  {
    this.m_ClimberLeaderMotor.getEncoder().setPosition(0.0);
    this.m_targetPosition = 0; // Reset target to zero
  }

  public void setIsHomed(boolean homed)
  {
    this.m_isHomed = homed;
  }

   public double getLeaderCurrent() {
    return this.m_ClimberLeaderMotor.getOutputCurrent();
  }

   public void setHomingVoltages(double voltage) {
    this.m_ClimberLeaderMotor.setVoltage(voltage);
  }

  public boolean isAtBottom() {
    // If the motor is drawing more than our threshold, it has hit the mechanical stop
    return m_ClimberLeaderMotor.getOutputCurrent() > ClimberConstants.kMaxHomingVoltage;
  }

  public void prepareForHoming()
  {
    m_ClimberLeaderMotor.configure(ClimberConfigs.homingConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  public void enableSoftLimits()
  {
    m_ClimberLeaderMotor.configure(ClimberConfigs.leaderConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
  }
}
