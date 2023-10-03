package frc.team3128.autonomous;

import java.util.HashMap;

import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team3128.Constants.SwerveConstants;
import frc.team3128.common.narwhaldashboard.NarwhalDashboard;
import frc.team3128.common.utility.Log;
import frc.team3128.subsystems.Swerve;

/**
 * Class to store information about autonomous routines.
 * @author Daniel Wang, Mason Lam
 */

public class AutoPrograms {

    public static Swerve swerve;

    public AutoPrograms() {
        swerve = Swerve.getInstance();

        Trajectories.initTrajectories();
        initAutoSelector();
    }

    private void initAutoSelector() {
        String[] autoStrings = new String[] {"b_cable_1Cone+1Cube","b_cable_1Cone+2Cube","b_cable_1Cone+1.5Cube+Climb","b_mid_1Cone+Climb"};
        NarwhalDashboard.addAutos(autoStrings);
    }

    public Command getAutonomousCommand() {
       //String selectedAutoName = NarwhalDashboard.getSelectedAutoName();
        String selectedAutoName = "b_cable_1Cone+2Cube"; //uncomment and change this for testing without opening Narwhal Dashboard
        SmartDashboard.putString(selectedAutoName, selectedAutoName);
        if (selectedAutoName == null) {
            return null;
        }

        return Trajectories.get(selectedAutoName,false);
    }
    
    // /** 
    //  * Follow trajectory and intake balls along the path
    //  */
    // private SequentialCommandGroup IntakePathCmd(String trajectory) {
    //     ParallelDeadlineGroup movement = new ParallelDeadlineGroup(
    //                                         trajectoryCmd(trajectory), 
    //                                         new ScheduleCommand(new CmdExtendIntakeAndRun()));
    //     return new SequentialCommandGroup(new InstantCommand(intake::ejectIntake, intake), movement);
    // }

    /**
     * Flip 180 degrees rotation wise but keep same pose translation 
     */
    private Pose2d inverseRotation(Pose2d pose) {
        return new Pose2d(pose.getTranslation(), new Rotation2d(pose.getRotation().getRadians() + Math.PI));
    }
}