package com.example.bl_uestc.sercurity_bysound;

import android.app.Application;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by qyxzzc on 2016/6/11.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }


}
