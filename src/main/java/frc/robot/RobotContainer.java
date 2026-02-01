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
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ExtenderIn;
import frc.robot.commands.ExtenderOut;
import frc.robot.commands.FeederOn;
import frc.robot.commands.HomeExtender;
import frc.robot.commands.IntakeEject;
import frc.robot.commands.IntakeReceive;
import frc.robot.commands.IntakeStop;
import frc.robot.commands.RollerForward;
import frc.robot.commands.RollerReverse;
import frc.robot.commands.LauncherIdle;
import frc.robot.commands.LauncherOff;
import frc.robot.commands.LauncherOn;
import frc.robot.subsystems.ExtenderSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.LauncherSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final LauncherSubsystem m_Launcher = new LauncherSubsystem();
  private final IntakeSubsystem m_Intake = new IntakeSubsystem();
  private final ExtenderSubsystem m_Extender = new ExtenderSubsystem();
  private final Rollers m_Rollers = new Rollers();
  private final FeederSubsystem m_Feeder = new FeederSubsystem();
  private boolean m_fieldRelative = true;

  // The driver's controller
  Joystick m_driverJoystick = new Joystick(OIConstants.kDriverJoystickPort);
  Joystick m_operatorJoystick = new Joystick(OIConstants.kOperatorJoystickPort);
  // JoystickButton LauncherOn = new JoystickButton(m_operatorJoystick,
  // OIConstants.kLauncherButton);
  JoystickButton intakeRecieve = new JoystickButton(m_operatorJoystick, OIConstants.kIntakeReceiveButton);
  JoystickButton intakeEject = new JoystickButton(m_operatorJoystick, OIConstants.kIntakeEjectButton);
  JoystickButton launch = new JoystickButton(m_operatorJoystick, OIConstants.kLauncherButton);
  JoystickButton extenderOut = new JoystickButton(m_operatorJoystick, OIConstants.kExtenderOutButton);
  JoystickButton extenderIn = new JoystickButton(m_operatorJoystick, OIConstants.kExtenderInButton);
  JoystickButton launcherIdleOn = new JoystickButton(m_operatorJoystick, OIConstants.kLauncherIdleOnButton);
  JoystickButton launcherIdleOff = new JoystickButton(m_operatorJoystick, OIConstants.kLauncherIdleOffButton);
  JoystickButton homeExtender = new JoystickButton(m_operatorJoystick, OIConstants.kExtenderHomeButton);

  // A chooser for autonomous commands
  private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
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
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // LauncherOn.whileTrue(new LauncherOn(this.m_Launcher));
    intakeRecieve.whileTrue(
        new ParallelCommandGroup(
            new IntakeReceive(m_Intake),
            new RollerForward(m_Rollers)));

    intakeEject.whileTrue(
        new ParallelCommandGroup(
            new IntakeEject(m_Intake),
            new RollerReverse(m_Rollers)));

    extenderOut.onTrue(
        new ExtenderOut(m_Extender));

    extenderIn.onTrue(
        new ParallelCommandGroup(
            new IntakeStop(m_Intake),
            new ExtenderIn(m_Extender)));

    launch.whileTrue(
        new ParallelCommandGroup(
            new RollerForward(m_Rollers),
            new LauncherOn(m_Launcher)
          ).andThen(
            new WaitUntilCommand(() -> m_Launcher.atSpeed()),
            new FeederOn(m_Feeder)
          )
    );

    homeExtender.onTrue(
      new HomeExtender(m_Extender));

    launcherIdleOn.onTrue(
        new LauncherIdle(m_Launcher));

    launcherIdleOff.onTrue(
        new LauncherOff(m_Launcher));

    // new Trigger(() -> m_driverJoystick.getRawButton(1))
    // .onTrue(new InstantCommand(() -> m_fieldRelative = !m_fieldRelative));
    // new Trigger(() -> m_driverJoystick.getRawButton(2)).onTrue(new
    // InstantCommand(m_robotDrive::zeroHeading, m_robotDrive));
    /*
     * / Schedule `ExampleCommand` when `exampleCondition` changes to `true`
     * new Trigger(m_exampleSubsystem::exampleCondition)
     * .onTrue(new ExampleCommand(m_exampleSubsystem));
     * 
     * // Schedule `exampleMethodCommand` when the Xbox controller's B button is
     * pressed,
     * // cancelling on release.
     * m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
     */
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // 1. Get whatever auto the driver picked from the dashboard
    Command selectedAuto = m_autoChooser.getSelected();

    // 2. Wrap it: Run HomeExtender FIRST, then run the selected auto
    return new HomeExtender(m_Extender).andThen(selectedAuto);
  }
}
