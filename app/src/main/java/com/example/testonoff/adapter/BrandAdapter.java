package com.example.testonoff.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testonoff.R;
import com.example.testonoff.my_interface.IItemOnClickOpenBrand;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {
    private ArrayList<String> listBrand;
    IItemOnClickOpenBrand onClickOpenBrand;

    public BrandAdapter(IItemOnClickOpenBrand onClickOpenBrand) {
        this.onClickOpenBrand = onClickOpenBrand;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<String> listBrand) {
        this.listBrand = listBrand;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_brand, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        String brand = listBrand.get(position);
        if (brand == null) {
            return;
        }
        holder.setTitleBrand(brand);
        holder.llOpenBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOpenBrand.onClickOpenBrand(brand);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listBrand != null) {
            return listBrand.size();
        }
        return 0;
    }

    class BrandViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llOpenBrand;
        private TextView tvBrand;
        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            llOpenBrand = itemView.findViewById(R.id.ll_open_test);
            tvBrand = itemView.findViewById(R.id.tv_title_brand);
        }
        public void setTitleBrand(String titleBrand) {
            tvBrand.setText(titleBrand);
        }
    }
}
