package com.example.omborboshqaruv.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omborboshqaruv.Models.Expense;
import com.example.omborboshqaruv.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvCategory.setText(expense.getCategory());
        holder.tvAmount.setText(expense.getAmount() + " so'm");
        holder.tvDate.setText(expense.getExpense_date());
        holder.tvDescription.setText(expense.getDescription());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvAmount, tvDate, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.textCategory);
            tvAmount = itemView.findViewById(R.id.textAmount);
            tvDate = itemView.findViewById(R.id.textDate);
            tvDescription = itemView.findViewById(R.id.textDesc);
        }
    }
}
