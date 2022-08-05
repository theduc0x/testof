package com.example.testonoff.models;

import com.example.testonoff.Util.Utils;

public class ACDetail {
    private String fragment;
    private String button_fragment;
    private String brand;
    private String frequency;
    private String main_frame;

    public ACDetail(String fragment, String button_fragment,
                    String brand, String frequency, String main_frame) {
        this.fragment = fragment;
        this.button_fragment = button_fragment;
        this.brand = brand;
        this.frequency = frequency;
        this.main_frame = main_frame;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getButton_fragment() {
        return button_fragment;
    }

    public void setButton_fragment(String button_fragment) {
        this.button_fragment = button_fragment;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getMain_frame() {
        return main_frame;
    }

    public void setMain_frame(String main_frame) {
        this.main_frame = main_frame;
    }


    /**
     *
     * @return true nếu như nút button chứa T_, có nghĩa là nó chỉ điều chỉnh nhiệt độ mà thôi
     */
    public boolean isButtonFragmentContainT_() {
        return button_fragment.contains("T_");
    }

    /**
     *
     * @return trả về nhiệt độ
     */
    public String getTemp() {
        String temp = "";
        if (button_fragment.contains("T_")) {
            temp = button_fragment.substring(2, 4);
        } else if (button_fragment.contains("_F_")) {
            temp = button_fragment.substring(0, 2);
        } else {
            temp = "";
        }
        return temp;
    }

    /**
     * @return true nếu có tồn tại chỉnh quạt speed, false nếu không có
     */
    public boolean isSpeedFanExists() {
        return (button_fragment.contains("_F_") || button_fragment.contains("_S_"));
    }

    /**
     * @return true nếu có tồn tại cả 4 loại speed A, 1, 2, 3
     */
    public boolean isSpeedFanFull() {
        return (button_fragment.contains("_F_1_") || button_fragment.contains("_S_1_"))
                || (button_fragment.contains("_F_2_") || button_fragment.contains("_S_2_"))
                || (button_fragment.contains("_F_3_") || button_fragment.contains("_S_3_"))
                || (button_fragment.contains("_F_A_") || button_fragment.contains("_S_A_"));
    }

    /**
     * @return true nếu tồn tại các Fan Speed riêng như FAN_AUTO, FAN_LOW, ....
     */
    public boolean isSpeedFan() {
        for (int i = 0; i < Utils.listSpeedData.size(); i++) {
            return button_fragment.equalsIgnoreCase(Utils.listSpeedData.get(i));
        }
        return true;
    }

    /**
     * @return true nếu có chế độ mode tổng hợp gồm temp + fan + mode
     */
    public boolean isModeFull() {
        return (button_fragment.contains("_H") || button_fragment.contains("_C"));
    }

    /**
     * @return true nếu như tồn tại các Mode riêng như MODE_COOL, MODE_HEAT,...
     */
    public boolean isMode() {
        for (int i = 0; i < Utils.listModeData.size(); i++) {
            return button_fragment.equalsIgnoreCase(Utils.listModeData.get(i));
        }
        return true;
    }

    public boolean isTempUpDown() {
        return button_fragment.contains("TempUp") || button_fragment.contains("TempDown");
    }
}
