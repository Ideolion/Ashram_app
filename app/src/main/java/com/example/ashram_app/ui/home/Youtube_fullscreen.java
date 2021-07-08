package com.example.ashram_app.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ashram_app.R;

public class Youtube_fullscreen extends Activity {
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_youtube_fullscreen);
        Bundle arguments = getIntent().getExtras();

        StringBuffer sb = new StringBuffer(arguments.get("URL").toString());
        sb.delete(0,17 );
        String url = sb.toString();



        WebView view=(WebView) findViewById(R.id.full_video);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        System.out.println(url);
        String videoStr = "<html><body><br><iframe width=\"650\" height=\"300\" src=\"https://www.youtube.com/embed/"+url+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings ws = view.getSettings();
        ws.setJavaScriptEnabled(true);
        view.loadData(videoStr, "text/html", "utf-8");









    }


    }




