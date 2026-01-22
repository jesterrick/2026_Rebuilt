// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.FeederConfigs;
import frc.robot.constants.FeederConstants;

public class FeederSubsystem extends SubsystemBase {
  /** Creates a new FeederSubsystem. */
  private final SparkMax m_FeederMotor;
  
  public FeederSubsystem() {
    this.m_FeederMotor = new SparkMax(FeederConstants.kFeederMotor, MotorType.kBrushless);
    this.m_FeederMotor.configure(FeederConfigs.config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic(){

  }

  public void engageFeeder(double speed) {
    this.m_FeederMotor.set(speed);
  }


  public void stopFeeder() {
    this.m_FeederMotor.set(0);
  }
}
