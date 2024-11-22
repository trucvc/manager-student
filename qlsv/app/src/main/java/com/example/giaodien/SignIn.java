package com.example.giaodien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    Button signIn;
    EditText email,pass;
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mapping();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String mail = "containai27042003@gmail.com";

                auth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignIn.this,"Yêu cầu gửi thành công",Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(SignIn.this,"Yêu cầu gửi thất bại",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void logIn() {
        String mail = email.getText().toString().trim();
        String password = pass.getText().toString().trim();
        if (mail.length()>0 && password.length()>0){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(mail,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(SignIn.this,MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }else{
                                Toast.makeText(SignIn.this,"Tài khoản hoặc mật khẩu không chính xác",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void mapping(){
        signIn = findViewById(R.id.login);
        email = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        forgot = findViewById(R.id.quenMK);
    }
}