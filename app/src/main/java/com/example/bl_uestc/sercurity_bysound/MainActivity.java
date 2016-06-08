package com.example.bl_uestc.sercurity_bysound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.bl_uestc.sercurity_bysound.ui.FenceActivity;
import com.example.bl_uestc.sercurity_bysound.ui.FenceActivity;
import com.example.bl_uestc.sercurity_bysound.ui.LocationActivity;
import com.example.bl_uestc.sercurity_bysound.ui.SecretKeyActivity;
import com.example.bl_uestc.sercurity_bysound.ui.ServiceActivity;

import butterknife.Bind;


public class MainActivity extends BaseActivity {

    @Bind(R.id.fence)
    View fence;

    @Bind(R.id.location)
    View location;

    @Bind(R.id.service)
    View service;

    @Bind(R.id.secret_key)
    View secret_key;

    @Bind(R.id.tv_bluetooth)
    TextView tv_bluetooth;

    @Bind(R.id.tv_battery)
    TextView tv_battery;

    @Bind(R.id.tv_location)
    TextView tv_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sh1ft安全智能手表");

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });

        fence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FenceActivity.class));
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServiceActivity.class));
            }
        });

        secret_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecretKeyActivity.class));
            }
        });

        if (BluetoothManager.isBluetoothEnabled()) {
            tv_bluetooth.setText("蓝牙:开");
        } else {
            tv_bluetooth.setText("蓝牙:关");
        }


        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            tv_battery.setText("电量" + intent.getIntExtra("level", 0) + "%");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
