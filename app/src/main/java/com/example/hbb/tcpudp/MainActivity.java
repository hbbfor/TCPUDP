package com.example.hbb.tcpudp;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hbb.tcpudp.core.LogToFile;
import com.example.hbb.tcpudp.core.NetTool;
import com.example.hbb.tcpudp.core.UDPBroadCast;
import com.example.hbb.tcpudp.core.UDPReceiveData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnUdp;
    private UDPReceiveData receiveData;
    private UDPBroadCast broadCast;
    private TextView tv_udpReceive;
    private Button btnUdpStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1000);
                //requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            }
            //app启动就初始化一次写日志文件
            LogToFile.init();
            initView();
            Log.d("tag", "initView over");
            LogToFile.d("tag", "initView over");
            initData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String content = bundle.getString("data");
            tv_udpReceive.setText(content);
        }
    };

    private void initData() {
        String localip = NetTool.getHostIP();
        Log.d("tag", "get localip over=" + localip);
        LogToFile.d("tag", "get localip over=" + localip);
        broadCast = new UDPBroadCast(localip.getBytes());
        receiveData = new UDPReceiveData(handler);
    }

    private void initView() {
        btnUdp = (Button) findViewById(R.id.btnUDPStart);
        btnUdp.setOnClickListener(this);
        tv_udpReceive = (TextView) findViewById(R.id.tv_udp_receive);
        btnUdpStop = (Button) findViewById(R.id.btnUDPStop);
        btnUdpStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUDPStart:
                UDPStart();
                break;
            case R.id.btnUDPStop:
                UDPStop();
                break;
        }
    }

    private void UDPStart() {
        new Thread(broadCast).start();
        new Thread(receiveData).start();
        Log.d("tag", "udp start over");
        LogToFile.d("tag", "udp start over");
    }

    private void UDPStop() {
        broadCast.StopUDPBroadCast();
        receiveData.StopUDPReceiveData();
    }
}
