//package com.example.testonoff;
//
//import android.annotation.SuppressLint;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class PowerTestAdapter extends RecyclerView.Adapter<PowerTestAdapter.PowerViewHolder> {
//    private ArrayList<String> listDevice;
//
//    @SuppressLint("NotifyDataSetChanged")
//    public void setData(ArrayList<String> listDevice) {
//        this.listDevice = listDevice;
//        notifyDataSetChanged();
//    }
//    @NonNull
//    @Override
//    public PowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_power_test, parent, false);
//        return new PowerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PowerViewHolder holder, int position) {
//        String device = listDevice.get(position);
//        if (device == null) {
//            return;
//        }
//        holder.ivTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        if (listDevice != null) {
//            return listDevice.size();
//        }
//        return 0;
//    }
//
//    class PowerViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivTest;
//        public PowerViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ivTest = itemView.findViewById(R.id.iv_power);
//        }
//    }
//}
