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
  private final SparkMax m_ExtenderLeaderMotor;
  private final SparkMax m_ExtenderFollowMotor;
  private final SparkClosedLoopController m_LeaderController;

  public ExtenderSubsystem() {
    this.m_ExtenderLeaderMotor = new SparkMax(CanIdConstants.kExtenderMotor1, MotorType.kBrushless);
    this.m_ExtenderFollowMotor = new SparkMax(CanIdConstants.kExtenderMotor2, MotorType.kBrushless);

    this.m_LeaderController = m_ExtenderLeaderMotor.getClosedLoopController();

    // Note: You do NOT need m_FollowController because it follows the leader in hardware
    
    this.m_ExtenderLeaderMotor.configure(ExtenderConfigs.leaderConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    this.m_ExtenderFollowMotor.configure(ExtenderConfigs.followConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    double leaderPos = getPositionInInches(m_ExtenderLeaderMotor);
    double followPos = getPositionInInches(m_ExtenderFollowMotor);

    // Dashboard Updates
    SmartDashboard.putNumber("Extender/Leader Inches", leaderPos);
    SmartDashboard.putNumber("Extender/Follower Inches", followPos);

    // CRITICAL: Skew/Twist Safety Check for box with no cross-shaft
    if (Math.abs(leaderPos - followPos) > 0.5) {
      stop();
      SmartDashboard.putBoolean("Extender/SKEW_ERROR", true);
    }
  }

  public double getPositionInInches(SparkMax motor) {
    return motor.getEncoder().getPosition();    
  }

  public void moveIn() {
    // Only command the LEADER. 
    // Use kMAXMotionPositionControl to use your cruise velocity/acceleration settings.
    m_LeaderController.setSetpoint(ExtenderConstants.kExtenderMotorIn, ControlType.kMAXMotionPositionControl);
  }

  public void moveOut() {
    m_LeaderController.setSetpoint(ExtenderConstants.kExtenderMotorOut, ControlType.kMAXMotionPositionControl);
  }

  public void stop() {
    // Stopping the leader automatically stops the hardware follower
    m_ExtenderLeaderMotor.stopMotor();
  }

  public boolean atTarget(double target) {
    // Checking leader is usually enough, but you can check both for extra safety
    return Math.abs(target - getPositionInInches(m_ExtenderLeaderMotor)) < 0.1;
  }
}