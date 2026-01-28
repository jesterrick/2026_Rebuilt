// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.LauncherConfigs;
import frc.robot.constants.CanIdConstants;

public class LauncherSubsystem extends SubsystemBase {
  /** Creates a new LauncherSubsystem. */
  private final SparkMax m_LauncherMotor;
  
  public LauncherSubsystem() {
    this.m_LauncherMotor = new SparkMax(CanIdConstants.kLauncherMotor, MotorType.kBrushless);
    this.m_LauncherMotor.configure(LauncherConfigs.config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void engageLauncher(double speed) {
    this.m_LauncherMotor.set(speed);
  }

  public void stopLauncher() {
    this.m_LauncherMotor.set(0);
  }
}
