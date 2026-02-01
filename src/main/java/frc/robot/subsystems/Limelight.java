// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.VisionConstants;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.cscore.HttpCamera;

/**
 * The Limelight subsystem is responsible for initializing and managing the Limelight
 * vision camera. It provides access to the Limelight's NetworkTable data and
 * streams its video feed to the FRC Driver Station.
 */
public class Limelight extends SubsystemBase {
  /** The NetworkTable instance for accessing Limelight data. */
  private final NetworkTable limelightTable;
  
  /**
   * Constructs a new Limelight subsystem.
   * Initializes the NetworkTable for the Limelight and starts streaming its video feed.
   */
  public Limelight() {
    limelightTable = NetworkTableInstance.getDefault().getTable(VisionConstants.LIMELIGHT_NAME);
    HttpCamera limelightCamera = new HttpCamera(VisionConstants.LIMELIGHT_NAME, VisionConstants.LIMELIGHT_URL);
    // Start automatically capturing the camera feed from the Limelight.
    CameraServer.startAutomaticCapture(limelightCamera);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // Any periodic tasks for the Limelight (e.g., updating modes) can go here.
  }
}
