package com.enes.chattapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.enes.chattapp.R;
import com.enes.chattapp.activity.Anactivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class loginUpActivity extends AppCompatActivity {
private EditText editTextemail,editTextparola;
private Button buttonloginup;
FirebaseAuth firebaseAuth;
DatabaseReference databaseReference;
FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_up);
        tanimla();

    }
    public  void tanimla(){
        editTextemail =findViewById(R.id.edittextemail);
        editTextparola =findViewById(R.id.edittextparola );
        buttonloginup = findViewById(R.id.buttonloginup);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        buttonloginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextemail.getText().toString();
                String parola= editTextparola.getText().toString();

                if(!email.equals("") && !parola.equals("")){  //email ve parola boş değil ise
                    editTextemail.setText(""); //kayit oldutan sonra boş bırak edittexti
                    editTextparola.setText("");

                    kayitol(email,parola);
                }
                else{ //parola boş ise
                    Toast.makeText(getApplicationContext(),"Alanlar boş Geçilemez",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    public void kayitol(String email,String parola){

        Log.i("test",""+firebaseAuth);
        firebaseAuth.createUserWithEmailAndPassword(email,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //paremtre başarılı ise

                    databaseReference = firebaseDatabase.getInstance().getReference().child("kullanıcılar").child(firebaseAuth.getUid()); //kullanıcılar ve kullancıı id aldık oluşturduk
                    Map map = new HashMap();
                    map.put("resim","null");
                    map.put("isim","null");
                    map.put("egitim","null");
                    map.put("dogumtarih","null");
                    map.put("hakkımda","null");

                    databaseReference.setValue(map);


                    Intent intent = new Intent(getApplicationContext(), Anactivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}