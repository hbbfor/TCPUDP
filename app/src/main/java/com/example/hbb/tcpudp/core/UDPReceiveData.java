package com.example.hbb.tcpudp.core;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by hbb on 2017/7/10.
 */

public class UDPReceiveData implements Runnable {

    private DatagramSocket datagramSocket = null;
    private DatagramPacket datagramPacket = null;
    private int udp_receive_port = 5679;
    private boolean flagReceive = true;
    private Handler handler;

    public UDPReceiveData(Handler handler1) {
        handler = handler1;
        flagReceive = true;
    }

    /**
     * 停止UDP接收数据
     */
    public void StopUDPReceiveData() {
        flagReceive = false;
    }

    public boolean isConnected() {
        return datagramSocket != null && datagramSocket.isConnected();
    }

    @Override
    public void run() {
        byte[] data = new byte[1024];
        try {
            datagramSocket = new DatagramSocket(null);
            datagramSocket.setReuseAddress(true);
            datagramSocket.bind(new InetSocketAddress(udp_receive_port));
            datagramPacket = new DatagramPacket(data, data.length);
        } catch (Exception ex) {
            Log.d("tag", "udpReceiveData run()=" + ex.toString());
            LogToFile.d("tag", "udpReceiveData run() ex=" + ex.toString());
            return;
        }
        while (flagReceive) {
            try {
                datagramSocket.receive(datagramPacket);

                //接收到的byte[]
                byte[] receiveData = Arrays.copyOf(datagramPacket.getData(), datagramPacket.getLength());
                String strReceive = new String(receiveData);
                Log.d("tag", "receive msg=" + strReceive);
                LogToFile.d("tag", "receive msg=" + strReceive);

                Message message = new Message();
                message.what = udp_receive_port;
                Bundle bundle = new Bundle();
                bundle.putString("data", strReceive);
                handler.sendMessage(message);
            } catch (Exception ex) {
                LogToFile.d("tag", "receive ex=" + ex.toString());
            }
        }
        datagramSocket.close();
    }
}
