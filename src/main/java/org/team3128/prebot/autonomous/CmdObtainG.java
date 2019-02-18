package org.team3128.prebot.autonomous;

import org.team3128.common.util.enums.Direction;
import org.team3128.common.util.units.Length;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.drive.SRXTankDrive.CmdPlotG;
import org.team3128.common.util.Log;
import com.kauailabs.navx.frc.AHRS;


import edu.wpi.first.wpilibj.command.CommandGroup;
public class Wrapper {
    String csv = "angular velocity, gL, gR";
}
public class CmdFancyCalibrateWheelBase extends CommandGroup {
    public CmdFancyCalibrateWheelBase(AHRS ahrs) {    
        Wrapper data = new Wrapper();
        SRXTankDrive drive = SRXTankDrive.getInstance();
        addSequential(drive.new CmdPlotG(data,ahrs,1.0,0.8,3));
        addSequential(drive.new CmdPlotG(data,ahrs,1.0,0.6,3));
        addSequential(drive.new CmdPlotG(data,ahrs,1.0,0.4,3));
        addSequential(drive.new CmdPlotG(data,ahrs,1.0,0.2,3));  
        addSequential(drive.new CmdPlotG(data,ahrs,0.8,1.0,3));
        addSequential(drive.new CmdPlotG(data,ahrs,0.6,1.0,3));
        addSequential(drive.new CmdPlotG(data,ahrs,0.4,1.0,3));
        addSequential(drive.new CmdPlotG(data,ahrs,0.2,1.0,3));
    }
}