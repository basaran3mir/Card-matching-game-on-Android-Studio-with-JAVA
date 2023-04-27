package com.emiremre.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class kayit_ol_activity extends AppCompatActivity {

    private EditText ET_kayit_mail, ET_kayit_sifre, ET_kayit_kullanici;
    private String STR_kayit_mail, STR_kayit_sifre, STR_kayit_kullanici;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private HashMap<String, Object> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        ET_kayit_kullanici = (EditText)findViewById(R.id.input_kayit_kullanici);
        ET_kayit_mail = (EditText)findViewById(R.id.input_kayit_mail);
        ET_kayit_sifre = (EditText)findViewById(R.id.input_kayit_sifre);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        Intent back_int = new Intent(kayit_ol_activity.this, home_activity.class);
        finish();
        startActivity(back_int);
    }

    public void kayit_ol(View v) {
        STR_kayit_mail = ET_kayit_mail.getText().toString();
        STR_kayit_sifre = ET_kayit_sifre.getText().toString();
        STR_kayit_kullanici = ET_kayit_kullanici.getText().toString();

        if ( !TextUtils.isEmpty((STR_kayit_kullanici)) && !TextUtils.isEmpty((STR_kayit_mail)) && !TextUtils.isEmpty((STR_kayit_sifre)) ){
            mAuth.createUserWithEmailAndPassword(STR_kayit_mail, STR_kayit_sifre)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mUser = mAuth.getCurrentUser();

                                mData = new HashMap<>();
                                mData.put("kullanici_ad", STR_kayit_kullanici);
                                mData.put("kullanici_mail", STR_kayit_mail);
                                mData.put("kullanici_sifre", STR_kayit_sifre);
                                mData.put("kullanici_id", mUser.getUid());

                                mReference.child("Kullanıcılar").child(mUser.getUid())
                                        .setValue(mData)
                                        .addOnCompleteListener(kayit_ol_activity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(kayit_ol_activity.this, "Kayıt başarılı.", Toast.LENGTH_SHORT).show();
                                                    Intent intnt = new Intent(kayit_ol_activity.this, home_activity.class);
                                                    finish();
                                                    startActivity(intnt);
                                                }
                                                else
                                                    Toast.makeText(kayit_ol_activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            else
                                Toast.makeText(kayit_ol_activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
            Toast.makeText(this, "Alanların tamamını doldurunuz.", Toast.LENGTH_SHORT).show();
    }
}