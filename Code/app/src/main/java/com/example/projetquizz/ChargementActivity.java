package com.example.projetquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

/**
 * La class pour le chargement de l'application.
 */
public class ChargementActivity extends AppCompatActivity {

    private final int TIMER_CHARGEMENT = 2250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //redirige vers le formulaire d'inscription après 2sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), InscriptionActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIMER_CHARGEMENT);
    }
}