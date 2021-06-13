package com.example.ashram_app.ui.favoriteVideo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashram_app.AddVideo.VideoProperties;
import com.example.ashram_app.R;
import com.example.ashram_app.ui.gallery.ViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;


public class FavoriteVideo extends Fragment {
    private MenuItem search;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    Query query;
    String name, URL;
    String userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_Gallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();
        query = FirebaseFirestore.getInstance()
                .collection("Favorite").document(userid).collection("VideoFavorite");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirestoreRecyclerOptions<VideoProperties> response = new FirestoreRecyclerOptions.Builder<VideoProperties>()
                .setQuery(query, VideoProperties.class)
                .build();
        FirestoreRecyclerAdapter<VideoProperties, ViewHolder> adapter = new FirestoreRecyclerAdapter
                <VideoProperties, ViewHolder>(response) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ViewHolder viewHolder, int i, VideoProperties videoProperties) {

                viewHolder.setExoplayer(getActivity().getApplication(), videoProperties.getName(), videoProperties.getVideourl());
                viewHolder.SetOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void OnItemLongClick(View view, int position) {
                        name = getItem(position).getName();
                        URL = getItem(position).getVideourl();
                        showDeleteDialogVideoFavorite(name, URL, userid);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        search = menu.findItem(R.id.search_firebase);
        search.setVisible(false);
    }






    private void showDeleteDialogVideoFavorite(String name, String URL, String userid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Удалить из избранного");
        builder.setMessage("Вы уверены что хотите удалить эту видео запись из избранного?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String[] docID = new String[1];
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Favorite").document(userid).collection("VideoFavorite").whereEqualTo("name", name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        docID[0] = document.getId();
                                    }
                                    Toast.makeText(getActivity(), docID[0].toString(), Toast.LENGTH_SHORT).show();
                                    db.collection("Favorite").document(userid).collection("VideoFavorite").document(docID[0].toString())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getActivity(), "Видео запись удалена из избранного", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Не удалось удалить видео из избранного", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(getActivity(), "Документ не существует в базе", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        builder.show();
    }
}
