// MainActivity.java
package com.example.omborboshqaruv;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.omborboshqaruv.Adapters.NotificationAdapter;
import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.NotificationModel;
import com.example.omborboshqaruv.Models.Product;
import com.example.omborboshqaruv.Models.User;
import com.example.omborboshqaruv.UI.*;
import com.example.omborboshqaruv.Utils.TokenManager;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView textFullName, textEmail;
    CardView cardViewProfile;
    CardView notificationCard;

    EditText etSearch;
    MaterialCardView addPraduct, viewPraduct, kirimCard, cardsotuv, cardStock, cardStatistic, cardExport, cardXarajatAdd, cardXarajatList,cardKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadUserProfile();
        setupListeners();
        fetchAndShowNotifications();
        etSearch = findViewById(R.id.izlashMain);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initViews() {
        textFullName = findViewById(R.id.textFulname);
        textEmail = findViewById(R.id.textEmail);
        cardViewProfile = findViewById(R.id.cardView);
        addPraduct = findViewById(R.id.addPraduct);
        kirimCard = findViewById(R.id.kirimCard);
        viewPraduct = findViewById(R.id.viewPraduct);
        cardsotuv = findViewById(R.id.cardsotuv);
        cardStock = findViewById(R.id.cardStock);
        cardStatistic = findViewById(R.id.cardStatistic);
        cardExport = findViewById(R.id.cardExport);
        cardXarajatAdd = findViewById(R.id.cardXarajatAdd);
        cardKirim = findViewById(R.id.cardKirim);
        cardXarajatList = findViewById(R.id.cardXarajatList);
        Toolbar toolbar = findViewById(R.id.toolbar);
        notificationCard = findViewById(R.id.notificationCard);

        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.backraund));
    }

    private void loadUserProfile() {
        RetrofitClient.getApiService(this).getProfile().enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, retrofit2.Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    // Foydalanuvchi bloklanganmi?
                    if (!user.isIs_active()) {
                        showBlockedDialog(); // <<< SHU YERDA FUNKSIYA CHAQIRILADI
                        return;
                    }

                    textFullName.setText(user.getFull_name());
                    textEmail.setText(user.getEmail());

                } else if (response.code() == 401) {
                    Toast.makeText(MainActivity.this, "Token muddati tugagan!", Toast.LENGTH_SHORT).show();
                    TokenManager.clearToken(MainActivity.this);
                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupListeners() {
        cardViewProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        addPraduct.setOnClickListener(v -> startActivity(new Intent(this, AddPraductActivity.class)));
        kirimCard.setOnClickListener(v -> startActivity(new Intent(this, AddEntriesActivity.class)));
        viewPraduct.setOnClickListener(v -> startActivity(new Intent(this, ProductListActivity.class)));
        cardsotuv.setOnClickListener(v -> startActivity(new Intent(this, AddExitActivity.class)));
        cardStock.setOnClickListener(v -> startActivity(new Intent(this, StockActivity.class)));
        cardStatistic.setOnClickListener(v -> startActivity(new Intent(this, SalesChartActivity.class)));
        cardXarajatAdd.setOnClickListener(v -> startActivity(new Intent(this, ExpenseActivity.class)));
        cardXarajatList.setOnClickListener(v -> startActivity(new Intent(this, ExpenseListActivity.class)));
        cardKirim.setOnClickListener(v -> startActivity(new Intent(this, EntryListActivity.class)));

        cardExport.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
            popupMenu.getMenuInflater().inflate(R.menu.export_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                String token = TokenManager.getToken(MainActivity.this);
                String baseUrl = "http://192.168.1.217:5000";
                if (item.getItemId() == R.id.menu_pdf) {
                    downloadFileWithToken(baseUrl + "/api/export/pdf?type=stock", token, "stock_report.pdf");
                    return true;
                } else if (item.getItemId() == R.id.menu_excel) {
                    downloadFileWithToken(baseUrl + "/api/export/excel?type=products", token, "products.xlsx");
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }

    private void downloadFileWithToken(String fileUrl, String token, String fileName) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fileUrl)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Yuklab olishda xatolik: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_LONG).show());
                    return;
                }

                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(downloadDir, fileName);

                InputStream inputStream = response.body().byteStream();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int len;

                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }

                outputStream.close();
                inputStream.close();

                runOnUiThread(() -> Toast.makeText(MainActivity.this, fileName + " saqlandi!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Toast.makeText(this, "Dastur haqida", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            TokenManager.clearToken(this);
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performSearch(String query) {
        if (query.trim().isEmpty()) return;

        String token = "Bearer " + TokenManager.getToken(this);
        RetrofitClient.getApiService(this).searchProducts(token, query, "products")

                .enqueue(new retrofit2.Callback<List<Product>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            showSearchDialogWithDetails(response.body());
                        } else {
                            Toast.makeText(MainActivity.this, "Hech narsa topilmadi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSearchDialogWithDetails(List<Product> productList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_search_result, null);
        LinearLayout container = dialogView.findViewById(R.id.containerResults);


        builder.setView(dialogView);
        AlertDialog dialog = builder.create();


        ImageView closeDialogResult = dialogView.findViewById(R.id.closeDialogResult);
        if (closeDialogResult != null) {
            closeDialogResult.setOnClickListener(v -> dialog.dismiss());
        }

        for (Product p : productList) {
            View itemView = inflater.inflate(R.layout.item_product_result, container, false);

            TextView txtName = itemView.findViewById(R.id.textProductName);
            TextView txtPrice = itemView.findViewById(R.id.textProductPrice);
            ImageView imgProduct = itemView.findViewById(R.id.imgProduct);

            txtName.setText(p.getName());
            txtPrice.setText("Narxi: " + p.getSelling_price() + " so'm");

            if (p.getImage_url() != null && !p.getImage_url().isEmpty()) {
                Glide.with(this)
                        .load(p.getImage_url())
                        .placeholder(R.drawable.img)
                        .error(R.drawable.img)
                        .into(imgProduct);
            } else {
                imgProduct.setImageResource(R.drawable.img);
            }

            container.addView(itemView);
        }

        dialog.show();
    }

    private void fetchAndShowNotifications() {
        String token = "Bearer " + TokenManager.getToken(this);

        RetrofitClient.getApiService(this).getNotifications(token).enqueue(new retrofit2.Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(retrofit2.Call<List<NotificationModel>> call, retrofit2.Response<List<NotificationModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationModel> allNotifications = response.body();


                    List<NotificationModel> unreadNotifications = new ArrayList<>();
                    for (NotificationModel n : allNotifications) {
                        if (!n.isRead()) {
                            unreadNotifications.add(n);
                        }
                    }

                    TextView notificationCount = findViewById(R.id.notificationCount);

                    if (!unreadNotifications.isEmpty()) {
                        notificationCount.setVisibility(View.VISIBLE);
                        notificationCount.setText(String.valueOf(unreadNotifications.size()));
                    } else {
                        notificationCount.setVisibility(View.GONE);
                    }

                    CardView notificationCard = findViewById(R.id.notificationCard);
                    notificationCard.setOnClickListener(v -> showNotificationDialog(allNotifications));

                } else {
                    Toast.makeText(MainActivity.this, "Xatolik: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<NotificationModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Tarmoq xatosi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNotificationDialog(List<NotificationModel> notifications) {
        for (NotificationModel notification : notifications) {
            notification.setRead(true);
        }

        TextView notificationCount = findViewById(R.id.notificationCount);
        notificationCount.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView customTitle = new TextView(this);
        customTitle.setText("Bildirishnomalar");
        customTitle.setPadding(40, 30, 40, 10);
        customTitle.setTextSize(20f);
        customTitle.setTypeface(null, Typeface.BOLD);
        customTitle.setTextColor(ContextCompat.getColor(this, R.color.login_btn));
        builder.setCustomTitle(customTitle);


        View view = LayoutInflater.from(this).inflate(R.layout.dialog_notifications, null);
        RecyclerView recyclerView = view.findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NotificationAdapter(notifications));

        builder.setView(view);
        builder.setPositiveButton("Yopish", (dialogInterface, which) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();


        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(this, R.color.backraund))
        );

        dialog.show();


        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.login_btn));
    }
    private void showBlockedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hisob bloklangan");
        builder.setMessage("Sizning hisobingiz faolsizlantirilgan. Iltimos, quyidagi raqamlar bilan bog‘laning:\n\n +998 88 607 45 55\n +998 90 987 65 43");

        builder.setCancelable(false);
        builder.setPositiveButton("Chiqish", (dialog, which) -> {
            TokenManager.clearToken(MainActivity.this);
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            finish();
        });

        AlertDialog dialog = builder.create();
        dialog.show();


        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.primary));
        }
    }



}
