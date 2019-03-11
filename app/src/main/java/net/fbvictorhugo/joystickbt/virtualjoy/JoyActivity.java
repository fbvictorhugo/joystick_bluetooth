package net.fbvictorhugo.joystickbt.virtualjoy;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import net.fbvictorhugo.joystickbt.BaseActivity;
import net.fbvictorhugo.joystickbt.R;
import net.fbvictorhugo.joystickbt.controller.BluetoothCmd;

import io.github.controlwear.virtual.joystick.android.JoystickView;

import static net.fbvictorhugo.joystickbt.controller.BluetoothCmd.BTN_B_ACTION_DOWN;

public class JoyActivity extends BaseActivity {

    private JoystickView joystick;
    private Button buttonLed;
    private Button buttonBuzz;
    private Toolbar mToolbar;

    private boolean isLedOn;

    private int mLastMoveCmd = -42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_joy);

        findViews();
        setSupportActionBar(mToolbar);

        configureJoystick();
        configureButtonLed();
        configureButtonBuzz();

        if (getSupportActionBar() != null) {
            String nameBT = application.appBluetoothDevice.getName();
            getSupportActionBar().setSubtitle(String.format(getString(R.string.connected_on), nameBT));
        }
    }

    @Override
    protected void onDestroy() {
        application.disconectBluetooth();
        super.onDestroy();
    }

    private void findViews() {
        mToolbar = findViewById(R.id.toolbar);

        joystick = findViewById(R.id.joystickView);
        buttonLed = findViewById(R.id.btnLed);
        buttonBuzz = findViewById(R.id.btnBuzz);
    }

    private void configureButtonLed() {

        buttonLed.setOnClickListener(new View.OnClickListener() {
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

    private void configureButtonBuzz() {

        buttonBuzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buzz();
            }
        });
    }

    private void configureJoystick() {
        joystick.setOnMoveListener(mJoystickMoveLister);
        joystick.setAutoReCenterButton(true);
    }

    JoystickView.OnMoveListener mJoystickMoveLister = new JoystickView.OnMoveListener() {
        @Override
        public void onMove(int angle, int strength) {

            if (angle == 0) {
                sendMoveBTCmd(BluetoothCmd.AXIS_CENTERED);
            } else if (angle > 0 && angle <= 70) {
                sendMoveBTCmd(BluetoothCmd.AXIS_RIGHT_UP);
            } else if (angle > 70 && angle <= 105) {
                sendMoveBTCmd(BluetoothCmd.AXIS_UP);
            } else if (angle > 105 && angle <= 180) {
                sendMoveBTCmd(BluetoothCmd.AXIS_LEFT_UP);
            } else if (angle > 180 && angle <= 255) {
                sendMoveBTCmd(BluetoothCmd.AXIS_LEFT_DOWN);
            } else if (angle > 255 && angle <= 285) {
                sendMoveBTCmd(BluetoothCmd.AXIS_DOWN);
            } else if (angle > 285 && angle <= 360) {
                sendMoveBTCmd(BluetoothCmd.AXIS_RIGHT_DOWN);
            }
        }
    };

    //region COMMANDS

    private void sendMoveBTCmd(final char cmd) {
        if (mLastMoveCmd != cmd) {
            sendBluetoothCmd(cmd);
        }
        mLastMoveCmd = cmd;
    }

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
