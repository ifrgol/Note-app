package app.my.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    ImageView exit;
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notelist);

        init();

        ReadData();

        exit.setOnClickListener(e->{
            OnLogout();
        });
    }

    private void init(){
        db = FirebaseFirestore.getInstance();
        exit = findViewById(R.id.exit);


        rec = findViewById(R.id.rec);
        empty = findViewById(R.id.empty);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rec.setLayoutManager(linearLayoutManager);

    }
    private void ReadData(){

        Query query = db.collection("notes").whereEqualTo("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Note, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(FriendsHolder holder, int position, Note model) {
               // progressBar.setVisibility(View.GONE);
                empty.setVisibility(View.INVISIBLE);
                holder.textName.setText(model.getTitle());
                holder.date.setText(model.getDate());
                holder.desc.setText(model.getDescription());

                model.setId(response.getSnapshots().getSnapshot(position).getId());
                holder.itemView.setOnClickListener(v -> {
                    Intent i = new Intent(getBaseContext(),NotePage.class);
                    i.putExtra("note",model);

                    startActivity(i);
                });

                holder.delete.setOnClickListener(e->{
                    DeleteNote(model.getId());
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

    public void onCreate(View view) {
        Intent i = new Intent(this,NotePage.class);
        startActivity(i);
    }

    public class FriendsHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView date;
        TextView desc;
        ImageView delete;

        public FriendsHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.desc);
            delete = itemView.findViewById(R.id.delete);

        }
    }

public void DeleteNote(String id){
    db.collection("notes").document(id)
            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {

            Toast.makeText(getApplicationContext(), "Note deleted !",
                    Toast.LENGTH_SHORT).show();

        }
    });
}
@Override
public void onStart() {
    super.onStart();
    empty.setVisibility(View.VISIBLE);
    adapter.startListening();


}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public void OnLogout() {

        FirebaseAuth.getInstance().signOut();
        finish();
    }
}