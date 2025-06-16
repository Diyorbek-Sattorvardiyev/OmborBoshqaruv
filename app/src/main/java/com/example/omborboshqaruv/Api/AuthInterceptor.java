package com.example.omborboshqaruv.Api;


import android.content.Context;
import com.example.omborboshqaruv.Utils.TokenManager;
import android.content.Context;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AuthInterceptor implements Interceptor {
    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = TokenManager.getToken(context);

        if (token != null) {
            Request modifiedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(modifiedRequest);
        }

        return chain.proceed(originalRequest);
    }
}

