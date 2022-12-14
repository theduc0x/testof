package com.example.testonoff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.testonoff.models.ACDetail;

import java.util.ArrayList;

public class EditRemoteActivity extends AppCompatActivity {
    EditText etNameDevice;
    AppCompatButton btPaired;
    String fragment = "";
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remote);
        etNameDevice = findViewById(R.id.et_device_name);
        btPaired = findViewById(R.id.bt_paired);
        ivBack = findViewById(R.id.iv_back_select);
        getDataIntent();
        MainActivity.showListRemoteDetail(fragment);

        btPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toActivityControl =
                        new Intent(EditRemoteActivity.this, ControlsActivity.class);
                startActivity(toActivityControl);
            }
        });

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            fragment = intent.getStringExtra("KEY");
        }
    }
}