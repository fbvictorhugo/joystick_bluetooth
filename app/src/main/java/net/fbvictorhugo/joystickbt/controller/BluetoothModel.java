package net.fbvictorhugo.joystickbt.controller;

import android.bluetooth.BluetoothDevice;

public class BluetoothModel {

    private String name;
    private String address;
    private boolean isBonded;

    //
    public BluetoothModel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public BluetoothModel(BluetoothDevice device) {
        this.name = device.getName();
        this.address = device.getAddress();
    }

    //region GETs and SETs
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isBonded() {
        return isBonded;
    }

    public void setBonded(boolean bonded) {
        isBonded = bonded;
    }

    //endregion

}
