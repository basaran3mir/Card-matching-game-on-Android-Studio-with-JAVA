package com.emiremre.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class kaybet_activity extends AppCompatActivity {

    private TextView TV_puan;
    float puan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaybet);

        TV_puan = (TextView)findViewById(R.id.lose_puan);

        Intent intent = getIntent();
        float puan = intent.getFloatExtra("puan" , 0);
        TV_puan.setText("PUANINIZ: " + puan);
    }

    public void onBackPressed() {
        Intent back_int = new Intent(kaybet_activity.this, oyuna_basla_activity.class);
        this.finish();
        startActivity(back_int);
    }

}