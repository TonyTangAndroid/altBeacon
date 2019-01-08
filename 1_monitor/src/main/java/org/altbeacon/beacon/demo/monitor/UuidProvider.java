package org.altbeacon.beacon.demo.monitor;

import java.util.ArrayList;
import java.util.List;

public class UuidProvider {

    /**
     * define the beacon that you want to scan.
     *
     * @return the list of beacon that you would like to scan.
     */
    public static List<String> regionToBeSimulated() {
        List<String> result = new ArrayList<>();
        result.add("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A:1:1");
        return result;
    }
}