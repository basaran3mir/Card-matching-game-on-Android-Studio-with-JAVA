package com.emiremre.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class cok_oyun_6x6_activity extends AppCompatActivity {

    private TextView sayac, TV_puan_oyuncu_1, TV_puan_oyuncu_2;
    private Button kartlari_olustur, oyuna_basla, muzik_durdur;

    private MediaPlayer game_music, end_time_music, cards_found_music, all_cards_found_music;

    private GridLayout gl;

    private kart kartlar[] = new kart[44];
    private kart kartlar_es[] = new kart[44];

    private kart kartlar_gryffindor[] = new kart[11];
    private kart kartlar_gryffindor_es[] = new kart[11];

    private kart kartlar_slytherin[] = new kart[11];
    private kart kartlar_slytherin_es[] = new kart[11];

    private kart kartlar_ravenclaw[] = new kart[11];
    private kart kartlar_ravenclaw_es[] = new kart[11];

    private kart kartlar_hufflepuff[] = new kart[11];
    private kart kartlar_hufflepuff_es[] = new kart[11];

    ArrayList<kart> ekran_kartlari = new ArrayList<>(); //Ekran kartlari
    kart son_kart; //Basilan son kart
    int counter = 0; //Secilecek 2 kart icin sayac

    int sira_kimde = 0;
    float oyuncu_1_puan = 0; //puan
    float oyuncu_2_puan = 0; //puan

    float kalan_sure = 0; //kalan sure
    float gecen_sure = 0; //gecen sure

    int kart_bulundu = 0; //Oyun boyunca kac kart bulundu tutacak

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cok_oyun_6x6);

        sayac = (TextView)findViewById(R.id.text_sayac);
        TV_puan_oyuncu_1 = (TextView)findViewById(R.id.text_oyuncu_1_puan);
        TV_puan_oyuncu_2 = (TextView)findViewById(R.id.text_oyuncu_2_puan);

        kartlari_olustur = (Button) findViewById(R.id.button_kartlari_olustur);
        oyuna_basla = (Button) findViewById(R.id.button_oyunu_baslat);
        muzik_durdur  = (Button) findViewById(R.id.button_stop_game_music);
        muzik_durdur.setVisibility(View.INVISIBLE);

        //Muzikleri tanimlama
        game_music = MediaPlayer.create(getApplicationContext(),R.raw.game_music);
        end_time_music = MediaPlayer.create(getApplicationContext(),R.raw.end_time_music);
        cards_found_music = MediaPlayer.create(getApplicationContext(),R.raw.card_found);
        all_cards_found_music = MediaPlayer.create(getApplicationContext(),R.raw.congratulations);

        //Basilan son karti tanimlama
        son_kart = new kart(this,0,0,"0","0","0");

        //Kart dizilerinin elemanlarini tanimlama
        for (int i = 0; i < 44; i++) {
            kartlar[i] = new kart(this,0,0,"0","0","0");
            kartlar_es[i] = new kart(this,0,0,"0","0","0");
        }
        for (int i = 0; i < 11; i++) {
            kartlar_gryffindor[i] = new kart(this,0,0,"0","0","0");
            kartlar_gryffindor_es[i] = new kart(this,0,0,"0","0","0");

            kartlar_slytherin[i] = new kart(this,0,0,"0","0","0");
            kartlar_slytherin_es[i] = new kart(this,0,0,"0","0","0");

            kartlar_ravenclaw[i] = new kart(this,0,0,"0","0","0");
            kartlar_ravenclaw_es[i] = new kart(this,0,0,"0","0","0");

            kartlar_hufflepuff[i] = new kart(this,0,0,"0","0","0");
            kartlar_hufflepuff_es[i] = new kart(this,0,0,"0","0","0");
        }

    }

    @Override
    public void onBackPressed() {
        Intent back_int = new Intent(cok_oyun_6x6_activity.this, oyuna_basla_activity.class);
        this.finish();
        startActivity(back_int);
        game_music.stop();
        end_time_music.stop();
        cards_found_music.stop();
        all_cards_found_music.stop();
    }

    //Secilen kartin resim harici bilgilerini yazdirir
    public void kart_bilgilerini_yazdir(kart new_kart) {
        System.out.println();
        System.out.println("Kart id: " + new_kart.kart_id);
        System.out.println("Kart ad: " + new_kart.kart_adi);
        System.out.println("Kart ev: " + new_kart.kart_evi);
        System.out.println("Kart puan: " + new_kart.kart_puani);
        System.out.println();
    }

    //Veritabanindaki kart verilerini kartlar ve kartlar_es dizilerine atama
    public void caliss(View v) {
        kartlari_olustur.setVisibility(View.INVISIBLE);

        for (int i = 1; i < 45; i++) {

            int id = i - 1;
            String s = String.valueOf(i);
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Kartlar").child(s);

            mReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String kart_adi = "";
                    String kart_evi = "";
                    int kart_puani = 0;
                    String onresim_id = "";

                    for (DataSnapshot snp : snapshot.getChildren()) {

                        if (snp.getKey().equals("kart_adi")) {
                            kart_adi = snp.getValue().toString();
                            //System.out.println("kart_adi: " + kart_adi);
                        } else if (snp.getKey().equals("kart_evi")) {
                            kart_evi = snp.getValue().toString();
                            //System.out.println("kart_evi: " + kart_evi);
                        } else if (snp.getKey().equals("kart_puani")) {
                            kart_puani = Integer.parseInt(snp.getValue().toString());
                            //System.out.println("kart_puani: " + kart_puani);
                        } else if (snp.getKey().equals("kart_resmi")) {
                            onresim_id = snp.getValue().toString();
                            //System.out.println("onresim_id: " + onresim_id);
                        }
                    }

                    kartlar[id] = new kart(cok_oyun_6x6_activity.this, id, kart_puani, kart_adi, kart_evi, onresim_id);
                    kartlar_es[id] = new kart(cok_oyun_6x6_activity.this, id, kart_puani, kart_adi, kart_evi, onresim_id);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    //Secilen sinif ve sinif_es ten benzersiz iki random sayi ile ekran_kartlarina ekleme yapar
    public void sinif_kart_ekle(kart[] sinif, kart[] sinif_es, int count) {
        //6x6 icin 4-4-5-5 diye bolunecek
        Integer[] array = new Integer[count];

        Random rand = new Random();
        for(int i=0;i<count;i++) {
            array[i]= rand.nextInt(11) ;
            for (int j=0;j<i;j++)
            {
                if (array[i] == array[j])
                {
                    i--;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            ekran_kartlari.add(sinif[array[i]]);
            ekran_kartlari.add(sinif_es[array[i]]);
        }

    }

    //Oyunu baslat
    public void oyunu_baslat(View v) throws IOException {
        oyuna_basla.setVisibility(View.INVISIBLE);
        muzik_durdur.setVisibility(View.VISIBLE);
        TV_puan_oyuncu_1.setText("1. Oyuncu Puanı: 0.0");
        TV_puan_oyuncu_2.setText("2. Oyuncu Puanı: 0.0");

        //Oyun muzigini baslatma
        game_music.start();

        //Kartlar icerisinde gezerek kartlari siniflarina gore yeni kart ve kart_es dizilerine atama
        int temp_g = 0;
        int temp_s = 0;
        int temp_r = 0;
        int temp_h = 0;
        for (int i = 0; i < 44; i++) {

            String kart_adi = kartlar[i].kart_adi;
            String kart_evi = kartlar[i].kart_evi;
            int kart_puani = kartlar[i].kart_puani;
            String onresim_id = kartlar[i].onresim_id;

            if(kart_evi.equals("Gryffindor")) {
                kartlar_gryffindor[temp_g] = new kart(this, temp_g, kart_puani,kart_adi,kart_evi,onresim_id);
                kartlar_gryffindor_es[temp_g] = new kart(this, temp_g, kart_puani,kart_adi,kart_evi,onresim_id);
                temp_g++;
            }
            else if(kart_evi.equals("Slytherin")) {
                kartlar_slytherin[temp_s] = new kart(this, temp_s, kart_puani,kart_adi,kart_evi,onresim_id);
                kartlar_slytherin_es[temp_s] = new kart(this, temp_s, kart_puani,kart_adi,kart_evi,onresim_id);
                temp_s++;
            }
            else if(kart_evi.equals("Ravenclaw")) {
                kartlar_ravenclaw[temp_r] = new kart(this, temp_r, kart_puani,kart_adi,kart_evi,onresim_id);
                kartlar_ravenclaw_es[temp_r] = new kart(this, temp_r, kart_puani,kart_adi,kart_evi,onresim_id);
                temp_r++;
            }
            else if(kart_evi.equals("Hufflepuff")) {
                kartlar_hufflepuff[temp_h] = new kart(this, temp_h, kart_puani,kart_adi,kart_evi,onresim_id);
                kartlar_hufflepuff_es[temp_h] = new kart(this, temp_h, kart_puani,kart_adi,kart_evi,onresim_id);
                temp_h++;
            }

        }

        //GridLayout baglama
        gl = (GridLayout) findViewById(R.id.kartlar);
        gl.setColumnCount(6);
        gl.setRowCount(6);

        //Her sinif kartlarindan rastgele secilen 2 tane(kopyalari ile beraber 4 tane) karti ekleme
        //Hatirlatma: Ayrica ekrana eklenen kartlar ekran_kartlari kart dizisine de atanıyor
        sinif_kart_ekle(kartlar_gryffindor, kartlar_gryffindor_es, 4);
        sinif_kart_ekle(kartlar_slytherin, kartlar_slytherin_es, 4);
        sinif_kart_ekle(kartlar_ravenclaw, kartlar_ravenclaw_es, 5);
        sinif_kart_ekle(kartlar_hufflepuff, kartlar_hufflepuff_es, 5);

        //Ekran kartlarini karistirma
        Collections.shuffle(ekran_kartlari);
        gl.removeAllViews();
        for (kart temp : ekran_kartlari) {
            gl.addView(temp,125,175);
        }

        //Dosya islemleri
        FileOutputStream fos = openFileOutput("kartlar.txt", Context.MODE_PRIVATE);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        for (int i = 0; i < ekran_kartlari.size(); i++) {
            osw.write((i+1) + ". " + "kart ");
            osw.write(ekran_kartlari.get(i).kart_adi + " ");
            osw.write(ekran_kartlari.get(i).kart_evi+ " ");
            osw.write(ekran_kartlari.get(i).kart_puani+ " ");
            osw.write("\n");

        }
        osw.close();
        fos.close();

        //Her bir karta OnClickListener atama ve oyunu dinleme
        for (int i = 0; i < 36; i++) {
            int id = i;
            ekran_kartlari.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Basilan kartı cevir
                    ekran_kartlari.get(id).cevir();

                    //Anlik olarak TextView'dan puan al
                    oyuncu_1_puan = Float.parseFloat(TV_puan_oyuncu_1.getText().toString().substring(17));
                    oyuncu_2_puan = Float.parseFloat(TV_puan_oyuncu_2.getText().toString().substring(17));

                    //Kartlara tiklamak icin 2 tane kart secim hakkim olacak
                    //Bu 2 kart secim hakki icin:

                    //1. karta tikladim 2. karta tikliyorum
                    if( son_kart.kart_adi != "0")
                    {
                        //BILME DURUUMU
                        if(son_kart.kart_adi == ekran_kartlari.get(id).kart_adi) {

                            System.out.println("BILDIN");
                            cards_found_music.start();
                            kart_bulundu++;

                            //Puan durumunu guncelleme
                            if(son_kart.kart_evi.equals("Gryffindor") || son_kart.kart_evi.equals("Slytherin")) {
                                if(sira_kimde % 2 == 0)
                                    oyuncu_1_puan = oyuncu_1_puan + ((2 * son_kart.kart_puani * 2));
                                else
                                    oyuncu_2_puan = oyuncu_2_puan + ((2 * son_kart.kart_puani * 2));
                            }
                            else {
                                if(sira_kimde % 2 == 0)
                                    oyuncu_1_puan = oyuncu_1_puan + ((2 * son_kart.kart_puani * 1)*(kalan_sure/10));
                                else
                                    oyuncu_2_puan = oyuncu_2_puan + ((2 * son_kart.kart_puani * 1)*(kalan_sure/10));                            }

                            //Anlik olarak puan yazdir
                            TV_puan_oyuncu_1.setText("1. Oyuncu Puanı: " + String.format("%.2f", oyuncu_1_puan));
                            TV_puan_oyuncu_2.setText("2. Oyuncu Puanı: " + String.format("%.2f", oyuncu_2_puan));

                            Handler h = new Handler();
                            h.postDelayed (new Runnable() {

                                @Override
                                public void run() { //1 saniye bekle ve sifirlama islemlerini yap
                                    System.out.println("Secilen ikinci kart:");
                                    kart_bilgilerini_yazdir(ekran_kartlari.get(id));

                                    counter = 0; //2 basma hakkimi tutan sayaci sifirla (sonda 1 arttigi icin)
                                    son_kart.cevir(); //Onceki bastigim karti cevir
                                    ekran_kartlari.get(id).cevir(); //Simdi bastigim karti cevir

                                    son_kart.setVisibility(View.INVISIBLE);
                                    ekran_kartlari.get(id).setVisibility(View.INVISIBLE);

                                    son_kart = new kart(cok_oyun_6x6_activity.this,0,0,"0","0","0"); //Son karti sifirla
                                }

                            },1000);

                        }

                        //BILEMEME DURUMU
                        else {

                            System.out.println("BILEMEDIN");

                            //Bilememe durumunda kart evleri AYNI ise
                            if(son_kart.kart_evi.equals(ekran_kartlari.get(id).kart_evi)) {
                                if(son_kart.kart_evi.equals("Gryffindor") || son_kart.kart_evi.equals("Slytherin")) {
                                    if(sira_kimde % 2 == 0)
                                        oyuncu_1_puan = oyuncu_1_puan - ((son_kart.kart_puani + ekran_kartlari.get(id).kart_puani)/2);
                                    else
                                        oyuncu_2_puan = oyuncu_2_puan - ((son_kart.kart_puani + ekran_kartlari.get(id).kart_puani)/2);
                                }
                                else {
                                    if(sira_kimde % 2 == 0)
                                        oyuncu_1_puan = oyuncu_1_puan - ((son_kart.kart_puani + ekran_kartlari.get(id).kart_puani)/1);
                                    else
                                        oyuncu_2_puan = oyuncu_2_puan - ((son_kart.kart_puani + ekran_kartlari.get(id).kart_puani)/1);
                                }
                            }

                            //Bilememe durumunda kart evleri FARKLI ise
                            else {
                                int kart_puan_ort = (son_kart.kart_puani + ekran_kartlari.get(id).kart_puani)/2;
                                int ev_1_katsayi, ev_2_katsayi;
                                if(son_kart.kart_evi.equals("Gryffindor")  || son_kart.kart_evi.equals("Slytherin")) {
                                    ev_1_katsayi = 2;
                                }
                                else {
                                    ev_1_katsayi = 1;
                                }

                                if(ekran_kartlari.get(id).kart_evi.equals("Gryffindor")  || ekran_kartlari.get(id).kart_evi.equals("Slytherin")) {
                                    ev_2_katsayi = 2;
                                }
                                else {
                                    ev_2_katsayi = 1;
                                }

                                if(sira_kimde % 2 == 0)
                                    oyuncu_1_puan = oyuncu_1_puan - (kart_puan_ort*ev_1_katsayi*ev_2_katsayi);
                                else
                                    oyuncu_2_puan = oyuncu_2_puan - (kart_puan_ort*ev_1_katsayi*ev_2_katsayi);                            }

                            //Puani guncelle
                            TV_puan_oyuncu_1.setText("1. Oyuncu Puanı: " + String.format("%.2f", oyuncu_1_puan));
                            TV_puan_oyuncu_2.setText("2. Oyuncu Puanı: " + String.format("%.2f", oyuncu_2_puan));

                            //2 karta da tiklanmis ise
                            if(counter == 1) {

                                Handler h = new Handler();
                                h.postDelayed (new Runnable() {
                                    @Override
                                    public void run() { //1 saniye bekle ve sifirlama islemlerini yap

                                        System.out.println("Secilen ikinci kart: ");
                                        kart_bilgilerini_yazdir(ekran_kartlari.get(id));

                                        counter = 0;
                                        son_kart.cevir();
                                        ekran_kartlari.get(id).cevir();
                                        son_kart = new kart(cok_oyun_6x6_activity.this,0,0,"0","0","0");

                                    }

                                },1000);

                            }
                            sira_kimde++;
                        }

                    }

                    //1. karta tiklamadim
                    else {
                        son_kart = ekran_kartlari.get(id); //Tikladigim karti son_kart kartina ata
                        System.out.println("Secilen ilk kart:"); //Tikladigim kartinn bilgilerini yaz
                        kart_bilgilerini_yazdir(son_kart);
                    }

                    counter++; //Kart tiklama sayacini bir arttir

                }
            });
        }

        //Zamana bagli islemler
        new CountDownTimer(60500, 1000) {

            String kim_kazandi_1 = "OYUNCU 1 KAZANDI";
            String kim_kazandi_2 = "OYUNCU 2 KAZANDI";

            @Override
            public void onTick(long l) {
                sayac.setText("Kalan zaman: " + (l/1000));
                kalan_sure = (int) (l/1000);
                gecen_sure = 60 - kalan_sure;

                //Tum kartlar bulundu ise
                if(kart_bulundu == 18) {
                    cards_found_music.stop();
                    all_cards_found_music.start();
                    cancel(); //Sayaci durdur

                    Intent intnt = new Intent(cok_oyun_6x6_activity.this, kazan_activity.class);
                    oyuncu_1_puan = Float.parseFloat(TV_puan_oyuncu_1.getText().toString().substring(17));
                    oyuncu_2_puan = Float.parseFloat(TV_puan_oyuncu_2.getText().toString().substring(17));

                    if (oyuncu_1_puan > oyuncu_2_puan) {
                        intnt.putExtra("puan" , oyuncu_1_puan);
                        intnt.putExtra("veri" , kim_kazandi_1);
                    }
                    else {
                        intnt.putExtra("puan" , oyuncu_2_puan);
                        intnt.putExtra("veri" , kim_kazandi_2);
                    }

                    finish();
                    startActivity(intnt);
                }

            }

            @Override
            public void onFinish() {
                sayac.setText("Kalan zaman: 0");
                if(kart_bulundu != 18) {
                    cards_found_music.stop();
                    end_time_music.start();

                    Intent intnt = new Intent(cok_oyun_6x6_activity.this, kazan_activity.class);
                    oyuncu_1_puan = Float.parseFloat(TV_puan_oyuncu_1.getText().toString().substring(17));
                    oyuncu_2_puan = Float.parseFloat(TV_puan_oyuncu_2.getText().toString().substring(17));

                    if (oyuncu_1_puan > oyuncu_2_puan) {
                        intnt.putExtra("puan" , oyuncu_1_puan);
                        intnt.putExtra("veri" , kim_kazandi_1);
                    }
                    else {
                        intnt.putExtra("puan" , oyuncu_2_puan);
                        intnt.putExtra("veri" , kim_kazandi_2);
                    }

                    finish();
                    startActivity(intnt);
                }
            }
        }.start();
    }

    //Oyun muzigini durdur
    public void stop_game_music(View v) {
        if(muzik_durdur.getText().toString().equals("||")) {
            game_music.pause();
            muzik_durdur.setText(">");
        }
        else {
            game_music.start();
            muzik_durdur.setText("||");
        }
    }

}