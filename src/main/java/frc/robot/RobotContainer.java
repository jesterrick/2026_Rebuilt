// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Autos;
import frc.robot.commands.DriveCommand;
import frc.robot.constants.OIConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DriveStraight;
import frc.robot.commands.ExtenderIn;
import frc.robot.commands.ExtenderOut;
import frc.robot.commands.FeederOff;
import frc.robot.commands.FeederOn;
import frc.robot.commands.IntakeEject;
import frc.robot.commands.IntakeReceive;
import frc.robot.commands.IntakeStop;
import frc.robot.commands.RollerOff;
import frc.robot.commands.RollerOn;
import frc.robot.commands.ShooterIdle;
import frc.robot.commands.ShooterOff;
import frc.robot.commands.ShooterOn;
import frc.robot.subsystems.ExtenderSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.ShooterSubsystem;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final ShooterSubsystem m_shooter= new ShooterSubsystem();
  private final IntakeSubsystem m_Intake= new IntakeSubsystem();
  private boolean m_fieldRelative = true;

  // The driver's controller
    Joystick m_driverJoystick = new Joystick(OIConstants.kDriverJoystickPort);
    Joystick m_operatorJoystick = new Joystick(OIConstants.kOperatorJoystickPort);
    JoystickButton ShooterOn = new JoystickButton(m_operatorJoystick, OIConstants.kShooterButton);
    JoystickButton intakeRecieve = new JoystickButton(m_operatorJoystick, OIConstants.kIntakeReceiveButton);

    // A chooser for autonomous commands
    private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
     // Configure default commands
      m_robotDrive.setDefaultCommand(
        new DriveCommand(
          m_robotDrive,
            () -> m_driverJoystick.getY(),
            () -> m_driverJoystick.getX(),
            () -> m_driverJoystick.getZ(),
            () -> m_fieldRelative));

    // Configure the trigger bindings
    configureBindings();

    // Add commands to the autonomous command chooser
    m_autoChooser.setDefaultOption("Do Nothing", Commands.none());
    m_autoChooser.addOption("Simple Auto", Autos.exampleAuto(m_robotDrive));

    // Put the chooser on the dashboard
    SmartDashboard.putData("Auto choices", m_autoChooser);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
   //ShooterOn.whileTrue(new ShooterOn(this.m_shooter));
   intakeRecieve.whileTrue(new IntakeReceive(this.m_Intake, 0.3));


    //new Trigger(() -> m_driverJoystick.getRawButton(1))
    //    .onTrue(new InstantCommand(() -> m_fieldRelative = !m_fieldRelative));
    //new Trigger(() -> m_driverJoystick.getRawButton(2)).onTrue(new InstantCommand(m_robotDrive::zeroHeading, m_robotDrive));
    /*/ Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
    */
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return m_autoChooser.getSelected();
  }
}
