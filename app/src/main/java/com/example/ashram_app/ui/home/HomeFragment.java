package com.example.ashram_app.ui.home;

import android.app.AlertDialog;
import android.content.Context;
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

import com.example.ashram_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.ashram_app.Value.admin1UID;

public class HomeFragment extends Fragment {
    FirebaseFirestore db;

    RecyclerView recyclerView;
    ArrayList<DataSetList> arrayList;
    private MenuItem action_addVideo;
    private MenuItem search;
    Context context;
    String URL;
    Query query;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recyclerview_Home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        arrayList = new ArrayList<>();
        db.collection("YoutubeLink")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StringBuffer sb = new StringBuffer(document.getData().toString());
                                sb.delete(0, 12);
                                sb.setLength(sb.length() - 1);
                                DataSetList dataSetList = new DataSetList(sb.toString());
                                arrayList.add(dataSetList);
                            }
                            YoutubeAdapter youtubeAdapter = new YoutubeAdapter(arrayList, getActivity().getApplication());
                            recyclerView.setAdapter(youtubeAdapter);
                        } else {
                            Toast.makeText(getActivity(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        search = menu.findItem(R.id.search_firebase);
        search.setVisible(false);

    }

    public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeViewHolder> {
        ArrayList<DataSetList> arrayList;
        Context context;
        String URL;


        public YoutubeAdapter(ArrayList<DataSetList> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;


        }

        @NonNull
        @Override
        public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.video_item_home, parent, false);
            return new YoutubeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull YoutubeViewHolder holder, int position) {
            final DataSetList current = arrayList.get(position);


            YoutubeViewHolder.webView.loadUrl(current.getYoutubeURL());

            YoutubeViewHolder.webView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    URL = current.getYoutubeURL();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userid = user.getUid();
                    if (userid.equals(admin1UID)) {
                        showDeleteDialog(URL);
                    }
                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        private void showDeleteDialog(String url) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Удалить видео Youtube");
            builder.setMessage("Вы уверены что хотите удалить это видео Youtube?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final String[] docID = new String[1];

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("YoutubeLink").whereEqualTo("youtubeURL", url)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            docID[0] = document.getId();

                                        }

                                        Toast.makeText(getActivity(), docID[0].toString(), Toast.LENGTH_SHORT).show();

                                        db.collection("YoutubeLink").document(docID[0].toString())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity(), "Видео удалено", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Не удалось удалить видео", Toast.LENGTH_SHORT).show();
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


}
