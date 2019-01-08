package org.altbeacon.beacon.demo.monitor;

import java.util.ArrayList;
import java.util.List;

public class UuidProvider {

    /**
     * define the beacon uuid that you want to scan.
     *
     * @return the list of beacon that you would like to scan.
     */
    public static List<String> beaconToMonitored() {
        List<String> result = new ArrayList<>();
        result.add("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6:1:2");
        return result;
    }
}