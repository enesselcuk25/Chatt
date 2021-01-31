package com.enes.chattapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enes.chattapp.R;
import com.enes.chattapp.adaptir.UserAdaptir;
import com.enes.chattapp.models.kullanicilar;
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


public class AnaSayfaFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    List<String> UserKeyList ;
    View view;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    UserAdaptir adaptir;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        tanimla();
        kullanicigetir();
        return view;
    }

    public void tanimla(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        UserKeyList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.home_reclerview);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        adaptir = new UserAdaptir(UserKeyList,getActivity(),getContext());  //adaptırı tanımladık

        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(mng);

        recyclerView.setAdapter(adaptir);  //reclerviewa adaptırı set ettim


    }
    public void kullanicigetir(){
        databaseReference.child("kullanıcılar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
           /*     Log.i("keyler",snapshot.getKey());
                 UserKeyList.add(snapshot.getKey());
                adaptir.notifyDataSetChanged();   //her veri geldiğinde adaptırı değiştir yani güncelle dedim

            */


                databaseReference.child("kullanıcılar").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        kullanicilar k = snapshot.getValue(kullanicilar.class);


                        if(!k.getIsim().equals("null") && !snapshot.getKey().equals(firebaseUser.getUid())){ // snapshot keyi girdiğimiz hesap ile eşit değil ise kullanıcılıarı göster dedim


                            if(UserKeyList.indexOf(snapshot.getKey()) == -1){
                                UserKeyList.add(snapshot.getKey());

                            }
                            adaptir.notifyDataSetChanged(); //her veri geldiğinde adaptırı değiştir yani güncelle dedim
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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