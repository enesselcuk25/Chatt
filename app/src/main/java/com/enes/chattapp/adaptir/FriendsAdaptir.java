package com.enes.chattapp.adaptir;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.enes.chattapp.R;
import com.enes.chattapp.models.kullanicilar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdaptir extends RecyclerView.Adapter<FriendsAdaptir.Viewholder> {

private List<String> userKeyList;
private Activity activity;
private Context context;
private FirebaseDatabase firebaseDatabase;
private DatabaseReference databaseReference;

private FirebaseUser firebaseUser; //ekleme ve reddetme için userid almamız gerekecek
private FirebaseAuth firebaseAuth;
String userId;

public FriendsAdaptir(List<String> userKeyList, Activity activity, Context context){
    this.userKeyList = userKeyList;
    this.activity = activity;
    this.context = context;
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference();


    firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();
    userId = firebaseUser.getUid();  //idileri aldık

}
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.friends,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull Viewholder holder, int position) {



        databaseReference.child("kullanıcılar").child(userKeyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String userName = snapshot.child("isim").getValue().toString();
               String userImage = snapshot.child("resim").getValue().toString();
               Boolean stateUser = Boolean.parseBoolean(snapshot.child("state").getValue().toString());

               if(stateUser == true){
                   holder.live_image.setImageResource(R.drawable.online_icon);
               }
               else{
                   holder.live_image.setImageResource(R.drawable.offline_icon);
               }

                    Picasso.get().load(userImage).into(holder.friends_image);
                    holder.friends_textView.setText(userName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }



    @Override
    public int getItemCount() {
        return userKeyList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

      TextView friends_textView;
      CircleImageView friends_image,live_image;



        public Viewholder(@NonNull View itemView) {
            super(itemView);


            friends_image = itemView.findViewById(R.id.friends_image);
            live_image = itemView.findViewById(R.id.live_image);
            friends_textView = itemView.findViewById(R.id.friends_textview);




        }
    }
}
