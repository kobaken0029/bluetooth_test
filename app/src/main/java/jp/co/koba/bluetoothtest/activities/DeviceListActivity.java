package jp.co.koba.bluetoothtest.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import jp.co.koba.bluetoothtest.R;

public class DeviceListActivity extends ActionBarActivity {
    public BluetoothAdapter mBtAdapter;
    private ArrayAdapter nonPairedDeviceAdapter;
    Map<String, BluetoothDevice> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        nonPairedDeviceAdapter = new ArrayAdapter(this, R.layout.list_item);
        deviceList = new HashMap<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);


        if (item.getItemId() == R.id.action_settings) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(broadcastReceiver, filter);

            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }

            mBtAdapter.startDiscovery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String deviceName = null;
            BluetoothDevice foundDevice;
            ListView nonPairedListView = (ListView) findViewById(R.id.non_paired_device_list);

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Log.d("SCAN", "スキャン開始");
            }

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ((deviceName = foundDevice.getName()) != null) {

                    if (!deviceList.containsKey(deviceName)) {
                        if (foundDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                            nonPairedDeviceAdapter.add(deviceName + "\n" + foundDevice.getAddress());
                            Log.d("SCAN", deviceName);
                        }
                    }
                    deviceList.put(deviceName, foundDevice);

                }
                nonPairedListView.setAdapter(nonPairedDeviceAdapter);
            }

            if (action.equals(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)) {
                foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (foundDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                    nonPairedDeviceAdapter.add(deviceName + "\n" + foundDevice.getAddress());
                }
                nonPairedListView.setAdapter(nonPairedDeviceAdapter);
            }

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Log.d("SCAN", "スキャン終了");
            }
        }
    };
}
