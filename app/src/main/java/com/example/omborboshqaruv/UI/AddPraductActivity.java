package com.example.omborboshqaruv.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.FileUtils;
import com.example.omborboshqaruv.Utils.TokenManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPraductActivity extends AppCompatActivity {

    Spinner spin;
    TextInputEditText etName, etCode, etBarcode, etPurchasePrice, etSellingPrice, etMinStock, etDescription;
    AppCompatButton addMahsulotButton, btnSelectImage;
    ImageView ivProductImage;

    private static final int IMAGE_PICK_CODE = 1000;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_praduct);


        spin = findViewById(R.id.codeSpinner);
        etName = findViewById(R.id.etName);
        etCode = findViewById(R.id.etCode);
        etBarcode = findViewById(R.id.etBarcode);
        etPurchasePrice = findViewById(R.id.etPurchasePrice);
        etSellingPrice = findViewById(R.id.etSellingPrice);
        etMinStock = findViewById(R.id.etMinStock);
        etDescription = findViewById(R.id.etDescription);
        addMahsulotButton = findViewById(R.id.addMahsulotButton);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String[] countryCodes = getResources().getStringArray(R.array.unit);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.textView, countryCodes);
        spin.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });


        addMahsulotButton.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String barcode = etBarcode.getText().toString().trim();
        String unit = spin.getSelectedItem().toString();
        String purchaseStr = etPurchasePrice.getText().toString().trim();
        String sellingStr = etSellingPrice.getText().toString().trim();
        String minStockStr = etMinStock.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String expiryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (name.isEmpty() || purchaseStr.isEmpty() || sellingStr.isEmpty()) {
            Toast.makeText(this, "Majburiy maydonlar to‘ldirilmagan", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = TokenManager.getToken(this);
        if (token == null) {
            Toast.makeText(this, "Token topilmadi", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = null;
        MultipartBody.Part imagePart = null;

        if (imageUri != null) {
            imageFile = new File(FileUtils.getPath(this, imageUri));
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), reqFile);
        }


        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody codeBody = RequestBody.create(MediaType.parse("text/plain"), code);
        RequestBody barcodeBody = RequestBody.create(MediaType.parse("text/plain"), barcode);
        RequestBody unitBody = RequestBody.create(MediaType.parse("text/plain"), unit);
        RequestBody purchaseBody = RequestBody.create(MediaType.parse("text/plain"), purchaseStr);
        RequestBody sellingBody = RequestBody.create(MediaType.parse("text/plain"), sellingStr);
        RequestBody minStockBody = RequestBody.create(MediaType.parse("text/plain"), minStockStr.isEmpty() ? "0" : minStockStr);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody expiryBody = RequestBody.create(MediaType.parse("text/plain"), expiryDate);

        RetrofitClient.getApiService(this).createProduct(
                "Bearer " + token,
                imagePart,
                nameBody, purchaseBody, sellingBody,
                codeBody, barcodeBody, unitBody,
                minStockBody, descriptionBody, expiryBody
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddPraductActivity.this, "Mahsulot saqlandi!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddPraductActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddPraductActivity.this, "Tarmoq xatosi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivProductImage.setImageURI(imageUri);
        }
    }
}
