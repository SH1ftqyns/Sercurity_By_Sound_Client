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
import com.example.bl_uestc.sercurity_bysound.FullyHomomorphicEncryption;
import com.example.bl_uestc.sercurity_bysound.R;
import com.example.bl_uestc.sercurity_bysound.StringTransfer;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.LogHelper;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.SinVoicePlayer;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.SinVoiceRecognition;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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


                /**
                 * 以下代码为客户端向服务器发送圆心位置信息所用
                 */
                StringTransfer st=new StringTransfer();

                int longtitude=(int)(marker.getPosition().longitude*Math.pow(10,6) );
                int latitude=(int)(marker.getPosition().latitude*Math.pow(10,6) );

                FullyHomomorphicEncryption obj = new FullyHomomorphicEncryption();
                obj.generateKey();
                //ArrayList<BigInteger> encrypt1 = obj.encrypt(st.StringToBin(String.valueOf(longtitude)));
                ArrayList<BigInteger> encrypt2 = obj.encrypt(st.StringToBin(String.valueOf(latitude)));
                ArrayList<BigInteger> encrypt3 = obj.encrypt(st.StringToBin(String.valueOf((int)(distance*Math.pow(10,6))  )));
                BigInteger encrypt_latitude=encrypt2.get(0);
                BigInteger encrypt_distance=encrypt3.get(0);

                HttpClient client=new DefaultHttpClient();
                HttpPost post=new HttpPost("");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("target","set_up" ));
                params.add(new BasicNameValuePair("longtitude",String.valueOf(longtitude) ));
                params.add(new BasicNameValuePair("latitude",encrypt_latitude.toString() ));
                params.add(new BasicNameValuePair("distance",encrypt_distance.toString() ));
                try {
                    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    HttpResponse reponse=client.execute(post);
                    if(reponse.getStatusLine().getStatusCode()==200){
                        Toast.makeText(FenceActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(FenceActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
