package com.example.bl_uestc.sercurity_bysound;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.duowan.mobile.example.netroid.netroid.Netroid;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.request.StringRequest;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static String DB_NAME = "SercuritySound"; //数据库名称
    private static int DB_VERSION = 1;
    TextView text;
    File diskCacheDir= null;
    int disksize=50*1024*1024;
    RequestQueue myquene = null;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView tv_close;
    //创建数据库
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diskCacheDir=new File(getCacheDir(), "netroid");
        myquene = Netroid.newRequestQueue(getApplicationContext(), new DiskCache(diskCacheDir, disksize));


        setContentView(R.layout.main);
        String url="http://www.baidu.com";
        text=(TextView)findViewById(R.id.textView);

        //建立数据库
        sqLiteHelper =new SQLiteHelper(this,DB_NAME,null,DB_VERSION);
        db=sqLiteHelper.getWritableDatabase();

        String s="";
        myquene.add(new StringRequest(url, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                text.setText(response);
            }
        }));

        init_toorbar();




    }


    private void init_toorbar(){
        tv_close= (TextView) this.findViewById(R.id.tv_close);
        android.support.v7.app.ActionBar actionbar=getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setLogo(R.drawable.ic_launcher);
        actionbar.setTitle("主界面");





    }

    private void initViews() {
        tv_close= (TextView) this.findViewById(R.id.tv_close);
        mToolbar= (Toolbar) this.findViewById(R.id.toolbar);
        mToolbar.setTitle("Toolbar");

        setSupportActionBar(mToolbar);
        //是否给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //设置侧或布局
        mDrawerLayout= (DrawerLayout) this.findViewById(R.id.id_drawerlayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        //使用Patette
        setPatette();
    }

    private void setPatette() {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.back_25);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(swatch.getRgb()));

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplication(),"setting",Toast.LENGTH_LONG).show();
            return true;
        }
        if (id==R.id.action_share){
            Toast.makeText(getApplication(),"share",Toast.LENGTH_LONG).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
