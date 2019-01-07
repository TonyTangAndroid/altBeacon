package org.altbeacon.beacon.demo.simulator;

import java.util.ArrayList;
import java.util.List;

public class UuidProvider {

    public static List<String> regionToBeSimulated() {
        List<String> result = new ArrayList<>();
        result.add("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A:1:1");
        result.add("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A:1:2");
        result.add("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A:1:3");
        result.add("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A:1:4");
        return result;
    }
}