package app.my.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Notelist_Activity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView rec;
    LinearLayoutManager linearLayoutManager;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notelist);
        db = FirebaseFirestore.getInstance();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,YYYY");
        String strDate = sdf.format(c.getTime());
        rec = findViewById(R.id.rec);
        init();

        ReadData();
    }

    private void init(){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rec.setLayoutManager(linearLayoutManager);

    }
    private void ReadData(){

        Query query = db.collection("notes");
        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Note, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(FriendsHolder holder, int position, Note model) {
               // progressBar.setVisibility(View.GONE);
                holder.textName.setText(model.getTitle());


                holder.itemView.setOnClickListener(v -> {
                    Snackbar.make(rec, model.getTitle(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });
            }

            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recycler_item, group, false);

                return new FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        rec.setAdapter(adapter);
    }

    public class FriendsHolder extends RecyclerView.ViewHolder {
        TextView textName;

        public FriendsHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.title);

        }
    }

//        db.collection("notes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                date_text.setText(""+task.getResult().size());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
@Override
public void onStart() {
    super.onStart();
    adapter.startListening();
}

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    private void addNewContact(){

        Map<String, Object> newContact = new HashMap<>();
        newContact.put("title", "test");
        newContact.put("description", "sdsd");
        newContact.put("userid", "332");
        newContact.put("date", "32334");
       db.collection("notes").add(newContact).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
           @Override
           public void onComplete(@NonNull Task<DocumentReference> task) {
               Toast.makeText(Notelist_Activity.this, "User Registered",
                               Toast.LENGTH_SHORT).show();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(Notelist_Activity.this, "ERROR" +e.toString(),
                               Toast.LENGTH_SHORT).show();
           }
       });
//        db.collection("notes").add(newContact)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(Notelist_Activity.this, "User Registered",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Notelist_Activity.this, "ERROR" +e.toString(),
//                                Toast.LENGTH_SHORT).show();
//                        Log.d("TAG", e.toString());
//                    }
//                });
    }

    public void OnLogout(View view) {
        addNewContact();
//        FirebaseAuth.getInstance().signOut();
//        finish();
    }
}