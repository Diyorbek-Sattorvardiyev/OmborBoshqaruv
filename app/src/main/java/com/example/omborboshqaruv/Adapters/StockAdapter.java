package com.example.omborboshqaruv.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.text.DecimalFormat;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omborboshqaruv.Models.StockItem;
import com.example.omborboshqaruv.R;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> implements Filterable {

    private final List<StockItem> originalList;
    private List<StockItem> filteredList;

    public StockAdapter(List<StockItem> stockList) {
        this.originalList = stockList;
        this.filteredList = new ArrayList<>(stockList);
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        StockItem item = filteredList.get(position);
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        holder.tvName.setText(item.getProduct_name());
        holder.tvUnit.setText("O'lchov birligi: " + item.getUnit());
        holder.tvCurrentStock.setText("Qoldiq: " + item.getCurrent_stock());
        holder.tvMinStock.setText("Min: " + item.getMin_stock());
        holder.tvValue.setText("Qiymati: " + formatter.format(item.getStock_value()) + " so'm");

        if (item.isLow_stock()) {
            holder.tvWarning.setText("⚠ Kam qoldi!");
            holder.tvWarning.setVisibility(View.VISIBLE);
        } else {
            holder.tvWarning.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<StockItem> filtered = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filtered.addAll(originalList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (StockItem item : originalList) {
                        if (item.getProduct_name().toLowerCase().contains(filterPattern)) {
                            filtered.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUnit, tvCurrentStock, tvMinStock, tvValue, tvWarning;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStockName);
            tvUnit = itemView.findViewById(R.id.tvStockUnit);
            tvCurrentStock = itemView.findViewById(R.id.tvStockCurrent);
            tvMinStock = itemView.findViewById(R.id.tvStockMin);
            tvValue = itemView.findViewById(R.id.tvStockValue);
            tvWarning = itemView.findViewById(R.id.tvStockWarning);
        }
    }
}
