package com.example.testonoff;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testonoff.models.ACDetail;

import java.util.ArrayList;

public class PowerActivity extends AppCompatActivity {
    private ArrayList<ACDetail> listAC;
    TextView tvNameDevice;
    ImageView ivPower, ivPrev, ivNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);
        initView();
        Log.d("AAA", listAC.get(1).getFragment());
    }
    private void initView() {
        tvNameDevice = findViewById(R.id.tv_name_device);
        ivPower = findViewById(R.id.iv_power);
        ivPrev = findViewById(R.id.iv_back);
        ivNext = findViewById(R.id.iv_next);
        listAC = MainActivity.listAC;
        tvNameDevice.setText(listAC.get(0).getFragment());
    }
}