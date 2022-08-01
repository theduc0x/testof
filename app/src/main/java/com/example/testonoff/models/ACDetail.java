package com.example.testonoff.models;

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
}
