package com.enes.chattapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enes.chattapp.R;
import com.enes.chattapp.activity.ChattActivity;
import com.enes.chattapp.models.kullanicilar;
import com.enes.chattapp.utils.showToastMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherFrofileFragment extends Fragment {


private View view;
private String otherId,userid;
private TextView textViewisim,textViewdogumtarihi,textViewegitim,textViewhakkimda,textViewtakipciler,textViewarkdaslar,textViewbegenme,textViewarkadasekle;
private ImageView imageView_arkadas_ekle,imageView_mesajGonder,imageView_begen;
private CircleImageView user_profile;

private FirebaseDatabase firebaseDatabase;
private DatabaseReference databaseReference,databaseReference2;

private FirebaseAuth firebaseAuth;
private FirebaseUser firebaseUser;

String kontrol = ""; //arkadaşlık verildi mi değil mi kontrol etmemiz gerekecek
String begenkontrol = "";

showToastMessage toastMessage ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =  inflater.inflate(R.layout.fragment_user_frofile, container, false);
            tanimla();
            action();
            begenisayisi();
           arkadassayisi();
        return view;
    }
    public void tanimla(){

        otherId = getArguments().getString("userid"); //fragment değişikliği için ChangeFragment classında  verdiğim id ile buraya belirttim

        textViewisim = view.findViewById(R.id.textviewisim);
        textViewdogumtarihi = view.findViewById(R.id.textviewdogumtarih);
        textViewegitim = view.findViewById(R.id.textviwegitim);
        textViewhakkimda = view.findViewById(R.id.textviwehakkımda);
        textViewtakipciler = view.findViewById(R.id.textviewtakipci);
        textViewarkdaslar = view.findViewById(R.id.textviewarkadaslar);
        textViewbegenme = view.findViewById(R.id.textviewbegen);
        textViewarkadasekle = view.findViewById(R.id.textviewarkadasekle);

        imageView_arkadas_ekle = view.findViewById(R.id.imageview_arkadasekle);
        imageView_mesajGonder = view.findViewById(R.id.imageview_mesajgonder);
        imageView_begen = view.findViewById(R.id.imageview_begen);
        user_profile = view.findViewById(R.id.userfrofile_image);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference2 = firebaseDatabase.getReference();


        //arkadasekle methodumda string userid çağırıyorum
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userid = firebaseUser.getUid();

        toastMessage = new showToastMessage(getContext());

        //kontrol ediyoruz arkadaşlık ekledi mi değil mi
        databaseReference2.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(userid)){
                   // kontrol = snapshot.child(userid).child("tipi").getValue().toString();

                    kontrol = "istek" ;
                    imageView_arkadas_ekle.setImageResource(R.drawable.takip_off); //imageview değiştirdik
                }
                else{

                   imageView_arkadas_ekle.setImageResource(R.drawable.arkadas_ekle); //imageview değiştirdik
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //arkdaşlar isteği attığımzda arkadaş isteği image değiştirmesi gerekiyor
        databaseReference2.child("arkadaslar").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(otherId)){
                    kontrol = "arkadas";
                    imageView_arkadas_ekle.setImageResource(R.drawable.deleting);
                    textViewarkadasekle.setText("Arkadaşınız");
                }
                else {
                    imageView_arkadas_ekle.setImageResource(R.drawable.arkadas_ekle);
                    textViewarkadasekle.setText("Arkadaş ekle");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //begenme işlemleri
        databaseReference.child("begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userid)){
                    begenkontrol = "begendi";
                    textViewbegenme.setText("beğendiniz");
                    imageView_begen.setImageResource(R.drawable.begenme);
                }
                else{
                    textViewbegenme.setText("Takip Et");
                    imageView_begen.setImageResource(R.drawable.begenmeme);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView_mesajGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChattActivity.class);
                intent.putExtra("username",textViewisim.getText().toString());
                intent.putExtra("id",otherId);
                startActivity(intent);
            }
        });


    }
    public void action(){
        databaseReference.child("kullanıcılar").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kullanicilar k = snapshot.getValue(kullanicilar.class);
                textViewisim.setText("İsim : "+k.getIsim());
                textViewdogumtarihi.setText("Doğum Tarihi : "+k.getDogumtarih());
                textViewegitim.setText("Eğitimi : "+ k.getEgitim());
                textViewhakkimda.setText("Hakkımda : "+k.getHakkinda());

                if(!k.getResim().equals("null")){
                    Picasso.get().load(k.getResim()).into(user_profile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView_arkadas_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(kontrol.equals("istek")){
                    arkadasiptal(otherId,userid);
                }
                else if(kontrol.equals("arkadas")){
                    arkadasTablosundanCikar(otherId,userid);
                }
                else{
                    arkadasekle(otherId,userid);  //arkadasekle methodunun parametrelerini girdim
                }
            }
        });

        //image tıklandığındığında yapılacak işlemler
        imageView_begen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(begenkontrol.equals("begendi")){
                    begeniIptal(userid,otherId);
                }
                else{
                    begen(userid,otherId);
                }
            }
        });
    }

    public void arkadasekle(String OtherId,String userId){
        databaseReference2.child("arkadaslik").child(userId).child(otherId).child("tipi").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    databaseReference2.child("arkadaslik").child(otherId).child(userId).child("tipi").setValue("aldı").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                kontrol = "istek"; //kontrole bir değer atadık çünkü iptal işlemini yapmak için

                                toastMessage.showtext("Arkadaşlık isteği başarıyla gönderildi");
                                imageView_arkadas_ekle.setImageResource(R.drawable.takip_off); //imageview değiştirdik
                                textViewarkadasekle.setText("Arkadaşlık İsteği Gönderildi");

                            }

                        }
                    });
                }
                else {

                    toastMessage.showtext("problem var");
                }
            }
        });

    }
    public void arkadasiptal(String otherId,String userid){
        databaseReference2.child(otherId).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference2.child(userid).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        imageView_arkadas_ekle.setImageResource(R.drawable.arkadas_ekle); //imageview değiştirdik

                        toastMessage.showtext("Arkadaşlık isteği iptal edildi");
                        textViewarkadasekle.setText("Arkadaş Ekle");

                    }
                });
            }
        });
    }

    public void arkadasTablosundanCikar(final String otherId, final String userid){
        databaseReference.child("arkadaslar").child(otherId).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference2.child(userid).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        imageView_arkadas_ekle.setImageResource(R.drawable.arkadas_ekle); //imageview değiştirdik

                        toastMessage.showtext("Arkadaşlıktan çıkarıldı");
                        arkadassayisi();
                    }
                });
            }
        });
    }

    //image begenme işlemi yapan methodu yaptım
    public void begen(String userid,String otherId){
        databaseReference.child("begeniler").child(otherId).child(userid).child("tipi").setValue("beğendi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    begenkontrol = "begendi";
                    toastMessage.showtext("beğendiniz");
                    imageView_begen.setImageResource(R.drawable.begenme);

                    textViewbegenme.setText("Beğendiniz");
                    begenisayisi();
                }


            }
        });
    }

    //begeni işlemin iptali
    public  void begeniIptal(String userid,String otherId){
        databaseReference.child("begeniler").child(otherId).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                begenkontrol = "";
                 imageView_begen.setImageResource(R.drawable.begenmeme);

                textViewbegenme.setText("Takip Et");
                 toastMessage.showtext("beğeni işlemi iptal edildi");
                begenisayisi();
            }
        });
    }

    //kaç kişi kullanıcı takip ediyor methodunu yazdım
    public void  begenisayisi(){
        textViewtakipciler.setText("0 Takipcisi Var");

        databaseReference.child("begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewtakipciler.setText(snapshot.getChildrenCount()+" Takipcisi Var");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


       //kaç tane arkadaşı var methodu kullandım
    public void arkadassayisi(){

        textViewarkdaslar.setText("0 Arkadaşı Var");

        databaseReference.child("arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewarkdaslar.setText(snapshot.getChildrenCount()+ " Arkadaşı Var");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}