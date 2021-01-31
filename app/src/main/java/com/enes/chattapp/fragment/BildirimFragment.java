package com.enes.chattapp.fragment;

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
import com.enes.chattapp.adaptir.FriendAdaptir;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class BildirimFragment extends Fragment {

  private View view;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
String userId;

FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
List<String> friend_list;
RecyclerView recyclerView;

FriendAdaptir friendAdaptir;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_bildirim, container, false);
        tanimla();
        istekler();
        return view;
    }

    public void tanimla(){
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid(); //kullanıcı id sini aldım
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("arkadaslik"); //referans olarak aldım

        friend_list = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recylerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);

        recyclerView.setLayoutManager(layoutManager);

        friendAdaptir = new FriendAdaptir(friend_list,getActivity(),getContext());
        recyclerView.setAdapter(friendAdaptir);
    }
    public void istekler(){
        databaseReference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //kileri almak için snapshot kullanmamzı gerekecek




              String kontrol = snapshot.child("tipi").getValue().toString(); //isteği kabul ettiğimizde tipini alıyoruz
                Log.i("değer",kontrol);

              if (kontrol.equals("aldı")){ //isteği aldı ise ise
                  Log.i("istekler",snapshot.getKey());


                  if(friend_list.indexOf(snapshot.getKey()) == -1){
                      friend_list.add(snapshot.getKey()); //kileri listeye attık
                  }

                  friendAdaptir.notifyDataSetChanged(); //güncelliyoruz
              }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                friend_list.remove(snapshot.getKey()); //silme işlemi yaptığımızda  kileri güncelledim
                friendAdaptir.notifyDataSetChanged(); //güncelliyoruz
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