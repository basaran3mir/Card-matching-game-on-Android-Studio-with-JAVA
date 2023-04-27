package com.emiremre.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class home_activity extends AppCompatActivity {

    private ImageView image;
    private EditText ET_mail, ET_sifre;
    private String STR_mail, STR_sifre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        image = findViewById(R.id.baslik_image);

        ET_mail = (EditText)findViewById(R.id.input_mail);
        ET_sifre = (EditText)findViewById(R.id.input_sifre);

        mAuth = FirebaseAuth.getInstance();
    }

    public void giris_yap(View v) {
        STR_mail = ET_mail.getText().toString();
        STR_sifre = ET_sifre.getText().toString();

        if ( !TextUtils.isEmpty((STR_mail)) && !TextUtils.isEmpty((STR_sifre)) ){
            mAuth.signInWithEmailAndPassword(STR_mail,STR_sifre)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(home_activity.this, "Giriş başarılı.", Toast.LENGTH_SHORT).show();
                            mUser = mAuth.getCurrentUser();
                            Intent intnt = new Intent(home_activity.this, oyuna_basla_activity.class);
                            finish();
                            startActivity(intnt);
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(home_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else
            Toast.makeText(this, "Alanların tamamını doldurunuz.", Toast.LENGTH_SHORT).show();
    }

    public void kayit_ol(View v) {
        Intent intnt = new Intent(home_activity.this, kayit_ol_activity.class);
        finish();
        startActivity(intnt);
    }

    public void sifre_degistir(View v) {
        Intent intnt = new Intent(home_activity.this, sifre_degistir_activity.class);
        finish();
        startActivity(intnt);
    }

}