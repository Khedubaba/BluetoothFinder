package com.adityakhedekar.khedubaba.bluetoothfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ListView mListView;
    TextView mStatusTetView;
    Button mSearchButton;
    ArrayList<String> mBluetoothDevices = new ArrayList<>();
    ArrayAdapter mArrayAdapter;
    ArrayList<String> mAddresses = new ArrayList<>();
    BluetoothAdapter mBluetoothAdapter;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mStatusTetView.setText("");
                    mSearchButton.setEnabled(true);
                }
            }
            else{
                mStatusTetView.setText("");
                mSearchButton.setEnabled(true);
            }
        }
        else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mStatusTetView.setText("");
                mSearchButton.setEnabled(true);
            }
            else{
                mStatusTetView.setText("Please allow location access to locate near by bluetooth devices");
                mSearchButton.setEnabled(false);
            }
        }
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "Action: " + action);
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                mStatusTetView.setText("Finished");
                mSearchButton.setEnabled(true);
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MAX_VALUE));
                Log.i(TAG, "Device Found: " + "Name: " + name + " Address: " + address + " RSSI: " + rssi);
                if (!mAddresses.contains(address)){
                    mAddresses.add(address);
                    String deviceString = "";
                    if (name == null || name.equals("")){

                        deviceString = address + " - RSSI " + rssi + "dBm";
                    }
                    else{
                        deviceString = name + " - RSSI " + rssi + "dBm";
                    }
                    mBluetoothDevices.add(deviceString);
                    mArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.listView);
        mStatusTetView = findViewById(R.id.statusTextView);
        mSearchButton = findViewById(R.id.searchButton);

        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mBluetoothDevices);
        mListView.setAdapter(mArrayAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBroadcastReceiver, intentFilter);


        if (Build.VERSION.SDK_INT <=23) {

        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                mStatusTetView.setText("Please allow location access to locate near by bluetooth devices");
                mSearchButton.setEnabled(false);
            }
            else{
                mStatusTetView.setText("");
                mSearchButton.setEnabled(true);
            }
        }


    }

    public void searchButtonClicked (View view){
        mStatusTetView.setText("Searching...");
        mSearchButton.setEnabled(false);
        mBluetoothDevices.clear();
        mAddresses.clear();
        mBluetoothAdapter.startDiscovery();
    }

}
