package com.example.RealFilm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.RealFilm.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import wseemann.media.FFmpegMediaMetadataRetriever;


public class WatchMoviesActivity extends AppCompatActivity {
    private SimpleExoPlayerView videoView;
    private SeekBar seekBar_video, seek_volume;
    private TextView textView_duration, textView_current_duration, text_show_volume, movies_name;
    private ImageView btn_play_pause, btn_replay, btn_forward, btn_back, imageView_volume;
    private LinearLayout linearLayoutController;
    private FrameLayout frameLayout;
    private Uri video;
    private SimpleExoPlayer exoPlayer;
    private AudioManager audioManager;
    private boolean checkSH = true, checkPlayPause = true;
    private Handler mHandler;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_movies);

        initUi();
        showhideControl();
        play();
        startPlayer();
        btn_play_pause_OnClick();
        btnForwardOnClick();
        btnReplayOnClick();
        btnBackOnClick();
        updateSeekBar();
        setSeekBar_video();
        seekBarVolume();
        muteVolumOnClick();
        exoPlayer.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    long realDurationMillis = exoPlayer.getDuration();
                    seekBar_video.setMax((int)realDurationMillis);
                    textView_duration.setText(getDuration());
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.stop();
    }

    private void initUi(){
        videoView = findViewById(R.id.videoView);
        seekBar_video = findViewById(R.id.seekBar_video);
        textView_duration = findViewById(R.id.textView_duration);
        textView_current_duration = findViewById(R.id.textView_duration_now);
        btn_play_pause = findViewById(R.id.btn_play_pause);
        btn_replay = findViewById(R.id.btn_replay);
        btn_forward = findViewById(R.id.btn_forward);
        btn_back = findViewById(R.id.btn_back);
        linearLayoutController = findViewById(R.id.linearLayoutController);
        frameLayout = findViewById(R.id.frameLayout);
        seek_volume = findViewById(R.id.seek_volume);
        imageView_volume = findViewById(R.id.imageView_volume);
        text_show_volume = findViewById(R.id.text_show_volume);
        movies_name = findViewById(R.id.movies_name);
        mHandler = new Handler();
        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Intent intent = getIntent();
        String str = intent.getStringExtra("link");
        String str_name = intent.getStringExtra("name");
        String str_year = intent.getStringExtra("year");
        movies_name.setText(str_name + " (" +str_year + ")");

        getData(str_name);
        video = Uri.parse(str);

    }

    private void getData(String str){

//        mDatabase.child(str).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()){
//                    if (task.getResult().exists()){
//                        DataSnapshot dataSnapshot = task.getResult();
//                        int viewcount = Integer.parseInt(dataSnapshot.child("viewcount").getValue().toString());
//                        mDatabase.child(str).child("viewcount").setValue(viewcount + 1);
//                    }
//                }
//
//            }
//        });

    }

    private void seekBarVolume(){
        seek_volume.setProgress(audioManager.getStreamVolume(exoPlayer.getAudioStreamType()));
        seek_volume.setMax(audioManager.getStreamMaxVolume(exoPlayer.getAudioStreamType()));
        text_show_volume.setText(String.valueOf(exoPlayer.getVolume()));
        float MAX_VOLUME = audioManager.getStreamMaxVolume(exoPlayer.getAudioStreamType());
        setVolume(MAX_VOLUME);
        seek_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(exoPlayer.getAudioStreamType(), i, 0);
                setVolume(MAX_VOLUME);
                checkSH = false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                checkSH = true;
            }
        });
    }

    private void muteVolumOnClick(){
        imageView_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioManager.setStreamVolume(exoPlayer.getAudioStreamType(), 0, 0);
                seek_volume.setProgress(0);
                float MAX_VOLUME = audioManager.getStreamMaxVolume(exoPlayer.getAudioStreamType());
                setVolume(MAX_VOLUME);
            }
        });
    }

    private void setVolume(float MAX_VOLUME){
        float VOLUME = (audioManager.getStreamVolume(exoPlayer.getAudioStreamType()) / MAX_VOLUME) *100;
        String formattedStringVolume = String.format("%.0f", VOLUME);

        if (VOLUME == 0){
            imageView_volume.setBackgroundResource(R.drawable.ic_round_volume_off_30);
        } else {
            imageView_volume.setBackgroundResource(R.drawable.ic_round_volume_up_24);
        }
        text_show_volume.setText(formattedStringVolume + "%");
    }

    private void btnBackOnClick(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.stop();
                onBackPressed();
                finish();
            }
        });
    }

    private void btnReplayOnClick(){
        btn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlayer();
                exoPlayer.seekTo(exoPlayer.getCurrentPosition() - 30000);
                startPlayer();
            }
        });
    }

    private void btnForwardOnClick(){
        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlayer();
                exoPlayer.seekTo(exoPlayer.getCurrentPosition() + 30000);
                startPlayer();
            }
        });
    }

    private void updateSeekBar(){
        WatchMoviesActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(exoPlayer != null){
                    seekBar_video.setProgress((int)exoPlayer.getCurrentPosition());
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private void setSeekBar_video(){
        seekBar_video.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView_current_duration.setText(formatTime((long) seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pausePlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startPlayer();
                exoPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private void play(){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
        videoView.setPlayer(exoPlayer);
        exoPlayer.prepare(mediaSource);
    }


    private void btn_play_pause_OnClick(){
        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPlayPause){
                    btn_play_pause.setBackground(getDrawable(R.drawable.ic_round_play_arrow_24));
                    pausePlayer();
                    checkPlayPause = false;
                }
                else {
                    linearLayoutController.setVisibility(View.VISIBLE);
                    btn_play_pause.setBackground(getDrawable(R.drawable.ic_round_pause_24));
                    startPlayer();
                    checkPlayPause = true;
                }
            }
        });
    }

    private void pausePlayer(){
        exoPlayer.setPlayWhenReady(false);
    }

    private void startPlayer(){
        exoPlayer.setPlayWhenReady(true);
    }

    public void showhideControl(){
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSH){
                    linearLayoutController.setVisibility(View.INVISIBLE);
                    checkSH = false;
                }
                else {
                    linearLayoutController.setVisibility(View.VISIBLE);
                    checkSH = true;
                }
            }
        });
    }

    public String getDuration(){
        FFmpegMediaMetadataRetriever mFFmpegMediaMetadataRetriever = new FFmpegMediaMetadataRetriever();
        mFFmpegMediaMetadataRetriever.setDataSource(video.toString());
        String mVideoDuration =  mFFmpegMediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
        long mTimeInMilliseconds= Long.parseLong(mVideoDuration);
        return formatTime(mTimeInMilliseconds);
    }

    public String formatTime(Long mTimeInMilliseconds){
        String duration = String.format("%d:%d:%d",
                TimeUnit.MILLISECONDS.toHours(mTimeInMilliseconds),
                TimeUnit.MILLISECONDS.toMinutes(mTimeInMilliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mTimeInMilliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(mTimeInMilliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mTimeInMilliseconds)));

        return duration;
    }

//    public long getTime(){
//        FFmpegMediaMetadataRetriever mFFmpegMediaMetadataRetriever = new FFmpegMediaMetadataRetriever();
//        mFFmpegMediaMetadataRetriever.setDataSource(video.toString());
//        String mVideoDuration =  mFFmpegMediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
//        long mTimeInMilliseconds= Long.parseLong(mVideoDuration);
//
//        return mTimeInMilliseconds;
//    }

}