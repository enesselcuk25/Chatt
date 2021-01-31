package com.enes.chattapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.enes.chattapp.fragment.BildirimFragment;
import com.enes.chattapp.fragment.kullaniciProfilFragment;
import com.enes.chattapp.utils.ChangeFragment;
import com.enes.chattapp.R;
import com.enes.chattapp.fragment.AnaSayfaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Anactivity extends AppCompatActivity {
public  ChangeFragment changeFragment;
  private   FirebaseAuth firebaseAuth;
  private  FirebaseUser firebaseUser;
  private Toolbar toolbarcikis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

         changeFragment = new ChangeFragment(Anactivity.this);
        changeFragment.change(new AnaSayfaFragment());

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);

        tanimla();
        kontrol();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
            = (item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    changeFragment.change(new AnaSayfaFragment());
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:
                    changeFragment.change(new kullaniciProfilFragment());
                    return true;


            }

            return false;
    });

    public void tanimla(){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        toolbarcikis = findViewById(R.id.toolbarcikis);

        setSupportActionBar(toolbarcikis);

        toolbarcikis.setTitle("");





    }


    public void kontrol(){

        if(firebaseUser == null){ //kullanıcı kayıt olmamış ise kayıt sayfasına gitsin
            Intent intent = new Intent(getApplicationContext(), loginUpActivity.class);
            startActivity(intent);
            finish();


        }
        else{
            //giriş yaptığımızda online oluyor.
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("kullanıcılar");
            databaseReference.child(firebaseUser.getUid()).child("state").setValue(true);
        }


    }


    //activity yaşam döngüsü uygulmayı kapattığımızda yapılacak işlemler
    @Override
    protected void onStop() {
        super.onStop();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("kullanıcılar");
        databaseReference.child(firebaseUser.getUid()).child("state").setValue(false);
    }

    //uygulamayı kapatıp açtığımızda neler olacağını gösterir
    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("kullanıcılar");
        databaseReference.child(firebaseUser.getUid()).child("state").setValue(true);

    }

    public void exit(){

        firebaseAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), loginInActivity.class);
        startActivity(intent);
        finish();

        //çıkış yaptığımızda kullanıcı offline oluyor
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("kullanıcılar");
        databaseReference.child(firebaseUser.getUid()).child("state").setValue(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cikis,menu);


        MenuItem menuItem = menu.findItem(R.id.action_cikis);
   

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_cikis:
                exit();
                return  true;
            case R.id.bildirimler:
                changeFragment.change(new BildirimFragment());
            default:
                return false;
        }

    }
}