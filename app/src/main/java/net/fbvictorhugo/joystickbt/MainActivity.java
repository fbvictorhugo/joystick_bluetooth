package net.fbvictorhugo.joystickbt;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import net.fbvictorhugo.joystickbt.controller.BluetoothModel;
import net.fbvictorhugo.joystickbt.controller.BluetoothRecyclerViewAdapter;
import net.fbvictorhugo.joystickbt.controller.ConnectionBluetoothCallback;
import net.fbvictorhugo.joystickbt.virtualjoy.JoyActivity;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends BaseActivity {

    private BluetoothRecyclerViewAdapter recyclerAdapter;
    private ArrayList<BluetoothModel> mPairedDevices;
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setSupportActionBar(mToolbar);

        mPairedDevices = new ArrayList<>();

        configureRecyclerView();

        initializeBluetooth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppApplication.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            pairedDevicesList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                pairedDevicesList();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void findViews() {
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_bluetooth_list);
    }

    private void configureRecyclerView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerAdapter = new BluetoothRecyclerViewAdapter(this, mPairedDevices);
        recyclerAdapter.setOnClickListener(new BluetoothRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(BluetoothModel device, int position) {
                deviceSelected(device);
            }
        });
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    private void initializeBluetooth() {

        if (application.appBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
        } else if (!application.appBluetoothAdapter.isEnabled()) {
            Intent intentEnableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentEnableBT, AppApplication.REQUEST_ENABLE_BT);
        } else {
            pairedDevicesList();
        }
    }

    private void pairedDevicesList() {
        Set<BluetoothDevice> pairedDevices = application.appBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            mPairedDevices.clear();

            for (BluetoothDevice bt : pairedDevices) {
                BluetoothModel btModel = new BluetoothModel(bt);
                btModel.setBonded(true);
                mPairedDevices.add(btModel);
            }
        } else {
            showToast("No Paired Bluetooth Devices Found.");
        }
        recyclerAdapter.Update(mPairedDevices);
    }

    private void deviceSelected(final BluetoothModel device) {

        mProgressDialog = ProgressDialog.show(this, getString(R.string.wait),
                Utils.higlightText(this, String.format(getString(R.string.try_connect_on), device.getName()), device.getName(), R.color.bluetooth_color), true);
        mProgressDialog.setCancelable(false);

        super.connectInDevice(device, new ConnectionBluetoothCallback() {

            @Override
            public void onConnected(BluetoothModel device) {
                mProgressDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, JoyActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(String error) {
                mProgressDialog.dismiss();
                showToast("BT Connection Failed \"" + device.getAddress() + "\"\n:" + error);
            }
        });

    }


}
