package com.example.ashram_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ashram_app.AddVideo.AddVideoActivity;
import com.example.ashram_app.AddYoutubeVideo.AddYoutubeVideo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.ashram_app.Value.admin1UID;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MenuItem action_addVideo, addVideoYoutube;
    private MenuItem search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_meditation, R.id.favoriteVideo, R.id.favoriteVideo)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        search = menu.findItem(R.id.search_firebase);
        action_addVideo = menu.findItem(R.id.action_addVideo);
        addVideoYoutube = menu.findItem(R.id.addVideoYoutube);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid=user.getUid();


             if(userid.equals(admin1UID)){
        action_addVideo.setVisible(true);
            addVideoYoutube.setVisible(true);

        }



        return super.onCreateOptionsMenu(menu);




    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }


    public void onClickQuite(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainActivity.this, LogActivity.class);
        startActivity(i);
        finish();
    }


    public void onClickAddVideo(MenuItem item) {
        Intent i = new Intent(MainActivity.this, AddVideoActivity.class);
        startActivity(i);
        //finish();
    }
    public void onClickAddVideoYoutube(MenuItem item) {
        Intent i = new Intent(MainActivity.this, AddYoutubeVideo.class);
        startActivity(i);
        //finish();
    }

    public void Instagram(MenuItem item) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://instagram.com/_u/" + "sergey_stefanovskii"));
            intent.setPackage("com.instagram.android");
            startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/" + "sergey_stefanovskii")));
        }



    }
    public void WhatsApp(MenuItem item) {
        String url = "https://api.whatsapp.com/send?phone="+"+79230155969";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);




    }
}