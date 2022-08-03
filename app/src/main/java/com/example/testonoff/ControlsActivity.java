package com.example.testonoff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.testonoff.Util.Utils;
import com.example.testonoff.models.ACDetail;

import java.util.ArrayList;
import java.util.HashMap;

public class ControlsActivity extends AppCompatActivity {
    private ConstraintLayout clDisPlayView;
    private ArrayList<ACDetail> listRemoteAc;
    private ACDetail acDetail;
    private TextView tvNameDevice;
    private TextView tvDisPlayTemp, tvMode, tvSpeedFan;
    private FrameLayout flPowerOn, flPowerOff, flMode, flReduce, flIncrease, flSpeed;
    private HashMap<String, String> speedListMap;
    private HashMap<String, String> modeListMap;

    private int frequency = 0;
    private int[] pattern = {};
    private String fan = "";
    private String mode = "";
    private String temp = "26";

    private ArrayList<String> listSpeedData;
    private ArrayList<String> listSpeedDataHave;

    ConsumerIrManager irManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);
        initView();
        setTextDevice();
        // Nút bật
        flPowerOn.setOnClickListener(v -> {
            transPowerOn();
        });
        // Nút tắt
        flPowerOff.setOnClickListener(v -> {
            transPowerOff();
        });
        // Chế độ
        flMode.setOnClickListener(v -> {
            transMode();
        });
        // Tốc đọ quạt
        flSpeed.setOnClickListener(v -> {
            transSpeed();
        });
        // Giảm nhiệt đọ
        flReduce.setOnClickListener(v -> {
            transReduceTemp();
        });
        // Tăng nhiệt độ
        flIncrease.setOnClickListener(v -> {
            transIncreaseTemp();
        });

    }

    private void transPowerOn() {
        acDetail = getAcDetail(Utils.POWER_ON);
        getDataAndTrans();
        disPlayViewVisible();
    }

    private void transPowerOff() {
        acDetail = getAcDetail(Utils.POWER_OFF);
        getDataAndTrans();
        disPlayViewInvisible();
    }

    private void transMode() {
        if (tvMode.getText().toString().equals(Utils.MODE_COOLING_TEXT)) {
            tvMode.setText(Utils.MODE_HEAT_TEXT);
            mode = Utils.MODE_HEAT;
        } else {
            tvMode.setText(Utils.MODE_COOLING_TEXT);
            mode = Utils.MODE_COOL;
        }
        mode = getButtonFragment();

        acDetail = getAcDetail(mode);
        Log.d("Mode", acDetail.getMain_frame());
        getDataAndTrans();
        disPlayViewVisible();
    }

    private void transSpeed() {

        if (tvSpeedFan.getText().equals(Utils.SPEED_AUTO)) {
            tvSpeedFan.setText(Utils.SPEED_LOW);
            fan = Utils.FAN_LOW;
        } else if (tvSpeedFan.getText().equals(Utils.SPEED_LOW)){
            tvSpeedFan.setText(Utils.SPEED_MEDIUM);
            fan = Utils.FAN_MED;
        } else if (tvSpeedFan.getText().equals(Utils.SPEED_MEDIUM)){
            tvSpeedFan.setText(Utils.SPEED_HIGH);
            fan = Utils.FAN_HIGH;
        } else if (tvSpeedFan.getText().equals(Utils.SPEED_HIGH)) {
            tvSpeedFan.setText(Utils.SPEED_AUTO);
            fan = Utils.FAN_AUTO;
        }

        for (int i = 0; i < listSpeedDataHave.size(); i++) {
            if (fan.equals(listSpeedDataHave.get(i))) {
                Log.d("DDDDD4", fan);
                fan = getButtonFragment();
                acDetail = getAcDetail(fan);
                getDataAndTrans();
            }
        }
        disPlayViewVisible();
    }

    private void transReduceTemp() {
        int tempp = Integer.parseInt(temp) - 1;
        temp = String.valueOf(tempp);
        tvDisPlayTemp.setText(temp);
        String button_fragment = getButtonFragment();
        acDetail = getAcDetail(button_fragment);
        getDataAndTrans();
        Log.d("DDDDD44", button_fragment);
        disPlayViewVisible();
    }

    private void transIncreaseTemp() {
        int tempp = Integer.parseInt(temp) + 1;
        temp = String.valueOf(tempp);
        tvDisPlayTemp.setText(temp);
        String button_fragment = getButtonFragment();
        acDetail = getAcDetail(button_fragment);
        getDataAndTrans();
        Log.d("DDDDD44", button_fragment);

        disPlayViewVisible();
    }

    // Gán tên thiết bị
    private void setTextDevice() {
        tvNameDevice.setText(listRemoteAc.get(0).getFragment());
    }

    // lấy dữ liệu và gửi nó đi, lặp lại nhiều lần
    private void getDataAndTrans() {
        frequency = Integer.parseInt(acDetail.getFrequency());
        pattern = Utils.convertStringToArrayInt(acDetail.getMain_frame());
        transIRData(frequency, pattern);
    }

    private void initView() {
        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);

        listRemoteAc = MainActivity.listRemoteAC;

        Log.d("DDDD", listRemoteAc.size() + "");

        tvNameDevice = findViewById(R.id.tv_name_device);
        tvDisPlayTemp = findViewById(R.id.tv_display_temp);
        tvMode = findViewById(R.id.tv_mode);
        tvSpeedFan = findViewById(R.id.tv_speed_fan);

        speedListMap = new HashMap<>();
        speedListMap.put(Utils.SPEED_LOW, "1");
        speedListMap.put(Utils.SPEED_MEDIUM, "2");
        speedListMap.put(Utils.SPEED_HIGH, "3");
        speedListMap.put(Utils.SPEED_AUTO, "A");

        modeListMap = new HashMap<>();
        modeListMap.put(Utils.MODE_COOLING_TEXT, "C");
        modeListMap.put(Utils.MODE_HEAT_TEXT, "H");

        listSpeedData = new ArrayList<>();
        listSpeedData.add(Utils.FAN_LOW);
        listSpeedData.add(Utils.FAN_MED);
        listSpeedData.add(Utils.FAN_HIGH);
        listSpeedData.add(Utils.FAN_AUTO);

        listSpeedDataHave = new ArrayList<>();
        // Xem với loại điều khiển này thì có những chức năng gì có trong database
        for (int i = 0; i < listRemoteAc.size(); i++) {
            for (int j = 0; j < listSpeedData.size(); j++) {
                if (listRemoteAc.get(i).getButton_fragment().equals(listSpeedData.get(j))) {
                    listSpeedDataHave.add(listSpeedData.get(j));
                }
            }
        }
        setDataEntry();
        flPowerOn = findViewById(R.id.fl_contain_power_on);
        flPowerOff = findViewById(R.id.fl_contain_power_off);
        flMode = findViewById(R.id.fl_contain_mode);
        flReduce = findViewById(R.id.fl_contain_reduce_temp);
        flIncrease = findViewById(R.id.fl_contain_increased_temp);
        flSpeed = findViewById(R.id.fl_contain_speed);

        clDisPlayView = findViewById(R.id.cl_display_view);
    }

    // Đặt dữ liệu ban đầu
    private void setDataEntry() {
        tvMode.setText(Utils.MODE_COOLING_TEXT);
        tvSpeedFan.setText(Utils.SPEED_AUTO);
    }


    private String getButtonFragment() {
        String name = temp + "_F_";
        name += speedListMap.get(tvSpeedFan.getText().toString().trim()) + "_";
        name += modeListMap.get(tvMode.getText().toString().trim());
        return name;
    }

    /**
     * @param frequency là tần số để truyền
     * @param pattern   là dãy số cần truyền để điều hòa nhận tín hiệu
     */
    private void transIRData(int frequency, int[] pattern) {
        if (irManager.hasIrEmitter()) {
            irManager.transmit(frequency, pattern);
        }
    }

    /**
     * @param button_fragment là tên tên nút cần tìm (ví dụ PowerOn)
     * @return trả về là 1 ACDetail
     */
    private ACDetail getAcDetail(String button_fragment) {
        ACDetail acDetail1 = null;
        for (int i = 0; i < listRemoteAc.size(); i++) {
            if (listRemoteAc.get(i).getButton_fragment().equals(button_fragment)) {
                acDetail1 = listRemoteAc.get(i);
                break;
            }
        }
        return acDetail1;
    }

    // Hiển thị view khi nhấn
    private void disPlayViewVisible() {
        clDisPlayView.setVisibility(View.VISIBLE);
    }

    // Ẩn màn hình hiển thị
    private void disPlayViewInvisible() {
        clDisPlayView.setVisibility(View.INVISIBLE);
    }
    // Reload lại màn hình display
    private void loadDisPlay() {
        tvDisPlayTemp.setText(temp);
        tvMode.setText(mode);
        tvSpeedFan.setText(fan);
    }
}