package com.example.hbb.tcpudp.core;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by hbb on 2017/7/10.
 */

public class NetTool {
    /**
     * 获取本机IP地址
     *
     * @return
     */
    public static String getLocalIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.d("tag", "getLocalIP ex=" + ex.toString());
        }
        return null;
    }

    /**
     * 获取本机IP
     * @return
     */
    public static String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("tag", "SocketException ="+e.toString());
        }
        return hostIp;
    }

    /**
     * 转换IP地址
     * WIFI返回的地下为B类
     *
     * @param IP
     * @return
     */
    public static String getBoradcastIP(String IP) {
        String temp = IP.substring(0, 11);   //因为这是一个b类地址
        Log.i("Data", "temp=" + temp);
        StringBuffer buffer = new StringBuffer();
        buffer.append(temp);
        buffer.append("255");
        return buffer.toString();
    }

    /**
     * 获取WIFI的IP地址
     *
     * @param context
     * @return
     */
    public static String getWIFIIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String SSID = wifiInfo.getSSID();
        Log.i("Data", "SSID=" + SSID);
        String IPAddress = intToIp(ipAddress);     //获取WIFI的IP地址
        return IPAddress;
    }

    /**
     * 把整数转换成IP地址
     *
     * @param i
     * @return
     */
    public static String intToIp(int i) {
        return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF));
    }

    /**
     * 把字节数据转成十六进制字符串
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
