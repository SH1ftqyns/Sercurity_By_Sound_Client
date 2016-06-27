package com.example.bl_uestc.sercurity_bysound.ui;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends BaseActivity {

    MapView mapview;
    BaiduMap baidumap;
    private LocationClient mLocClient;
    private boolean isFirstLoc = true;
    private MyLocationListenner myListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("实时定位");

        MyLocationConfiguration.LocationMode mcurrentmode= MyLocationConfiguration.LocationMode.NORMAL;
        mapview = (MapView) findViewById(R.id.bmapView);
        baidumap=mapview.getMap();
        baidumap.setMyLocationEnabled(true);
        mLocClient=new LocationClient(this);
        myListener = new MyLocationListenner();
        mLocClient.registerLocationListener(myListener);
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


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapview == null)
                return;

            //location.setLongitude(120);
            //location.setLatitude(38);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            baidumap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                baidumap.animateMapStatus(u);
            }

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    int longtitude=(int)(location.getLongitude() *Math.pow(10,6));
                    int latitude=(int)(location.getLatitude()*Math.pow(10,6) );
                    // TODO 唯一标识符还没有完成
                    String identifier="";
                    // TODO uid还没有解决
                    String myuid="";
                    String touid="";

                    HttpClient client=new DefaultHttpClient();
                    HttpPost post=new HttpPost("");
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("target","getposttion"));
                    params.add(new BasicNameValuePair("myuid",myuid));
                    params.add(new BasicNameValuePair("identifier",identifier));
                    try {
                        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                        HttpResponse reponse=client.execute(post);
                        if(reponse.getStatusLine().getStatusCode()==200){
                            String result=EntityUtils.toString(reponse.getEntity());


                        }


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }.start();


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
