// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;

import frc.robot.configs.DriveConfigs;

/**
 * The MAXSwerveModule class represents a single swerve drive module,
 * encapsulating its driving and turning motors, their associated encoders,
 * and PID controllers for precise control.
 * This configuration is specific to the REV MAXSwerve Module built with NEOs,
 * SPARKS MAX motor controllers, and a Through Bore Encoder for absolute
 * positioning.
 */
public class MAXSwerveModule {
  /** Motor controller for the drive wheel. */
  private final SparkMax m_drivingSpark;
  /** Motor controller for the turning (steer) motor. */
  private final SparkMax m_turningSpark;

  /** Encoder for the drive motor, measuring distance. */
  private final RelativeEncoder m_drivingEncoder;
  /** Absolute encoder for the turning motor, measuring angle. */
  private final AbsoluteEncoder m_turningEncoder;

  /** Closed-loop controller for the drive motor (velocity). */
  private final SparkClosedLoopController m_drivingClosedLoopController;
  /** Closed-loop controller for the turn motor (position). */
  private final SparkClosedLoopController m_turningClosedLoopController;

  /** Angular offset of this module relative to the chassis. */
  private double m_chassisAngularOffset = 0;

  /**
   * Constructs a MAXSwerveModule and configures its driving and turning motors,
   * encoders, and PID controllers.
   * This configuration is tailored for the REV MAXSwerve Module using NEO motors,
   * SPARK MAX motor controllers, and a Through Bore Encoder.
   *
   * @param drivingCANId         The CAN ID of the SparkMax controller for the
   *                             driving motor.
   * @param turningCANId         The CAN ID of the SparkMax controller for the
   *                             turning motor.
   * @param chassisAngularOffset The angular offset of this module relative to the
   *                             chassis, in radians.
   */
  public MAXSwerveModule(int drivingCANId, int turningCANId, double chassisAngularOffset) {
    m_drivingSpark = new SparkMax(drivingCANId, MotorType.kBrushless);
    m_turningSpark = new SparkMax(turningCANId, MotorType.kBrushless);

    m_drivingEncoder = m_drivingSpark.getEncoder();
    m_turningEncoder = m_turningSpark.getAbsoluteEncoder();

    m_drivingClosedLoopController = m_drivingSpark.getClosedLoopController();
    m_turningClosedLoopController = m_turningSpark.getClosedLoopController();

    // Apply the respective configurations to the SPARK MAX controllers.
    // Parameters are reset to a known good state before applying custom
    // configurations,
    // and then persisted to the controller's flash memory to retain settings after
    // power cycles.
    m_drivingSpark.configure(DriveConfigs.MAXSwerveModule.drivingConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    m_turningSpark.configure(DriveConfigs.MAXSwerveModule.turningConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

    this.m_chassisAngularOffset = chassisAngularOffset;
    // Set the driving encoder's position to zero initially.
    m_drivingEncoder.setPosition(0);
  }

  /**
   * Returns the current state of the swerve module.
   *
   * @return A {@link SwerveModuleState} object representing the module's current
   *         velocity and angle.
   */
  public SwerveModuleState getState() {
    // Apply chassis angular offset to the turning encoder position
    // to get the position relative to the chassis's coordinate system.
    return new SwerveModuleState(m_drivingEncoder.getVelocity(),
        new Rotation2d(m_turningEncoder.getPosition() - m_chassisAngularOffset));
  }

  /**
   * Returns the current position of the swerve module.
   *
   * @return A {@link SwerveModulePosition} object representing the module's total
   *         distance driven and angle.
   */
  public SwerveModulePosition getPosition() {
    // Apply chassis angular offset to the turning encoder position
    // to get the position relative to the chassis's coordinate system.
    return new SwerveModulePosition(
        m_drivingEncoder.getPosition(),
        new Rotation2d(m_turningEncoder.getPosition() - m_chassisAngularOffset));
  }

  /**
   * Sets the desired state for the swerve module.
   * This method optimizes the angle to prevent unnecessary spinning
   * and then commands the driving and turning motors to achieve the desired speed
   * and angle.
   *
   * @param desiredState The {@link SwerveModuleState} containing the desired
   *                     speed (meters per second)
   *                     and angle (Rotation2d) for the module.
   */
  public void setDesiredState(SwerveModuleState desiredState) {
    // 1. Get the current rotation from the turning encoder
    Rotation2d currentRotation = new Rotation2d(m_turningEncoder.getPosition());

    // 2. Optimize the state to avoid spinning more than 90 degrees
    desiredState.optimize(currentRotation);

    // 3. Scale speed by cosine of angle error for smoother driving
    // This prevents the robot from "driving" before the wheels are aligned.
    desiredState.cosineScale(currentRotation);

    // 4. Command the SPARK MAX controllers
    m_drivingClosedLoopController.setSetpoint(desiredState.speedMetersPerSecond, ControlType.kVelocity);
    m_turningClosedLoopController.setSetpoint(desiredState.angle.getRadians(), ControlType.kPosition);
  }

  /** Resets the drive motor's position encoder to zero. */
  public void resetEncoders() {
    m_drivingEncoder.setPosition(0);
  }
}