// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.signals.InvertedValue;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.RollerConstants;

public class Rollers extends SubsystemBase { 
   SparkMax kRollorMotor1;

    /** Creates new roller */
    public Rollers() {
      this.kRollorMotor1 = new SparkMax(RollerConstants.kRollorMotor1, MotorType.kBrushless);
    
     SparkMaxConfig kRollorMotor1config = new SparkMaxConfig();

      this.kRollorMotor1.configure(kRollorMotor1config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    }

    public void rollerOn(double speed) {
    this.kRollorMotor1.set(speed);
    }

    public void rollerOff(){
    this.kRollorMotor1.set(0);
    }




}
  