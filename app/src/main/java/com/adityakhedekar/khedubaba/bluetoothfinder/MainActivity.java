package com.adityakhedekar.khedubaba.bluetoothfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    TextView mStatusTetView;
    Button mSearchButton;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.listView);
        mStatusTetView = findViewById(R.id.statusTextView);
        mSearchButton = findViewById(R.id.searchButton);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothAdapter.startDiscovery();
    }

    public void searchButtonClicked (View view){
        mStatusTetView.setText("Searching...");
        mSearchButton.setEnabled(false);
    }
}
