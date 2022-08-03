package com.example.testonoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.Log;

import com.example.testonoff.Util.Utils;
import com.example.testonoff.adapter.PowerTestAdapter;
import com.example.testonoff.models.ACDetail;
import com.example.testonoff.my_interface.IItemOnClickPowerTest;
import com.example.testonoff.my_interface.IItemOnClickYesNoWorking;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class PowerActivity extends AppCompatActivity {
    private ArrayList<ACDetail> listAC;
    ArrayList<String> listNameDevice;
    TabLayout tlDevice;
    ViewPager2 vp2Power;
    PowerTestAdapter adapter;
    ConsumerIrManager irManager;

    ACDetail acDetail = null;

    String deviceACCurrent = "";
    int acCurrentPos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);
        initView();
        for (int i = 0; i < listAC.size(); i++) {
            Log.d("AAA", listAC.get(i).getFragment());
            Log.d("AAA", listAC.get(i).getButton_fragment());
        }

    }
    private void initView() {
        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        tlDevice = findViewById(R.id.tl_device);
        vp2Power = findViewById(R.id.vp2_power);

        listAC = MainActivity.listAC;
        listNameDevice = MainActivity.listNameDevice;

        adapter = new PowerTestAdapter(new IItemOnClickPowerTest() {
            @Override
            // Khi click PowerOn hoặc Off
            public void onClickPower() {
                // nếu vị trí tab mh đang ở là chẵn lẻ thì sẽ gọi PowerOn hoặc PowerOff
                if (acCurrentPos % 2 == 0) {
                    sendPowerOnOrOff("PowerOn", "Power");
                } else {
                    sendPowerOnOrOff("PowerOff", "Power");
                }
            }
        }, new IItemOnClickYesNoWorking() {
            @Override
            // Khi click vào không hoạt động
            public void onClickNoWorking() {
                nextPage();
            }

            @Override
            // Khi click vào hoạt động tốt
            public void onClickYesWorking() {
                // Nếu người dùng nhấn hoạt động tốt thì chuyển sang activity điều khiển;
                openEditActivity(deviceACCurrent);
            }
        });
        // Thiết bị hiện tại tên là gì
        deviceACCurrent = listNameDevice.get(1);

        vp2Power.setAdapter(adapter);
        vp2Power.setOffscreenPageLimit(20);
        adapter.setData(listAC, listNameDevice);

        new TabLayoutMediator(tlDevice, vp2Power, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                for (int i = 0; i < listNameDevice.size(); i++) {
                    if (position == i) {
                        tab.setText(listNameDevice.get(i));
                    }
                }
            }
        }).attach();

        onTabSelect();
    }


    private void openEditActivity(String fragment) {
        Intent toEditActivity = new Intent(PowerActivity.this, EditRemoteActivity.class);
        toEditActivity.putExtra("KEY", fragment);
        startActivity(toEditActivity);
    }

    private void nextPage() {
        int pos = vp2Power.getCurrentItem();
        int s = listNameDevice.size();
        if (pos == s) {
            return;
        }
        vp2Power.setCurrentItem(pos + 1);
    }

    // Khi chuyển tab thì ta sẽ ghi lại dữ liệu của tên tab hiện tại và vị trí của tab
    private void onTabSelect() {
        tlDevice.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                acCurrentPos = tab.getPosition();
                deviceACCurrent = (String) tab.getText();
                Log.d("DDDDTen", tab.getText()+"");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void sendPowerOnOrOff(String button_fragment1, String button_fragment2) {
        MainActivity.showACDetail(deviceACCurrent, button_fragment1,
                button_fragment2);
        acDetail = MainActivity.acDetail;
        int frequency = Integer.parseInt(acDetail.getFrequency());
        int[] pattern = Utils.convertStringToArrayInt(acDetail.getMain_frame());
        if (Utils.hasIrEmitter(irManager)) {
            Utils.sendDataIR(irManager, frequency, pattern);
        }
    }



}