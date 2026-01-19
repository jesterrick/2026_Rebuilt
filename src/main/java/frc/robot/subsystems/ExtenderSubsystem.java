// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ExtenderConstants;

public class ExtenderSubsystem extends SubsystemBase {
  private final SparkMax m_ExtenderMotor;
  private final RelativeEncoder m_ExtenderMotorEncoder;

  private double targetPosition = ExtenderConstants.kExtenderMotorIn; // Start up

  /** Creates a new IntakeRotator. */
  public ExtenderSubsystem() {
    this.m_ExtenderMotor = new SparkMax(ExtenderConstants.kExtenderMotor, MotorType.kBrushless);

    SparkMaxConfig extenderConfig = new SparkMaxConfig();
    extenderConfig.inverted(true);
    extenderConfig.idleMode(IdleMode.kBrake);
    extenderConfig.smartCurrentLimit(40);

    this.m_ExtenderMotorEncoder = m_ExtenderMotor.getEncoder();
    EncoderConfig extenderEncoderConfig = new EncoderConfig();
    extenderConfig.encoder.apply(extenderEncoderConfig);
    
    this.m_ExtenderMotor.configure(extenderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    double currentPos = this.m_ExtenderMotorEncoder.getPosition();
    double error = targetPosition - currentPos;
    
    double output = ExtenderConstants.kExtenderMotorSpeed;
    
    // Simple bang-bang control with slow zone
    if (error > 5.0) {
      output = output * 1.0; // Move down fast
    } else if (error > 1.0) {
      output = output * 0.35; // Slow down near target
    } else if (error < -5.0) {
      output = output * -1.0; // Move up fast
    } else if (error < -1.0) {
      output = output * -0.35; // Slow down near target
    } else {
      output = 0.0; // At target, stop (brake mode will hold it)
    }
    
    // Safety limits
    if (currentPos <= ExtenderConstants.kExtenderMotorIn && output < 0) {
      output = 0.0;
    }
    if (currentPos >= ExtenderConstants.kExtenderMotorOut && output > 0) {
      output = 0.0;
    }
    
    this.m_ExtenderMotor.set(output);
    
    SmartDashboard.putNumber("Intake/pos", currentPos);
    SmartDashboard.putNumber("Intake/target", targetPosition);
    SmartDashboard.putNumber("Intake/error", error);
    SmartDashboard.putNumber("Intake/output", output);
  }

  // Simple methods to move up or down
  public void moveUp() {
    this.targetPosition = ExtenderConstants.kExtenderMotorIn;
  }
  
  public void moveDown() {
    this.targetPosition = ExtenderConstants.kExtenderMotorOut;
  }
  
  // Check if at target (within 1 rotation)
  public boolean atTarget() {
    return Math.abs(this.targetPosition - this.m_ExtenderMotorEncoder.getPosition()) < 1.0;
  }
  
  public double getPosition() {
    return this.m_ExtenderMotorEncoder.getPosition();
  }
}