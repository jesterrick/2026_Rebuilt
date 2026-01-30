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
  private final SparkMax m_ExtenderMotor1;
  private final SparkMax m_ExtenderMotor2;

  private final SparkClosedLoopController m_closedLoopController1; // New for 2026
    private final SparkClosedLoopController m_closedLoopController2; // New for 2026

  private double m_targetInches1;
  private double m_targetInches2;


  public ExtenderSubsystem() {
    this.m_ExtenderMotor1 = new SparkMax(CanIdConstants.kExtenderMotor1, MotorType.kBrushless);
    this.m_ExtenderMotor2 = new SparkMax(CanIdConstants.kExtenderMotor2, MotorType.kBrushless);

    // Initialize the closed loop controller from the motor object
    this.m_closedLoopController1 = m_ExtenderMotor1.getClosedLoopController();
    this.m_closedLoopController2 = m_ExtenderMotor2.getClosedLoopController();


    // Apply the configuration from ExtenderConfigs
    this.m_ExtenderMotor1.configure(ExtenderConfigs.config, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    this.m_ExtenderMotor2.configure(ExtenderConfigs.config, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    
    m_targetInches1 = getPositionInInchesMotor1(); // Initialize target to current position
    m_targetInches2 = getPositionInInchesMotor2(); // Initialize target to current position

  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Extender/Position (Inches)", getPositionInInchesMotor1());
    SmartDashboard.putNumber("Extender/Target (Inches)", m_targetInches1);
    SmartDashboard.putNumber("Extender/Position (Inches)", getPositionInInchesMotor2());
    SmartDashboard.putNumber("Extender/Target (Inches)", m_targetInches2);
  }

  public double getPositionInInchesMotor1() {
    return m_ExtenderMotor1.getEncoder().getPosition();
    
  }

  public double getPositionInInchesMotor2() {
    return m_ExtenderMotor2.getEncoder().getPosition();
    
  }

  /**
   * Moves the extender to the 'In' position using PID.
   */
  public void moveIn() {
    m_targetInches1 = ExtenderConstants.kExtenderMotorIn;
    m_targetInches2 = ExtenderConstants.kExtenderMotorIn;

    // Uses ControlType.kPosition to move to the specific distance
    m_closedLoopController1.setSetpoint(m_targetInches1, ControlType.kPosition);
    m_closedLoopController2.setSetpoint(m_targetInches2, ControlType.kPosition);

  }

  /**
   * Moves the extender to the 'Out' position using PID.
   */
  public void moveOut() {
    m_targetInches1 = ExtenderConstants.kExtenderMotorOut;
    m_targetInches2 = ExtenderConstants.kExtenderMotorOut;

    // The library handles conversion because of the factor set in ExtenderConfigs
    m_closedLoopController1.setSetpoint(m_targetInches1, ControlType.kPosition);
    m_closedLoopController2.setSetpoint(m_targetInches2, ControlType.kPosition);

  }

  public boolean atTarget1() {
    return Math.abs(m_targetInches1 - getPositionInInchesMotor1()) < 0.1;
    
  }

  public boolean atTarget2() {
    return Math.abs(m_targetInches2 - getPositionInInchesMotor2()) < 0.1;
    
  }

  public void stop() {
    m_ExtenderMotor1.stopMotor();
    m_ExtenderMotor2.stopMotor();

  }
}