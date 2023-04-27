package com.emiremre.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class oyuna_basla_activity extends AppCompatActivity {

    private Button tek;
    private Button cok;

    private Button zor_2x2;
    private Button zor_4x4;
    private Button zor_6x6;

    private Button basla;

    private TextView TV_secim;
    private int INT_secim = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyuna_basla);

        tek = (Button)findViewById(R.id.btn_tek);
        cok = (Button)findViewById(R.id.btn_cok);

        zor_2x2 = (Button)findViewById(R.id.btn_2x2);
        zor_4x4 = (Button)findViewById(R.id.btn_4x4);
        zor_6x6 = (Button)findViewById(R.id.btn_6x6);

        basla = (Button)findViewById(R.id.btn_basla);

        TV_secim = (TextView)findViewById(R.id.secim);
        TV_secim.setGravity(Gravity.CENTER);
        TV_secim.setText("Başlamak için sırası ile tür ve zorluk seçin.");

    }

    @Override
    public void onBackPressed() {
        Intent back_int = new Intent(oyuna_basla_activity.this, home_activity.class);
        finish();
        startActivity(back_int);
    }


    public void sec_tek(View v) {
        tek.setVisibility(View.INVISIBLE);
        cok.setVisibility(View.INVISIBLE);
        TV_secim.setText("Tek Oyunculu");
        INT_secim = INT_secim + 0;
    }

    public void sec_cok(View v) {
        tek.setVisibility(View.INVISIBLE);
        cok.setVisibility(View.INVISIBLE);
        TV_secim.setText("Çok Oyunculu");
        INT_secim = INT_secim + 1;
    }

    public void sec_2x2(View v) {
        zor_2x2.setVisibility(View.INVISIBLE);
        zor_4x4.setVisibility(View.INVISIBLE);
        zor_6x6.setVisibility(View.INVISIBLE);
        TV_secim.setText(TV_secim.getText().toString() + " 2x2");
        INT_secim = INT_secim + 2;
    }

    public void sec_4x4(View v) {
        zor_2x2.setVisibility(View.INVISIBLE);
        zor_4x4.setVisibility(View.INVISIBLE);
        zor_6x6.setVisibility(View.INVISIBLE);
        TV_secim.setText(TV_secim.getText().toString() +" 4x4");
        INT_secim = INT_secim + 4;
    }

    public void sec_6x6(View v) {
        zor_2x2.setVisibility(View.INVISIBLE);
        zor_4x4.setVisibility(View.INVISIBLE);
        zor_6x6.setVisibility(View.INVISIBLE);
        TV_secim.setText(TV_secim.getText().toString() +" 6x6");
        INT_secim = INT_secim + 6;
    }

    public void basla(View v) {
        //2: tek oyunculu 2x2 zorluk
        if ( INT_secim == 2 )
        {
            Intent intnt = new Intent(oyuna_basla_activity.this, oyun_2x2_activity.class);
            finish();
            startActivity(intnt);
        }

        //3: cok oyunculu 2x2 zorluk
        if ( INT_secim == 3 )
        {
            Intent intnt = new Intent(oyuna_basla_activity.this, cok_oyun_2x2_activity.class);
            finish();
            startActivity(intnt);
        }

        //4: tek oyunculu 4x4 zorluk
        if ( INT_secim == 4 )
        {
            Intent intnt = new Intent(oyuna_basla_activity.this, oyun_4x4_activity.class);
            finish();
            startActivity(intnt);
        }

        //5: cok oyunculu 4x4 zorluk
        if ( INT_secim == 5 )
        {
            Intent intnt = new Intent(oyuna_basla_activity.this, cok_oyun_4x4_activity.class);
            finish();
            startActivity(intnt);
        }

        //6: tek oyunculu 6x6 zorluk
        if ( INT_secim == 6 )
        {
            Intent intnt = new Intent(oyuna_basla_activity.this, oyun_6x6_activity.class);
            finish();
            startActivity(intnt);
        }

        //6: cok oyunculu 6x6 zorluk
        if ( INT_secim == 7 )
        {
            Intent intnt = new Intent(oyuna_basla_activity.this, cok_oyun_6x6_activity.class);
            finish();
            startActivity(intnt);
        }
    }
}