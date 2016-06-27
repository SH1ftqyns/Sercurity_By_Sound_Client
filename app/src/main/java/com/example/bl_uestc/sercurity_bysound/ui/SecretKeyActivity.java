package com.example.bl_uestc.sercurity_bysound.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bl_uestc.sercurity_bysound.BaseActivity;
import com.example.bl_uestc.sercurity_bysound.BluetoothManager;
import com.example.bl_uestc.sercurity_bysound.R;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.LogHelper;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.SinVoicePlayer;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.SinVoiceRecognition;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SecretKeyActivity extends BaseActivity  implements SinVoicePlayer.Listener,SinVoiceRecognition.Listener {



    @Bind(R.id.start_play)
    Button start_play;

    @Bind(R.id.receive_str)
    TextView receive_str;

    private final static int MSG_SET_RECG_TEXT = 1;  //识别结果
    private final static int MSG_RECG_START = 2;// 开始识别，负责将StringBuilder清空
    private final static int MSG_RECG_END = 3; //结束识别
    public static Context context=null;
    public static String metesxt="";
    public static String othertext="";

    int MAX_NUMBER=5;
    private final static String CODEBOOK = "12345"; //码书
    private RegHandler mHanlder;
    private SinVoicePlayer mSinVoicePlayer;
    private SinVoiceRecognition mRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("同步秘钥");
        Log.i("main","更换秘钥界面");
        context = getApplicationContext();
        mHanlder = new RegHandler();
        mHanlder.receive_str = receive_str;
        mSinVoicePlayer = new SinVoicePlayer(CODEBOOK);
        mSinVoicePlayer.setListener(this);

        mRecognition = new SinVoiceRecognition(CODEBOOK);
        mRecognition.setListener(this);




        start_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mRecognition.start();
                String text=genText(6);
                metesxt=text;
                mSinVoicePlayer.play(text, false, 1000);//传入参数分别为发送字符串，是否重复，静默间隔

            }
        });
    }

    @Override
    public void onRecognitionStart() {
        // TODO Auto-generated method stub
        Log.i("main","onRecognitionStart");
        mHanlder.sendEmptyMessage(MSG_RECG_START);

    }


    @Override
    public void onRecognition(char ch) {
        Log.i("main","onRecognition:"+ch);
        // TODO Auto-generated method stub
        mHanlder.sendMessage(mHanlder.obtainMessage(MSG_SET_RECG_TEXT, ch, 0));

    }


    @Override
    public void onRecognitionEnd() {
        // TODO Auto-generated method stub
        mHanlder.sendEmptyMessage(MSG_RECG_END);


        mRecognition.stop();//

        if(! othertext.substring(4).equals(jiaoyan(metesxt) ) ){
            findError();
        }
        else{
            finishall();
        }



    }

    public static String jiaoyan(String s){
        int k=0;
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<s.length();i++){
            int temp=(int)s.charAt(i);
            k^=temp;
        }
        k%=100;
        if (k<10){
            sb.append("0");
            sb.append(k);
        }
        else{
            sb.append(k);
        }

        return sb.toString();
    }


    private void findError(){

    }


    /**
     * 得到了双方的结果后，最总处理的函数
     */
    private void finishall(){
        String s1=metesxt;//我方生成的字符串
        String s2=othertext;//对方生成的字符串

    }

    private static class RegHandler extends Handler {

        private StringBuilder mTextBuilder = new StringBuilder();

        TextView receive_str;


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_SET_RECG_TEXT:
                    Toast.makeText(context,"MSG_SET_RECG_TEXT",Toast.LENGTH_LONG).show();
                    char ch = (char) msg.arg1;
                    Log.i("main","更换秘钥界面");
                    Log.i("main", "MSG_SET_RECG_TEXT");
                    mTextBuilder.append(ch);
//                    if (null != receive_str) {
//                        receive_str.setText(mTextBuilder.toString());
//                    }
                    break;

                case MSG_RECG_START:
                    Toast.makeText(context,"MSG_RECG_START",Toast.LENGTH_LONG).show();
                    Log.i("main", "recognition start");
                    mTextBuilder.delete(0, mTextBuilder.length());
                    break;

                case MSG_RECG_END:
                    Toast.makeText(context,"MSG_RECG_END",Toast.LENGTH_LONG).show();
                    Log.i("main", "recognition end");
                    othertext=mTextBuilder.toString();
                    Toast.makeText(context, othertext, Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }


    }



    private String genText(int count) {
        StringBuilder sb = new StringBuilder();
        int pre = 0;
        while (count > 0) {
            int x = (int) (Math.random() * MAX_NUMBER + 1);
            if (Math.abs(x - pre) > 0) {
                sb.append(x);
                --count;
                pre = x;
            }
        }

        return sb.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_secret_key;
    }

    @Override
    public void onPlayStart() {
        Log.i("main","play start");
    }

    @Override
    public void onPlayEnd() {

        mRecognition.start();


    }


}
