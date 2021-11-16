package com.order.gstcalculator;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TableAMTAdapter extends RecyclerView.Adapter<TableAMTAdapter.ViewHolder> {

    private final ArrayList<ConstructorAmount> constructorAmount;
    private final ItemListener listener;

    public interface ItemListener{
        void onLongItemCLick(int index);
    }

    TableAMTAdapter (ItemListener listener, ArrayList<ConstructorAmount> list) {
        this.listener = listener;
        this.constructorAmount = list;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPrimaryId, tvQuantity, tvListPrice, tvDisc, tvPrice, tvTaxable, tvTaxRate, tvGST, tvTotal;


        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvPrimaryId = itemView.findViewById(R.id.tvPrimaryId);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvListPrice = itemView.findViewById(R.id.tvListPrice);
            tvDisc = itemView.findViewById(R.id.tvDisc);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTaxable = itemView.findViewById(R.id.tvTaxable);
            tvTaxRate = itemView.findViewById(R.id.tvTaxRate);
            tvGST = itemView.findViewById(R.id.tvGST);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }

    @NonNull
    @Override
    public TableAMTAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvtableamt, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAMTAdapter.ViewHolder holder, final int position) {
        holder.tvPrimaryId.setText(String.valueOf(constructorAmount.get(position).getId()));
        holder.tvQuantity.setText(constructorAmount.get(position).getQuantity());
        holder.tvListPrice.setText(constructorAmount.get(position).getListPrice());
        holder.tvDisc.setText(constructorAmount.get(position).getDiscount());
        holder.tvPrice.setText(constructorAmount.get(position).getPrice());
        holder.tvTaxable.setText(constructorAmount.get(position).getTaxableValue());
        holder.tvTaxRate.setText(constructorAmount.get(position).getTaxRate());
        holder.tvGST.setText(constructorAmount.get(position).getGST());
        holder.tvTotal.setText(constructorAmount.get(position).getTotalAmount());

        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongItemCLick(constructorAmount.get(position).getId());
            return true;
        });



    }

    @Override
    public int getItemCount() {
        return constructorAmount.size();
    }

}
