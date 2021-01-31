package com.enes.chattapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enes.chattapp.R;
import com.enes.chattapp.adaptir.FriendsAdaptir;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ArkdaslarFragment extends Fragment {


private View view;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
private RecyclerView recyclerView;
private FriendsAdaptir friendsAdaptir;
List<String> keylist;
FirebaseUser firebaseUser;
FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_arkdaslar, container, false);
        tanimla();
        arkadaslar();
        return view;
    }
    public  void  tanimla(){
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = firebaseDatabase.getReference().child("arkadaslar");
        recyclerView = view.findViewById(R.id.recylerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);

        keylist = new ArrayList<>();
        friendsAdaptir = new FriendsAdaptir(keylist,getActivity(),getContext());
        recyclerView.setAdapter(friendsAdaptir);



    }
    //arkadasları getriyoruz
    public void arkadaslar(){
        databaseReference.child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (keylist.indexOf(snapshot.getKey()) == -1){
                    keylist.add(snapshot.getKey());
                }

                friendsAdaptir.notifyDataSetChanged();
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