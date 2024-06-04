package com.example.RealFilm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RealFilm.R;
import com.example.RealFilm.adapter.MovieAdapter;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Comment;
import com.example.RealFilm.model.Favorite;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.model.Rate;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.CommentService;
import com.example.RealFilm.service.FavoriteService;
import com.example.RealFilm.service.MovieService;
import com.example.RealFilm.service.RatingService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartActivity extends AppCompatActivity {

    TextView tvMovieAmount;
    ImageButton iBtnBack;
    Button btnExport;
    RecyclerView rvMovieList;
    PieChart pieChart;

    private List<Movie> movieList = new ArrayList<>();
    private List<Favorite> favorites = new ArrayList<>();
    private List<Rate> rates = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setControl();
        getMovies();
        setEvent();
    }

    private void setEvent() {
        iBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });
    }

    private void getMovies() {
        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse<List<Movie>>> call = movieService.getMoviesLatest();
        call.enqueue(new Callback<ApiResponse<List<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Movie>>> call, Response<ApiResponse<List<Movie>>> response) {
                if (response.isSuccessful()) {
                    List<Movie> moviesRes = response.body().getData();
                    if (moviesRes != null && !moviesRes.isEmpty()) {
                        movieList = moviesRes;
                        //Lấy data rồi tạo chart
                        setupDataForChart();
                        String str = "Số lượng phim: " + moviesRes.size();
                        tvMovieAmount.setText(str);
                        rvMovieList.setLayoutManager(new LinearLayoutManager(ChartActivity.this));
                        rvMovieList.setHasFixedSize(true);
                        movieAdapter = new MovieAdapter(ChartActivity.this, movieList);
                        rvMovieList.setAdapter(movieAdapter);
                    } else {
                        Log.d("MOVIE_LIST", "Empty movie list");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Movie>>> call, Throwable t) {
                Log.e("MOVIE_LIST", "API call failed", t);
            }
        });
    }

    private void createPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(favorites.size(), "Lượt thích"));
        entries.add(new PieEntry(rates.size(), "Lượt đánh giá"));
        entries.add(new PieEntry(comments.size(), "Lượt bình luận"));
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(Color.parseColor("#1d6fb1"),
                Color.parseColor("#1c97bb"), Color.parseColor("#ac2011"));
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getLegend().setTextColor(Color.WHITE);
        pieChart.getLegend().setTextSize(15);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.invalidate();
    }

    private void setupDataForChart() {
        for (Movie mv : movieList) {
            String mvId = String.valueOf(mv.getId());
            getFavoriteById(mvId);
            getRatesById(mvId);
            getCommentsById(mvId);
        }
    }

    private void getFavoriteById(String mvId) {
        FavoriteService favoriteService = ApiService.createService(FavoriteService.class);
        Call<ApiResponse<List<Favorite>>> call = favoriteService.getMovieFavorite(Integer.parseInt(mvId));

        call.enqueue(new Callback<ApiResponse<List<Favorite>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Favorite>>> call, Response<ApiResponse<List<Favorite>>> response) {
                if (response.isSuccessful() && response.body().getData().size() > 0) {
                    favorites.addAll(response.body().getData());
                    createPieChart();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Favorite>>> call, Throwable t) {

            }
        });
    }

    private void getRatesById(String mvId) {
        RatingService ratingService = ApiService.createService(RatingService.class);
        Call<ApiResponse<List<Rate>>> call = ratingService.getMovieRate(Integer.parseInt(mvId));

        call.enqueue(new Callback<ApiResponse<List<Rate>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Rate>>> call, Response<ApiResponse<List<Rate>>> response) {
                if (response.isSuccessful() && response.body().getData() != null) {
                    rates.addAll(response.body().getData());
                    createPieChart();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Rate>>> call, Throwable t) {

            }
        });
    }

    private void getCommentsById(String mvId) {
        CommentService commentService = ApiService.createService(CommentService.class);
        Call<ApiResponse<List<Comment>>> call = commentService.getCommentByMovie(Integer.parseInt(mvId));

        call.enqueue(new Callback<ApiResponse<List<Comment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comment>>> call, Response<ApiResponse<List<Comment>>> response) {
                if (response.isSuccessful() && response.body().getData().size() > 0) {
                    comments.addAll(response.body().getData());
                    createPieChart();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comment>>> call, Throwable t) {

            }
        });
    }

    //  Tạo và xuất file
    private void createPdf() {
        PdfDocument doc = new PdfDocument();
        // Tạo trang thứ nhất
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = doc.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        drawPage(canvas); // Vẽ nội dung và biểu đồ
        doc.finishPage(page);
        // Lưu file PDF
        savePdfFile(doc);
    }

    private void drawPage(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        String mvAmount = tvMovieAmount.getText().toString();
        // Vẽ văn bản
        float xTime = 60;
        float yTime = 80;
        canvas.drawText(mvAmount, xTime, yTime, paint);
        // Vẽ biểu đồ
        drawChart(canvas);
    }

    private void drawChart(Canvas canvas) {
        Bitmap pieChart = convertChartToBitMap();
        float xChart = 40;
        float yChart = 100; // Điều chỉnh vị trí cho phù hợp với trang
        canvas.drawBitmap(pieChart, xChart, yChart, null); // Sử dụng null cho Paint vì không cần vẽ văn bản nữa
    }

    private Bitmap convertChartToBitMap() {
        Bitmap bitmap = null;
        if (pieChart != null && pieChart.getWidth() > 0 && pieChart.getHeight() > 0) {
            pieChart.getLegend().setTextColor(Color.BLACK);
            bitmap = pieChart.getChartBitmap();
            pieChart.getLegend().setTextColor(Color.WHITE);
        }
        return bitmap;
    }

    private void savePdfFile(PdfDocument doc) {
        File downloadDIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "real_film_pie_chart.pdf";
        File file = new File(downloadDIR, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            doc.writeTo(fos);
            doc.close();
            fos.close();
            Toast.makeText(this, "File đã được lưu thành công", Toast.LENGTH_SHORT).show();
            openPdfFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openPdfFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void setControl() {
        tvMovieAmount = findViewById(R.id.txt_movie_amount);
        iBtnBack = findViewById(R.id.btn_back);
        btnExport = findViewById(R.id.btn_export);
        rvMovieList = findViewById(R.id.rv_movies);
        pieChart = findViewById(R.id.pie_chart);
    }
}