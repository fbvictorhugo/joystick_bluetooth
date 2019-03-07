package net.fbvictorhugo.joystickbt.controller;

public interface ConnectionBluetoothCallback {

    void onConnected(BluetoothModel device);

    void onFailed(String error);
}
