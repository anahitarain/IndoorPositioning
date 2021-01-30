package com.example.indoorpositioning;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WifiScanner {

    private WifiManager mWifiManager;
    private int REQUEST_PERMISSION_CODE = 1;
    private String[] mPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE};

    boolean start;
    private List<ScanResult> results; // scanResults containing wifi information

    // used in registerReceiver() in scan_wifi()
    BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            txt.setText("WIFI LIST \n");

            results = mWifiManager.getScanResults();
            unregisterReceiver(this);

            results = mWifiManager.getScanResults();
            Log.d("Wifi", String.valueOf(results.size()));
//            prints out the available wifis around
            for (ScanResult res : results) {
                txt.append("SSID:" + res.SSID + " RSSI:" + res.level + " Frequency: " + res.frequency + "\n");
                txt.append("\n");
            }
        }
    };

    /* Views */
    private Button scan_btn;
    private RecyclerView list;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_wifi();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scan_wifi();
    }

    private void scan_wifi() {
        // get runtime permission
        if(!start) {
            requestPermissions();
        }

        // if permissions are granted
        mWifiManager = (WifiManager)
                getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!mWifiManager.isWifiEnabled()) { // user's wifi is off
            Toast.makeText(this, "WIFI DISABLED", Toast.LENGTH_LONG).show();
            mWifiManager.setWifiEnabled(true); // turn on the wifi
        }

        // registerReceiver method is in one of the parent classes (ContextWrapper)
        registerReceiver(mWifiReceiver, new
                IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        mWifiManager.startScan();
        Toast.makeText(this, "SCANNING", Toast.LENGTH_SHORT).show();
    }

    private void requestPermissions() {
        if ((ContextCompat.checkSelfPermission(this, mPermissions[0]) !=
                PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, mPermissions[1]) !=
                        PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, mPermissions[2]) !=
                        PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, mPermissions[3]) !=
                        PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this, new String[]{mPermissions[0],
                            mPermissions[1],
                            mPermissions[2],
                            mPermissions[3]},
                    REQUEST_PERMISSION_CODE);

        } else {
            start = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                start = true;
            } else {
                Toast.makeText(this, "Permission not granted",
                        Toast.LENGTH_SHORT).show();
                start = false;
            }
        }
    }



    private void setupViews() {
        scan_btn = (Button) findViewById(R.id.btn_main_scan);
//        list = (RecyclerView) findViewById(R.id.rv_main_items);
        txt = (TextView) findViewById(R.id.txt_main_rssiView);
    }
}
