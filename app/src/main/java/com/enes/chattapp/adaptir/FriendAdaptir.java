package com.enes.chattapp.adaptir;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.enes.chattapp.R;
import com.enes.chattapp.fragment.OtherFrofileFragment;
import com.enes.chattapp.models.kullanicilar;
import com.enes.chattapp.utils.ChangeFragment;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdaptir extends RecyclerView.Adapter<FriendAdaptir.Viewholder> {

private List<String> userKeyList;
private Activity activity;
private Context context;
private FirebaseDatabase firebaseDatabase;
private DatabaseReference databaseReference;

private FirebaseUser firebaseUser; //ekleme ve reddetme için userid almamız gerekecek
private FirebaseAuth firebaseAuth;
String userId;
public FriendAdaptir(List<String> userKeyList, Activity activity, Context context){
    this.userKeyList = userKeyList;
    this.activity = activity;
    this.context = context;
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference();


    firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();
    userId = firebaseUser.getUid();  //idleri aldık

}
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.friend_layout,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull Viewholder holder, int position) {

    //holder.textView.setText(userKeyList.get(position)); // keyleri textviewlara getirdik

        databaseReference.child("kullanıcılar").child(userKeyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kullanicilar k = snapshot.getValue(kullanicilar.class);
                if(!k.getIsim().equals("")){
                    Picasso.get().load(k.getResim()).into(holder.friend_image);
                    holder.friend_textView.setText(k.getIsim());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.friend_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kabulEt(userId,userKeyList.get(position)); //kabul etmek için kişinin idsi ve göneerilen kişinin idsi almamız gerekecek
            }
        });

        holder.friend_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reddet(userId,userKeyList.get(position));   //kabul etmek için kişinin idsi ve göneerilen kişinin idsi almamız gerekecek
            }
        });



    }

    public void kabulEt(String userId,String otherid){

        String pattern = "dd-M-yyyy hh:mm:ss ";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());


    databaseReference.child("arkadaslar").child(userId).child(otherid).child("tarih").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            databaseReference.child("arkadaslar").child(otherid).child(userId).child("tarih").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if(task.isSuccessful()) {
                      Toast.makeText(context,"arkdaşlık isteği kabul ettiniz ",Toast.LENGTH_LONG).show();


                       //kabul ettiğimizde bu işlemler yapsın
                      databaseReference.child("arkadaslik").child(userId).child(otherid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              databaseReference.child("arkadaslik").child(otherid).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {

                                  }
                              });
                          }
                      });

                  }
                }
            });
        }
    });
    }
    public void reddet(String userId, String otherid){

        databaseReference.child("arkadaslik").child(userId).child(otherid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child("arkadaslik").child(otherid).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"arkadaşlık isteğini reddettiniz",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userKeyList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
      CardView cardView;
      TextView friend_textView;
      CircleImageView friend_image;
      Button friend_ekle,friend_red;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            friend_image = itemView.findViewById(R.id.friend_image);
            friend_textView = itemView.findViewById(R.id.fried_textview);
            friend_ekle = itemView.findViewById(R.id.friend_ekle);
            friend_red = itemView.findViewById(R.id.friend_red);


        }
    }
}
