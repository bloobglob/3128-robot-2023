package frc.team3128.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team3128.common.hardware.motorcontroller.NAR_TalonSRX;
import frc.team3128.common.utility.NAR_Shuffleboard;

import static frc.team3128.Constants.ManipulatorConstants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

public class Manipulator extends SubsystemBase {
    
    private DoubleSolenoid m_solenoid;
    private NAR_TalonSRX m_roller;

    private static Manipulator instance;

    public boolean objectPresent;

    public Manipulator(){
        configPneumatics();
        configMotor();
    }

    public static Manipulator getInstance() {
        if (instance == null){
            instance = new Manipulator() ;  
        }
        
        return instance;
    }

    public void configPneumatics(){
        m_solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, SOLENOID_FORWARD_CHANNEL_ID, SOLENOID_BACKWARD_CHANNEL_ID);
        m_solenoid.set(Value.kForward);
    }

    public void configMotor(){
        m_roller = new NAR_TalonSRX(ROLLER_MOTOR_ID);
        m_roller.setInverted(false);
        m_roller.setNeutralMode(NeutralMode.Brake);
        m_roller.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 20, 10, 0.5));
        m_roller.clearStickyFaults(10);
    }

    public void openClaw(){
        m_solenoid.set(Value.kForward);
    }

    public void closeClaw(){
        m_solenoid.set(Value.kReverse);
    }
    
    public void toggleClaw() {
        m_solenoid.toggle();
    }

    public Value getClawState() {
        return m_solenoid.get();
    }

    public void setRollerPower(double power){
        m_roller.set(power);
    }

    public void enableRollers(boolean isForwards){
        m_roller.set(0.5);
    }

    public void stopRoller(){
        m_roller.set(0);
    }

    public double getCurrent(){
        return m_roller.getStatorCurrent();
    }

    public boolean hasObjectPresent(){
        if(getCurrent() > CURRENT_THRESHOLD){
            objectPresent = true;
        }
        else {
            objectPresent = false;
        }

        return objectPresent;
    }

    public boolean compensateVoltage(){
        double precentOutput;

        if(hasObjectPresent()){
            precentOutput = 0.3;
        }
        else {
            precentOutput = 0.5;
        }
        setRollerPower(Math.copySign(precentOutput, m_roller.getMotorOutputPercent()));
        return hasObjectPresent();
    }

    public void initShuffleboard() {
        NAR_Shuffleboard.addData("Manipulator","Value", () -> getClawState().toString(),0,0);
    }
}
