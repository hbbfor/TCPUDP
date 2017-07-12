package com.example.hbb.tcpudp.core;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by hbb on 2017/7/10.
 */

public class UDPBroadCast extends Thread {

    private byte[] getData = null;
    private DatagramSocket udpSocket;
    private int udp_send_port = 5678;
    private boolean flagSend = true;

    public UDPBroadCast(byte[] data) {
        this.getData = data;
        flagSend = true;
    }

    /**
     * 停止UDP广播
     */
    public void StopUDPBroadCast() {
        flagSend = false;
    }

    public boolean isConnected() {
        return udpSocket != null && udpSocket.isConnected();
    }

    public void run() {
        DatagramPacket dataPacket = null;
        try {
            udpSocket = new DatagramSocket();
            dataPacket = new DatagramPacket(getData, getData.length);
            dataPacket.setData(getData);
            dataPacket.setLength(getData.length);
            dataPacket.setPort(udp_send_port);
            dataPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        } catch (Exception e) {
            Log.e("tag", e.toString());
            return;
        }
        while (flagSend) {
            try {
                udpSocket.send(dataPacket);
                Log.d("tag","send packet");
                sleep(1000);
            } catch (Exception e) {
                Log.e("tag", e.toString());
            }
        }
        udpSocket.close();
    }
}
