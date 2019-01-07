package org.altbeacon.beacon.demo.simulator;

import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.List;

public class UuidMapper {


    public static Region constructRegion(String uuid) {
        String[] parts = uuid.split(":");
        List<Identifier> identifierList = new ArrayList<>();
        identifierList.add(Identifier.parse(parts[0]));
        identifierList.add(Identifier.parse(parts[1]));
        identifierList.add(Identifier.parse(parts[2]));
        return new Region(uuid, identifierList);
    }

    public static Beacon toBeacon(String uuid) {
        String[] parts = uuid.split(":");
        return new AltBeacon.Builder()
                .setId1(parts[0]).setId2(parts[1]).setId3(parts[2])
                .setRssi(-55).setTxPower(-55)
                .build();
    }
}