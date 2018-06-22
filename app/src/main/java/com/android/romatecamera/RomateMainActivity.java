package com.android.romatecamera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RomateMainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_settings;
    private Button bt_cloud;
    private Button bt_control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romate_main);
        initView();
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
                break;
            case R.id.bt_cloud:
                Intent intent_support = new Intent(this, FragmentActivity.class);
                intent_support.putExtra("support", true);
                startActivity(intent_support);
                break;
            case R.id.bt_control:
                Intent intent = new Intent(this, FragmentActivity.class);
                intent.putExtra("support", true);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
