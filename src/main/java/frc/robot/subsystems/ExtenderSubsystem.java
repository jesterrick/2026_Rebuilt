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
  private final SparkClosedLoopController m_FollowController;  
  
  private double m_globalTargetInches = 0.0;
  private boolean m_isHomed = false; // Track if the box is squared and zeroed

  public ExtenderSubsystem() {
    this.m_ExtenderLeaderMotor = new SparkMax(CanIdConstants.kExtenderMotor1, MotorType.kBrushless);
    this.m_ExtenderFollowMotor = new SparkMax(CanIdConstants.kExtenderMotor2, MotorType.kBrushless);

    this.m_LeaderController = m_ExtenderLeaderMotor.getClosedLoopController();
    this.m_FollowController = m_ExtenderFollowMotor.getClosedLoopController();

    this.m_ExtenderLeaderMotor.configure(ExtenderConfigs.leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    this.m_ExtenderFollowMotor.configure(ExtenderConfigs.followConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    double leaderPos = getPositionInInches(m_ExtenderLeaderMotor);
    double followPos = getPositionInInches(m_ExtenderFollowMotor);
    double error = leaderPos - followPos;

    // Only sync if we are homed and not in an error state
    if (m_isHomed) {
      // 0.1 means for every 1 inch of skew, we "nudge" the follower 0.1 inches
      double kSyncP = 0.1; 
      double correction = error * kSyncP;

      // Command both motors independently. The Follower gets the "nudge."
      m_LeaderController.setSetpoint(m_globalTargetInches, ControlType.kMAXMotionPositionControl);
      m_FollowController.setSetpoint(m_globalTargetInches + correction, ControlType.kMAXMotionPositionControl);

      // Hard Safety Stop
      if (Math.abs(error) > ExtenderConstants.kMaxPositionDifference) {
        stop();
        m_isHomed = false;
        SmartDashboard.putBoolean("Extender/SKEW_ERROR", true);
      }
    }

    // Dashboard Updates
    SmartDashboard.putNumber("Extender/Leader Inches", leaderPos);
    SmartDashboard.putNumber("Extender/Follower Inches", followPos);
    SmartDashboard.putBoolean("Extender/Is Homed", m_isHomed);

    // Warning if the robot is enabled but not homed
    if (!m_isHomed) {
      SmartDashboard.putString("Extender/Status", "RE-ZERO REQUIRED");
    } else {
      SmartDashboard.putString("Extender/Status", "READY");
    }
  }

  public void moveOut() {
    if (!m_isHomed) return; // Safety: Don't move to 12" if we don't know where 0 is
    m_globalTargetInches = ExtenderConstants.kExtenderMotorOut;
  }

  public void moveIn() {
    if (!m_isHomed) return;
    m_globalTargetInches = ExtenderConstants.kExtenderMotorIn;
  }

  public void resetEncoders() {
    m_ExtenderLeaderMotor.getEncoder().setPosition(0);
    m_ExtenderFollowMotor.getEncoder().setPosition(0);
    this.m_globalTargetInches = 0;
    this.m_isHomed = true; // Box is now square and trusted
  }

  public void stop() {
    m_ExtenderLeaderMotor.stopMotor();
    m_ExtenderFollowMotor.stopMotor();
  }

  public double getPositionInInches(SparkMax motor) {
    return motor.getEncoder().getPosition();
  }

  public boolean atTarget(double target) {
    // Checking leader is usually enough, but you can check both for extra safety
    return Math.abs(target - getPositionInInches(m_ExtenderLeaderMotor)) < 0.1;
  }

  // Add these to your ExtenderSubsystem.java

  /** Use raw voltage for homing to bypass PID/Sync logic temporarily */
  public void setHomingVoltages(double voltage) {
    m_ExtenderLeaderMotor.setVoltage(voltage);
    m_ExtenderFollowMotor.setVoltage(voltage);
  }

  /** Check current to see if we've hit the back of the frame */
  public double getLeaderCurrent() {
    return m_ExtenderLeaderMotor.getOutputCurrent();
  }

  public double getFollowerCurrent() {
    return m_ExtenderFollowMotor.getOutputCurrent();
  }
}