package com.example.omborboshqaruv.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.MainActivity;
import com.example.omborboshqaruv.Models.ApiResponse;
import com.example.omborboshqaruv.Models.LoginRequest;
import com.example.omborboshqaruv.Models.LoginResponse;
import com.example.omborboshqaruv.Models.RegisterRequest;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WelcomeActivity extends AppCompatActivity {

    AppCompatButton regsterBtn,loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        regsterBtn=findViewById(R.id.regsterBtn);
        loginButton=findViewById(R.id.loginButton);

        if (TokenManager.getToken(this) != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        regsterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterBottomDialog();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginBottomDialog();
            }
        });




    }
    private void showRegisterBottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                WelcomeActivity.this,
                R.style.CustomBottomSheetDialogTheme // <-- bu muhim
        );
        View view = LayoutInflater.from(this)
                .inflate(R.layout.register_dialog, null);


        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);

        // Elementlar bilan ishlash
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText fulNameEditText = view.findViewById(R.id.fulNameEditText);
        EditText phoneEditText = view.findViewById(R.id.phoneEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        AppCompatButton registerSubmit = view.findViewById(R.id.regsterBtnDialog);
        ImageView closeIcon = view.findViewById(R.id.closeDialog);
        TextView textKirish = view.findViewById(R.id.textKirish);
        textKirish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                showLoginBottomDialog();
            }
        });
        registerSubmit.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String fullName = fulNameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            RegisterRequest registerRequest = new RegisterRequest(email, password, fullName, phone);

            RetrofitClient.getApiService(this).register(registerRequest).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(WelcomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    } else {
                        Toast.makeText(WelcomeActivity.this, "Xato: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(WelcomeActivity.this, "Xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        closeIcon.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }
    private void showLoginBottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                WelcomeActivity.this,
                R.style.CustomBottomSheetDialogTheme
        );
        View view = LayoutInflater.from(this)
                .inflate(R.layout.login_dialog, null);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);

        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        AppCompatButton loginSubmit = view.findViewById(R.id.loginBtnDialog);
        ImageView closeIcon = view.findViewById(R.id.closeDialog);
        TextView regstrationText=view.findViewById(R.id.regstrationText);

        regstrationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                showRegisterBottomDialog();
            }
        });

        loginSubmit.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            LoginRequest loginRequest = new LoginRequest(email, password);

            RetrofitClient.getApiService(this).login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getAccess_token();
                        TokenManager.saveToken(WelcomeActivity.this, token);

                        Toast.makeText(WelcomeActivity.this, "Tizimga kirildi", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(WelcomeActivity.this, "Login xato: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(WelcomeActivity.this, "Xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            bottomSheetDialog.dismiss();
        });


        closeIcon.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

}