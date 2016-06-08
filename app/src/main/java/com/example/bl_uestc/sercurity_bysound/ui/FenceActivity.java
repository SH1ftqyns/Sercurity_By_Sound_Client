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

import com.example.bl_uestc.sercurity_bysound.BaseActivity;
import com.example.bl_uestc.sercurity_bysound.BluetoothManager;
import com.example.bl_uestc.sercurity_bysound.R;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.LogHelper;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.SinVoicePlayer;
import com.example.bl_uestc.sercurity_bysound.libra.sinvoice.SinVoiceRecognition;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FenceActivity extends BaseActivity implements SinVoicePlayer.Listener,SinVoiceRecognition.Listener{

    private final static int MSG_SET_RECG_TEXT = 1;  //ʶ����
    private final static int MSG_RECG_START = 2;// ��ʼʶ�𣬸���StringBuilder���
    private final static int MSG_RECG_END = 3; //����ʶ��


//    @Bind(R.id.recyclerView)
//    RecyclerView recyclerView;
//
//    @Bind(R.id.bluetooth_switch)
//    CheckBox bluetooth_switch;
//
//    @Bind(R.id.connect)
//    Button connect;

    public static Context context=null;
    public static String metesxt="";
    public static String othertext="";

    int MAX_NUMBER=5;
    private final static String CODEBOOK = "12345";
    private Handler mHanlder;
    private SinVoicePlayer mSinVoicePlayer;
    private SinVoiceRecognition mRecognition;
    private Button playbutton;

    /**
     * 自定义的打开 Bluetooth 的请求码，与 onActivityResult 中返回的 requestCode 匹配。
     */
    private static final int REQUEST_CODE_BLUETOOTH_ON = 1313;

    /**
     * Bluetooth 设备可见时间，单位：秒。
     */
    private static final int BLUETOOTH_DISCOVERABLE_DURATION = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("电子围栏");
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new Adapter());
        init();
        context = getApplicationContext();
        mHanlder = new RegHandler();
        mSinVoicePlayer = new SinVoicePlayer(CODEBOOK);
        mSinVoicePlayer.setListener(this);

        mRecognition = new SinVoiceRecognition(CODEBOOK);
        mRecognition.setListener(this);

//        connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "start play sound", Toast.LENGTH_LONG).show();
//                String text=genText(6);
//                metesxt=text;
//                mSinVoicePlayer.play(text, false, 1000);
//            }
//        });


    }

    @Override
    public void onRecognitionStart() {
        // TODO Auto-generated method stub
        mHanlder.sendEmptyMessage(MSG_RECG_START);

    }


    @Override
    public void onRecognition(char ch) {
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
     * �õ���˫���Ľ�������ܴ���ĺ���
     */
    private void finishall(){
        String s1=metesxt;//�ҷ����ɵ��ַ���
        String s2=othertext;//�Է����ɵ��ַ���

    }

    private static class RegHandler extends Handler {

        private StringBuilder mTextBuilder = new StringBuilder();
        private TextView mRecognisedTextView;



        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_SET_RECG_TEXT:
                    Toast.makeText(context,"MSG_SET_RECG_TEXT",Toast.LENGTH_LONG).show();
                    char ch = (char) msg.arg1;
                    LogHelper.d("main", "MSG_SET_RECG_TEXT");
                    mTextBuilder.append(ch);
                    if (null != mRecognisedTextView) {
                        mRecognisedTextView.setText(mTextBuilder.toString());
                    }
                    break;

                case MSG_RECG_START:
                    Toast.makeText(context,"MSG_RECG_START",Toast.LENGTH_LONG).show();
                    LogHelper.d("main", "recognition start");
                    mTextBuilder.delete(0, mTextBuilder.length());
                    break;

                case MSG_RECG_END:
                    Toast.makeText(context,"MSG_RECG_END",Toast.LENGTH_LONG).show();
                    LogHelper.d("main", "recognition end");
                    othertext=othertext;
                    Toast.makeText(context, othertext, Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }


    }
    private void init() {

        if (!BluetoothManager.isBluetoothSupported()) {
//            bluetooth_switch.setEnabled(false);
            toast("设备不支持蓝牙");
            return;
        }
//        bluetooth_switch.setChecked(BluetoothManager.isBluetoothEnabled());
//        bluetooth_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    turnOnBluetooth();
//                } else {
//                    if (BluetoothManager.turnOffBluetooth()) {
//                        toast("关闭失败");
//                    } else {
//                        toast("关闭成功");
//                    }
//                }
//            }
//        });
    }


    /**
     * 弹出系统弹框提示用户打开 Bluetooth
     */
    private void turnOnBluetooth() {
        // 请求打开 Bluetooth
        Intent requestBluetoothOn = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);

        // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
        requestBluetoothOn
                .setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        // 设置 Bluetooth 设备可见时间
        requestBluetoothOn.putExtra(
                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                BLUETOOTH_DISCOVERABLE_DURATION);

        // 请求开启 Bluetooth
        this.startActivityForResult(requestBluetoothOn,
                REQUEST_CODE_BLUETOOTH_ON);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // requestCode 与请求开启 Bluetooth 传入的 requestCode 相对应
        if (requestCode == REQUEST_CODE_BLUETOOTH_ON) {
            switch (resultCode) {
                // 点击确认按钮
                case Activity.RESULT_OK: {
                    toast("蓝牙打开成功");
                }
                break;

                // 点击取消按钮或点击返回键
                case Activity.RESULT_CANCELED: {
                    toast("蓝牙打开失败");
                }
                break;
                default:
                    break;
            }
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
        return R.layout.activity_fence;
    }

    @Override
    public void onPlayStart() {
        mRecognition.start();
    }

    @Override
    public void onPlayEnd() {

    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {


        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public class Holder extends RecyclerView.ViewHolder {

            @Bind(R.id.name)
            TextView name;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
