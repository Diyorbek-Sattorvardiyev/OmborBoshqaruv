package com.example.omborboshqaruv.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.omborboshqaruv.Models.Product;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.UI.PraductDetailActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> productList;

    // Interface orqali edit/delete bosilganda ishlash uchun:
    public interface OnItemActionListener {
        void onEdit(Product product);
        void onDelete(Product product);
    }

    private final OnItemActionListener listener;

    public ProductAdapter(Context context, List<Product> productList, OnItemActionListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_praduct, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product p = productList.get(position);

        holder.tvName.setText(p.getName());
        holder.tvPrice.setText("Narxi: " + p.getSelling_price() + " so‘m");
        holder.tvDate.setText(p.getExpiry_date() != null ? p.getExpiry_date() : "----");


        if (p.getImage_url() != null && !p.getImage_url().isEmpty()) {
            Glide.with(context).load(p.getImage_url()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.img_2); // default image
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PraductDetailActivity.class);
            intent.putExtra("product_id", p.getId());
            context.startActivity(intent);
        });



        holder.btnEdit.setOnClickListener(v -> listener.onEdit(p));


        holder.btnDelete.setOnClickListener(v -> listener.onDelete(p));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName, tvPrice, tvDate;
        CardView btnEdit, btnDelete;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mahsulotRasmItem);
            tvName = itemView.findViewById(R.id.textMashulotNomiItem);
            tvPrice = itemView.findViewById(R.id.textMahsulotNarxiItem);
            tvDate = itemView.findViewById(R.id.textMahsulotQoshilganSana);
            btnEdit = itemView.findViewById(R.id.editButtonCard);
            btnDelete = itemView.findViewById(R.id.deleteButtonCard);
        }
    }
}
