package com.example.indoorpositioning;

import android.app.Activity;

import android.content.Context;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class Actoovity extends Activity {
    TextView rssiView;
    Button refresh;

    WifiManager wifi;
    String[] wifis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actoovity_layout);
        setupViews();


        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifi.startScan();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<ScanResult> wifiScanList = wifi.getScanResults();
        rssiView.setText(wifiScanList.toString());
//        System.out.println( " 111111::::: " + wifiScanList);

        String[] ssid = new String[wifiScanList.size()];
        int[] levels = new int[wifiScanList.size()];

        wifis = new String[wifiScanList.size()];
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wifi.startScan();
//            }
//        });
        StringBuilder printable = new StringBuilder();
        for (int i = 0; i < wifiScanList.size(); i++) {
            levels[i] = wifiScanList.get(i).level;
            ssid[i] = wifiScanList.get(i).BSSID;
            printable.append("level ").append(i).append(" is: ").append(levels[i]);

            wifis[i] = (wifiScanList.get(i)).toString();
//            wifis[i] += "DISTANCE : ";
//            wifis[i] += calculateDistance(wifiScanList.get(i).level, wifiScanList.get(i).frequency);

        }
        rssiView.setText(printable.toString());
        System.out.println(printable);
        wifi.startScan();
    }




    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    private void setupViews() {
        rssiView = findViewById(R.id.txt_main_rssiView);
    }

}
