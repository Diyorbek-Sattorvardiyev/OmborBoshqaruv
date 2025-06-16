package com.example.omborboshqaruv.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.EntryRequest;
import com.example.omborboshqaruv.Models.Product;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEntriesActivity extends AppCompatActivity {

    Spinner spinnerProduct;
    TextInputEditText etQuantity, etUnitPrice, etSupplier, etNotes, etEntryDate;
    Button btnSubmitEntry;
    CardView carrdViewv;

    List<Product> productList = new ArrayList<>();
    ArrayAdapter<String> productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entries);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        spinnerProduct = findViewById(R.id.spinnerProduct);
        etQuantity = findViewById(R.id.etQuantity);
        etUnitPrice = findViewById(R.id.etUnitPrice);
        etSupplier = findViewById(R.id.etSupplier);
        etNotes = findViewById(R.id.etNotes);
        etEntryDate = findViewById(R.id.etEntryDate);
        btnSubmitEntry = findViewById(R.id.btnSubmitEntry);



        etEntryDate.setOnClickListener(v -> showDatePicker());


        loadProducts();


        btnSubmitEntry.setOnClickListener(v -> submitEntry());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    String selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
                    etEntryDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void loadProducts() {
        String token = TokenManager.getToken(this);
        RetrofitClient.getApiService(this).getProducts("Bearer " + token)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            productList = response.body();
                            List<String> productNames = new ArrayList<>();
                            for (Product p : productList) {
                                productNames.add(p.getName());
                            }

                            productAdapter = new ArrayAdapter<>(
                                    AddEntriesActivity.this,
                                    R.layout.spinner_layout2,
                                    R.id.textView333,
                                    productNames
                            );
                            productAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            spinnerProduct.setAdapter(productAdapter);

                        } else {
                            Toast.makeText(AddEntriesActivity.this, "Mahsulotlarni yuklashda xatolik", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Toast.makeText(AddEntriesActivity.this, "Ulanishda xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void submitEntry() {
        int position = spinnerProduct.getSelectedItemPosition();
        if (position < 0 || productList.isEmpty()) {
            Toast.makeText(this, "Mahsulot tanlanmadi", Toast.LENGTH_SHORT).show();
            return;
        }

        Product selectedProduct = productList.get(position);

        String quantityStr = etQuantity.getText().toString().trim();
        String priceStr = etUnitPrice.getText().toString().trim();

        if (quantityStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Miqdor va narxni kiriting", Toast.LENGTH_SHORT).show();
            return;
        }

        EntryRequest request = new EntryRequest();
        request.setProduct_id(selectedProduct.getId());
        request.setQuantity(Double.parseDouble(quantityStr));
        request.setUnit_price(Double.parseDouble(priceStr));
        request.setSupplier_name(etSupplier.getText().toString().trim());
        request.setNotes(etNotes.getText().toString().trim());
        request.setEntry_date(etEntryDate.getText().toString().trim());

        String token = TokenManager.getToken(this);
        RetrofitClient.getApiService(this)
                .createEntry(request, "Bearer " + token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Kirim qo‘shildi", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Ulanishda xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
