package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType; // Required for 2026 API
import com.revrobotics.spark.SparkClosedLoopController; // Required for 2026 API

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.ExtenderConfigs;
import frc.robot.constants.CanIdConstants;
import frc.robot.constants.ExtenderConstants;

public class ExtenderSubsystem extends SubsystemBase {
  private final SparkMax m_ExtenderMotor;
  private final SparkClosedLoopController m_closedLoopController; // New for 2026
  private double m_targetInches;

  public ExtenderSubsystem() {
    this.m_ExtenderMotor = new SparkMax(CanIdConstants.kExtenderMotor, MotorType.kBrushless);
    
    // Initialize the closed loop controller from the motor object
    this.m_closedLoopController = m_ExtenderMotor.getClosedLoopController();

    // Apply the configuration from ExtenderConfigs
    this.m_ExtenderMotor.configure(ExtenderConfigs.config, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    
    m_targetInches = getPositionInInches(); // Initialize target to current position
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Extender/Position (Inches)", getPositionInInches());
    SmartDashboard.putNumber("Extender/Target (Inches)", m_targetInches);
  }

  public double getPositionInInches() {
    return m_ExtenderMotor.getEncoder().getPosition();
  }

  /**
   * Moves the extender to the 'In' position using PID.
   */
  public void moveIn() {
    m_targetInches = ExtenderConstants.kExtenderMotorIn;
    // Uses ControlType.kPosition to move to the specific distance
    m_closedLoopController.setSetpoint(m_targetInches, ControlType.kPosition);
  }

  /**
   * Moves the extender to the 'Out' position using PID.
   */
  public void moveOut() {
    m_targetInches = ExtenderConstants.kExtenderMotorOut;
    // The library handles conversion because of the factor set in ExtenderConfigs
    m_closedLoopController.setSetpoint(m_targetInches, ControlType.kPosition);
  }

  public boolean atTarget() {
    return Math.abs(m_targetInches - getPositionInInches()) < 0.1;
  }

  public void stop() {
    m_ExtenderMotor.stopMotor();
  }
}