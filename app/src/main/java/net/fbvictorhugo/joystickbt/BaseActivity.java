package net.fbvictorhugo.joystickbt;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.fbvictorhugo.joystickbt.controller.BluetoothModel;
import net.fbvictorhugo.joystickbt.controller.ConnectionBluetoothCallback;

import java.io.IOException;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected AppApplication application;
    private String error;
    private ConnectionBluetoothCallback mConnectionBTListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (AppApplication) getApplication();
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void connectInDevice(BluetoothModel device, ConnectionBluetoothCallback connectionBluetoothListener) {
        if (device != null && device.getAddress() != null) {
            mConnectionBTListener = connectionBluetoothListener;
            new ConnectBT().execute(device.getAddress());
        } else {
            mConnectionBTListener.onFailed("BT Device is null!");
        }
    }

    private class ConnectBT extends AsyncTask<String, Integer, Boolean>  // UI thread
    {

        @Override
        protected void onPreExecute() {
            //show a progress dialog
        }

        @Override
        protected Boolean doInBackground(String... address) {
            try {

                application.appBluetoothDevice = application.appBluetoothAdapter.getRemoteDevice(address[0]);
                application.appBluetoothSocket = application.appBluetoothDevice.createInsecureRfcommSocketToServiceRecord(AppApplication.MY_UUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                application.appBluetoothSocket.connect();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                error = e.getLocalizedMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
        {
            if (result) {

                if (mConnectionBTListener != null) {
                    mConnectionBTListener.onConnected(new BluetoothModel(application.appBluetoothDevice));
                }
            } else {
                if (mConnectionBTListener != null) {
                    mConnectionBTListener.onFailed(error);
                }
            }
        }

    }

    protected void sendBluetoothCmd(final char cmd) {
        if (application.appBluetoothSocket != null) {
            try {
                String msg = Character.toString(cmd);
                application.appBluetoothSocket.getOutputStream().write(msg.getBytes());
            } catch (IOException e) {
                showToast("CMD Error: " + e.getLocalizedMessage());
            }
        }
    }

}
