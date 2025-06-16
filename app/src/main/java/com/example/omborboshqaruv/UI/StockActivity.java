package com.example.omborboshqaruv.UI;

import android.os.Bundle;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omborboshqaruv.Adapters.StockAdapter;
import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.StockItem;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StockAdapter adapter;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_stock);

            recyclerView = findViewById(R.id.recyclerStock);
            EditText etSearch1 = findViewById(R.id.etSearch1);

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 ustun

            loadStock();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());

            etSearch1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s); // to'g'ri nom
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {}
            });
        }

        private void loadStock() {
            String token = TokenManager.getToken(this);
            RetrofitClient.getApiService(this).getStock("Bearer " + token)
                    .enqueue(new Callback<List<StockItem>>() {
                        @Override
                        public void onResponse(Call<List<StockItem>> call, Response<List<StockItem>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                adapter = new StockAdapter(response.body());
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(StockActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<StockItem>> call, Throwable t) {
                            Toast.makeText(StockActivity.this, "Ulanishda xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

