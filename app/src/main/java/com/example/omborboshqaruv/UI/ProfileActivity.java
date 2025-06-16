package com.example.omborboshqaruv.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.User;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView userNameTextView,userEmailTextView,userPhoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        userNameTextView=findViewById(R.id.userNameTextView);
        userEmailTextView=findViewById(R.id.userEmailTextView);
        userPhoneTextView=findViewById(R.id.userPhoneTextView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.backraund));

        RetrofitClient.getApiService(this).getProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    userNameTextView.setText(user.getFull_name());
                    userEmailTextView.setText(user.getEmail());
                    userPhoneTextView.setText(user.getPhone());
                } else if (response.code() == 401) {
                    Toast.makeText(ProfileActivity.this, "Token muddati tugagan!", Toast.LENGTH_SHORT).show();
                    TokenManager.clearToken(ProfileActivity.this);
                    startActivity(new Intent(ProfileActivity.this, WelcomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}