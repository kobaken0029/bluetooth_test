package jp.co.koba.bluetoothtest.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.koba.bluetoothtest.R;
import jp.co.koba.bluetoothtest.activities.DeviceListActivity;
import jp.co.koba.bluetoothtest.activities.MainActivity;
import jp.co.koba.bluetoothtest.utils.UiUtils;

public class MainFragment extends Fragment {
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        if (bt == null) {
            Log.d("BLUETOOTH", "Bluetoothがサポートされてません。");
        } else {
            Log.d("BLUETOOTH", "Bluetoothがサポートいます。");

            if (bt.isEnabled()) {
                UiUtils.showToast(activity.getApplicationContext(), "ONになってます。");
                startActivity(new Intent(activity, DeviceListActivity.class));
                activity.finish();
            } else {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).move();
                }
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
