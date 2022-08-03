package com.example.testonoff.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testonoff.R;
import com.example.testonoff.models.ACDetail;
import com.example.testonoff.my_interface.IItemOnClickPowerTest;
import com.example.testonoff.my_interface.IItemOnClickYesNoWorking;

import java.util.ArrayList;

public class PowerTestAdapter extends RecyclerView.Adapter<PowerTestAdapter.PowerViewHolder> {
    private ArrayList<ACDetail> listAC;
    private ArrayList<String> listNameDevice;
    IItemOnClickPowerTest onClickPowerTest;
    IItemOnClickYesNoWorking onClickYesNoWorking;

    public PowerTestAdapter(IItemOnClickPowerTest onClickPowerTest,
                            IItemOnClickYesNoWorking onClickYesNoWorking) {
        this.onClickPowerTest = onClickPowerTest;
        this.onClickYesNoWorking = onClickYesNoWorking;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<ACDetail> listDevice , ArrayList<String> listNameDevice) {
        this.listAC = listDevice;
        this.listNameDevice = listNameDevice;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_power_test, parent, false);
        return new PowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PowerViewHolder holder, int position) {
        holder.ivTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.setVisibleButton();
                onClickPowerTest.onClickPower();
            }
        });
        holder.btNotW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickYesNoWorking.onClickNoWorking();
            }
        });
        holder.btW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickYesNoWorking.onClickYesWorking();
            }
        });
    }


    @Override
    public int getItemCount() {
        if (listNameDevice != null) {
            return listNameDevice.size();
        }
        return 0;
    }

    class PowerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTest;
        AppCompatButton btNotW, btW;
        public PowerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTest = itemView.findViewById(R.id.iv_power);
            btNotW = itemView.findViewById(R.id.bt_not_working);
            btW = itemView.findViewById(R.id.bt_working);
        }
        private void setVisibleButton() {
            btW.setVisibility(View.VISIBLE);
            btNotW.setVisibility(View.VISIBLE);
        }
    }
}
