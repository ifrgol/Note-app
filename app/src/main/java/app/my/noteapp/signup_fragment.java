package app.my.noteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signup_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signup_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signup_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signup_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static signup_fragment newInstance(String param1, String param2) {
        signup_fragment fragment = new signup_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    EditText pass_text;
    EditText email_text;
    TextView already;
    Context context;
    Button singup_btn;
    ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup_fragment, container, false);

        context = getContext();
        already = v.findViewById(R.id.already);
        singup_btn = v.findViewById(R.id.singup_btn);
        pass_text = v.findViewById(R.id.pass_text);
        email_text = v.findViewById(R.id.email_text);
        progress = v.findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

        singup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);

                signup(email_text.getText().toString(),pass_text.getText().toString());
            }
        });
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigation.findNavController(view).navigate(R.id.action_login_fragment_to_signup_fragment);
                Navigation.findNavController(view).popBackStack();

            }
        });
        return v;
    }

    public void signup(String email,String password) {

//        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
      //  Toast.makeText(context, user, Toast.LENGTH_SHORT).show();
        /*
        sign up
         */
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(context, "Succesful", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);

                Intent i = new Intent(context,Notelist_Activity.class);
                startActivity(i);            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.setVisibility(View.INVISIBLE);

                        Toast.makeText(context, "fail"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}