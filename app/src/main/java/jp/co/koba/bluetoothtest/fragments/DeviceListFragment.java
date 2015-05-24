package jp.co.koba.bluetoothtest.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.koba.bluetoothtest.R;
import jp.co.koba.bluetoothtest.activities.DeviceListActivity;

public class DeviceListFragment extends Fragment {

    @InjectView(R.id.paired_device_list)
    ListView deviceListView;

    private DeviceListActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BluetoothAdapter btAdapter = activity.mBtAdapter;
        ArrayAdapter pairedDeviceAdapter = new ArrayAdapter(activity, R.layout.list_item);
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            deviceListView.setAdapter(pairedDeviceAdapter);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (DeviceListActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
