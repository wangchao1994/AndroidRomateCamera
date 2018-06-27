package com.android.librarycamera.materialcamera.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SendDataThread extends  Thread{
    private String address;
    private String msg;
    private Socket socket = null;
    public SendDataThread(String address,String msg){
        this.address = address;
        this.msg = msg;
    }
    @Override
    public void run() {
        PrintWriter printWriter = null;
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
            try {
                socket = new Socket(address,12345);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (msg != null){
            try {
                 outputStream = socket.getOutputStream();
                 outputStreamWriter = new OutputStreamWriter(outputStream);
                 printWriter = new PrintWriter(outputStreamWriter);
                printWriter.write(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (printWriter != null){
                    printWriter.close();
                }
                if (outputStreamWriter != null){
                    try {
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
