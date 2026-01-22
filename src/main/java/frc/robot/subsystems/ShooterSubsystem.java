// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.ShooterConfigs;
import frc.robot.constants.ShooterConstants;


public class ShooterSubsystem extends SubsystemBase {
  /** Creates a new ShooterSubsystem. */
  private final SparkMax m_ShooterMotor;
  
  public ShooterSubsystem() {
    this.m_ShooterMotor = new SparkMax(ShooterConstants.kShooterMotor, MotorType.kBrushless);
    this.m_ShooterMotor.configure(ShooterConfigs.config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic(){

  }

  public void engageShooter(double speed) {
    this.m_ShooterMotor.set(speed);
  }


  public void stopShooter() {
    this.m_ShooterMotor.set(0);
  }
}
