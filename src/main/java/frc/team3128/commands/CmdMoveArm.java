package frc.team3128.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team3128.Constants.ArmConstants.ArmPosition;
import frc.team3128.subsystems.Intake;
import frc.team3128.subsystems.Manipulator;
import frc.team3128.subsystems.Pivot;
import frc.team3128.subsystems.Telescope;
import frc.team3128.subsystems.Vision;

public class CmdMoveArm extends CommandBase{

    private Pivot pivot = Pivot.getInstance();
    private Telescope telescope = Telescope.getInstance();
    private Manipulator manipulator = Manipulator.getInstance();

    private double angle;
    private double dist;
    private boolean teleStatic;
    
    public CmdMoveArm(double angle, double dist, boolean reversed){
        this.angle = reversed ? -angle : angle;
        this.dist = dist;

        addRequirements(pivot, telescope);
    }

    public CmdMoveArm(ArmPosition position, boolean reversed){
        this.angle = reversed ? -position.pivotAngle : position.pivotAngle;
        this.dist = position.teleDist;
        addRequirements(pivot, telescope);
    }

    @Override
    public void initialize(){

        teleStatic = false;

        if (dist >= telescope.getDist()) {
            pivot.startPID(angle);
            if (dist == telescope.getSetpoint()) teleStatic = true;
            // might have a bug if pidcontroller setpoint resets when pid disabled
        }
        else 
            telescope.startPID(dist);

    }

    @Override
    public void execute(){
        if (pivot.atSetpoint() && (telescope.getSetpoint() != dist))
            telescope.startPID(dist);
        if (telescope.atSetpoint() && (pivot.getSetpoint() != angle || !pivot.isEnabled())) 
            pivot.startPID(angle);
    }

    @Override
    public void end(boolean interrupted){
        telescope.stopTele(); 
    }

    @Override
    public boolean isFinished(){
        return pivot.atSetpoint() && (telescope.atSetpoint() || teleStatic);
    }

}
