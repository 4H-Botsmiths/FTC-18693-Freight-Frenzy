package org.firstinspires.ftc.teamcode.Hardware;


import android.graphics.Color;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.JavaUtil;


/**
 * This is NOT an opmode.
 * <p>
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 * <p>
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 * <p>
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class RobotHardware {
    /* Public OpMode members. */
    public DcMotorEx leftRear = null;
    public DcMotorEx rightRear = null;
    public DcMotorEx leftFront = null;
    public DcMotorEx rightFront = null;
    public DcMotorEx arm = null;
    public Servo claw = null;
    public DcMotorEx spinner = null;
    /*
        public LED leftLeftFrontRed = null;
        public LED leftLeftFrontGreen = null;
        public LED leftRightFrontRed = null;
        public LED leftRightFrontGreen = null;
        public LED rightLeftFrontRed = null;
        public LED rightLeftFrontGreen = null;
        public LED rightRightFrontRed = null;
        public LED rightRightFrontGreen = null;
        public LED leftLeftRearRed = null;
        public LED leftLeftRearGreen = null;
        public LED leftRightRearRed = null;
        public LED leftRightRearGreen = null;
        public LED rightLeftRearRed = null;
        public LED rightLeftRearreen = null;
        public LED rightRightRearRed = null;
        public LED rightRightRearGreen = null;
    */
    //public LED[] lights = new LED[16];
    public ColorSensor color = null;
    public Rev2mDistanceSensor distanceLeft = null;
    public Rev2mDistanceSensor distanceRight = null;


    public VoltageSensor voltageSensor = null;

    public Boolean initialized = null;
    public final double driveMotorTPR = 288;
    public final double driveRatio = 90.0 / 30.0;
    public final double driveWheelTPR = driveRatio * driveMotorTPR;
    public final double driveWheelRPS = 6;
    public final double maxDriveVelocity = driveWheelTPR * driveWheelRPS;
    public double driveVelocity = maxDriveVelocity;

    public double lowBattery = 10.5;
    public double reallyLowBattery = 9.5;
    public double circumferenceMM = 280;
    public final double driveTickPerMillimeter = driveWheelTPR / circumferenceMM;
    public double circumferenceIN = 11;
    public final double driveTickPerInch = driveWheelTPR / circumferenceIN;

    public final double armTPR = 288;
    public final double armRPS = 2;
    public final double maxArmVelocity = armTPR * armRPS;
    public double armVelocity = maxArmVelocity;
    public final double armMax = 170;
    public final double armMin = 30;

    public final double spinnerTPR = 288;
    public final double spinnerRPS = 1;
    public final double maxSpinnerVelocity = spinnerTPR * spinnerRPS;
    public final double spinnerVelocity = maxSpinnerVelocity;


    /* Note: This sample uses the all-objects Tensor Flow model (FreightFrenzy_BCDM.tflite), which contains
     * the following 4 detectable objects
     *  0: Ball,
     *  1: Cube,
     *  2: Duck,
     *  3: Marker (duck location tape marker)
     *
     *  Two additional model assets are available which only contain a subset of the objects:
     *  FreightFrenzy_BC.tflite  0: Ball,  1: Cube
     *  FreightFrenzy_DM.tflite  0: Duck,  1: Marker
     */
    /* local OpMode members. */
    HardwareMap hwMap = null;


    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, int wheelType) {
        initialized = false;
        // Save reference to Hardware map

        hwMap = ahwMap;
        claw = hwMap.get(Servo.class, "Servo_0");
        claw.scaleRange(0, 0.65);
        claw.setDirection(Servo.Direction.REVERSE);
        claw.setPosition(1);
        arm = hwMap.get(DcMotorEx.class, "Motor_4");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        double Arm_F = 32767 / maxDriveVelocity, Arm_P = 0.1 * Arm_F, Arm_I = 0.1 * Arm_P;
        arm.setVelocityPIDFCoefficients(Arm_P, Arm_I, 0, Arm_F);
        //arm.setPositionPIDFCoefficients(32767/maxArmVelocity*0.3);
        spinner = hwMap.get(DcMotorEx.class, "Motor_5");
        //spinner.setDirection(DcMotorSimple.Direction.REVERSE);

        // Define and Initialize Motors
        leftRear = hwMap.get(DcMotorEx.class, "Motor_0");
        leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
        leftRear.setPower(0);
        leftRear.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear = hwMap.get(DcMotorEx.class, "Motor_3");
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightRear.setPower(0);
        rightRear.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        double Back_Motors_F = 32767 / maxDriveVelocity, Back_Motors_P = 0.1 * Back_Motors_F, Back_Motors_I = 0.1 * Back_Motors_P;
        leftRear.setVelocityPIDFCoefficients(Back_Motors_P, Back_Motors_I, 0, Back_Motors_F);
        rightRear.setVelocityPIDFCoefficients(Back_Motors_P, Back_Motors_I, 0, Back_Motors_F);

        if (wheelType == 2) {
            leftFront = hwMap.get(DcMotorEx.class, "Motor_1");
            leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
            leftFront.setPower(0);
            leftFront.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            leftFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightFront = hwMap.get(DcMotorEx.class, "Motor_2");
            rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
            rightFront.setPower(0);
            rightFront.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            rightFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            double Front_Motors_F = 32767 / maxDriveVelocity, Front_Motors_P = 0.1 * Front_Motors_F, Front_Motors_I = 0.01 * Front_Motors_P;
            leftFront.setVelocityPIDFCoefficients(Front_Motors_P, Front_Motors_I, 0, Front_Motors_F);
            rightFront.setVelocityPIDFCoefficients(Front_Motors_P, Front_Motors_I, 0, Front_Motors_F);
        }


        // touchBottom = hwMap.get(TouchSensor.class, "Touch_0");
        // touchTop = hwMap.get(TouchSensor.class, "Touch_1");
        // Define and initialize ALL installed lights.
       /* for (int i = 0; i < greenLights.length; i += 2) {
            greenLights[i] = hwMap.get(LED.class, "Light_" + i);
        }
        for (int i = 1; i < redLights.length; i += 2) {
            redLights[i] = hwMap.get(LED.class, "Light_" + i);
        }
        */
        /*for (int i = 0; i < lights.length; i++) {
            lights[i] = hwMap.get(LED.class, "Light_" + i);
        }
         */

        // Define and initialize ALL installed distance/light sensors.
        color = hwMap.get(ColorSensor.class, "Color_0");
        distanceLeft = hwMap.get(Rev2mDistanceSensor.class, "Distance_1");
        distanceRight = hwMap.get(Rev2mDistanceSensor.class, "Distance_2");

        // Define and initialize ALL internal sensors.
        voltageSensor = hwMap.get(VoltageSensor.class, "Control Hub");
        initialized = true;
    }
    /*
    public void setLights(boolean enable) {
        for (LED light : lights) {
            light.enable(enable);
        }
    }
    public void setLights(boolean left, boolean right){
        for (int i = 0; i < lights.length; i++){
            if (i < lights.length/2){
                lights[i].enable(left);
            } else{
                lights[i].enable(right);
            }
        }
    }

    public void setGreenLights(boolean enable) {
        for (int i = 1; i < lights.length; i += 2) {
            lights[i].enable(enable);
        }
    }
    public void setGreenLights(boolean left, boolean right){
        for (int i = 1; i < lights.length; i += 2){
            if (i < lights.length/2){
                lights[i].enable(left);
            } else{
                lights[i].enable(right);
            }
        }
    }

    public void setRedLights(boolean enable) {
        for (int i = 0; i < lights.length; i += 2) {
            lights[i].enable(enable);
        }
    }
    public void setRedLights(boolean left, boolean right){
        for (int i = 0; i < lights.length; i += 2){
            if (i < lights.length/2){
                lights[i].enable(left);
            } else{
                lights[i].enable(right);
            }
        }
    }
*/
    public String detectColor() {
        int colorHSV;
        float hue;
        //float sat;
        //float val;
        // Convert RGB values to HSV color model.
        // See https://en.wikipedia.org/wiki/HSL_and_HSV for details on HSV color model.
        colorHSV = Color.argb(color.alpha(), color.red(), color.green(), color.blue());
        // Get hue.
        hue = JavaUtil.colorToHue(colorHSV);
        //telemetry.addData("Hue", hue);
        // Get saturation.
        //sat = JavaUtil.colorToSaturation(colorHSV);
        //telemetry.addData("Saturation", sat);
        // Get value.
        //val = JavaUtil.colorToValue(colorHSV);
        //telemetry.addData("Value", val);
        // Use hue to determine if it's red, green, blue, etc..
        if (hue < 30) {
            return "Red";
        } else if (hue < 60) {
            return "Orange";
        } else if (hue < 90) {
            return "Yellow";
        } else if (hue < 150) {
            return "Green";
        } else if (hue < 225) {
            return "Blue";
        } else if (hue < 350) {
            return "Purple";
        } else {
            return "Not Detected";
        }
    }
}
