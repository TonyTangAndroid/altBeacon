package org.altbeacon.beacon.demo.simulator;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.simulator.BeaconSimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedBeaconSimulator implements BeaconSimulator {

    private final Callback callback;
    private List<Beacon> beacons;
    private ScheduledExecutorService scheduleTaskExecutor;

    public TimedBeaconSimulator(Callback callback) {
        this.callback = callback;
        beacons = new ArrayList<>();
    }


    public List<Beacon> getBeacons() {
        return beacons;
    }

    public void createTimedSimulatedBeacons() {

        final List<Beacon> finalBeacons = new ArrayList<>(beaconToBeSimulated());
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(() -> run(finalBeacons), 1, 3, TimeUnit.SECONDS);

    }

    private List<Beacon> beaconToBeSimulated() {
        List<Beacon> beacons = new ArrayList<>();
        for (String uuid : UuidProvider.regionToBeSimulated()) {
            Beacon beacon = UuidMapper.toBeacon(uuid);
            beacons.add(beacon);
        }
        return beacons;
    }

    private void run(List<Beacon> finalBeacons) {
        int index = beacons.size();
        if (finalBeacons.size() > index) {
            Beacon beacon = finalBeacons.get(index);
            add(beacon);
        } else {
            shutdown();
        }
    }

    public void shutdown() {
        beacons.clear();
        scheduleTaskExecutor.shutdown();
        callback.onShutdown();
    }

    private void add(Beacon beacon) {
        beacons.add(beacon);
    }

    public interface Callback {
        void onShutdown();
    }

}