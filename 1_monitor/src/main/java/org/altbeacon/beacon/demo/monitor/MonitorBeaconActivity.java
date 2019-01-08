package org.altbeacon.beacon.demo.monitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.List;

import hugo.weaving.DebugLog;

@DebugLog
public class MonitorBeaconActivity extends AppCompatActivity
        implements MonitorNotifier, BeaconConsumer {

    private static final int REQUEST_TURN_ON_BLUETOOTH = 1;
    private TextView tv_log;
    private Button btn_scan;
    private Button btn_enable_bluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        tv_log = findViewById(R.id.tv_log);
        btn_scan = findViewById(R.id.btn_scan);
        btn_enable_bluetooth = findViewById(R.id.btn_enable_bluetooth);
        initBeaconScanSettings();
        initUI();

    }

    private void initBeaconScanSettings() {
        BeaconManager.setRegionExitPeriod(5000);
    }

    private void initUI() {
        if (bluetoothAvailable()) {
            boolean bluetoothEnabled = bluetoothEnabled();
            initBluetoothStatus(bluetoothEnabled);
            updateSimulatorUI(bluetoothEnabled);
        } else {
            btn_scan.setEnabled(false);
            btn_enable_bluetooth.setEnabled(false);
            tv_log.setText(R.string.bluetooth_not_available);
        }

    }

    private boolean bluetoothAvailable() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    private void updateSimulatorUI(boolean bluetoothEnabled) {
        if (bluetoothEnabled) {
            bindBeaconManager();
        } else {
            btn_scan.setEnabled(false);
            clear();
            append(getString(R.string.scan_not_ready));
        }
    }

    private void clear() {
        tv_log.setText(null);
    }

    private void initBluetoothStatus(boolean bluetoothEnabled) {
        if (bluetoothEnabled) {
            btn_enable_bluetooth.setText(R.string.bluetooth_enabled);
            btn_enable_bluetooth.setEnabled(false);
            btn_enable_bluetooth.setVisibility(View.INVISIBLE);
        } else {
            btn_enable_bluetooth.setText(R.string.enable_bluetooth);
            btn_enable_bluetooth.setEnabled(true);
            btn_enable_bluetooth.setVisibility(View.VISIBLE);
        }

    }

    private boolean bluetoothEnabled() {
        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = manager.getAdapter();
        return bluetoothAdapter.isEnabled();
    }

    private void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_TURN_ON_BLUETOOTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TURN_ON_BLUETOOTH:
                updateSimulatorUI(bluetoothEnabled());
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    private void bindBeaconManager() {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.addMonitorNotifier(this);
        beaconManager.bind(this);
    }

    public void append(final String log) {
        tv_log.append(log);
    }

    public void toggleSimulate(View view) {
        if (btn_scan.getText().equals(getString(R.string.scan))) {
            onScanRequested();
        } else {
            onStopRequested();
        }

    }

    private void onStopRequested() {
        stopBeacon();
        append("\nBeacon scanning stopped.\n");
        btn_scan.setText(R.string.scan);
    }

    private void stopBeacon() {
        deregisterBeaconToBeMonitored(UuidProvider.beaconToMonitored());
    }

    private void onScanRequested() {
        append("\nBegin to scan beacon.\n");
        btn_scan.setText(R.string.stop);
        scanBeacon();
    }

    private void scanBeacon() {
        registerBeaconToBeMonitored(UuidProvider.beaconToMonitored());
    }


    @Override
    public void didEnterRegion(Region region) {
        append("enter:" + region.getUniqueId() + "\n");
    }

    @Override
    public void didExitRegion(Region region) {
        append("exit:" + region.getUniqueId() + "\n");
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {

    }

    @Override
    public void onBeaconServiceConnect() {
        clear();
        append(getString(R.string.scan_ready));
        btn_scan.setEnabled(true);

    }

    private void registerBeaconToBeMonitored(List<String> beacons) {
        try {
            BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
            for (String beacon : beacons) {
                append("monitor beacon : " + beacon + "\n");
                beaconManager.startMonitoringBeaconsInRegion(UuidMapper.constructRegion(beacon));
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    private void deregisterBeaconToBeMonitored(List<String> beacons) {
        try {
            BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
            for (String beacon : beacons) {
                append("stop monitoring beacon : " + beacon + "\n");
                beaconManager.stopMonitoringBeaconsInRegion(UuidMapper.constructRegion(beacon));
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void enableBluetooth(View view) {
        enableBluetooth();
    }

}
