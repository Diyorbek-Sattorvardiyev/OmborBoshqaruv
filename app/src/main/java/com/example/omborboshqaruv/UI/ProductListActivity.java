package com.example.omborboshqaruv.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omborboshqaruv.Adapters.ProductAdapter;
import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.Product;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView rvProducts;
    EditText etSearch;
    List<Product> productList = new ArrayList<>();
    List<Product> filteredList = new ArrayList<>();
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list);

        rvProducts = findViewById(R.id.rvProducts);
        etSearch = findViewById(R.id.etSearch);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        // Qidiruv bo‘yicha real-time filter
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.backraund));

        String token = TokenManager.getToken(this);
        RetrofitClient.getApiService(this)
                .getProducts("Bearer " + token)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            productList = response.body();
                            filteredList = new ArrayList<>(productList);

                            adapter = new ProductAdapter(ProductListActivity.this, filteredList, new ProductAdapter.OnItemActionListener() {
                                @Override
                                public void onEdit(Product product) {
                                    Toast.makeText(ProductListActivity.this, "Tahrirlash: " + product.getName(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onDelete(Product product) {
                                    new android.app.AlertDialog.Builder(ProductListActivity.this)
                                            .setTitle("Mahsulotni o‘chirish")
                                            .setMessage(product.getName() + " mahsulotni o‘chirishni istaysizmi?")
                                            .setPositiveButton("Ha", (dialog, which) -> deleteProductFromServer(product))
                                            .setNegativeButton("Yo‘q", null)
                                            .show();
                                }
                            });

                            rvProducts.setAdapter(adapter);
                        } else {
                            Toast.makeText(ProductListActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Toast.makeText(ProductListActivity.this, "Ulanishda xatolik: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });




    }

    private void filterProducts(String query) {
        if (adapter == null) return;

        filteredList.clear();
        for (Product p : productList) {
            if (p.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void deleteProductFromServer(Product product) {
        String token = TokenManager.getToken(this);
        if (token == null) {
            Toast.makeText(this, "Token mavjud emas. Avval login qiling.", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService(this)
                .deleteProduct("Bearer " + token, product.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProductListActivity.this, "Mahsulot o‘chirildi", Toast.LENGTH_SHORT).show();


                            productList.remove(product);
                            filteredList.remove(product);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ProductListActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ProductListActivity.this, "Tarmoq xatosi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
