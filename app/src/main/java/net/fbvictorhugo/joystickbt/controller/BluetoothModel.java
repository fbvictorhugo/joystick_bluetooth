package net.fbvictorhugo.joystickbt.controller;

import android.bluetooth.BluetoothDevice;

public class BluetoothModel {

    private String name;
    private String address;
    private boolean isBonded;
    private BluetoothDevice mDevice;

    public BluetoothModel(BluetoothDevice device) {
        mDevice = device;
        this.name = device.getName();
        this.address = device.getAddress();
    }

    //region GETs and SETs
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public boolean isBonded() {
        return isBonded;
    }

    public void setBonded(boolean bonded) {
        isBonded = bonded;
    }

    public BluetoothDevice getBluetoothDevice() {
        return mDevice;
    }

    //endregion

}
