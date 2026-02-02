// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.hardware.MotorControllerWrapper;

/**
 * The Rollers subsystem controls the robot's roller mechanism,
 * typically used for intaking, indexing, or outtaking game pieces.
 */
public class Rollers extends SubsystemBase { 
   /** The motor responsible for driving the rollers. */
   public final MotorControllerWrapper m_RollerMotor;

    /**
     * Constructs a new Rollers subsystem.
     * Initializes the roller motor and configures it with predefined settings.
     */
    public Rollers(MotorControllerWrapper motor) {
      this.m_RollerMotor = motor;
    }

    /**
     * Turns the roller motor on at a specified speed.
     * A positive speed typically means intake, while a negative speed means outtake.
     * @param speed The speed to set the roller motor to, typically a value between -1.0 and 1.0.
     */
    public void rollerOn(double speed) {
      this.m_RollerMotor.set(speed);
    }

    /**
     * Turns the roller motor off by setting its speed to zero.
     */
    public void rollerOff(){
      this.m_RollerMotor.set(0);
    }
}
  