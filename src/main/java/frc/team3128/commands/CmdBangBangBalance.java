package frc.team3128.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.team3128.subsystems.Swerve;

public class CmdBangBangBalance extends CommandBase{
    private int plateauCount = 0;
    private Swerve swerve;
    private double maxRoll; 

    //Essentially WaitUntilCommand
    public CmdBangBangBalance() {
        
        swerve = Swerve.getInstance();
        plateauCount = 0;
        maxRoll = 0;
    }

    @Override
    public void initialize() {
        plateauCount = 0;
        maxRoll = Math.abs(swerve.getRoll());
    }
    @Override
    public void execute() {
        if (Math.abs(swerve.getRoll()) > maxRoll) {
            plateauCount = 0;
            maxRoll = swerve.getRoll();
        }
        else {
            plateauCount++;
        }
    }

    @Override
    public void end(boolean interrupted) {
        // Timer.delay(time.getAsDouble());
        new RunCommand(()-> swerve.xlock(), swerve).schedule();
    }

    @Override
    public boolean isFinished() {
        return plateauCount >= 10;
    }
}