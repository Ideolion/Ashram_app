package com.example.ashram_app.AddYoutubeVideo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ashram_app.MainActivity;
import com.example.ashram_app.R;
import com.example.ashram_app.ui.home.DataSetList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddYoutubeVideo extends AppCompatActivity {
    EditText YoutubeURL;
    String URL;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_youtube_video);
    }

    public void back(View view) {
        Intent i = new Intent(AddYoutubeVideo.this, MainActivity.class);
        startActivity(i);
        }

    public void UploadLink(View view) {
        YoutubeURL = findViewById(R.id.YoutubeURL);
        URL = YoutubeURL.getText().toString();
        DataSetList dataSetList = new DataSetList(URL);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("YoutubeLink")
                .add(dataSetList)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Ссылка загружена", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}