package org.team3128.athos.main;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import org.team3128.common.generics.RobotConstants;

import org.team3128.common.NarwhalRobot;
import org.team3128.common.control.trajectory.Trajectory;
import org.team3128.common.control.trajectory.TrajectoryGenerator;
import org.team3128.common.control.trajectory.constraint.TrajectoryConstraint;
import org.team3128.common.drive.DriveCommandRunning;
import org.team3128.common.hardware.limelight.Limelight;
<<<<<<< HEAD
import org.team3128.common.hardware.navigation.Gyro;
import org.team3128.common.hardware.navigation.NavX;
import org.team3128.common.util.Constants;
import org.team3128.common.util.units.Angle;
import org.team3128.common.util.units.Length;
import org.team3128.common.vision.CmdAutoAim;
import org.team3128.gromit.util.DeepSpaceConstants;
import org.team3128.common.util.Log;
import org.team3128.common.util.RobotMath;
import org.team3128.common.util.datatypes.PIDConstants;
=======
import org.team3128.common.hardware.gyroscope.Gyro;
import org.team3128.common.hardware.gyroscope.NavX;
import org.team3128.common.utility.units.Angle;
import org.team3128.common.utility.units.Length;
import org.team3128.common.vision.CmdHorizontalOffsetFeedbackDrive;
import org.team3128.gromit.constants.DeepSpaceConstants; // Eventually move this to the common directory with organization for yearly game constants
import org.team3128.athos.subsystems.Constants;
import org.team3128.common.utility.Log;
import org.team3128.common.utility.RobotMath;
import org.team3128.common.utility.datatypes.PIDConstants;
>>>>>>> upstream/code-overhaul
import org.team3128.common.narwhaldashboard.NarwhalDashboard;
import org.team3128.common.listener.ListenerManager;
import org.team3128.common.listener.controllers.ControllerExtreme3D;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.hardware.motor.LazyCANSparkMax;
import org.team3128.common.utility.math.Pose2D;
import org.team3128.common.utility.math.Rotation2D;
import org.team3128.athos.subsystems.NEODrive;
import org.team3128.athos.subsystems.RobotTracker;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.ArrayList;
import java.util.concurrent.*;

import org.team3128.common.generics.ThreadScheduler;

public class MainAthos extends NarwhalRobot {
    NEODrive drive = NEODrive.getInstance();
    RobotTracker robotTracker = RobotTracker.getInstance();

    ExecutorService executor = Executors.newFixedThreadPool(4);
    ThreadScheduler scheduler = new ThreadScheduler();
    Thread auto;

    public Joystick joystick;
    public ListenerManager lm;
    public Gyro gyro;

    public PIDConstants visionPID, blindPID;

    public NetworkTable table;
    public NetworkTable limelightTable;

<<<<<<< HEAD
    public DriveCalibrationUtility dcu;
    public Wheelbase calculatedWheelbase;

    public CmdAutoAim alignCommand;
    private DriveCommandRunning driveCmdRunning;

    public Limelight limelight = new Limelight("limelight", 26 * Angle.DEGREES, 6.15 * Length.in, 0 * Length.in, 14.5 * Length.in);
	@Override
	protected void constructHardware()
	{
		limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

        rightDriveFront = new TalonSRX(5);
        rightDriveMiddle = new TalonSRX(3);
        rightDriveBack = new TalonSRX(2);

        leftDriveFront = new TalonSRX(0);
        leftDriveMiddle = new TalonSRX(4);
        leftDriveBack = new TalonSRX(1);

        rightDriveFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);
        rightDriveMiddle.set(ControlMode.Follower, rightDriveFront.getDeviceID());
        rightDriveBack.set(ControlMode.Follower, rightDriveFront.getDeviceID());

        leftDriveFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.CAN_TIMEOUT);
        leftDriveMiddle.set(ControlMode.Follower, leftDriveFront.getDeviceID());
        leftDriveBack.set(ControlMode.Follower, leftDriveFront.getDeviceID());
=======
    public double kP = Constants.K_AUTO_LEFT_P;
    public double kD = Constants.K_AUTO_LEFT_D;
    public double kF = Constants.K_AUTO_LEFT_F;
>>>>>>> upstream/code-overhaul

    public double startTime = 0;

    public String trackerCSV = "Time, X, Y, Theta, Xdes, Ydes";

    public ArrayList<Pose2D> waypoints = new ArrayList<Pose2D>();
    public Trajectory trajectory;

    @Override
    protected void constructHardware() {

        scheduler.schedule(drive, executor);
        scheduler.schedule(robotTracker, executor);

        // Instatiator if we're using the NavX
        gyro = new NavX();

        // Instatiator if we're using the KoP Gyro
        // gyro = new AnalogDevicesGyro();
        // ((AnalogDevicesGyro) gyro).recalibrate();

        joystick = new Joystick(1);
        lm = new ListenerManager(joystick);
        addListenerManager(lm);

        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("P Gain", kP);
        SmartDashboard.putNumber("D Gain", kD);
        SmartDashboard.putNumber("F Gain", kF);


        //straight
        //waypoints.add(new Pose2D(0, 0, Rotation2D.fromDegrees(180)));
        // waypoints.add(new Pose2D(60 * Constants.inchesToMeters, 0 * Constants.inchesToMeters, Rotation2D.fromDegrees(0)));

        //quarterturn
        // waypoints.add(new Pose2D(0, 0, Rotation2D.fromDegrees(0)));
        // waypoints.add(new Pose2D(60 * Constants.inchesToMeters, 60 * Constants.inchesToMeters, Rotation2D.fromDegrees(90)));

        // waypoints.add(new Pose2D(0, 0, Rotation2D.fromDegrees(0)));
        // waypoints.add(new Pose2D(60 * Constants.inchesToMeters, 30 * Constants.inchesToMeters, Rotation2D.fromDegrees(45)));

        // waypoints.add(new Pose2D(0, 0, Rotation2D.fromDegrees(0)));
        // waypoints.add(new Pose2D(30 * Constants.inchesToMeters, 0 * Constants.inchesToMeters, Rotation2D.fromDegrees(0)));
        waypoints.add(new Pose2D(0, 0, Rotation2D.fromDegrees(0)));
        // waypoints.add(new Pose2D(70 * Constants.inchesToMeters, 40 * Constants.inchesToMeters, Rotation2D.fromDegrees(20)));
        // waypoints.add(new Pose2D(140 * Constants.inchesToMeters, 85 * Constants.inchesToMeters, Rotation2D.fromDegrees(45)));
        waypoints.add(new Pose2D(0*Constants.inchesToMeters, 70*Constants.inchesToMeters, Rotation2D.fromDegrees(-45)));

        trajectory = TrajectoryGenerator.generateTrajectory(waypoints, new ArrayList<TrajectoryConstraint>(), 0, 0,
                120 * Constants.inchesToMeters, 0.5, false);
    }

    @Override
    protected void constructAutoPrograms() {
    }

    @Override
    protected void setupListeners() {
        lm.nameControl(ControllerExtreme3D.TWIST, "MoveTurn");
        lm.nameControl(ControllerExtreme3D.JOYY, "MoveForwards");
        lm.nameControl(ControllerExtreme3D.THROTTLE, "Throttle");
        lm.nameControl(new Button(5), "ResetGyro");
        lm.nameControl(new Button(6), "PrintCSV");
        lm.nameControl(new Button(3), "ClearTracker");
        lm.nameControl(new Button(4), "ClearCSV");

        lm.addMultiListener(() -> {
            drive.arcadeDrive(-0.7 * RobotMath.thresh(lm.getAxis("MoveTurn"), 0.1),
                    -1.0 * RobotMath.thresh(lm.getAxis("MoveForwards"), 0.1), -1.0 * lm.getAxis("Throttle"), true);

        }, "MoveTurn", "MoveForwards", "Throttle");

        lm.nameControl(ControllerExtreme3D.TRIGGER, "AlignToTarget");
        lm.addButtonDownListener("AlignToTarget", () -> {
            // TODO: Add current implementation of vision alignment
            Log.info("MainAthos.java", "[Vision Alignment] Not created yet, would've started");
        });
        lm.addButtonUpListener("AlignToTarget", () -> {
            Log.info("MainAthos.java", "[Vision Alignment] Not created yet, would've ended");
        });

<<<<<<< HEAD
		lm.nameControl(ControllerExtreme3D.TRIGGER, "AlignToTarget");
		lm.addButtonDownListener("AlignToTarget", () -> { 
            alignCommand = new CmdAutoAim(gyro, limelight, visionPID, driveCmdRunning,
            -1 * Angle.DEGREES, 14.5 * Length.in, DeepSpaceConstants.DECELERATE_START_DISTANCE, DeepSpaceConstants.DECELERATE_END_DISTANCE,
            blindPID, false);
            alignCommand.start();
=======
        lm.addButtonDownListener("ResetGyro", () -> {
            drive.resetGyro();
>>>>>>> upstream/code-overhaul
        });
        lm.addButtonDownListener("PrintCSV", () -> {
            Log.info("MainAthos", trackerCSV);
        });
        lm.addButtonDownListener("ClearCSV", () -> {
            trackerCSV = "Time, X, Y, Theta, Xdes, Ydes";
            Log.info("MainAthos", "CSV CLEARED");
            startTime = Timer.getFPGATimestamp();
        });

        lm.addButtonDownListener("ClearTracker", () -> {
            robotTracker.resetOdometry();
        });

    }

    @Override
    protected void teleopPeriodic() {
    }

    double maxLeftSpeed = 0;
    double maxRightSpeed = 0;
    double maxSpeed = 0;
    double minLeftSpeed = 0;
    double minRightSpeed = 0;
    double minSpeed = 0;

    double currentLeftSpeed;
    double currentLeftDistance;
    double currentRightSpeed;
    double currentRightDistance;
    double currentSpeed;
    double currentDistance;

    @Override
    protected void updateDashboard() {
        currentLeftSpeed = drive.getLeftSpeed();
        currentLeftDistance = drive.getLeftDistance();
        currentRightSpeed = drive.getRightSpeed();
        currentRightDistance = drive.getRightDistance();

        currentSpeed = drive.getSpeed();
        currentDistance = drive.getDistance();

        NarwhalDashboard.put("time", DriverStation.getInstance().getMatchTime());
        NarwhalDashboard.put("voltage", RobotController.getBatteryVoltage());

        SmartDashboard.putNumber("Gyro Angle", drive.getAngle());
        SmartDashboard.putNumber("Left Distance", currentLeftDistance);
        SmartDashboard.putNumber("Right Distance", currentRightDistance);

        SmartDashboard.putNumber("Distance", currentDistance);

        SmartDashboard.putNumber("Left Velocity", currentLeftSpeed);
        SmartDashboard.putNumber("Right Velocity", currentRightSpeed);

        SmartDashboard.putNumber("Velocity", drive.getSpeed());

        SmartDashboard.putNumber("RobotTracker - x:", robotTracker.getOdometry().getTranslation().getX());
        SmartDashboard.putNumber("RobotTracker - y:", robotTracker.getOdometry().getTranslation().getY());
        SmartDashboard.putNumber("RobotTracker - theta:", robotTracker.getOdometry().getRotation().getDegrees());

        maxLeftSpeed = Math.max(maxLeftSpeed, currentLeftSpeed);
        maxRightSpeed = Math.max(maxRightSpeed, currentRightSpeed);
        maxSpeed = Math.max(maxSpeed, currentSpeed);
        minLeftSpeed = Math.min(minLeftSpeed, currentLeftSpeed);
        minRightSpeed = Math.min(minRightSpeed, currentLeftSpeed);
        minSpeed = Math.min(minSpeed, currentSpeed);

        SmartDashboard.putNumber("Max Left Speed", maxLeftSpeed);
        SmartDashboard.putNumber("Min Left Speed", minLeftSpeed);
        SmartDashboard.putNumber("Max Right Speed", maxRightSpeed);
        SmartDashboard.putNumber("Min Right Speed", minRightSpeed);

        SmartDashboard.putNumber("Max Speed", maxSpeed);
        SmartDashboard.putNumber("Min Speed", minSpeed);

        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("P Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);
        double f = SmartDashboard.getNumber("F Gain", 0);

        boolean hasChanged = false;
        if ((p != kP)) {
            kP = p;
            hasChanged = true;
        }
        if ((d != kD)) {
            kD = d;
            hasChanged = true;
        }
        if ((f != kF)) {
            kF = f;
            hasChanged = true;
        }
        if (hasChanged) {
            drive.setDualVelocityPID(kP, kD, kF);
        }

        trackerCSV += "\n" + String.valueOf(Timer.getFPGATimestamp() - startTime) + ","
                + String.valueOf(robotTracker.getOdometry().translationMat.getX()) + ","
                + String.valueOf(robotTracker.getOdometry().translationMat.getY()) + ","
                + String.valueOf(robotTracker.getOdometry().rotationMat.getDegrees()) + ","
                + String.valueOf(robotTracker.trajOdometry.translationMat.getX()) + "," 
                + String.valueOf(robotTracker.trajOdometry.translationMat.getY());
    }

    @Override
    protected void teleopInit() {
        scheduler.resume();
    }

    @Override
    protected void autonomousInit() {
        trackerCSV = "Time, X, Y, Theta, Xdes, Ydes";
        Log.info("MainAthos", "going into autonomousinit");
        scheduler.resume();
        robotTracker.resetOdometry();
        drive.setAutoTrajectory(trajectory, false);
        drive.startTrajectory();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    protected void disabledInit() {
        scheduler.pause();
    }

    public static void main(String... args) {
        RobotBase.startRobot(MainAthos::new);
    }
}