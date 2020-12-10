package app.my.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NotePage extends AppCompatActivity {

    EditText title_edit,desc_edit;
    TextView date;
    ImageView save;
    Note note;
    ProgressBar progress;
    FirebaseFirestore db;
    Calendar c;
    boolean isnew = false;
    SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        init();
        save.setOnClickListener(e->{

            String title = title_edit.getText().toString().trim();
            String desc = desc_edit.getText().toString().trim();
            if(title.matches("") || desc.matches("")){
                Toast.makeText(getApplicationContext(), "Enter some characters", Toast.LENGTH_SHORT).show();
            }else{
                if(isnew){
                    progress.setVisibility(View.VISIBLE);
                    CreateNote();
                }else {
                    if(note.getTitle().equals(title_edit.getText().toString())   && note.getDescription().equals(desc_edit.getText().toString())){

                        finish();
                    }else{
                        progress.setVisibility(View.VISIBLE);

                        UpdateNote();
                    }
                }
            }

        });

    }
    private void CreateNote(){
        c = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd,MM,YYYY");
        String strDate = sdf.format(c.getTime());

        Map<String, Object> newContact = new HashMap<>();
        newContact.put("title", title_edit.getText().toString());
        newContact.put("description", desc_edit.getText().toString());
        newContact.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        newContact.put("date", strDate);
        db.collection("notes").add(newContact).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(getApplicationContext(), "Note saved",
                        Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "ERROR" +e.toString(),
                        Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }
    private  void UpdateNote(){
         c = Calendar.getInstance();
         sdf = new SimpleDateFormat("dd,MM,YYYY");
        String strDate = sdf.format(c.getTime());
       DocumentReference doc = db.collection("notes").document(note.getId());
       note.setTitle(title_edit.getText().toString());
       note.setDescription(desc_edit.getText().toString());
       doc.update("title",note.getTitle());
       doc.update("description",note.getDescription());
       doc.update("date",strDate).addOnSuccessListener(aVoid -> {
           progress.setVisibility(View.INVISIBLE);
           Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_LONG).show();
       }).addOnFailureListener(e -> {
           progress.setVisibility(View.INVISIBLE);
           Toast.makeText(getApplicationContext(), "error: "+e.getMessage(), Toast.LENGTH_LONG).show();

       });
       desc_edit.clearFocus();
       title_edit.clearFocus();
    }
    public void init(){
        db = FirebaseFirestore.getInstance();

        date = findViewById(R.id.date);
        progress = findViewById(R.id.progress);
        save = findViewById(R.id.exit);
        title_edit = findViewById(R.id.title_edit);
        desc_edit = findViewById(R.id.desc_edit);
        progress.setVisibility(View.INVISIBLE);
        if(getIntent().hasExtra("note")){
            Bundle data = getIntent().getExtras();

            note = (Note) data.getParcelable("note");
            title_edit.setText(note.getTitle());
            desc_edit.setText(note.getDescription());
            date.setText(note.getDate());
        }else{
            isnew=true;
            note = new Note();
        }


        save.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
    }
}