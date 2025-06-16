package com.example.omborboshqaruv.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.omborboshqaruv.Api.ApiService;
import com.example.omborboshqaruv.Api.RetrofitClient;
import com.example.omborboshqaruv.Models.SalesResponse;

import com.example.omborboshqaruv.Models.TopProduct;
import com.example.omborboshqaruv.R;
import com.example.omborboshqaruv.Utils.TokenManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesChartActivity extends AppCompatActivity {

    Spinner spinnerPeriod;
    BarChart barChart;
    LineChart lineChart;
    ApiService apiService;
    TextView textTotalSales,textTotalQuantity,textTotalExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_chart);

        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);
        apiService = RetrofitClient.getApiService(this);
         textTotalSales = findViewById(R.id.textTotalSales);
         textTotalQuantity = findViewById(R.id.textTotalQuantity);
         textTotalExpense = findViewById(R.id.textTotalExpense);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        loadChartData("monthly");


        spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] values = {"daily", "weekly", "monthly"};
                loadChartData(values[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadChartData(String period) {
        String token = "Bearer " + TokenManager.getToken(this);

        apiService.getSalesStatistics(token, period)
                .enqueue(new Callback<SalesResponse>() {
                    @Override
                    public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            SalesResponse data = response.body();





                            DecimalFormat formatter = new DecimalFormat("#,###.##");


                            textTotalSales.setText("Jami tushum: " + formatter.format(data.getTotal_sales()) + " so'm");
                            textTotalExpense.setText("Jami xarajat: " + formatter.format(data.getTotal_expense()) + " so'm");
                            textTotalQuantity.setText("Sotilgan miqdor: " + formatter.format(data.getTotal_quantity()));


                            drawCharts(response.body().getTopProducts());


                        } else {
                            Toast.makeText(SalesChartActivity.this, "Ma'lumotlar topilmadi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SalesResponse> call, Throwable t) {
                        Toast.makeText(SalesChartActivity.this, "Xatolik: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void drawCharts(List<TopProduct> products) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<Entry> lineEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            TopProduct product = products.get(i);
            barEntries.add(new BarEntry(i, (float) product.getRevenue()));
            lineEntries.add(new Entry(i, product.getQuantity()));
            labels.add(product.getName());
        }

        // Bar Chart (Tushum)
        BarDataSet barDataSet = new BarDataSet(barEntries, "Tushum (so'm)");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        XAxis xAxisBar = barChart.getXAxis();
        xAxisBar.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setLabelRotationAngle(45f);
        barChart.getDescription().setText("Top mahsulotlar bo‘yicha tushum");
        barChart.animateY(1000);
        barChart.invalidate();

        // Line Chart (Sotilgan miqdor)
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Sotilgan miqdor");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        XAxis xAxisLine = lineChart.getXAxis();
        xAxisLine.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisLine.setLabelRotationAngle(45f);
        lineChart.getDescription().setText("Top mahsulotlar bo‘yicha miqdor");
        lineChart.animateY(1000);
        lineChart.invalidate();
    }
}
