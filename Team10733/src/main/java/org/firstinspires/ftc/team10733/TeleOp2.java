/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.team10733;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="teleopwithArm", group="Tele Op")

public class TeleOp2 extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;
    private ArmController arm = null;


    // Code to run ONCE when the driver hits INIT

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        //COUGARS: define your other motors (or sensors) here.
        //You can see how the motors for the front wheels are defined.
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "frontleft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backleft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backright");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        //rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        //rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        arm = new ArmController(hardwareMap);


        // Tell the driver that initialization is complete.
        telemetry.addData("Status is", "Initialized");

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftFrontPower;
        double rightFrontPower;
        double leftBackPower;
        double rightBackPower;
        //double leftPower;
        //double rightPower;


        // Choose to drive using either Tank Mode, or POV Mode
        // Comment out the method that's not used.  The default below is POV.

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.
        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        double strafe = -gamepad1.left_stick_x;
        double armPower = gamepad2.left_stick_y;
        double servoGrab = gamepad2.right_stick_y;
        boolean trayGrab = gamepad2.right_bumper;
        boolean trayRelease = gamepad2.left_bumper;

        arm.freeGrab(servoGrab);

/*
        leftFrontPower   = Range.clip(drive + turn + strafe, -1.0, 1.0) ;
        rightFrontPower   = Range.clip(drive - turn - strafe, -1.0, 1.0) ;
        leftBackPower   = Range.clip(drive + turn - strafe, -1.0, 1.0) ;
        rightBackPower   = Range.clip(drive - turn + strafe, -1.0, 1.0) ;
*/
// see https://ftcforum.firstinspires.org/forum/ftc-technology/blocks-programming/52307-mecanum-drive-w-block-coding-help/page2
        //
        leftFrontPower   = Range.clip(drive + turn - strafe, -1.0, 1.0) ;
        rightFrontPower   = Range.clip(drive - turn + strafe, -1.0, 1.0) ;
        leftBackPower   = Range.clip(drive + turn + strafe, -1.0, 1.0) ;
        rightBackPower   = Range.clip(drive - turn - strafe, -1.0, 1.0) ;

        //leftPower   = Range.clip(drive + turn, -1.0, 1.0) ;
        //rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
        leftFrontPower   = leftFrontPower * .5;
        rightFrontPower   = rightFrontPower * .5;
        leftBackPower   = leftBackPower * .5;
        rightBackPower   = rightBackPower * .5;

        arm.extend(armPower);
        if (gamepad2.dpad_down){
            arm.grab();
        }
        if (gamepad2.dpad_up){
            arm.release();
        }
        if (gamepad2.x){
            arm.servosToHome();
        }

        if (trayGrab){
            arm.trayGrab();
        }

        if (trayRelease){
            arm.trayRelease();
        }

        telemetry.addData("leftServo: ","%5.2f", arm.leftServoPosition );
        telemetry.addData("rightServo: ", "%5.2f", arm.rightServoPosition);


        //read gamepad
        //open and close
        // gamepad2.
        // arm.grab();


        /*
         Tank Mode uses one stick to control each wheel.
         - This requires no math, but it is hard to drive forward slowly and keep straight.
         leftPower  = -gamepad1.left_stick_y ;
         rightPower = -gamepad1.right_stick_y ;
         COUGARS: You can use the joystick and call other methods (functions)
         to calculate how to power your 4 mechanum wheels.  You can even try using
         the HOMAR library that St. Paul Academy demonstrated last year.  You will need to include
         the HOMAR library in this project and then add it to the import statements at the top of
         this class.  Twyla added this to last year's Rover Ruckus code.
         Also Eagan created a web tool for FTC teams to use to create the basic
         autonomous drive code as a starting point.
         Send calculated power to wheels
        */

        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        /*
         Show the elapsed game time and wheel power.
         COUGARS: You can use telemetry to display data to the phone screen.  That's really helpful
         to see the motor power and other useful things.
        */
        //telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "leftFront (%.2f), rightFront (%.2f), leftBack (%.2f), rightBack (%.2f)", leftFrontPower, rightFrontPower, leftBackPower, rightBackPower);
        telemetry.addData("actual arm power: ","%5.2f", arm.getArmPower() );

    }

    // Code to run ONCE after the driver hits STOP

    @Override
    public void stop() {
    }

}
