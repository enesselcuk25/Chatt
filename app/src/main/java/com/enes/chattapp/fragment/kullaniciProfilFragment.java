package com.enes.chattapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.enes.chattapp.R;
import com.enes.chattapp.models.kullanicilar;
import com.enes.chattapp.utils.ChangeFragment;
import com.enes.chattapp.utils.randomName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class kullaniciProfilFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

   private EditText editTextname,editTexthakkimda,editTextegitim,editTextdogumtarihi;
    private Button buttonkaydet,ButtonArkadaslar,ButtonTakipciler;
    private CircleImageView profileimage;

String imageurl;

StorageReference storageReference ; //image için
    FirebaseStorage firebaseStorage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_kullanici_profil, container, false); //return  sildim yerine view return yaptım
        tanimla();
        bilgilerigetir();





         return  view;


    }

    public void tanimla(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("kullanıcılar").child(firebaseAuth.getUid()); //kullanıcılar idisini aldık

        //image tanımlıyorum
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference(); //image tanımladık

        editTextname = (EditText)view.findViewById(R.id.edittextname);
        editTextdogumtarihi = (EditText)view.findViewById(R.id.edittext_dogumtarihi);
        editTextegitim = (EditText)view.findViewById(R.id.edittext_egitim);
        editTexthakkimda =(EditText)view.findViewById(R.id.edittext_hakkimda);
        profileimage = view.findViewById(R.id.profile_image);
        buttonkaydet = (Button)view.findViewById(R.id.buttonkaydet);
        ButtonArkadaslar = view.findViewById(R.id.buttonArkdaslar);
        ButtonTakipciler = view.findViewById(R.id.ButtonTakipcileri);





        buttonkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriac();
            }
        });


        //buttonlara tıklandığında arkdaşlara ve takipcilere gitsin
        ButtonArkadaslar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new ArkdaslarFragment());
            }
        });

        ButtonTakipciler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new BildirimFragment());

            }
        });



    }
    private void galeriac(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult(intent,1);
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data ){  //activitylerde ile çalışırsan hemen gösterir foksiyonu fakat fragmentlerle çalışırsan elle hepsini yazman gerekecek
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();

           // Log.i("hata",""+uri);
           StorageReference ref =  storageReference.child("kullanıcıResimleri").child(randomName.getSaltString()+".jpg");  //random atıyarak farklı  kullanıcı resim seçerek resimler karışmıyor

           ref.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                   if(task.isSuccessful()){
                       String isim =   editTextname.getText().toString();
                       String egitim = editTextegitim.getText().toString();
                       String dtarihi= editTextdogumtarihi.getText().toString();
                       String hakkinda =  editTexthakkimda.getText().toString();



                       databaseReference = firebaseDatabase.getReference().child("kullanıcılar").child(Objects.requireNonNull(firebaseAuth.getUid())); //kullanıcılar ve kullancıı id aldık oluşturduk
                       Map map = new HashMap();

                       map.put("isim",isim);
                       map.put("egitim",egitim);
                       map.put("dogumtarih",dtarihi);
                       map.put("hakkinda",hakkinda);
                       map.put("resim", task.getResult().getStorage().getDownloadUrl().toString()); //resim güncellenecek

                       databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                               if(task.isSuccessful()){
                                   ChangeFragment changeFragment = new ChangeFragment(getContext());  //başırıyla gerçekleşmişse kullanıcı fragmantı tekrar yükleniyor
                                   changeFragment.change(new kullaniciProfilFragment());

                                   Toast.makeText(getContext(),"bilgiler başarıyla güncellendi",Toast.LENGTH_SHORT).show();

                               }
                               else{
                                   Toast.makeText(getContext(),"hata",Toast.LENGTH_SHORT).show();
                               }

                           }
                       });

                   }






               }
           });

            //Log.i("bilgiler"," "+uri);

        }
    }

    public void bilgilerigetir(){


        //verileri getiriyoruz
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                // 1.yol

                kullanicilar k = snapshot.getValue(kullanicilar.class);

                editTextname.setText(k.getIsim());
                editTextegitim.setText(k.getEgitim());
                editTextdogumtarihi.setText(k.getDogumtarih());
                editTexthakkimda.setText(k.getHakkinda());
                imageurl = k.getResim();

                
                if(!k.getResim().equals("null")){
                    //resmi seçmemişsek
                    Picasso.get().load(k.getResim()).into(profileimage);  //picasso kütüphanesi ekledim
                }




                /* 2.yol
                String adi = snapshot.child("isim").getValue().toString();
                String egitim = snapshot.child("egitim").getValue().toString();
                Log.i("bilgileri",adi+egitim);

                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void guncelle(){
        //edittexteki verileri alıyoruz
      String isim =   editTextname.getText().toString();
       String egitim = editTextegitim.getText().toString();
       String dtarihi= editTextdogumtarihi.getText().toString();
        String hakkinda =  editTexthakkimda.getText().toString();


        databaseReference = firebaseDatabase.getReference().child("kullanıcılar").child(firebaseAuth.getUid()); //kullanıcılar ve kullancıı id aldık oluşturduk
        Map map = new HashMap();

        map.put("isim",isim);
        map.put("egitim",egitim);
        map.put("dogumtarih",dtarihi);
        map.put("hakkinda",hakkinda);

        if(imageurl.equals("null")){
            //resim boş ise null atsın
            map.put("resim","null");
        }
        else{
            //image null değilse imageurl atayacağız
            map.put("resim",imageurl);
        }

        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    ChangeFragment changeFragment = new ChangeFragment(getContext());  //başırıyla gerçekleşmişse kullanıcı fragmantı tekrar yükleniyor
                    changeFragment.change(new kullaniciProfilFragment());




                    Toast.makeText(getContext(),"bilgiler başarıyla güncellendi",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getContext(),"hata",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}