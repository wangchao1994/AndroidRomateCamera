package com.android.librarycamera.materialcamera.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendBitmapThread extends Thread{

    private Socket socket;
    private String address;
    private byte [] data = null;

    public SendBitmapThread(String addres, byte[] data) {
        this.address = addres;
        this.data = data;
    }
    @Override
    public void run() {
        OutputStream outputStream  = null;
        try {
            socket = new Socket(address,12346);
            if (data != null){
                outputStream = socket.getOutputStream();
                outputStream.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
