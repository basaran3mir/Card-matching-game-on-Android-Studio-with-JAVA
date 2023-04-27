package com.emiremre.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class kart extends androidx.appcompat.widget.AppCompatButton {

    public int kart_id;
    public int kart_puani;
    public String kart_adi;
    public String kart_evi;

    public String onresim_id;
    public int arkaresim_id;

    public boolean acik_mi = false;

    public kart(Context context, int kid, int kkart_puani, String kkart_adi, String kkart_evi, String konresim_id) {

        super(context);

        this.kart_id = kid;
        this.kart_puani = kkart_puani;
        this.kart_adi = kkart_adi;
        this.kart_evi = kkart_evi;
        this.onresim_id = konresim_id;

        arkaresim_id = R.drawable.back_card;
        Drawable arka = ContextCompat.getDrawable(context, arkaresim_id);
        setBackground(arka);

    }

    public void cevir() {

        if(acik_mi == false) {
            acik_mi = true;
            byte[] code = Base64.decode(onresim_id, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(code, 0, code.length);
            Drawable on = new BitmapDrawable(getResources(), bitmap);
            setBackground(on);
        }
        else {
            acik_mi = false;
            arkaresim_id = R.drawable.back_card;
            Drawable arka = ContextCompat.getDrawable(this.getContext(),arkaresim_id);
            setBackground(arka);
        }

    }

}
