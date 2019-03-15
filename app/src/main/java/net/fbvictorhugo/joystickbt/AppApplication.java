package net.fbvictorhugo.joystickbt;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import net.fbvictorhugo.joystickbt.controller.BluetoothModel;

import java.io.IOException;
import java.util.UUID;

public class AppApplication extends Application {

    public final static int REQUEST_ENABLE_BT = 100;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothAdapter appBluetoothAdapter = null;
    public BluetoothSocket appBluetoothSocket = null;
    public BluetoothModel appBluetoothModel;

    @Override
    public void onCreate() {
        super.onCreate();
        appBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void disconectBluetooth() {
        try {
            appBluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            appBluetoothSocket = null;
            appBluetoothModel = null;
        }
    }

}
