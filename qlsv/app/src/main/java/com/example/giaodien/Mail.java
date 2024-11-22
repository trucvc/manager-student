package com.example.giaodien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giaodien.SinhVien.SinhVien;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail extends AppCompatActivity {
    EditText tieude,noidung;
    Button gui,huy;
    TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        mapping();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mail_sv");
        SinhVien sv = (SinhVien) bundle.getSerializable("mail");
        mail.setText("Email: "+sv.getEmail());

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = "containai27042003@gmail.com";
                    String pass = "udexmruesvdtwnaw";
                    String ml = sv.getEmail();
                    String nd = noidung.getText().toString().trim();
                    String td = tieude.getText().toString().trim();

                    if (nd.length() > 0 && td.length() > 0){
                        String host = "smtp.gmail.com";
                        Properties properties = System.getProperties();
                        properties.put("mail.smtp.host",host);
                        properties.put("mail.smtp.port","465");
                        properties.put("mail.smtp.ssl.enable","true");
                        properties.put("mail.smtp.auth","true");
                        Session session = Session.getInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(name,pass);
                            }
                        });
                        MimeMessage mimeMessage = new MimeMessage(session);
                        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(ml));
                        mimeMessage.setSubject(td);
                        mimeMessage.setText(nd);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Transport.send(mimeMessage);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                        noidung.setText("");
                        tieude.setText("");
                        Toast.makeText(Mail.this,"Đã gửi tin nhắn",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Mail.this,"Nhập đủ dữ liệu",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(Mail.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mapping(){
        tieude = findViewById(R.id.tieuDe);
        noidung = findViewById(R.id.noiDung);
        gui = findViewById(R.id.gui);
        mail = findViewById(R.id.mail);
        huy = findViewById(R.id.huy);
    }
}