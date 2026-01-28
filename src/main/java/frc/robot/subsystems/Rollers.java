// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CanIdConstants;

public class Rollers extends SubsystemBase { 
   public final SparkMax m_RollerMotor;

    /** Creates new roller */
    public Rollers() {
      this.m_RollerMotor = new SparkMax(CanIdConstants.kRollerMotor, MotorType.kBrushless);
  
     SparkMaxConfig m_RollerMotorconfig = new SparkMaxConfig();

      this.m_RollerMotor.configure(m_RollerMotorconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    }

    /** Turns roller on */
    public void rollerOn(double speed) {
    this.m_RollerMotor.set(speed);
    }

    /** Turns roller off */
    public void rollerOff(){
    this.m_RollerMotor.set(0);
    }




}
  