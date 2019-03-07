package net.fbvictorhugo.joystickbt.virtualjoy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import net.fbvictorhugo.joystickbt.BaseActivity;
import net.fbvictorhugo.joystickbt.R;
import net.fbvictorhugo.joystickbt.controller.BluetoothCmd;

import io.github.controlwear.virtual.joystick.android.JoystickView;

import static net.fbvictorhugo.joystickbt.controller.BluetoothCmd.BTN_B_ACTION_DOWN;

public class JoyActivity extends BaseActivity {

    private TextView textView;
    private JoystickView joystick;
    private Button buttonA;
    private Button buttonB;

    private boolean isLedOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_joy);

        findViews();
        configureJoystick();
        configureButtonA();
        configureButtonB();

        String nameBT = application.appBluetoothDevice.getName();
        textView.setText("Connected: " + nameBT);

    }

    @Override
    protected void onDestroy() {
        application.disconectBluetooth();
        super.onDestroy();
    }

    private void findViews() {
        joystick = findViewById(R.id.joystickView);
        textView = findViewById(R.id.text);
        buttonA = findViewById(R.id.btnLed);
        buttonB = findViewById(R.id.btnBuzz);
    }

    private void configureButtonA() {

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLedOn) {
                    turnOffLed();
                    isLedOn = false;
                } else {
                    turnOnLed();
                    isLedOn = true;
                }
            }
        });
    }

    private void configureButtonB() {

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buzz();
            }
        });
    }


    private void configureJoystick() {
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                Log.d("JoyActivity", "angle: " + angle + " | strength: " + strength);
                String msg = "Angle: " + angle + " | Strength: " + strength + " | coord: " + "x" + joystick.getNormalizedX() + "  y:" + joystick.getNormalizedY();
                textView.setText(msg);

                if (angle == 0) {
                    sendBluetoothCmd(BluetoothCmd.AXIS_CENTERED);
                } else if (angle <= 180) {
                    sendBluetoothCmd(BluetoothCmd.AXIS_UP);
                } else {
                    sendBluetoothCmd(BluetoothCmd.AXIS_DOWN);
                }
            }
        });

        joystick.setAutoReCenterButton(true);
    }

    //region COMMANDS

    private void turnOffLed() {
        sendBluetoothCmd(BluetoothCmd.BTN_A_ACTION_DOWN);
    }

    private void turnOnLed() {
        sendBluetoothCmd(BluetoothCmd.BTN_A_ACTION_UP);
    }

    private void buzz() {
        sendBluetoothCmd(BTN_B_ACTION_DOWN);
    }

    //endregion

}
