package com.android.librarycamera.materialcamera.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class IPGet {
    public  static IPGet ipGet;
    public static IPGet getIpGet() {
        if (ipGet == null){
            synchronized (IPGet.class){
                ipGet = new IPGet();
            }
        }
        return ipGet;
    }

    /**
     * 读取linux文件
     * 获取链接的设备IP
     * */
    public static ArrayList<String> getConnectedIP() {// 获取连接在服务器上的所有设备
        ArrayList<String> connectedIP = new ArrayList<String>();
        // 读取Linux文件系统中的某个文件

        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }

}
