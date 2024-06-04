package com.example.RealFilm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RealFilm.R;
import com.example.RealFilm.fragment.EntryInfoDetailFragment;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Comment;
import com.example.RealFilm.model.Favorite;
import com.example.RealFilm.model.Rate;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.CommentService;
import com.example.RealFilm.service.FavoriteService;
import com.example.RealFilm.service.RatingService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartMovieDetailActivity extends AppCompatActivity {
    Spinner spSelectItem, spSelectTime;
    LineChart lineChart;
    BarChart barChart;
    ImageButton btnBack;
    Button btnDate, btnRefresh, btnExport;
    DatePickerDialog datePickerDialog;
    TextView txtEntryDetail, txtDetailFav, txtDetailRate, txtDetailCmt;
    FrameLayout entryInfoPlacement;
    private List<Favorite> favorites;
    private List<Rate> rates;
    private List<Comment> comments;
    private List<String> xValues;
    List<Integer> yValuesFavorite;
    List<Integer> yValuesRate;
    List<Integer> yValuesComment;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_IMG = Manifest.permission.READ_MEDIA_IMAGES;
    private static final String PERMISSION_READ_VIDEO = Manifest.permission.READ_MEDIA_VIDEO;
    private static final String PERMISSION_READ_AUDIO= Manifest.permission.READ_MEDIA_AUDIO;
    private static final int PERMISSION_REQ_CODE = 100;
    boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_movie_detail);
        setControl();
        createSelect();
        setEvent();
    }

    private void setEvent() {
        setTimePicker();
        setChartContent();

        btnBack.setOnClickListener(v -> onBackPressed());

        btnRefresh.setOnClickListener(v -> {
            refreshAllListModel();
            createLineChart();
        });

        btnExport.setOnClickListener(v -> {
            requestRuntimePermission();
        });

        // Hiện danh sách chi tiết của entry được chọn
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String option = spSelectItem.getSelectedItem().toString();

                if (!option.equals("Tất cả") && !option.equals("Lượt thích")) {
                    txtEntryDetail.setVisibility(View.VISIBLE);
                    entryInfoPlacement.setVisibility(View.VISIBLE);
                    String xValue = xValues.get((int) e.getX());
                    EntryInfoDetailFragment fragment = new EntryInfoDetailFragment();
                    fragment.setxValue(xValue);
                    fragment.setTimeOption(spSelectTime.getSelectedItem().toString());
                    if (option.equals("Lượt đánh giá")) {
                        fragment.setRates(rates);
                    }
                    else if (option.equals("Lượt bình luận")) {
                        fragment.setComments(comments);
                    }
                    replaceFragment(fragment);
                }
            }
            @Override
            public void onNothingSelected() {

            }
        });
    }

//    Số lượng tổng quát tương ứng với biểu đồ
    private void showAllAmount() {
        String option = spSelectItem.getSelectedItem().toString();
        txtDetailFav.setVisibility(View.GONE);
        txtDetailRate.setVisibility(View.GONE);
        txtDetailCmt.setVisibility(View.GONE);
        if (option.equals("Tất cả")) {
            showAmount(yValuesFavorite, txtDetailFav, "Lượt thích");
            showAmount(yValuesRate, txtDetailRate, "Lượt đánh giá");
            showAmount(yValuesComment, txtDetailCmt, "Lượt bình luận");
        } else if (option.equals("Lượt thích")) {
            showAmount(yValuesFavorite, txtDetailFav, "Lượt thích");
        } else if (option.equals("Lượt đánh giá")) {
            showAmount(yValuesRate, txtDetailRate, "Lượt đánh giá");
        } else if (option.equals("Lượt bình luận")) {
            showAmount(yValuesComment, txtDetailCmt, "Lượt bình luận");
        }
    }

    private void showAmount(List<Integer> values, TextView textView, String label) {
        int sum = 0;
        if (values != null) {
            for (int y : values) {
                sum += y;
            }
            textView.setText(label + ": " + sum);
        }
        textView.setVisibility(View.VISIBLE);
    }

//    Xin quyền truy cập bộ nhớ để xuất file
    private void requestRuntimePermission() {
        String[] storagePermissions = {PERMISSION_WRITE_STORAGE, PERMISSION_READ_STORAGE,
                PERMISSION_READ_IMG, PERMISSION_READ_VIDEO, PERMISSION_READ_AUDIO};
        permissionGranted = checkPermissions(storagePermissions);
        if (permissionGranted) {
            createPdf();
        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_WRITE_STORAGE)
        || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_READ_IMG)) {
            showRationalDialog();
        }
        else {
            if (Build.VERSION.SDK_INT <= 32) {
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_STORAGE, PERMISSION_WRITE_STORAGE},
                        PERMISSION_REQ_CODE);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_IMG,
                        PERMISSION_READ_VIDEO, PERMISSION_READ_AUDIO},
                        PERMISSION_REQ_CODE);
            }
        }
    }

    private boolean checkPermissions(String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void showRationalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app require PERMISSION_WRITE_STORAGE")
                .setTitle("Permission Required")
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    if (Build.VERSION.SDK_INT <= 32) {
                        ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_STORAGE, PERMISSION_WRITE_STORAGE},
                                PERMISSION_REQ_CODE);
                    }
                    else {
                        ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_IMG,
                                        PERMISSION_READ_VIDEO, PERMISSION_READ_AUDIO},
                                PERMISSION_REQ_CODE);
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQ_CODE
        && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createPdf();
        }
        else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_WRITE_STORAGE)
                && !ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_READ_IMG)) {
            showPermissionDeniedDialog();
        }
        else {
            requestRuntimePermission();
        }
    }

    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This feature is unavailable because it doesn't have the required permission")
                .setTitle("Permission Required")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Settings", (dialog, which) -> navigateToAppSettings());
        builder.show();
    }

    private void navigateToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }


//  Tạo và xuất file
    private void createPdf() {
        PdfDocument doc = new PdfDocument();
        // Tạo trang thứ nhất
        PdfDocument.PageInfo pageInfo1 = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page1 = doc.startPage(pageInfo1);
        Canvas canvas1 = page1.getCanvas();
        drawPage1(canvas1); // Vẽ nội dung và biểu đồ lên trang thứ nhất
        doc.finishPage(page1);
        // Tạo trang thứ hai
        PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(1080, 1920, 2).create();
        PdfDocument.Page page2 = doc.startPage(pageInfo2);
        Canvas canvas2 = page2.getCanvas();
        drawPage2(canvas2);
        doc.finishPage(page2);
        // Lưu file PDF
        savePdfFile(doc);
        btnRefresh.performClick();
    }

    private void drawPage1(Canvas canvas) {
        // Vẽ văn bản và hình ảnh lên trang đầu tiên
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        String time = "";
        time = btnDate.getText().toString();
        // Vẽ văn bản
        float xTime = 60;
        float yTime = 80;
        canvas.drawText(time, xTime, yTime, paint);
        // Vẽ biểu đồ
        drawChart(canvas);
    }

    private void drawChart(Canvas canvas) {
        // Vẽ biểu đồ lên trang thứ nhất
        Bitmap bmLineChart = convertChartToBitMap();
        float xChart = 40;
        float yChart = 100; // Điều chỉnh vị trí cho phù hợp với trang
        canvas.drawBitmap(bmLineChart, xChart, yChart, null); // Sử dụng null cho Paint vì không cần vẽ văn bản nữa
    }

    private Bitmap convertChartToBitMap() {
        Bitmap bitmap = null;
        if (lineChart != null && lineChart.getWidth() > 0 && lineChart.getHeight() > 0) {
            setBlackChart(lineChart);
            bitmap = lineChart.getChartBitmap();
        }
        return bitmap;
    }

    private void drawPage2(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        String totalTitle = "Tổng quát";
        float xTotal = 60;
        float yTotal = 80;
        canvas.drawText(totalTitle, xTotal, yTotal, paint);

        float xFav = 70, yFav = yTotal+80;
        float xRate = 70, yRate = yFav+80;
        float xCmt = 70, yCmt = yRate+80;
        String favCount = "Lượt thích: 0";
        String rateCount = "Lượt đánh giá: 0";
        String cmtCount = "Lượt bình luận: 0";

        if (txtDetailFav.getVisibility() == View.VISIBLE) favCount = txtDetailFav.getText().toString();
        if (txtDetailRate.getVisibility() == View.VISIBLE) rateCount = txtDetailRate.getText().toString();
        if (txtDetailCmt.getVisibility() == View.VISIBLE) cmtCount = txtDetailCmt.getText().toString();

        // Vẽ thông tin số lượng tổng quát
        canvas.drawText(totalTitle, xTotal, yTotal, paint);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        canvas.drawText(favCount, xFav, yFav, paint);
        canvas.drawText(rateCount, xRate, yRate, paint);
        canvas.drawText(cmtCount, xCmt, yCmt, paint);

        // Vẽ danh sách chi tiết entry được nhấn vào
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.entry_info_placement);
        if (fragment != null) {
            String detailTitle = "Chi tiết";
            float xDetail = 60;
            float yDetail = yCmt+100;
            paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            canvas.drawText(detailTitle, xDetail, yDetail, paint);
            Bitmap bmListDetail = createBitmapFromFragment(fragment);
            float xChart = 30;
            float yChart = yDetail+40;
            canvas.drawBitmap(bmListDetail, xChart, yChart, null);
        }
    }

    private Bitmap createBitmapFromFragment(Fragment fragment) {
        // Đo lường và chụp nội dung của fragment
        fragment.getView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        fragment.getView().layout(0, 0, fragment.getView().getMeasuredWidth(), fragment.getView().getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(fragment.getView().getWidth(), fragment.getView().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        fragment.getView().draw(canvas);
        return bitmap;
    }

    private void savePdfFile(PdfDocument doc) {
        File downloadDIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "real_film_line_chart.pdf";
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

    private void setBlackChart(LineChart lineChart) {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setAxisLineColor(Color.BLACK);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextColor(Color.BLACK);
        yAxis.setAxisLineColor(Color.BLACK);

        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.BLACK);

        Description description = lineChart.getDescription();
        description.setTextColor(Color.BLACK);

        LineData lineData = lineChart.getData();
        if (lineData != null) {
            for (int i = 0; i < lineData.getDataSetCount(); i++) {
                ILineDataSet dataSet = lineData.getDataSetByIndex(i);
                if (dataSet != null) {
                    dataSet.setValueTextColor(Color.BLACK);
                }
            }
        }
    }

//    Thay framelayout thành fragment
    private void replaceFragment(EntryInfoDetailFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.entry_info_placement, fragment);
        fragmentTransaction.commit();
    }

    private void refreshAllListModel() {
        favorites.clear();
        rates.clear();
        comments.clear();
        Intent intent = getIntent();
        String mvId = intent.getStringExtra("MovieId");
        getFavoriteById(mvId);
        getRatesById(mvId);
        getCommentsById(mvId);
        txtEntryDetail.setVisibility(View.GONE);
        entryInfoPlacement.setVisibility(View.GONE);
    }

//    Đặt nội dung biểu đồ tùy theo spSelectItem
    private void setChartContent() {
        Intent intent = getIntent();
        String mvId = intent.getStringExtra("MovieId");

        spSelectItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                txtEntryDetail.setVisibility(View.GONE);
                entryInfoPlacement.setVisibility(View.GONE);
                if (item.equals("Tất cả")) {
                    getFavoriteById(mvId);
                    getRatesById(mvId);
                    getCommentsById(mvId);
                }
                else if (item.equals("Lượt thích")) {
                    getFavoriteById(mvId);
                }
                else if (item.equals("Lượt đánh giá")) {
                    getRatesById(mvId);
                }
                else if (item.equals("Lượt bình luận")) {
                    getCommentsById(mvId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

//    Tạo biểu đồ đường
    private void createLineChart() {
        createTimeListAndOptionCount(spSelectItem.getSelectedItem().toString());
        setupXAxis();
        setupYAxis();

        //Tạo các Entry List để tạo DataSet
        List<Entry> favoriteEntries = null;
        List<Entry> rateEntries = null;
        List<Entry> commentEntries = null;
        if (favorites != null) {
            favoriteEntries = addEntryToEntryList("Favorite");
        }
        if (rates != null) {
            rateEntries = addEntryToEntryList("Rate");
        }
        if (comments != null) {
            commentEntries = addEntryToEntryList("Comment");
        }

        if (favoriteEntries != null && rateEntries != null && commentEntries != null)
            setMaximumYAxis(favoriteEntries, rateEntries, commentEntries);

        LineData lineData = new LineData();
        createLineData(lineData, favoriteEntries, rateEntries, commentEntries);
        setupLegend();
        setupDescription();
        updateChart(lineData);
    }

//    setMaximumYAxis dựa theo entries
    private void setMaximumYAxis(List<Entry>favoriteEntries, List<Entry>rateEntries, List<Entry>commentEntries) {
        List<Entry> entries = new ArrayList<>();
        entries.addAll(favoriteEntries);
        entries.addAll(rateEntries);
        entries.addAll(commentEntries);

        // Trục y biểu thị số lượng của entry.Y
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        for (Entry e : entries) {
            if (e.getY() > yAxis.getAxisMaximum()) {
                yAxis.setAxisMaximum(e.getY() + 2);
                yAxis.setLabelCount((int) e.getY() + 2);
            }
        }
    }

    private List<Entry> addEntryToEntryList(String entriesType) {
        int i = 0;
        List<Entry> entries = new ArrayList<>();
        if (xValues != null && (favorites != null || rates != null || comments != null)) {
            createYValues(entriesType);
            if (entriesType.equals("Favorite")) {
                if (yValuesFavorite != null) {
                    for (Integer y : yValuesFavorite) {
                        entries.add(new Entry(i, y));
                        i++;
                    }
                }
            } else if (entriesType.equals("Rate")) {
                if (yValuesRate != null) {
                    for (Integer y : yValuesRate) {
                        entries.add(new Entry(i, y));
                        i++;
                    }
                }
            } else if (entriesType.equals("Comment")) {
                if (yValuesComment != null) {
                    for (Integer y : yValuesComment) {
                        entries.add(new Entry(i, y));
                        i++;
                    }
                }
            }
        }

        return entries;
    }

    private void setupXAxis() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(xValues.size());
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.WHITE);
    }

    private void setupYAxis() {
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setAxisLineColor(Color.WHITE);
        lineChart.getAxisRight().setDrawLabels(false);
    }

    private void createYValues(String entriesType) {
        List<Integer> yValues = new ArrayList<>();
        for (String x : xValues) {
            int count = 0;
            if (entriesType.equals("Favorite")) {
                for (Favorite fav : favorites) {
                    count += countForYValues(fav.getUpdatedAt(), x);
                }
            } else if (entriesType.equals("Rate")) {
                for (Rate rate : rates) {
                    count += countForYValues(rate.getUpdatedAt(), x);
                }
            } else if (entriesType.equals("Comment")) {
                for (Comment cmt : comments) {
                    count += countForYValues(cmt.getUpdatedAt(), x);
                }
            }
            yValues.add(count);
        }
        setYValues(entriesType, yValues);
    }

    private void setYValues(String entriesType, List<Integer> values) {
        if (entriesType.equals("Favorite")) {
            yValuesFavorite = values;
        } else if (entriesType.equals("Rate")) {
            yValuesRate = values;
        } else if (entriesType.equals("Comment")) {
            yValuesComment = values;
        }
    }

    private int countForYValues(Date date, String x) {
        int count = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (spSelectTime.getSelectedItem().toString().equals("Ngày")) {
            String hourMinute = makeHMString(hour, minute);
            if (hourMinute.equals(x))
                count = 1;
        }
        else if (spSelectTime.getSelectedItem().toString().equals("Tháng")) {
            if (String.valueOf(day).equals(x))
                count = 1;
        }
        else if (spSelectTime.getSelectedItem().toString().equals("Năm")) {
            String dayMonth = makeDayMonthString(day, month);
            if (dayMonth.equals(x))
                count = 1;
        }
        return count;
    }

    private void createLineData(LineData lineData, List<Entry> favoriteEntries, List<Entry> rateEntries, List<Entry> commentEntries) {
        String option = spSelectItem.getSelectedItem().toString();

        if (option.equals("Tất cả") || option.equals("Lượt thích")) {
            lineData.addDataSet(createDataSet(favoriteEntries, "Yêu thích", Color.RED));
        }
        if (option.equals("Tất cả") || option.equals("Lượt đánh giá")) {
            lineData.addDataSet(createDataSet(rateEntries, "Đánh giá", Color.BLUE));
        }
        if (option.equals("Tất cả") || option.equals("Lượt bình luận")) {
            lineData.addDataSet(createDataSet(commentEntries, "Bình luận", Color.GREEN));
        }
    }

    private LineDataSet createDataSet(List<Entry> entries, String label, int color) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(color);
        return dataSet;
    }

    private void setupLegend() {
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(16f);
    }

    private void setupDescription() {
        String optionItem = spSelectItem.getSelectedItem().toString();
        String optionTime = spSelectTime.getSelectedItem().toString();
        String xDes = getXAxisDescription(optionTime);
        String yDes = getYAxisDescription(optionItem);
        String desStr = "Trục x: " + xDes + " - Trục y: " + yDes;

        Description description = new Description();
        description.setText(desStr);
        description.setTextSize(12f);
        description.setTextColor(Color.WHITE);
        description.setPosition(lineChart.getWidth() - 50f, 110f);
        lineChart.setDescription(description);
    }

    private String getXAxisDescription(String optionTime) {
        if (optionTime.equals("Ngày")) return "giờ:phút:giây";
        else if (optionTime.equals("Tháng")) return "ngày";
        else if (optionTime.equals("Năm")) return "ngày/tháng";
        return "";
    }

    private String getYAxisDescription(String optionItem) {
        if (optionItem.equals("Tất cả")) return "số lượng";
        return optionItem.toLowerCase();
    }

    private void updateChart(LineData lineData) {
        lineChart.setData(lineData);
        lineChart.getData().setValueTextColor(Color.WHITE);
        lineChart.getData().setValueTextSize(12f);
        lineChart.invalidate();
        showAllAmount();
    }

//    Tạo dữ liệu cho trục x
    private void createTimeListAndOptionCount(String option) {
        xValues = new ArrayList<>();
        List<Date> dateList = new ArrayList<>();

        if (option.equals("Tất cả")) {
            if (favorites != null) dateList.addAll(getUpdateDates(favorites));
            if (rates != null) dateList.addAll(getUpdateDates(rates));
            if (comments != null) dateList.addAll(getUpdateDates(comments));
        } else if (option.equals("Lượt thích")) {
            if (favorites != null) dateList.addAll(getUpdateDates(favorites));
        } else if (option.equals("Lượt đánh giá")) {
            if (rates != null) dateList.addAll(getUpdateDates(rates));
        } else if (option.equals("Lượt bình luận")) {
            if (comments != null) dateList.addAll(getUpdateDates(comments));
        }

        Collections.sort(dateList);

        addToXValues(dateList, btnDate);
    }

    private List<Date> getUpdateDates(List<?> list) {
        List<Date> dateList = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Favorite) {
                dateList.add(((Favorite) obj).getUpdatedAt());
            } else if (obj instanceof Rate) {
                dateList.add(((Rate) obj).getUpdatedAt());
            } else if (obj instanceof Comment) {
                dateList.add(((Comment) obj).getUpdatedAt());
            }
        }
        return dateList;
    }

    private void addToXValues(List<Date> dateList, Button btn) {
        String selectedDate = btn.getText().toString();
        for (Date date : dateList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String xValue = getXValue(selectedDate, calendar);
            if (xValue != null && !xValues.contains(xValue)) {
                xValues.add(xValue);
            }
        }
    }

    private String getXValue(String selectedDate, Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (selectedDate.equals(makeDateString(day, month, year)))
            return makeHMString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        else if (selectedDate.equals(makeMonthYearString(month, year)))
            return String.valueOf(day);
        else if (selectedDate.equals(makeYearString(year)))
            return makeDayMonthString(day, month);
        else return "";
    }

    private void getFavoriteById(String mvId) {
        FavoriteService favoriteService = ApiService.createService(FavoriteService.class);
        Call<ApiResponse<List<Favorite>>> call = favoriteService.getMovieFavorite(Integer.parseInt(mvId));

        call.enqueue(new Callback<ApiResponse<List<Favorite>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Favorite>>> call, Response<ApiResponse<List<Favorite>>> response) {
                if (response.isSuccessful()) {
                    favorites = response.body().getData();
                    createLineChart();
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
                if (response.isSuccessful()) {
                    rates = response.body().getData();
                    createLineChart();
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
                if (response.isSuccessful()) {
                    comments = response.body().getData();
                    createLineChart();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comment>>> call, Throwable t) {

            }
        });
    }

//    Thiết lập dữ liệu cho hai Spinner
    private void createSelect() {
        ArrayAdapter<String> arrayAdapter;

        // Thiết lập dữ liệu cho Spinner spSelectItem
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arrays.asList("Tất cả", "Lượt thích", "Lượt đánh giá", "Lượt bình luận"));
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spSelectItem.setAdapter(arrayAdapter);

        // Thiết lập dữ liệu cho Spinner spSelectTime
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arrays.asList("Ngày", "Tháng", "Năm"));
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spSelectTime.setAdapter(arrayAdapter);
    }

    //    Tạo custom time picker
    private void setTimePicker() {
        spSelectTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                txtEntryDetail.setVisibility(View.GONE);
                entryInfoPlacement.setVisibility(View.GONE);
                if (item.equals("Ngày")) {
                    btnDate.setText(getTodaysDate("getTodaysDate"));
                }
                else if (item.equals("Tháng")) {
                    btnDate.setText(getTodaysDate("getTodaysMonthYear"));
                }
                else if (item.equals("Năm")) {
                    btnDate.setText(getTodaysDate("getTodaysYear"));
                }
                initDatePicker(item);
                createLineChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnDate.setOnClickListener(view -> datePickerDialog.show());
    }

    private void initDatePicker(String option) {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = "";
            if (option.equals("Ngày")) {
                date = makeDateString(day, month, year);
            } else if (option.equals("Tháng")) {
                date = makeMonthYearString(month, year);
            } else if (option.equals("Năm")) {
                date = makeYearString(year);
            }
            btnDate.setText(date);
            createLineChart();
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = createDatePickerDialog(this, style, dateSetListener, day, month, year, option);
    }

    private DatePickerDialog createDatePickerDialog(Context context, int style, DatePickerDialog.OnDateSetListener dateSetListener, int day, int month, int year, String option) {
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        if (option.equals("Tháng")) {
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        }
        else if (option.equals("Năm")) {
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("month", "id", "android")).setVisibility(View.GONE);
        }
        return datePickerDialog;
    }

    private String getTodaysDate(String format) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date = "";
        if (format.equals("getTodaysDate")) {
            date = makeDateString(day, month, year);
        }
        else if (format.equals("getTodaysMonthYear")) {
            date = makeMonthYearString(month, year);
        }
        else if (format.equals("getTodaysYear")) {
            date = makeYearString(year);
        }
        return date;
    }

//    Tạo date format
    private String makeDateString(int day, int month, int year) {return getMonthFormat(month) + " " + day + " " + year;}
    private String makeMonthYearString(int month, int year) {return getMonthFormat(month) + " " + year;}
    private String makeYearString(int year) {return String.valueOf(year);}
    private String makeHMString(int hour, int minute) {return hour + ":" + minute;}
    private String makeDayMonthString(int day, int month) {return day + "/" + month;}
    private String getMonthFormat(int month) {
        final String[] MONTH_NAMES = {"", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        if (month >= 1 && month <= 12) {
            return MONTH_NAMES[month];
        }
        return "JAN"; // Mặc định trả về "JAN" nếu không hợp lệ
    }

    private void setControl() {
        spSelectItem = findViewById(R.id.sp_select_item);
        spSelectTime = findViewById(R.id.sp_select_time);
        lineChart = findViewById(R.id.line_chart);
        barChart = findViewById(R.id.bar_chart);
        btnBack = findViewById(R.id.btn_back);
        btnDate = findViewById(R.id.btn_date);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnExport = findViewById(R.id.btn_export);
        txtEntryDetail = findViewById(R.id.txt_entry_detail);
        txtDetailFav = findViewById(R.id.txt_detail_fav);
        txtDetailRate = findViewById(R.id.txt_detail_rate);
        txtDetailCmt = findViewById(R.id.txt_detail_cmt);
        entryInfoPlacement = findViewById(R.id.entry_info_placement);
    }
}