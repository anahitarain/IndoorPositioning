package com.example.indoorpositioning;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.Collections;

public class RssiHandler {
    public static void handle(ArrayList<ScanResult> scanResults) {
        ArrayList<Integer> lowestLevels = setLowestLevels(scanResults);
    }
    public static ArrayList<Integer> setLowestLevels(ArrayList<ScanResult> scanResults) {
        ArrayList<Integer> lowestLevels = new ArrayList<>();
        for (int i = 0; i < scanResults.size(); i++) {
            lowestLevels.set(i, scanResults.get(i).level);
        }
        Collections.sort(lowestLevels, Collections.reverseOrder());
        int removeOtherThanFirst3objects = lowestLevels.size() - 3;
        for (int i = 0; i < removeOtherThanFirst3objects; i++) { // remove indexes from last
            lowestLevels.remove(lowestLevels.size() - 1);
        }
        return lowestLevels;
    }
}
