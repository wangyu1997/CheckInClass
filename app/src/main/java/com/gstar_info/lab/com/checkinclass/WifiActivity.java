package com.gstar_info.lab.com.checkinclass;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangyu on 07/04/2017.
 */

public class WifiActivity extends AppCompatActivity {

    private TextView wifiText;
    private WifiManager wifi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_activity);
        TextView infoText = (TextView) this.findViewById(R.id.info);
        wifiText = (TextView) this.findViewById(R.id.wifi);
        infoText.setText(getInfo());


        Thread t = new Thread(new Runnable() {
            public void run() {

                while (true) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            obtainWifiInfo();
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    private String getInfo() {
        wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();

        String maxText = info.getMacAddress();
        String ipText = intToIp(info.getIpAddress());
        String status = "";
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            status = "WIFI_STATE_ENABLED";
        }
        String ssid = info.getSSID();
        int networkID = info.getNetworkId();
        int speed = info.getLinkSpeed();
        String bssid = info.getBSSID();
        return "mac：" + maxText + "\n\r"
                + "ip：" + ipText + "\n\r"
                + "wifi status :" + status + "\n\r"
                + "ssid :" + ssid + "\n\r"
                + "net work id :" + networkID + "\n\r"
                + "connection speed:" + speed + "\n\r"
                + "BSSID:" + bssid + "\n\r"
                ;
    }

    private String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }

    private void obtainWifiInfo() {

        // 显示扫描到的所有wifi信息：
        wifi = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            StringBuilder scanBuilder = new StringBuilder();
            List<ScanResult> scanResults = wifi.getScanResults();//搜索到的设备列表

            for (ScanResult scanResult : scanResults) {

                scanBuilder.append("\n设备名：" + scanResult.SSID
                        + "\n信号强度：" + wifi.calculateSignalLevel(scanResult.level, 1001)
                        + "\nBSSID:" + scanResult.BSSID);
            }
            wifiText.setText(scanBuilder);
        }

    }
}


