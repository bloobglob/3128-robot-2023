package frc.team3128.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import static frc.team3128.Constants.ManipulatorConstants.STALL_POWER;
import frc.team3128.subsystems.Manipulator;


public class CmdManipGrab extends SequentialCommandGroup {

    public CmdManipGrab(boolean cone) {
        var manipulator = Manipulator.getInstance();
        
        addCommands(
            new InstantCommand(()-> manipulator.intake(cone), manipulator),
            new WaitCommand(0.4),
            new WaitUntilCommand(()-> manipulator.hasObjectPresent()),
            new WaitCommand(cone ? 0.1 : 0),
            new InstantCommand(()-> manipulator.stallPower(), manipulator)
        );
    }
}
