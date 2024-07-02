package net.fbvictorhugo.joystickbt.virtualjoy;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import net.fbvictorhugo.joystickbt.BaseActivity;
import net.fbvictorhugo.joystickbt.R;
import net.fbvictorhugo.joystickbt.controller.BluetoothCmd;

//import io.github.controlwear.virtual.joystick.android.JoystickView;

import static net.fbvictorhugo.joystickbt.controller.BluetoothCmd.BTN_BUZZ;

public class JoyActivity extends BaseActivity {

 //   private JoystickView mJoystickView;
    private SwitchCompat mSwitchLed;
    private ImageButton mButtonBuzz;
    private Toolbar mToolbar;
    private TextView mTextJoystickStatus;

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
            String nameBT = application.appBluetoothModel.getName();
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

    //    mJoystickView = findViewById(R.id.joystickView);
        mSwitchLed = findViewById(R.id.switchLed);
        mButtonBuzz = findViewById(R.id.btnBuzz);
        mTextJoystickStatus = findViewById(R.id.joystick_tv_status);
    }

    private void configureButtonLed() {


        mSwitchLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    turnOnLed();
                } else {
                    turnOffLed();
                }
            }
        });
    }

    private void configureButtonBuzz() {

        mButtonBuzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buzz();
            }
        });
    }

    private void configureJoystick() {
    //    mJoystickView.setOnMoveListener(mJoystickMoveLister);
    //    mJoystickView.setAutoReCenterButton(true);
    }

  /*  JoystickView.OnMoveListener mJoystickMoveLister = new JoystickView.OnMoveListener() {
        @Override
        public void onMove(int angle, int strength) {

            String axis = "";
            if (angle == 0) {
                sendMoveBTCmd(BluetoothCmd.AXIS_CENTERED);
                axis = "AXIS_CENTERED";
            } else if (angle > 0 && angle <= 70) {
                sendMoveBTCmd(BluetoothCmd.AXIS_RIGHT_UP);
                axis = "AXIS_RIGHT_UP";
            } else if (angle > 70 && angle <= 105) {
                sendMoveBTCmd(BluetoothCmd.AXIS_UP);
                axis = "AXIS_UP";
            } else if (angle > 105 && angle <= 180) {
                sendMoveBTCmd(BluetoothCmd.AXIS_LEFT_UP);
                axis = "AXIS_LEFT_UP";
            } else if (angle > 180 && angle <= 255) {
                sendMoveBTCmd(BluetoothCmd.AXIS_LEFT_DOWN);
                axis = "AXIS_LEFT_DOWN";
            } else if (angle > 255 && angle <= 285) {
                sendMoveBTCmd(BluetoothCmd.AXIS_DOWN);
                axis = "AXIS_DOWN";
            } else if (angle > 285 && angle <= 360) {
                sendMoveBTCmd(BluetoothCmd.AXIS_RIGHT_DOWN);
                axis = "AXIS_RIGHT_DOWN";
            }
            mTextJoystickStatus.setText(String.format(getString(R.string.angle), angle, axis));
        }
    };*/

    //region COMMANDS

    private void sendMoveBTCmd(final char cmd) {
        if (mLastMoveCmd != cmd) {
            sendBluetoothCmd(cmd);
        }
        mLastMoveCmd = cmd;
    }

    private void turnOnLed() {
        sendBluetoothCmd(BluetoothCmd.BTN_LED_ON);
    }

    private void turnOffLed() {
        sendBluetoothCmd(BluetoothCmd.BTN_LED_OFF);
    }

    private void buzz() {
        sendBluetoothCmd(BTN_BUZZ);
    }

    //endregion

}
