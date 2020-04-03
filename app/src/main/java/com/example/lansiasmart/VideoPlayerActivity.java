package com.example.lansiasmart;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {
    private int VIDEO_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            VIDEO_ID = extras.getInt("videoFilename");
            //The key argument here must match that used in the other activity
        }
        try {
            getSupportActionBar().hide();

            VideoView videoView = findViewById(R.id.videoView);
            final MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoView);


            videoView.setMediaController(mediacontroller);
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + VIDEO_ID);
            videoView.requestFocus();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnVideoSizeChangedListener((mp1, width, height) -> {
                        videoView.setMediaController(mediacontroller);
                        mediacontroller.setAnchorView(videoView);

                    });
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.d("API123", "What " + what + " extra " + extra);
                    return false;
                }
            });

            videoView.start();
        } catch (Exception e) {
        }

    }
}
