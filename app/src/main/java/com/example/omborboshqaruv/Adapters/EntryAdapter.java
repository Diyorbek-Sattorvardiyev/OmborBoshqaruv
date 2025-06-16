package com.example.omborboshqaruv.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omborboshqaruv.Models.Entry;
import com.example.omborboshqaruv.R;

import java.text.DecimalFormat;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    List<Entry> entryList;

    public EntryAdapter(List<Entry> entryList) {
        this.entryList = entryList;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry entry = entryList.get(position);
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        holder.productName.setText(entry.product_name);
        holder.quantity.setText("Soni: " + entry.quantity);
        holder.unitPrice.setText("Narxi: " + entry.unit_price + " so'm");
        holder.totalAmount.setText("Jami: " + formatter.format(entry.total_amount) + " so'm");
        holder.date.setText("Sana: " + entry.entry_date);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView productName, quantity, unitPrice, totalAmount, date;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.txtProductName);
            quantity = itemView.findViewById(R.id.txtQuantity);
            unitPrice = itemView.findViewById(R.id.txtUnitPrice);
            totalAmount = itemView.findViewById(R.id.txtTotalAmount);
            date = itemView.findViewById(R.id.txtEntryDate);
        }
    }
}

