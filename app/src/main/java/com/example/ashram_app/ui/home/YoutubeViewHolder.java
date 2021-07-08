package com.example.ashram_app.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashram_app.R;
import com.example.ashram_app.ui.gallery.ViewHolder;

public class YoutubeViewHolder extends RecyclerView.ViewHolder {
    static WebView webView;
    ImageView imageViewFullScreen;

    // static Button button;
/*модификатор статик тут не должен быть, но без него в классе ютуб адаптер не видно этих кнопок и веб вью, проверить как будет время*/
    @SuppressLint("SetJavaScriptEnabled")
    public YoutubeViewHolder(@NonNull View itemView) {
        super(itemView);
        webView = itemView.findViewById(R.id.video_view);
       // button = itemView.findViewById(R.id.fullscreen);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);



        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnItemLongClick(view,getAdapterPosition());
                return false;
            }
        });

        imageViewFullScreen = itemView.findViewById(R.id.youtube_fullscreen_icon);

        imageViewFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Youtube_fullscreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

               // view.getContext().startActivity(intent);

                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            }
        });


    }

    private static ViewHolder.ClickListener mClickListener;

    public interface ClickListener {
        void OnItemLongClick(View view, int position);


    }

    public static void SetOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;

    }
}
