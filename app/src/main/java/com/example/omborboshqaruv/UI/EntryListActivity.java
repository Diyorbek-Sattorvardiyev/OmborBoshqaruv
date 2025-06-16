package com.example.omborboshqaruv.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omborboshqaruv.Adapters.EntryAdapter;
import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.Entry;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryListActivity extends AppCompatActivity {

    RecyclerView rvEntry;
    EditText etSearch;
    EntryAdapter entryAdapter;
    List<Entry> allEntries = new ArrayList<>();
    List<Entry> filteredEntries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);

        // Toolbar sozlamasi
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // UI elementlar
        rvEntry = findViewById(R.id.rvEtntry);
        etSearch = findViewById(R.id.etSearch);
        rvEntry.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        entryAdapter = new EntryAdapter(filteredEntries);
        rvEntry.setAdapter(entryAdapter);

        // Izlash funksiyasi
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEntries(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Ma'lumotlarni yuklash
        loadEntries();
    }

    private void loadEntries() {
        String token = "Bearer " + TokenManager.getToken(this);
        RetrofitClient.getApiService(this).getEntries(token)
                .enqueue(new Callback<List<Entry>>() {
                    @Override
                    public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allEntries.clear();
                            allEntries.addAll(response.body());
                            filterEntries(etSearch.getText().toString());
                        } else {
                            Toast.makeText(EntryListActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Entry>> call, Throwable t) {
                        Toast.makeText(EntryListActivity.this, "Ulanishda xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterEntries(String query) {
        filteredEntries.clear();
        for (Entry e : allEntries) {
            if (e.product_name != null && e.product_name.toLowerCase().contains(query.toLowerCase())) {
                filteredEntries.add(e);
            }
        }
        entryAdapter.notifyDataSetChanged();
    }
}
