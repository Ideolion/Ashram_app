package com.example.ashram_app.ui.meditation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashram_app.R;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.ashram_app.Value.admin1UID;

public class MeditationFragment extends Fragment {
    private MenuItem action_addVideo;
    private MenuItem search;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ProgressBar progressBar;
    ListView listView;
    StorageReference storageReference;
    ArrayList<String> arrayListSongsName = new ArrayList<>();
    ArrayList<String> arrayListSongsUrl = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();


    public MeditationFragment() {
        super(R.layout.fragment_meditation);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_meditation, container, false);

        listView = view.findViewById(R.id.myListView);

        jcPlayerView = view.findViewById(R.id.jcplayer);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference questionsRef = rootRef.collection("Audio");
        questionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        AudioProperties songObj = document.toObject(AudioProperties.class);
                        arrayListSongsName.add(songObj.getAudioName());
                        arrayListSongsUrl.add(songObj.getAudioURL());
                        jcAudios.add(JcAudio.createFromURL(songObj.getAudioName(), songObj.getAudioURL()));
                    }
                    arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListSongsName);
                    listView.setAdapter(arrayAdapter);
                    jcPlayerView.initPlaylist(jcAudios, null);
                } else {
                    Toast.makeText(getActivity(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();

                }


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {


                String name = jcAudios.get(pos).getTitle().toString();
                String URL = jcAudios.get(pos).getPath().toString();
                showDeleteDialogAudio(name, URL);
//                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//                System.out.println(URL);
//                Toast.makeText(getActivity(), "длинный клик по аудио", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        search = menu.findItem(R.id.search_firebase);
        action_addVideo = menu.findItem(R.id.action_addVideo);
        search.setVisible(false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        if (userid.equals(admin1UID)) {
            action_addVideo.setVisible(true);

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showDeleteDialogAudio(String name, String URL) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Удалить аудио запись");
        builder.setMessage("Вы уверены что хотите удалить эту аудио запись");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String[] docID = new String[1];

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Audio").whereEqualTo("audioName", name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        docID[0] = document.getId();

                                    }

                                    Toast.makeText(getActivity(), docID[0].toString(), Toast.LENGTH_SHORT).show();

                                    db.collection("Audio").document(docID[0].toString())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getActivity(), "Аудио запись удалена", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Не удалось удалить аудио", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(getActivity(), "Документ не существует в базе", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(URL);
                storageReference.delete();
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