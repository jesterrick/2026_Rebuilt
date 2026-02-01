// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.constants.AprilTagConstants;


/** Add your docs here. */
public class VisionUtils {
    public static boolean isTargetingCorrectHoop(int currentTagID) {
    var alliance = DriverStation.getAlliance();
    int[] validTags;

    if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red) {
        validTags = AprilTagConstants.kRedHubTags;
    } else {
        validTags = AprilTagConstants.kBlueHubTags;
    }

    // Check if the current tag is in our "valid" list
    for (int tag : validTags) {
        if (currentTagID == tag) {
            return true;
        }
    }
    return false;
}
}
