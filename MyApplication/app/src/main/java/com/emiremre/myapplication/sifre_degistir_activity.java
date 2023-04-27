package com.emiremre.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class sifre_degistir_activity extends AppCompatActivity {

    private EditText ET_mail, ET_eski_sifre, ET_yeni_sifre;
    private String STR_mail, STR_eski_sifre, STR_yeni_sifre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private HashMap<String, Object> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);

        ET_mail = (EditText)findViewById(R.id.input_mail);
        ET_eski_sifre = (EditText)findViewById(R.id.input_eski_sifre);
        ET_yeni_sifre = (EditText)findViewById(R.id.input_yeni_sifre);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        Intent back_int = new Intent(sifre_degistir_activity.this, home_activity.class);
        finish();
        startActivity(back_int);
    }

    public void change_pass(View v) {
        STR_mail = ET_mail.getText().toString();
        STR_eski_sifre = ET_eski_sifre.getText().toString();
        STR_yeni_sifre = ET_yeni_sifre.getText().toString();

        if ( !TextUtils.isEmpty((STR_mail)) && !TextUtils.isEmpty((STR_eski_sifre)) && !TextUtils.isEmpty((STR_yeni_sifre)) ){
            mAuth.signInWithEmailAndPassword(STR_mail,STR_eski_sifre)
                    .addOnSuccessListener(sifre_degistir_activity.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            //Giriş
                            mUser = mAuth.getCurrentUser();

                            //Authentication şifre güncelle
                            mUser.updatePassword(STR_yeni_sifre).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        Toast.makeText(sifre_degistir_activity.this, "Şifreniz değiştirilmiştir. Giriş ekranına yönlendiriliyorsunuz.", Toast.LENGTH_SHORT).show();
                                        //Giriş ekranına geri döndür
                                        Intent intnt = new Intent(sifre_degistir_activity.this, home_activity.class);
                                        finish();
                                        startActivity(intnt);
                                }
                            });

                            //Database şifre güncelle
                            mData = new HashMap<>();
                            mData.put("kullanici_sifre", STR_yeni_sifre);
                            mReference = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(mUser.getUid());
                            mReference.updateChildren(mData)
                                    .addOnCompleteListener(sifre_degistir_activity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Toast.makeText(sifre_degistir_activity.this, "Şifreniz değiştirilmiştir. Giriş ekranına yönlendiriliyorsunuz.", Toast.LENGTH_SHORT).show();
                                                //Giriş ekranına geri döndür
                                                Intent intnt = new Intent(sifre_degistir_activity.this, home_activity.class);
                                                finish();
                                                startActivity(intnt);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(sifre_degistir_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(sifre_degistir_activity.this, "Alanların tamamını doldurunuz.", Toast.LENGTH_SHORT).show();
        }
    }

}