package com.enes.chattapp.adaptir;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.enes.chattapp.R;
import com.enes.chattapp.fragment.OtherFrofileFragment;
import com.enes.chattapp.models.kullanicilar;
import com.enes.chattapp.utils.ChangeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdaptir extends RecyclerView.Adapter<UserAdaptir.Viewholder> {

private List<String> userKeyList;
private Activity activity;
private Context context;
private FirebaseDatabase firebaseDatabase;
private DatabaseReference databaseReference;

public UserAdaptir(List<String> userKeyList,Activity activity,Context context){
    this.userKeyList = userKeyList;
    this.activity = activity;
    this.context = context;
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference();

}
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.userlayout,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull Viewholder holder, int position) {

    //holder.textView.setText(userKeyList.get(position).toString()); // keyleri textviewlara getirdik

        databaseReference.child("kullanıcılar").child(userKeyList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kullanicilar k = snapshot.getValue(kullanicilar.class);

                Boolean userState = Boolean.parseBoolean(snapshot.child("state").getValue().toString());

                if(userState == true){
                    holder.online_image.setImageResource(R.drawable.online_icon);
                }
                else{
                    holder.online_image.setImageResource(R.drawable.offline_icon);
                }

                if(!k.getIsim().equals("")){
                    Picasso.get().load(k.getResim()).into(holder.user_image);
                    holder.textView.setText(k.getIsim());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment fragment = new ChangeFragment(context);
                fragment.changeWithparemeter(new OtherFrofileFragment(), userKeyList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userKeyList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
      CardView cardView;
      TextView textView;
      CircleImageView user_image,online_image;
      LinearLayout user_layout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textView = itemView.findViewById(R.id.username_textview);
            user_image = itemView.findViewById(R.id.user_image);
            user_layout = itemView.findViewById(R.id.user_layout);
            online_image = itemView.findViewById(R.id.online_image);
        }
    }
}
