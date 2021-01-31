package com.enes.chattapp.adaptir;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import com.enes.chattapp.models.message;
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

import javax.xml.transform.sax.TemplatesHandler;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdaptir extends RecyclerView.Adapter<MessageAdaptir.Viewholder> {

//private List<String> userKeyList;
private Activity activity;
private Context context;
private FirebaseDatabase firebaseDatabase;
private DatabaseReference databaseReference;

private FirebaseUser firebaseUser; //ekleme ve reddetme için userid almamız gerekecek
private FirebaseAuth firebaseAuth;
String userId;

List<message> messageList;
Boolean state;

int view_type_sent = 1, view_type_recieved = 2;

public MessageAdaptir(List<String> userKeyList, Activity activity, Context context,List<message> messageList){
//    this.userKeyList = userKeyList;
    this.activity = activity;
    this.context = context;
    this.messageList = messageList;
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference();

    state = false;

    firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();
    userId = firebaseUser.getUid();  //idleri aldık

}
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

                if(viewType == view_type_sent){

                    view = LayoutInflater.from(context).inflate(R.layout.message_send,parent,false);

                    return new Viewholder(view);
                }
                else {
                    view = LayoutInflater.from(context).inflate(R.layout.message_received_layout,parent,false);

                    return new Viewholder(view);
                }

    }

    @Override
    public void onBindViewHolder(final @NonNull Viewholder holder, final int position) {

  //      Log.i("messagetext",messageList.get(position).getText());
    holder.message_text.setText(messageList.get(position).getText());

    }




    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {


      TextView message_text;




        public Viewholder(@NonNull View itemView) {
            super(itemView);

            if (state ==true){
                message_text = itemView.findViewById(R.id.message_send_text);
            }
            else {
                message_text = itemView.findViewById(R.id.message_recieved_text);
            }

        }
    }



    @Override
    public int getItemViewType(int position) {


        if(messageList.get(position).getFrom().equals(userId)){

            state = true;
            return view_type_sent;
        }
        else {

            state = false;
            return  view_type_recieved;
        }
    }
}
