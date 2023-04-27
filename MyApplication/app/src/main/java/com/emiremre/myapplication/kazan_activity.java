package com.emiremre.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class kazan_activity extends AppCompatActivity {

    private TextView TV_puan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kazan);

        TV_puan = (TextView)findViewById(R.id.win_puan);

        Intent intent = getIntent();
        float kazanan_puan = intent.getFloatExtra("puan" , 0);
        String data = getIntent().getExtras().getString("veri","");

        TV_puan.setText("PUANINIZ: " + kazanan_puan + " " + data);

    }

    public void onBackPressed() {
        Intent back_int = new Intent(kazan_activity.this, oyuna_basla_activity.class);
        this.finish();
        startActivity(back_int);
    }

}