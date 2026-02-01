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

public class ClimberSubsystem extends SubsystemBase {
  private final SparkMax m_ClimberLeaderMotor;
  private final SparkMax m_ClimberFollowMotor;
  private final SparkClosedLoopController m_LeaderController;
  private final SparkClosedLoopController m_FollowController;

  private double m_targetPosition = 0.0;

  /** Creates a new ClimberSubsystem. */
  public ClimberSubsystem() {
    this.m_ClimberLeaderMotor = new SparkMax(CanIdConstants.kClimberMotor1, MotorType.kBrushless);
    this.m_ClimberFollowMotor = new SparkMax(CanIdConstants.kClimberMotor2, MotorType.kBrushless);

    this.m_LeaderController = m_ClimberLeaderMotor.getClosedLoopController();
    this.m_FollowController = m_ClimberFollowMotor.getClosedLoopController();

    this.m_ClimberLeaderMotor.configure(ClimberConfigs.leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    this.m_ClimberFollowMotor.configure(ClimberConfigs.followConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    double climberPos = getClimberHeight();

    this.m_LeaderController.setSetpoint(m_targetPosition, ControlType.kMAXMotionPositionControl);

    SmartDashboard.putNumber("Climber Height", climberPos);
  }

  public  void climberUp()
  {
    this.m_targetPosition = ClimberConstants.kClimberMaxExtend;
  }

  public void climberDown()
  {
    this.m_targetPosition = ClimberConstants.kClimberZero;
  }

  private double getClimberHeight()
  {
    return this.m_ClimberFollowMotor.getEncoder().getPosition();
  }

  public void stop()
  {
    this.m_targetPosition = getClimberHeight();
    this.m_ClimberLeaderMotor.stopMotor();
  }
}
