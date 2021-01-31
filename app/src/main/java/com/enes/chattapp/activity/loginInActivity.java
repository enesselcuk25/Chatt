package com.enes.chattapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.enes.chattapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginInActivity extends AppCompatActivity {
private FirebaseAuth firebaseAuth;
private EditText editTextexemail,editTextparola;
private Button buttonsignup;
private ImageView imageViewsignin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

tanimla();

    }
    public void tanimla(){
        firebaseAuth = FirebaseAuth.getInstance();
        editTextexemail = findViewById(R.id.edittextemail);
        editTextparola = findViewById(R.id.edittextparola);

        buttonsignup = findViewById(R.id.buttonsignup);
        imageViewsignin = findViewById(R.id.imagesignin);

        imageViewsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextexemail.getText().toString();
                String parola = editTextparola.getText().toString();

                if(!email.equals("") && !parola.equals("")){
                    editTextexemail.setText("");
                    editTextparola.setText("");

                    signin(email,parola);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"boş alan bırakmayınız",Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intet = new Intent(getApplicationContext(),loginUpActivity.class);
                startActivity(intet);

            }
        });
    }
    public void signin(String email,String parola){


        firebaseAuth.signInWithEmailAndPassword(email,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Intent intet = new Intent(getApplicationContext(),Anactivity.class);
                startActivity(intet);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}