package com.example.bl_uestc.sercurity_bysound.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.bl_uestc.sercurity_bysound.BaseActivity;
import com.example.bl_uestc.sercurity_bysound.BluetoothManager;
import com.example.bl_uestc.sercurity_bysound.R;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.LogHelper;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.SinVoicePlayer;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.SinVoiceRecognition;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FenceActivity extends BaseActivity {


    private MapView mapview;
    private BaiduMap baidumap;
    private LocationClient mLocClient;
    private boolean isFirstLoc = true;

    CircleOptions circleOptions;
    Overlay overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("电子围栏");

        MyLocationConfiguration.LocationMode mcurrentmode= MyLocationConfiguration.LocationMode.NORMAL;
        mapview = (MapView) findViewById(R.id.bmapView);
        baidumap=mapview.getMap();
        baidumap.setMyLocationEnabled(true);
        mLocClient=new LocationClient(this);
        //   listener=new MyLocationListener();
//        mLocClient.registerLocationListener(listener);
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
        locationData=new MyLocationData.Builder().accuracy(40).direction(100)
                .latitude(30.675822) .longitude(104.103092).build();
        baidumap.setMyLocationData(locationData);

        LatLng pt = new LatLng(30.675822,104.103092);
        circleOptions = new CircleOptions();

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        OverlayOptions oo=new MarkerOptions().position(pt).zIndex(9).icon(bitmap)
                .draggable(true);  //设置手势拖拽;
        Marker marker = (Marker) (baidumap.addOverlay(oo));


        baidumap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                //circleOptions.center(new LatLng(latitude, longitude));

                //计算两点之间的距离
                double distance= DistanceUtil.getDistance(baidumap.getMapStatus().bound.northeast,baidumap.getMapStatus().bound.southwest);

                circleOptions.center(marker.getPosition());                          //设置圆心坐标
                circleOptions.fillColor(0xAAFFFF00);               //圆填充颜色
                circleOptions.radius((int)(distance/5));                         //设置半径
                circleOptions.stroke(new Stroke(5, 0xAA00FF00));   // 设置边框
                circleOptions.visible(true);
                overlay= baidumap.addOverlay(circleOptions);

            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                if(overlay!=null){
                    overlay.remove();
                }
            }
        });


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fence;
    }


}
