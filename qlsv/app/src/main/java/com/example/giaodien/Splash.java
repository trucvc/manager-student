package com.example.giaodien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                signIn();
            }
        },1000);
    }

    private void signIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Intent intent = new Intent(Splash.this, SignIn.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}