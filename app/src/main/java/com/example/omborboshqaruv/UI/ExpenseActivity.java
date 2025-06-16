package com.example.omborboshqaruv.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.ApiResponse;
import com.example.omborboshqaruv.Models.ExpenseRequest;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    TextInputEditText etAmount, etDescription, etExpenseDate;
    Button btnSubmitExpense;

    Map<String, String> categoryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        spinnerCategory = findViewById(R.id.spinnerCategory);
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        etExpenseDate = findViewById(R.id.etDate);
        btnSubmitExpense = findViewById(R.id.btnSaveExpense);

        etExpenseDate.setOnClickListener(v -> showDatePicker());

        // Spinnerga ko‘rinadigan va backendga yuboriladigan qiymatlar mosligi
        categoryMap = new HashMap<>();
        categoryMap.put("Elektr", "elektr");
        categoryMap.put("Ijara", "ijara");
        categoryMap.put("Transport", "transport");
        categoryMap.put("Ish haqi", "ish_haqi");
        categoryMap.put("Boshqa", "boshqa");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_layout2,
                R.id.textView333,
                categoryMap.keySet().toArray(new String[0])
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerCategory.setAdapter(adapter);

        btnSubmitExpense.setOnClickListener(v -> submitExpense());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    String selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
                    etExpenseDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void submitExpense() {
        String label = spinnerCategory.getSelectedItem().toString();
        String category = categoryMap.get(label); // backendga yuboriladigan enum qiymat
        String amountStr = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String expenseDate = etExpenseDate.getText().toString().trim();

        if (category == null || amountStr.isEmpty()) {
            Toast.makeText(this, "Kategoriya va miqdor majburiy", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Miqdor noto‘g‘ri formatda", Toast.LENGTH_SHORT).show();
            return;
        }

        ExpenseRequest request = new ExpenseRequest(
                category,
                amount,
                description,
                expenseDate.isEmpty() ? null : expenseDate
        );

        String token = "Bearer " + TokenManager.getToken(this);
        RetrofitClient.getApiService(this).createExpense(token, request)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ExpenseActivity.this, "Xarajat muvaffaqiyatli saqlandi", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ExpenseActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(ExpenseActivity.this, "Ulanishda xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
