package com.example.omborboshqaruv.UI;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.Product;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PraductDetailActivity extends AppCompatActivity {
    int productId;
    ImageView imgProduct;
    TextView textMahsulotNomi, textMahsulotkod, textMahsulotShtixKod,
            textMahsulotBirligi, textMahsulotKirimNarxi, textMahsulotchiqimNarxi,
            textMahsulotMinZaxira, textMahsulotIzox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_praduct_detail);

        imgProduct = findViewById(R.id.mahsulotRasmi);
        textMahsulotNomi = findViewById(R.id.textMahsulotNomi);
        textMahsulotkod = findViewById(R.id.textMahsulotkod);
        textMahsulotShtixKod = findViewById(R.id.textMahsulotShtixKod);
        textMahsulotBirligi = findViewById(R.id.textMahsulotBirligi);
        textMahsulotKirimNarxi = findViewById(R.id.textMahsulotKirimNarxi);
        textMahsulotchiqimNarxi = findViewById(R.id.textMahsulotchiqimNarxi);
        textMahsulotMinZaxira = findViewById(R.id.textMahsulotMinZaxira);
        textMahsulotIzox = findViewById(R.id.textMahsulotIzox);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        productId = getIntent().getIntExtra("product_id", -1);

        if (productId != -1) {

            loadProductById(productId);
        } else {
            Toast.makeText(this, "Mahsulot ID topilmadi", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    private void loadProductById(int id) {
        String token = TokenManager.getToken(this);
        RetrofitClient.getApiService(this).getProductById(id, "Bearer " + token)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Product product = response.body();
                            showProductInfo(product);
                        } else {
                            Toast.makeText(PraductDetailActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Toast.makeText(PraductDetailActivity.this, "Ulanishda xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showProductInfo(Product product) {
        textMahsulotNomi.setText("Mahsulot nomi: " + product.getName());
        textMahsulotkod.setText("Mahsulot kodi: " + product.getCode());
        textMahsulotShtixKod.setText("Shtrix kodi: " + product.getBarcode());
        textMahsulotBirligi.setText("O‘lchov birligi: " + product.getUnit());
        textMahsulotKirimNarxi.setText("Sotib olish narxi: " + product.getPurchase_price() + " so‘m");
        textMahsulotchiqimNarxi.setText("Sotish narxi: " + product.getSelling_price() + " so‘m");
        textMahsulotMinZaxira.setText("Minimal zaxira: " + product.getMin_stock()+" "+product.getUnit());
        textMahsulotIzox.setText("Izoh: " + (product.getDescription() != null ? product.getDescription() : "mavjud emas"));



        if (product.getImage_url() != null && !product.getImage_url().isEmpty()) {
            Glide.with(this).load(product.getImage_url()).into(imgProduct);
        } else {
            imgProduct.setImageResource(R.drawable.img_2);
        }
    }

}