package com.example.lansiasmart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class YoutubePlayerActivity extends AppCompatActivity {
    private String VIDEO_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            VIDEO_URL = extras.getString("videoUrl");
            //The key argument here must match that used in the other activity
        }

        setContentView(R.layout.activity_youtube_player);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebView videoWeb = (WebView) findViewById(R.id.videoWebView);
        videoWeb.getSettings().setJavaScriptEnabled(true);
        videoWeb.setWebChromeClient(new WebChromeClient());
        String embeddData = "<body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"" + VIDEO_URL + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body>";
        videoWeb.loadData(embeddData, "text/html" , "utf-8" );
    }
}
