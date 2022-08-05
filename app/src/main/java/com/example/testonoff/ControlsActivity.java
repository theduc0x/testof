package com.example.testonoff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    private ImageView ivBack;
    private Vibrator vibrator;
    private TextView tvDisPlayTemp, tvMode, tvSpeedFan;
    private TextView tvContainSpeed, tvContainMode, tvContainSwing;
    private FrameLayout flPowerOn, flPowerOff, flReduce, flIncrease, flMode, flSpeed;
    private HashMap<String, String> speedListMap;
    private HashMap<String, String> modeListMap;

    private int frequency = 0;
    private int[] pattern = {};
    private String fan = "";
    private String mode = "";
    private String temp = "26";
    private String maxTemp = "0";
    private String minTemp = "0";

    private int MODE_TYPE = 0;
    private int SPEED_TYPE = 0;
    private int TEMP_UP_DOWN = 0;
    // Xem nếu như button_fragment chứa T_ thì đó là device ko có mode, mà nó chỉ có chỉnh Nhiệt độ
    private boolean DEVICE_CONTAIN_T = false;

    private ArrayList<String> listSpeedDataHave;
    private ArrayList<String> listModeData;

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
        tvContainMode.setOnClickListener(v -> {
            transMode();
        });
        // Tốc đọ quạt
        tvContainSpeed.setOnClickListener(v -> {
            transSpeed();
        });
        // Giảm nhiệt đọ
        flReduce.setOnClickListener(v -> {
            tempRegulation(Utils.TEMP_REDUCE);
        });
        // Tăng nhiệt độ
        flIncrease.setOnClickListener(v -> {
            tempRegulation(Utils.TEMP_INCREASE);
        });

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void transPowerOn() {
        disPlayViewVisible();
        acDetail = getAcDetail(Utils.POWER_ON);
        getDataAndTrans();
    }

    private void transPowerOff() {
        disPlayViewInvisible();
        acDetail = getAcDetail(Utils.POWER_OFF);
        getDataAndTrans();
    }

    private void transMode() {
        disPlayViewVisible();
        if (tvMode.getText().toString().equals(Utils.MODE_COOLING_TEXT)) {
            tvMode.setText(Utils.MODE_HEAT_TEXT);
            mode = Utils.MODE_HEAT;
        } else {
            tvMode.setText(Utils.MODE_COOLING_TEXT);
            mode = Utils.MODE_COOL;
        }

        mode = getButtonFragment();
        acDetail = getAcDetail(mode);
        Log.d("DDDDMode", acDetail.getButton_fragment());
        getDataAndTrans();

    }

    private void transSpeed() {
        disPlayViewVisible();

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
        // Đây là trường hợp có speed nhưng chỉ có auto, không có mấy cái còn lại
        if (SPEED_TYPE == 1) {
            for (int i = 0; i < listSpeedDataHave.size(); i++) {
                if (fan.equals(listSpeedDataHave.get(i))) {
                    acDetail = getAcDetail(fan);
                    Log.d("DDDDFan", acDetail.getButton_fragment());
                    getDataAndTrans();
                }
            }
        // TH bình thường
        } else {
            for (int i = 0; i < listSpeedDataHave.size(); i++) {
                if (fan.equals(listSpeedDataHave.get(i))) {
                    fan = getButtonFragment();
                    acDetail = getAcDetail(fan);
                    Log.d("DDDDFan", acDetail.getButton_fragment());
                    getDataAndTrans();
                }
            }
        }

    }

    /**
     *
     * @param regulation là Tăng hoặc giảm
     */
    private void tempRegulation(String regulation) {
        disPlayViewVisible();
        int tempp = 0;
        if (regulation.equals(Utils.TEMP_INCREASE)) {
            if (temp.equals(maxTemp)) {
                return;
            }
            if (TEMP_UP_DOWN == 1) {
                TEMP_UP_DOWN = 2;
            }
            tempp = Integer.parseInt(temp) + 1;
        } else {
            if (temp.equals(minTemp)) {
                return;
            }
            if (TEMP_UP_DOWN == 1) {
                TEMP_UP_DOWN = 3;
            }
            tempp = Integer.parseInt(temp) - 1;
        }
        temp = String.valueOf(tempp);
        tvDisPlayTemp.setText(temp);
        String button_fragment = getButtonFragment();
        Log.d("DDDDD44", button_fragment);
        acDetail = getAcDetail(button_fragment);
        getDataAndTrans();

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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView() {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        flPowerOn = findViewById(R.id.fl_contain_power_on);
        flPowerOff = findViewById(R.id.fl_contain_power_off);
        tvContainMode = findViewById(R.id.tv_contain_mode);
        flReduce = findViewById(R.id.fl_contain_reduce_temp);
        flIncrease = findViewById(R.id.fl_contain_increased_temp);
        flSpeed = findViewById(R.id.fl_contain_speed);
        flMode = findViewById(R.id.fl_contain_mode);
        tvContainSpeed = findViewById(R.id.tv_contain_speed);
        tvContainSwing = findViewById(R.id.tv_contain_swing);
        ivBack = findViewById(R.id.iv_back_home);

        clDisPlayView = findViewById(R.id.cl_display_view);
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



        listModeData = new ArrayList<>();

        maxTemp = "20";
        minTemp = "20";

        listSpeedDataHave = new ArrayList<>();
        // Xem với loại điều khiển này thì có những chức năng gì có trong database
        for (int i = 0; i < listRemoteAc.size(); i++) {
            // Xem trong device này có những điều chỉnh quạt nào, như auto, 1, 2, 3
            for (int j = 0; j < Utils.listSpeedData.size(); j++) {
                if (listRemoteAc.get(i).getButton_fragment().equals(Utils.listSpeedData.get(j))) {
                    listSpeedDataHave.add(Utils.listSpeedData.get(j));
                }
            }
            // Xem trong device này có những loại mode nào, như mode cool, heat, ..
            for (int k = 0; k < Utils.listModeData.size(); k++) {
                if (listRemoteAc.get(i).getButton_fragment().equals(Utils.listModeData.get(k))) {
                    listModeData.add(Utils.listModeData.get(k));
                }
            }
        }
        /**
         * SPEED_TYPE:
         * 1: Có fan nhưng chỉ có auto, không có chế độ khác
         * 2: Có đủ các loại fan Auto, 1, 2, 3
         * 3: Chỉ có chế đọ fan speed riêng mà không kết hợp đc với temp + mode, ví dụ nhưn FAN_AUTO
         * 4: Không có bất kỳ chế độ speed nào
         */


        for (int i = 0; i < listRemoteAc.size(); i++) {
            ACDetail ac = listRemoteAc.get(i);
            if (ac.isSpeedFanExists()) {
                SPEED_TYPE = 1;
               if (ac.isSpeedFanFull()) {
                   SPEED_TYPE = 2;
                   break;
               }
            } else if (ac.isSpeedFan() && SPEED_TYPE == 0) {
                SPEED_TYPE = 3;
            }
        }

        /**
         * MODE_TYPE
         * 1: Có mode nhưng là các mode chế độ riêng, không kết hợp
         * 2: Có đủ các loại mode kết hợp với fan + temp
         * 3: Không có chọn chế độ mode
         */

        for (int i = 0; i < listRemoteAc.size(); i++) {
            ACDetail ac = listRemoteAc.get(i);
            if (ac.isMode()) {
                MODE_TYPE = 1;
            } else {
                MODE_TYPE = 3;
            }
            if (ac.isModeFull()) {
                MODE_TYPE = 2;
                break;
            }
        }

        for (ACDetail ac : listRemoteAc) {
            if (ac.isTempUpDown()) {
                TEMP_UP_DOWN = 1;
            }
        }

        Log.d("DDDDCHECK_SPEED", SPEED_TYPE+"");
        Log.d("DDDDCHECK_MODE", MODE_TYPE+"");
        Log.d("DDDDK", listModeData.size()+"");
        /**
         * Có 2 device, 1 loại chỉ có chỉnh nhiệt độ, 1 loại chỉnh nhiệt độ và cả speed, mode
         * Nếu như chỉ điều chỉnh nhiệt độ thì sẽ chạy speed, mode về ban đầu,
         * Nếu DEVICE_CONTAIN_T = true thì có nghĩa là chỉ có điều chỉnh nhiệt độ thôi
         */
        for (int i = 0; i < listRemoteAc.size(); i++) {
            DEVICE_CONTAIN_T = listRemoteAc.get(i).isButtonFragmentContainT_();
            if (DEVICE_CONTAIN_T) {
                break;
            }
        }
        /**
         * Tìm xem TempMax và TempMin
         */
        for (int i = 0; i < listRemoteAc.size(); i++) {
            if (!listRemoteAc.get(i).getTemp().equals("")) {
                if (Integer.parseInt(maxTemp) < Integer.parseInt(listRemoteAc.get(i).getTemp())) {
                    maxTemp = listRemoteAc.get(i).getTemp();
                }
                if (Integer.parseInt(minTemp) > Integer.parseInt(listRemoteAc.get(i).getTemp())) {
                    minTemp = listRemoteAc.get(i).getTemp();
                }
            }
        }


        Log.d("DDDDDDevice_contain_t", DEVICE_CONTAIN_T+"");

        setDataEntry();

        // Nếu không có mode hoặc speed thì sẽ ko cho dùng
        // Nếu không có danh sách speed  nhưng tồn tại SPEED trong database
        if (listSpeedDataHave.size() == 0 && (SPEED_TYPE == 0 || SPEED_TYPE == 3)) {
            tvContainSpeed.setEnabled(false);
            flSpeed.setBackground(getResources().getDrawable(R.color.enable_false));
        }
        // Nếu list không có mode nhưng trong database lại có thì vẫn chạy đc
        if (listModeData.size() == 0 && MODE_TYPE == 3) {
            tvContainMode.setEnabled(false);
            flMode.setBackground(getResources().getDrawable(R.color.enable_false));
        }
    }

    // Đặt dữ liệu ban đầu, để Mode là Cool và fan là auto
    private void setDataEntry() {
        tvMode.setText(Utils.MODE_COOLING_TEXT);
        tvSpeedFan.setText(Utils.SPEED_AUTO);
    }

    /**
     *
     * @return trả về tên nút cần tìm, sau đó sẽ gọi getAcDetail để lấy các nội dung chi tiết
     */
    private String getButtonFragment() {
        String name = "";
        if (DEVICE_CONTAIN_T) {
            name = "T_" + temp;
        // Đây là TH mà remote chỉ có mỗi điều khiển auto mà ko có mức fan khác
        } else  if (SPEED_TYPE == 1) {
            name = temp + "_F_";
            name +=  "A_";
            name += modeListMap.get(tvMode.getText().toString().trim());
        } else if (TEMP_UP_DOWN == 2) {
            name = "TempUp";
            TEMP_UP_DOWN = 1;
        } else if (TEMP_UP_DOWN == 3) {
            name = "TempDown";
            TEMP_UP_DOWN = 1;
        }
        else {
            name = temp + "_F_";
            name += speedListMap.get(tvSpeedFan.getText().toString().trim()) + "_";
            name += modeListMap.get(tvMode.getText().toString().trim());
        }

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
     * list RemoteAc là danh sách các nút như PowerOn, ....
     * @param button_fragment là tên tên nút cần tìm (ví dụ PowerOn)
     * @return trả về là 1 ACDetail là chi tiết của 1 nút
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
        vibrator.vibrate(100);
    }

    // Ẩn màn hình hiển thị
    private void disPlayViewInvisible() {
        clDisPlayView.setVisibility(View.INVISIBLE);
        vibrator.vibrate(100);
    }
}