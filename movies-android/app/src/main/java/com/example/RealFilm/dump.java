package com.example.RealFilm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class dump {

//    private void addToXValuesDayOption(List<Date> dateList) {
//        String selectedDate = btnDate.getText().toString();
//        for (Date date : dateList) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            int month = calendar.get(Calendar.MONTH) + 1;
//            int year = calendar.get(Calendar.YEAR);
//            if (selectedDate.equals(makeDateString(day, month, year))) {
//                optionCount++;
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//                if (!xValues.contains(makeHMString(hour, minute)))
//                    xValues.add(makeHMString(hour, minute));
//            }
//        }
//    }
//
//    private void addToXValuesMonthYearOption(List<Date> dateList) {
//        String selectedMonthYear = btnMonthYear.getText().toString();
//        for (Date date : dateList) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            int month = calendar.get(Calendar.MONTH) + 1;
//            int year = calendar.get(Calendar.YEAR);
//            if (selectedMonthYear.equals(makeMonthYearString(month, year))) {
//                optionCount++;
//                if (!xValues.contains(String.valueOf(day)))
//                    xValues.add(String.valueOf(day));
//            }
//        }
//    }
//
//    private void addToXValuesYearOption(List<Date> dateList) {
//        String selectedYear = btnYear.getText().toString();
//        for (Date date : dateList) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            int month = calendar.get(Calendar.MONTH) + 1;
//            int year = calendar.get(Calendar.YEAR);
//            String data = makeDayMonthString(day, month);
//            if (selectedYear.equals(makeYearString(year))) {
//                optionCount++;
//                if (!xValues.contains(data))
//                    xValues.add(data);
//            }
//        }
//    }

    //    private void exportCurrentChart() {
//        if (lineChart != null && lineChart.getWidth() > 0 && lineChart.getHeight() > 0) {
//            LineChart lineChartDarkMode = niggafyChart(lineChart);
//            Bitmap bitmap = lineChartDarkMode.getChartBitmap();
//            if (bitmap != null) {
//                String fileName = "line_chart_img.png";
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
//                try {
//                    FileOutputStream outputStream = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                    outputStream.flush();
//                    outputStream.close();
//                    Toast.makeText(this, "Biểu đồ đã được lưu thành công", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Lỗi khi lưu biểu đồ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//        else {
//            Toast.makeText(this, "Biểu đồ chưa được vẽ hoặc hiển thị", Toast.LENGTH_SHORT).show();
//        }
//    }

    //    private String getTodaysDate() {
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        month += 1;
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//        return makeDateString(day, month, year);
//    }
//
//    private String getTodaysMonthYear() {
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        month += 1;
//        return makeMonthYearString(month, year);
//    }
//
//    private String getTodaysYear() {
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        return makeYearString(year);
//    }
}
