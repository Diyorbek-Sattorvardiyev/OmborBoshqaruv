package com.example.omborboshqaruv.Api;

import com.example.omborboshqaruv.Models.ApiResponse;
import com.example.omborboshqaruv.Models.ChartData;
import com.example.omborboshqaruv.Models.Entry;
import com.example.omborboshqaruv.Models.EntryRequest;
import com.example.omborboshqaruv.Models.ExitRequest;
import com.example.omborboshqaruv.Models.Expense;
import com.example.omborboshqaruv.Models.ExpenseRequest;
import com.example.omborboshqaruv.Models.LoginRequest;
import com.example.omborboshqaruv.Models.LoginResponse;
import com.example.omborboshqaruv.Models.NotificationModel;
import com.example.omborboshqaruv.Models.Product;
import com.example.omborboshqaruv.Models.ProductResponse;
import com.example.omborboshqaruv.Models.RegisterRequest;
import com.example.omborboshqaruv.Models.SalesChartPoint;
import com.example.omborboshqaruv.Models.SalesResponse;
import com.example.omborboshqaruv.Models.SalesStatsResponse;
import com.example.omborboshqaruv.Models.StockItem;
import com.example.omborboshqaruv.Models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/register")
    Call<ApiResponse> register(@Body RegisterRequest request);

    @GET("/api/profile")
    Call<User> getProfile();

    @Multipart
    @POST("/api/products")
    Call<ResponseBody> createProduct(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("purchase_price") RequestBody purchasePrice,
            @Part("selling_price") RequestBody sellingPrice,
            @Part("code") RequestBody code,
            @Part("barcode") RequestBody barcode,
            @Part("unit") RequestBody unit,
            @Part("min_stock") RequestBody minStock,
            @Part("description") RequestBody description,
            @Part("expiry_date") RequestBody expiryDate
    );
    @GET("/api/products")
    Call<List<Product>> getProducts(@Header("Authorization") String token);

    @DELETE("/api/products/{id}")
    Call<Void> deleteProduct(
            @Header("Authorization") String token,
            @Path("id") int productId
    );
    @GET("api/products/{id}")
    Call<Product> getProductById(@Path("id") int productId, @Header("Authorization") String token);

    @POST("api/entries")
    Call<ResponseBody> createEntry(@Body EntryRequest entryRequest, @Header("Authorization") String token);
    @POST("/api/exits")
    Call<ApiResponse> createExit(
            @Body ExitRequest exitRequest,
            @Header("Authorization") String token
    );
    @GET("/api/stock")
    Call<List<StockItem>> getStock(@Header("Authorization") String token);

    @GET("api/statistics/sales-chart")
    Call<List<SalesChartPoint>> getSalesChartData(
            @Header("Authorization") String token,
            @Query("period") String period
    );

    @GET("/api/statistics/sales")
    Call<SalesResponse> getSalesStatistics(
            @Header("Authorization") String token,
            @Query("period") String period
    );


    @GET("/api/charts/sales")
    Call<List<ChartData>> getSalesChart(
            @Header("Authorization") String token,
            @Query("period") String period
    );

    @POST("/api/expenses")
    Call<ApiResponse> createExpense(
            @Header("Authorization") String token,
            @Body ExpenseRequest expenseRequest
    );
    @GET("/api/expenses")
    Call<List<Expense>> getExpenses(@Header("Authorization") String token);

    @GET("/api/search")
    Call<List<Product>> searchProducts(
            @Header("Authorization") String token,
            @Query("q") String query,
            @Query("type") String type
    );

    @GET("/api/notifications")
    Call<List<NotificationModel>> getNotifications(@Header("Authorization") String token);

    @GET("/api/entries")
    Call<List<Entry>> getEntries(@Header("Authorization") String token);







}
