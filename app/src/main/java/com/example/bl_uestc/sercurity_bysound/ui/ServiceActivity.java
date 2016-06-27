package com.example.bl_uestc.sercurity_bysound.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bl_uestc.sercurity_bysound.BaseActivity;
import com.example.bl_uestc.sercurity_bysound.R;

import butterknife.Bind;

public class ServiceActivity extends BaseActivity {


    @Bind(R.id.validate)
    Button validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("实时守护");
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("调用同态加密模块，条用通信模块，向服务商发送加密验证信息");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service;
    }
}
