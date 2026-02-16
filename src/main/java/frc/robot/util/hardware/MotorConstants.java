// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util.hardware;

import frc.robot.util.hardware.MotorSettings.MotorRotation;
import frc.robot.util.hardware.MotorSettings.NeutralBehavior;

/**
 * An interface for defining a "contract" for motor configuration constants.
 * Any class that implements this interface can be used by the HardwareFactory
 * to automatically build a complete MotorConfig.
 * 
 * This pattern allows for centralized, type-safe, and boilerplate-free motor configuration.
 * Default methods are provided for values that are not applicable to all motor types.
 */
public interface MotorConstants {

    // --- General Settings ---
    default NeutralBehavior getNeutralBehavior() { return NeutralBehavior.kCoast; }
    default int getCurrentLimit() { return 40; } // Default to a moderate 40 amps
    default MotorRotation getMotorRotation() { return MotorRotation.kCounterClockwise; }
    default double getConversionRatio() { return 1.0; }

    // --- PIDF Settings ---
    default double getP() { return 0.0; }
    default double getI() { return 0.0; }
    default double getD() { return 0.0; }
    default double getV() { return 0.0; } // 'V' for Velocity Feedforward (kV)
    default double getS() { return 0.0; } // 'S' for Static Feedforward (kS)

    // --- Motion Profiling ---
    default boolean isMotionProfilingEnabled() { return false; }
    default double getCruiseVelocity() { return 0.0; }
    default double getMaxAcceleration() { return 0.0; }
    default double getAllowedError() { return 0.0; }

    // --- Soft Limits ---
    default boolean isForwardLimitEnabled() { return false; }
    default double getForwardLimit() { return 0.0; }
    default boolean isReverseLimitEnabled() { return false; }
    default double getReverseLimit() { return 0.0; }
}
