package com.android.romatecamera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.librarycamera.materialcamera.MaterialCamera;
import com.android.romatecamera.wifi.HPSetup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RomateMainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_settings;
    private Button bt_cloud;
    private Button bt_control;
    private static final int CAMERA_RQ = 6969;
    private static final int PERMISSION_RQ = 84;
    private  boolean flag = false;//wifihot
    private String password = "12345678";
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romate_main);
        initView();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(
                    this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }
    }

    private void initView() {
        bt_settings = findViewById(R.id.bt_settings);
        bt_cloud = findViewById(R.id.bt_cloud);
        bt_control = findViewById(R.id.bt_control);
        bt_control.setOnClickListener(this);
        bt_cloud.setOnClickListener(this);
        bt_settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.bt_settings:
                flag = !flag;
                HPSetup hpSetup = HPSetup.getInstance(this);
                try {
                    hpSetup.setupWifiAp(Build.MODEL,password,flag);
                } catch (Exception e) {
                    Log.d("wchao","HPsetup error="+e.getMessage());
                    e.printStackTrace();
                }
                break;
            case R.id.bt_cloud:
                if (flag){
                    MaterialCamera materialCamera =
                            new MaterialCamera(this)
                                    .showPortraitWarning(true)
                                 //   .allowRetry(true)
                                    .defaultToFrontFacing(false);
                    materialCamera.stillShot(); // launches the Camera in stillshot mode
                    materialCamera.start(CAMERA_RQ);
                }else{
                    Toast.makeText(this,"Please open wifiHot!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_control:
                File saveDir = null;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Only use external storage directory if permission is granted, otherwise cache directory is used by default
                    saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
                    saveDir.mkdirs();
                }
                MaterialCamera materialCamera =
                        new MaterialCamera(this)
                                .saveDir(saveDir)
                                .showPortraitWarning(true)
                                .allowRetry(true)
                                .defaultToFrontFacing(false);
                materialCamera.stillShot(); // launches the Camera in stillshot mode
                materialCamera.start(CAMERA_RQ);
                break;
                default:
                    break;
        }
    }

    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[] {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups))
                + " "
                + units[digitGroups];
    }

    private String fileSize(File file) {
        return readableFileSize(file.length());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == Activity.RESULT_OK) {
                final File file = new File(data.getData().getPath());
                Toast.makeText(
                        this,
                        String.format("Saved to: %s, size: %s", file.getAbsolutePath(), fileSize(file)),
                        Toast.LENGTH_LONG)
                        .show();
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Sample was denied WRITE_EXTERNAL_STORAGE permission
            Toast.makeText(
                    this,
                    "Videos will be saved in a cache directory instead of an external storage directory since permission was denied.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
