package com.example.RealFilm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.RealFilm.R;
import com.example.RealFilm.model.Rate;

import java.util.Calendar;
import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {
    Context context;
    List<Rate> rateList;

    public RateAdapter(Context context, List<Rate> rateList) {
        this.context = context;
        this.rateList = rateList;
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_entry_rate, parent, false);
        return new RateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        Rate rate = rateList.get(position);
        if (rate.getUser().getPhotoURL() != null) {
            Glide.with(holder.itemView)
                    .load(rate.getUser().getPhotoURL())
                    .circleCrop()
                    .into(holder.imgAvatar);
        }
        else {
            Glide.with(holder.itemView).load(context.getResources().getDrawable(R.drawable.default_avatar))
                    .circleCrop()
                    .into(holder.imgAvatar);
        }
        holder.txtUsername.setText(rate.getUser().getName());

        float AVERRAGE = rate.getRating();
        if (AVERRAGE > 4) {
            setStar5(holder);
        } else {
            if (AVERRAGE > 3) {
                setStar4(holder);
            } else {
                if (AVERRAGE > 2) {
                    setStar3(holder);
                } else {
                    if (AVERRAGE > 1) {
                        setStar2(holder);
                    } else {
                        if (AVERRAGE == 0) {
                            setStar0(holder);
                        } else {
                            setStar1(holder);
                        }
                    }
                }
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rate.getUpdatedAt());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        holder.txtTime.setText(makeFullDateString(hour, minute, day, month, year));
    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public static class RateViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, star1, star2, star3, star4, star5;
        TextView txtUsername, txtTime;
        LinearLayout layoutTransBg;

        public RateViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            txtUsername = itemView.findViewById(R.id.txt_username);
            star1 = itemView.findViewById(R.id.star_1);
            star2 = itemView.findViewById(R.id.star_2);
            star3 = itemView.findViewById(R.id.star_3);
            star4 = itemView.findViewById(R.id.star_4);
            star5 = itemView.findViewById(R.id.star_5);
            txtTime = itemView.findViewById(R.id.txt_time);
            layoutTransBg = itemView.findViewById(R.id.layout_trans_bg);
        }
    }

    private void setStar5(RateViewHolder holder) {
        holder.star1.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star2.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star3.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star4.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star5.setBackgroundResource(R.drawable.ic_round_star_24);
    }

    private void setStar4(RateViewHolder holder) {
        holder.star1.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star2.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star3.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star4.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar3(RateViewHolder holder) {
        holder.star1.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star2.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star3.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar2(RateViewHolder holder) {
        holder.star1.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star2.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar1(RateViewHolder holder) {
        holder.star1.setBackgroundResource(R.drawable.ic_round_star_24);
        holder.star2.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar0(RateViewHolder holder) {
        holder.star1.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star2.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        holder.star5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private String makeFullDateString(int hour, int minute, int day, int month, int year) {
        return hour + ":" + minute + ", "
                + day + "/" + month + "/" + year;
    }
}
