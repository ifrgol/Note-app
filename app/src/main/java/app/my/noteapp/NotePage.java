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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotePage extends AppCompatActivity {

    EditText title_edit,desc_edit;
    TextView date;
    ImageView save;
    Note note;
    ProgressBar progress;
    FirebaseFirestore db;

    boolean isnew = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        init();
        save.setOnClickListener(e->{

            if(note.getTitle().equals(title_edit.getText().toString())   && note.getDescription().equals(desc_edit.getText().toString())){
                //Toast.makeText(this, "تغییر نداره", Toast.LENGTH_SHORT).show();
                    finish();
            }else{
                progress.setVisibility(View.VISIBLE);
               // Toast.makeText(this, "تغییر داده شده است", Toast.LENGTH_SHORT).show();
                UpdateNote();
            }
        });

    }
    private  void UpdateNote(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,YYYY");
        String strDate = sdf.format(c.getTime());
       DocumentReference doc = db.collection("notes").document(note.getId());
       note.setTitle(title_edit.getText().toString());
       note.setDescription(desc_edit.getText().toString());
       doc.update("title",note.getTitle());
       doc.update("description",note.getDescription());
       doc.update("date",strDate).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               progress.setVisibility(View.INVISIBLE);
               Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_LONG).show();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progress.setVisibility(View.INVISIBLE);
               Toast.makeText(getApplicationContext(), "error: "+e.getMessage(), Toast.LENGTH_LONG).show();

           }
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
        Bundle data = getIntent().getExtras();
        if(data.containsKey("note")){
            note = (Note) data.getParcelable("note");
            title_edit.setText(note.getTitle());
            desc_edit.setText(note.getDescription());
            date.setText(note.getDate());
        }else{
            note = new Note();
        }


        save.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
    }
}