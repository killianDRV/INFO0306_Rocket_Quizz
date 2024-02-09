package com.example.projetquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * La class pour les réponses aux questions.
 */
public class ReponseActivity extends AppCompatActivity {

    /**
     * TextView, l'explication des réponses aux questions.
     */
    TextView textReponse,
    /**
     * TextView, Prend tout l'écran.
     */
    textEcran;
    /**
     * int, le numéro de la question.
     */
    int numeroQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reponse);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textReponse = findViewById(R.id.textReponse);
        textEcran = findViewById(R.id.textEcran);

        numeroQuestion = getIntent().getIntExtra("numeroQuestion", 0);
        numeroQuestion--;

        //Affiche les réponses après les questions.
        affichageReponse();
    }

    /**
     * BackQuestion.
     * Renvoie au menu quand l'utilisateur a fini un monde.
     *
     * @param v View
     */
    public void backQuestion(View v){
        if(numeroQuestion==10){
            textReponse.setText(getResources().getString(R.string.FiniMonde1));
            numeroQuestion++;
        }else if(numeroQuestion==11){
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            intent.putExtra("numeroQuestion",numeroQuestion);
            intent.putExtra("decoReco",2);
            startActivity(intent);
        }else{
            finish();
        }
    }

    /**
     * Affichage reponse.
     * Affiche la réponse au question.
     */
    public void affichageReponse(){
        switch(numeroQuestion){
            case 1:
                textReponse.setText(getResources().getString(R.string.ReponseQ1));
                break;
            case 2:
                textReponse.setText(getResources().getString(R.string.ReponseQ2));
                break;
            case 3:
                textReponse.setText(getResources().getString(R.string.ReponseQ3));
                break;
            case 4:
                textReponse.setText(getResources().getString(R.string.ReponseQ4));
                break;
            case 5:
                textReponse.setText(getResources().getString(R.string.ReponseQ5));
                break;
            case 6:
                textReponse.setText(getResources().getString(R.string.ReponseQ6));
                break;
            case 7:
                textReponse.setText(getResources().getString(R.string.ReponseQ7));
                break;
            case 8:
                textReponse.setText(getResources().getString(R.string.ReponseQ8));
                break;
            case 9:
                textReponse.setText(getResources().getString(R.string.ReponseQ9));
                break;
            case 10:
                textReponse.setText(getResources().getString(R.string.ReponseQ10));
                break;
            default:
                break;
        }
    }

}