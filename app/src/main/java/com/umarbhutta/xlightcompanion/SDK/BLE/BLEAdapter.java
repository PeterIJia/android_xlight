package com.umarbhutta.xlightcompanion.SDK.BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by sunboss on 2016-12-10.
 */

@SuppressWarnings({"UnusedDeclaration"})
public class BLEAdapter {
    // misc
    private static final String TAG = BLEAdapter.class.getSimpleName();
    public static final int REQUEST_ENABLE_BT = 1010;
    private static final String XLIGHT_BLE_NAME_PREFIX = "XLIGHT";
    private static final int XLIGHT_BLE_CLASS = 0;

    private static boolean m_bInitialized = false;
    private static BluetoothAdapter m_btAdapter;
    private static Context m_Context;
    private static boolean m_bSupported = false;
    private static boolean m_bEnabled = false;

    public static ArrayList<BluetoothDevice> mPairedDevices = new ArrayList<>();

    public static void init(Context context) {
        m_Context = context;
        m_bSupported = m_Context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if  (!m_bSupported) {
            Log.e(TAG, "Bluetooth NOT supported!");
            return;
        }
        m_btAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBluetoothState();
        m_bInitialized = true;
    }

    public static boolean initialized() {
        return m_bInitialized;
    }

    public static boolean IsSupported() {
        return m_bSupported;
    }

    public static boolean IsEnabled() {
        return m_bEnabled;
    }

    public static void CheckBluetoothState() {
        if (m_btAdapter != null) {
            m_bEnabled = m_btAdapter.isEnabled();
        } else {
            m_bEnabled = false;
        }

        if (m_bEnabled) {
            Log.d(TAG, "Bluetooth is enabled...");
            mPairedDevices.clear();
            Set<BluetoothDevice> devices = m_btAdapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                if (device.getBluetoothClass().hashCode() == XLIGHT_BLE_CLASS || device.getName().startsWith(XLIGHT_BLE_NAME_PREFIX)) {
                    mPairedDevices.add(device);
                }
            }
        }
    }

    public static BluetoothDevice SearchDeviceName(final String devName) {
        for (BluetoothDevice device : mPairedDevices) {
            if (device.getName().equalsIgnoreCase(devName)) {
                return device;
            }
        }
        return null;
    }
}
