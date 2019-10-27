package org.team3128.gromit.main;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import org.team3128.common.hardware.motor.LazyTalonSRX;
import org.team3128.common.hardware.motor.LazyVictorSPX;

import org.team3128.common.hardware.limelight.StreamMode;
import org.team3128.common.hardware.misc.Piston;
import org.team3128.common.narwhaldashboard.NarwhalDashboard;
import org.team3128.common.utility.units.Angle;
import org.team3128.common.utility.units.Length;
import org.team3128.gromit.mechanisms.Climber;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.team3128.gromit.constants.GromitConstants;
import org.team3128.common.generics.RobotConstants;

public class MainGromit extends MainDeepSpaceRobot {
    // Pnuematics
    Piston placeholder;

    // Climber
    public Climber climber;
    public Piston climb_Piston;
    public LazyTalonSRX climbMotor;

    private CommandGroup climbCommand;

    @Override
    protected void constructHardware() {
<<<<<<< HEAD
        wheelbase = 37 * Length.in;
        driveMaxSpeed = 5800;
        //gearRatio = 2.9 + 54/990;
        wheelCirc = 12.01 * Length.in;

        leftSpeedScalar = 1.00;
        rightSpeedScalar = 1.00;
=======
        // Setting constants. This shouldn't be edited.
        wheelbase = GromitConstants.WHEEL_CIRCUMFERENCE;
        driveMaxSpeed = GromitConstants.DRIVE_MAX_SPEED;
        wheelCirc = GromitConstants.WHEEL_CIRCUMFERENCE;
        leftSpeedScalar = GromitConstants.LEFT_SPEED_SCALAR;
        rightSpeedScalar = GromitConstants.RIGHT_SPEED_SCALAR;
        shiftUpSpeed = GromitConstants.SHIFT_UP_SPEED;
        shiftDownSpeed = GromitConstants.SHIFT_DOWN_SPEED;
>>>>>>> upstream/code-overhaul

        // Pnuematics Constants
        gearshift_Piston = new Piston(GromitConstants.GEARSHIFT_SOL_A, GromitConstants.GEARSHIFT_SOL_B);
        gearshift_Piston.setPistonOn();

        climb_Piston = new Piston(GromitConstants.CLIMB_SOL_A, GromitConstants.CLIMB_SOL_B);
        climb_Piston.setPistonOff();

        hatchIntake_Piston = new Piston(GromitConstants.HATCH_INTAKE_SOL_A, GromitConstants.HATCH_INTAKE_SOL_B);

        placeholder = new Piston(GromitConstants.PLACEHOLDER_SOL_A, GromitConstants.PLACEHOLDER_SOL_B);
        placeholder.setPistonOn();

        // Setting Mechanism Constants
        liftLimitSwitch = new DigitalInput(GromitConstants.LIFT_LIMIT_SWITCH);
        liftSwitchPosition = GromitConstants.LIFT_SWITCH_POSITION;// 170;
        liftMaxVelocity = GromitConstants.LIFT_MAX_VELOCITY;

        fourBarLimitSwitch = new DigitalInput(GromitConstants.FOURBAR_LIMIT_SWITCH);
        fourBarRatio = GromitConstants.FOURBAR_RATIO;
        fourBarSwitchPosition = GromitConstants.FOURBAR_SWITCH_POSITION;
        fourBarMaxVelocity = GromitConstants.FOURBAR_MAX_VELOCITY;

        bottomLLHeight = GromitConstants.BOTTOM_LIMELIGHT_HEIGHT;
        bottomLLAngle = GromitConstants.BOTTOM_LIMELIGHT_ANGLE;

        topLLHeight = GromitConstants.TOP_LIMELIGHT_HEIGHT;
        topLLAngle = -12.0 * Angle.DEGREES;

        // Construct and Configure Drivetrain
        leftDriveLeader = new LazyTalonSRX(10);
        leftDriveFollower = new LazyVictorSPX(11);
        rightDriveLeader = new LazyTalonSRX(15);
        rightDriveFollower = new LazyVictorSPX(16);

        super.constructHardware();

        topLimelight.setStreamMode(StreamMode.DRIVER_CAMERA);
        bottomLimelight.setStreamMode(StreamMode.DRIVER_CAMERA);

        leftDriveLeader.setInverted(InvertType.None);
        leftDriveFollower.setInverted(InvertType.FollowMaster);
        leftDriveLeader.setSensorPhase(false);

        rightDriveLeader.setInverted(InvertType.InvertMotorOutput);
        rightDriveFollower.setInverted(InvertType.FollowMaster);
        rightDriveLeader.setSensorPhase(true);

        // Create the Climber
        climbMotor = new LazyTalonSRX(40);
        Climber.initialize(climb_Piston, climbMotor);
        climber = Climber.getInstance();

        NarwhalDashboard.addButton("climb_12", (boolean down) -> {
            if (down) {
                if (climbCommand != null)
                    climbCommand.cancel();

                climbCommand = climber.new CmdClimb1to2();
                climbCommand.start();
            }
        });
        NarwhalDashboard.addButton("climb_23", (boolean down) -> {
            if (down) {
                if (climbCommand != null)
                    climbCommand.cancel();

                climbCommand = climber.new CmdClimb2to3();
                climber.new CmdClimb2to3().start();
            }
        });
        NarwhalDashboard.addButton("cancel_climb", (boolean down) -> {
            if (down) {
                if (climbCommand != null)
                    climbCommand.cancel();
                climbCommand = null;
            }
        });

        // Debug
        NarwhalDashboard.addButton("rezero_backleg", (boolean down) -> {
            if (down) {
                climbMotor.set(ControlMode.PercentOutput, -0.8);
            } else {
                climbMotor.setSelectedSensorPosition(0);
                climbMotor.set(ControlMode.PercentOutput, 0);
            }
        });

        // Lift Inverts
        liftMotorLeader.setInverted(false);
        liftMotorLeader.setSensorPhase(true);

        liftMotorFollower.setInverted(false);

        // Lift Intake Invert
        liftIntakeMotor.setInverted(false);

        // FourBar Invert
        fourBarMotor.setInverted(true);
        fourBarMotor.setSensorPhase(false);

        // Climber Invert
        climbMotor.setSensorPhase(true);

        //2 is big camera for lars KEEP AT 2
        limelight.driverMode(2);
        limelight.turnOffLED();

<<<<<<< HEAD
		// Scoring Structure Controls
		listenerRight.nameControl(new Button(8), "SelectCargoShip");
		listenerRight.addButtonDownListener("SelectCargoShip", () -> {
			currentScoreTarget = ScoreTarget.CARGO_SHIP;
		});

		// Height Controls
		listenerRight.nameControl(new Button(7), "SelectTopLevel");
		listenerRight.addButtonDownListener("SelectTopLevel", () -> {
			currentScoreTarget = ScoreTarget.ROCKET_TOP;
		});

		listenerRight.nameControl(new Button(9), "SelectMidLevel");
		listenerRight.addButtonDownListener("SelectMidLevel", () -> {
			currentScoreTarget = ScoreTarget.ROCKET_MID;
		});

		listenerRight.nameControl(new Button(11), "SelectLowLevel");
		listenerRight.addButtonDownListener("SelectLowLevel", () -> {
			currentScoreTarget = ScoreTarget.ROCKET_LOW;
		});

		// Compressor
		listenerRight.nameControl(new Button(10), "StartCompressor");
		listenerRight.addButtonDownListener("StartCompressor", () ->
		{
			compressor.start();
			Log.info("MainGromit", "Starting Compressor");
		});

		listenerRight.nameControl(new Button(12), "StopCompressor");
		listenerRight.addButtonDownListener("StopCompressor", () ->
		{
			compressor.stop();
			Log.info("MainGromit", "Stopping Compressor");
		});

		// MANUAL CONTROLS AND OVERRIDES

		listenerLeft.nameControl(ControllerExtreme3D.TRIGGER, "Override");
		listenerLeft.nameControl(new Button(2), "ManualMode");
		listenerLeft.nameControl(ControllerExtreme3D.JOYY, "ManualControl");

		listenerLeft.addMultiListener(() -> {
			if (listenerLeft.getButton("ManualMode")) {
				lift.override = false;
				lift.powerControl(0);

				fourBar.override = listenerLeft.getButton("Override");
				fourBar.powerControl(listenerLeft.getAxis("ManualControl"));
			}
			else {
				fourBar.override = false;
				fourBar.powerControl(0);

				lift.override = listenerLeft.getButton("Override");
				lift.powerControl(listenerLeft.getAxis("ManualControl"));
			}
		}, "ManualMode", "Override", "ManualControl");


		listenerLeft.nameControl(new POV(0), "ManualIntakePOV");
        listenerLeft.addListener("ManualIntakePOV", (POVValue povVal) -> {
            switch (povVal.getDirectionValue()) {
                case 8:
                case 1:
                case 7:
					liftIntake.setState(LiftIntakeState.CARGO_OUTTAKE);
					break;
                case 3:
                case 4:
                case 5:
					liftIntake.setState(LiftIntakeState.CARGO_INTAKE);
					break;
                default:
					liftIntake.setState(LiftIntakeState.CARGO_HOLDING);
					break;
            }
		});

		listenerLeft.nameControl(new Button(7), "Climb1to2");
        listenerLeft.addButtonDownListener("Climb1to2", () -> {
            climber.new CmdClimb1to2().start();
        });

    @Override
    protected void setupListeners() {
        super.setupListeners();

=======
>>>>>>> upstream/master
        // listenerLeft.nameControl(new Button(9), "ClimbPistonExtend");
        // listenerLeft.addButtonDownListener("ClimbPistonExtend", () -> {
        // climbPiston.setPistonOn();
        // });

        // listenerLeft.nameControl(new Button(10), "ClimbPistonRetract");
        // listenerLeft.addButtonDownListener("ClimbPistonRetract", () -> {
        // climbPiston.setPistonOff();
        // });

        // listenerLeft.nameControl(new Button(11), "BackLegDown");
        // listenerLeft.nameControl(new Button(12), "BackLegUp");
        // listenerLeft.addMultiListener(() -> {
        // if (listenerLeft.getButton("BackLegDown") &&
        // !listenerLeft.getButton("BackLegUp")) {
        // climbMotor.set(ControlMode.PercentOutput, +1.0);
        // }
        // else if (listenerLeft.getButton("BackLegUp") &&
        // !listenerLeft.getButton("BackLegDown")) {
        // climbMotor.set(ControlMode.PercentOutput, -1.0);
        // }
        // else {
        // climbMotor.set(ControlMode.PercentOutput, 0.0);
        // }
        // }, "BackLegDown", "BackLegUp");
    }

    @Override
    protected void updateDashboard() {
        super.updateDashboard();

        SmartDashboard.putNumber("Back Leg Position (nu)", climbMotor.getSelectedSensorPosition());
    }

    public static void main(String... args) {
        RobotBase.startRobot(MainGromit::new);
    }
}