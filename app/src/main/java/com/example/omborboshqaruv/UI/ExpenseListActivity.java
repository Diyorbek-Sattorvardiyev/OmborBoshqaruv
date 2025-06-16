package com.example.omborboshqaruv.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omborboshqaruv.Adapters.ExpenseAdapter;
import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.Expense;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseListActivity extends AppCompatActivity {

    RecyclerView rvExpense;
    EditText etSearch;
    ExpenseAdapter adapter;
    List<Expense> allExpenses = new ArrayList<>();
    List<Expense> filteredExpenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rvExpense = findViewById(R.id.rvExpense);
        etSearch = findViewById(R.id.etSearch);
        rvExpense.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ExpenseAdapter(filteredExpenses);
        rvExpense.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterExpenses(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        loadExpenses();
    }

    private void loadExpenses() {
        String token = "Bearer " + TokenManager.getToken(this);
        RetrofitClient.getApiService(this).getExpenses(token)
                .enqueue(new Callback<List<Expense>>() {
                    @Override
                    public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allExpenses.clear();
                            allExpenses.addAll(response.body());
                            filterExpenses(etSearch.getText().toString());
                        } else {
                            Toast.makeText(ExpenseListActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Expense>> call, Throwable t) {
                        Toast.makeText(ExpenseListActivity.this, "Ulanishda xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterExpenses(String query) {
        filteredExpenses.clear();
        for (Expense e : allExpenses) {
            if (e.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                    e.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredExpenses.add(e);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
