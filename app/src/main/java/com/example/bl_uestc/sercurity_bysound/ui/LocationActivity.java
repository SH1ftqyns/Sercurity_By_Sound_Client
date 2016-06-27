package com.example.bl_uestc.sercurity_bysound.ui;

import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.bl_uestc.sercurity_bysound.BaseActivity;
import com.example.bl_uestc.sercurity_bysound.R;

public class LocationActivity extends BaseActivity {

    MapView mapview;
    BaiduMap baidumap;
    private LocationClient mLocClient;
    private boolean isFirstLoc = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("实时定位");

        MyLocationConfiguration.LocationMode mcurrentmode= MyLocationConfiguration.LocationMode.NORMAL;
        mapview = (MapView) findViewById(R.id.bmapView);
        baidumap=mapview.getMap();
        baidumap.setMyLocationEnabled(true);
        mLocClient=new LocationClient(this);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000); //设置发起定位请求的间隔时间为1000ms
        baidumap.setMyLocationConfigeration(new MyLocationConfiguration(mcurrentmode,true,null));
        baidumap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mLocClient.setLocOption(option);
        mLocClient.start();

        MyLocationData locationData=new MyLocationData.Builder().accuracy(40).direction(100)
                .latitude(30.675822) .longitude(104.103092).build();

        baidumap.setMyLocationData(locationData);
        if(isFirstLoc){
            isFirstLoc=false;
            LatLng ll=new LatLng(30.675822,104.103092);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            baidumap.animateMapStatus(u);

        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        baidumap.setMyLocationEnabled(false);
        mapview.onDestroy();
        mapview = null;
        super.onDestroy();
    }





}
