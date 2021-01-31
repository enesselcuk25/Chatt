package com.enes.chattapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.enes.chattapp.R;
import com.enes.chattapp.adaptir.MessageAdaptir;
import com.enes.chattapp.models.message;
import com.enes.chattapp.utils.getData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattActivity extends AppCompatActivity {

    private EditText chat_editext;
    private FloatingActionButton button_gonder;

    private Button button_back;
    private TextView chat_textview;
    private RecyclerView chat_recylerview;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    List<message> messageList;

    MessageAdaptir messageAdaptir;

    List<String> keyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatt);
        tanimla();
        action();
        loadMessage();
    }

    private void tanimla() {

        chat_editext = findViewById(R.id.chat_edittext);
        button_gonder = findViewById(R.id.button_gonder);

        button_back = findViewById(R.id.button_back);
        chat_textview = findViewById(R.id.chat_textview);
        chat_recylerview = findViewById(R.id.chat_recylerview);

        chat_textview.setText(getusername());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        messageList = new ArrayList<>();

        RecyclerView.LayoutManager manager = new GridLayoutManager(ChattActivity.this,1);
        chat_recylerview.setLayoutManager(manager);

        keyList = new ArrayList<>();
        messageAdaptir = new MessageAdaptir(keyList,ChattActivity.this,ChattActivity.this,messageList);
        chat_recylerview.setAdapter(messageAdaptir);


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChattActivity.this,Anactivity.class);
                startActivity(intent);
                finish();
            }
        });





    }

    public void action(){



        button_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = chat_editext.getText().toString();
                chat_editext.setText("");

                sendMessage(firebaseUser.getUid(),getId(),"text", getData.getData(),false,message);
            }
        });
    }


    public String getusername(){
        Bundle bundle = getIntent().getExtras();
       String username =   bundle.getString("username");
       return username;
    }

    public String getId(){
        String id = getIntent().getExtras().getString("id").toString();
        return id;
    }

    public void sendMessage(final String userId,final String otherId,String textType,String date,Boolean seen,String messageText){

      String messageId = databaseReference.child("Mesajlar").child(userId).child(otherId).push().getKey(); //idimizi aldık

        Map messageMap = new HashMap(); //ekleme işlemlerini yaptık
        messageMap.put("type",textType);
        messageMap.put("seen",seen);
        messageMap.put("time",date);
        messageMap.put("text",messageText);
        messageMap.put("from",userId);

        databaseReference.child("Mesajlar").child(userId).child(otherId).child(messageId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    databaseReference.child("Mesajlar").child(otherId).child(userId).child(messageId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

            }
        });







    }
    public void loadMessage(){
        databaseReference.child("Mesajlar").child(firebaseUser.getUid()).child(getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                message messagemodel = snapshot.getValue(message.class); //tip değişiklği yaparak messagemodel aldık
                messageList.add(messagemodel);
                messageAdaptir.notifyDataSetChanged();
                keyList.add(snapshot.getKey());
                chat_recylerview.scrollToPosition(messageList.size()-1);  //mesaj atarken klavyenin altına inmiyor diye bir kod yazdım
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}