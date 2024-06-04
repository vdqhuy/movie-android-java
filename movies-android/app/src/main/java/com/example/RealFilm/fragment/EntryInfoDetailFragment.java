package com.example.RealFilm.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.RealFilm.R;
import com.example.RealFilm.activity.ChartMovieDetailActivity;
import com.example.RealFilm.adapter.CommentAdapter;
import com.example.RealFilm.adapter.RateAdapter;
import com.example.RealFilm.model.Comment;
import com.example.RealFilm.model.Rate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryInfoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryInfoDetailFragment extends Fragment {
    TextView txt_content;
    RecyclerView rvListEntry;


    private String xValue;
    private List<Rate> rates;
    private List<Comment> comments;
    private String timeOption;
    private RateAdapter rateAdapter;
    private CommentAdapter commentAdapter;

    public String getxValue() {
        return xValue;
    }

    public void setxValue(String xValue) {
        this.xValue = xValue;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTimeOption() {
        return timeOption;
    }

    public void setTimeOption(String timeOption) {
        this.timeOption = timeOption;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry_info_detail, container, false);
        setControl(view);

        if (rates != null) {
            String content = "";
            List<Rate> rateListTemp = new ArrayList<>();
            for (Rate rate : rates) {
                if (filterEntryList(rate.getUpdatedAt())) {
                    content += "Id: " + rate.getId() + ", Rating: " + rate.getRating() + "\n";
                    rateListTemp.add(rate);
                }
            }
            txt_content.setText(content);
            rvListEntry.setLayoutManager(new LinearLayoutManager(getContext()));
            rvListEntry.setHasFixedSize(true);
            rateAdapter = new RateAdapter(getContext(), rateListTemp);
            rvListEntry.setAdapter(rateAdapter);
        }
        else if (comments != null) {
            String content = "";
            List<Comment> cmtListTemp = new ArrayList<>();
            for (Comment cmt : comments) {
                if (filterEntryList(cmt.getUpdatedAt())) {
                    content += "Id: " + cmt.getId() + ", Content: " + cmt.getComment() + "\n";
                    cmtListTemp.add(cmt);
                }
            }
            txt_content.setText(content);
            rvListEntry.setLayoutManager(new LinearLayoutManager(getContext()));
            rvListEntry.setHasFixedSize(true);
            commentAdapter = new CommentAdapter(getContext(), cmtListTemp);
            rvListEntry.setAdapter(commentAdapter);
        }

        return view;
    }

    private boolean filterEntryList(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        boolean match = false;
        if (timeOption.equals("Ngày")) {
            String hourMinute = makeHMString(hour, minute);
            if (hourMinute.equals(xValue))
                match = true;
        }
        else if (timeOption.equals("Tháng")) {
            if (String.valueOf(day).equals(xValue))
                match = true;
        }
        else if (timeOption.equals("Năm")) {
            String dayMonth = makeDayMonthString(day, month);
            if (dayMonth.equals(xValue))
                match = true;
        }
        return match;
    }

    private String makeHMString(int hour, int minute) {
        return hour + ":" + minute;
    }

    private String makeDayMonthString(int day, int month) {
        return day + "/" + month;
    }

    private void setControl(View view) {
        txt_content = view.findViewById(R.id.txt_content);
        rvListEntry = view.findViewById(R.id.rv_list_entry);
    }
}