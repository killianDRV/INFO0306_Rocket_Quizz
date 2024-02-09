package com.example.projetquizz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * La class pour afficher toutes les questions.
 */
public class QuestionActivity extends AppCompatActivity {

    /**
     * TextView, affiche le titre de la question.
     */
    TextView titreQuestion,
    /**
     * TextView, affiche les coordonnées du clic de l'utilisateur(question 4).
     */
    montionText,
    /**
     * TextView, affiche le titre de la question.
     */
    NombreCliqueTextView,
    /**
     * TextView, affiche le numéro de la question.
     */
    numQuestionTextView;
    /**
     * Button, pour la réponse A.
     */
    Button button1,
    /**
     * Button, pour la réponse B.
     */
    button2,
    /**
     * Button, pour la réponse C.
     */
    button3,
    /**
     * Button, pour la réponse D.
     */
    button4,
    /**
     * Button, pour prendre une photo (question 6).
     */
    boutonPrendrePhoto;
    /**
     * ImageView, affiche le coeur plein 1 de l'lutilisateur.
     */
    ImageView imageCoeur1,
    /**
     * ImageView, affiche le coeur plein 2 de l'lutilisateur.
     */
    imageCoeur2,
    /**
     * ImageView, affiche le coeur plein 3 de l'lutilisateur.
     */
    imageCoeur3,
    /**
     * ImageView, affiche le coeur vide 1 de l'lutilisateur.
     */
    imageCoeurvide1,
    /**
     * ImageView, affiche le coeur vide 2 de l'lutilisateur.
     */
    imageCoeurvide2,
    /**
     * ImageView, affiche le coeur vide 3 de l'lutilisateur.
     */
    imageCoeurvide3,
    /**
     * ImageView, affiche la photo prise par l'utilisateur.
     */
    imageViewCamera;
    /**
     * int, le nombre de vie de l'utilisateur.
     */
    int nbVie=3,
    /**
     * int, le numéro de la question.
     */
    numeroQuestion,
    /**
     * int, le compteur pour le motionEvent.
     */
    compteurToucherEcran=0;
    /**
     * Timer, timer pour l'affichage des questions.
     */
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        titreQuestion = findViewById(R.id.titreQuestion);
        montionText = findViewById(R.id.montionText);
        NombreCliqueTextView = findViewById(R.id.NombreCliqueTextView);
        numQuestionTextView = findViewById(R.id.numQuestionTextView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        boutonPrendrePhoto = findViewById(R.id.boutonPrendrePhoto);
        imageCoeur1 = findViewById(R.id.coeur1);
        imageCoeur2 = findViewById(R.id.coeur2);
        imageCoeur3 = findViewById(R.id.coeur3);
        imageCoeurvide1 = findViewById(R.id.coeurvide1);
        imageCoeurvide2 = findViewById(R.id.coeurvide2);
        imageCoeurvide3 = findViewById(R.id.coeurvide3);
        imageViewCamera = findViewById(R.id.imageViewCamera);

        numeroQuestion = getIntent().getIntExtra("numeroQuestion", 0);

        //S'occupe du onTouch sur l'écran.
        ConstraintLayout fond = (ConstraintLayout)findViewById(R.id.fond);
        fond.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent m) {
                handleTouch(m);
                return false;
            }
        });

        //Permission pour utilisation de la camera
        if (ContextCompat.checkSelfPermission(QuestionActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QuestionActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },100);
        }

        //Redirige vers l'appareil photo
        boutonPrendrePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Change l'affichage pour toutes les questions.
        changerAffichageQ1();
        changerAffichage();

        //Enregistre des données dans l'instance.
        if (savedInstanceState != null) {
            numeroQuestion = savedInstanceState.getInt("numeroQuestion");
            nbVie = savedInstanceState.getInt("nbVie");
            compteurToucherEcran = savedInstanceState.getInt("compteurToucherEcran");
            verifCoeur();
        }
    }

    //récupère la photo.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageViewCamera.setImageBitmap(captureImage);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numeroQuestion", numeroQuestion);
        outState.putInt("nbVie", nbVie);
        outState.putInt("compteurToucherEcran", compteurToucherEcran);
    }

    /**
     * HandleTouch.
     * Récupère la position de clic de l'itulisateur sur l'écran (question 4).
     * Incrémente le nombre de clic de l'utilisateur.
     *
     * @param m MotionEvent
     */
    void handleTouch(MotionEvent m) {
        if(numeroQuestion==4){
            int poiterCount = m.getPointerCount();
            for (int i = 0; i < poiterCount; i++) {
                int x = (int) m.getX(i);
                int y = (int) m.getY(i);
                int id = m.getPointerId(i);

                String touchStatus = getResources().getString(R.string.Coordonne)+"\nX: " + x + " Y: " + y;

                if (id == 0) {
                    montionText.setText(touchStatus);
                    NombreCliqueTextView.setText(getResources().getString(R.string.NbClique) + (compteurToucherEcran+1));
                    compteurToucherEcran++;
                }
                if (compteurToucherEcran == 5 || compteurToucherEcran == 10 || compteurToucherEcran == 15) {
                    perdreVie();
                    verifSiPerdu();
                }
                if ((x > 100 && x < 200) && (y > 500 && y < 600)) {
                    changerNumeroQuestion();
                    reactiverBoutonQ5();
                    changerAffichage();
                    ActivityReponse();
                }
            }
        }
    }

    /**
     * OnClickButton1.
     * Gère les cliques sur les boutons des réponses.
     *
     * @param v View
     */
    public void onClickButton1(View v){
        switch(numeroQuestion){
            case 1:     //Mauvaise réponse
                perdreVie();
                break;
            case 2:     //Mauvaise réponse
                perdreVie();
                break;
            case 3:     //Mauvaise réponse
                perdreVie();
                break;
            case 5:     //Bonne réponse
                changerNumeroQuestion();
                activerBoutonQ6();
                changerAffichage();
                ActivityReponse();
                break;
            case 7:     //Mauvaise réponse
                perdreVie();
                break;
            case 8:     //Mauvaise réponse
                perdreVie();
                break;
            case 9:     //Mauvaise réponse
                perdreVie();
                break;
            case 10:     //Mauvaise réponse
                perdreVie();
                break;
            default:
                break;
        }
        verifSiPerdu();
    }

    /**
     * OnClickButton2.
     * Gère les cliques sur les boutons des réponses.
     *
     * @param v View
     */
    public void onClickButton2(View v){
        switch(numeroQuestion){
            case 1:     //Mauvaise réponse
                perdreVie();
                break;
            case 2:     //Mauvaise réponse
                perdreVie();
                break;
            case 3:     //Mauvaise réponse
                perdreVie();
                break;
            case 5:     //Mauvaise réponse
                perdreVie();
                break;
            case 7:     //Bonne réponse
                changerNumeroQuestion();
                changerAffichage();
                ActivityReponse();
                break;
            case 8:     //Mauvaise réponse
                changerNumeroQuestion();
                changerAffichage();
                ActivityReponse();
                break;
            case 9:     //Mauvaise réponse
                perdreVie();
                break;
            case 10:     //Bonne réponse
                changerNumeroQuestion();
                changerAffichage();
                ActivityReponse();
                break;
            default:
                break;
        }
        verifSiPerdu();
    }

    /**
     * OnClickButton3.
     * Gère les cliques sur les boutons des réponses.
     *
     * @param v View
     */
    public void onClickButton3(View v){
        switch(numeroQuestion){
            case 1:     //Bonne réponse
                changerNumeroQuestion();
                changerAffichage();
                ActivityReponse();
                break;
            case 2:     //Mauvaise réponse
                perdreVie();
                break;
            case 3:     //Bonne réponse
                changerNumeroQuestion();
                changerAffichage();
                montionText.setVisibility(View.VISIBLE);
                NombreCliqueTextView.setVisibility(View.VISIBLE);
                ActivityReponse();
                break;
            case 4:     //Bonne réponse
                changerNumeroQuestion();
                reactiverBoutonQ5();
                changerAffichage();
                ActivityReponse();
                break;
            case 5:     //Mauvaise réponse
                perdreVie();
                break;
            case 7:     //Mauvaise réponse
                perdreVie();
                break;
            case 8:     //Mauvaise réponse
                perdreVie();
                break;
            case 9:     //Mauvaise réponse
                perdreVie();
                break;
            case 10:     //Mauvaise réponse
                perdreVie();
                break;
            default:
                break;
        }
        verifSiPerdu();
    }

    /**
     * OnClickButton4.
     * Gère les cliques sur les boutons des réponses.
     *
     * @param v View
     */
    public void onClickButton4(View v){
        switch(numeroQuestion){
            case 1:     //Mauvaise réponse
                perdreVie();
                break;
            case 2:     //Bonne réponse
                changerNumeroQuestion();
                changerAffichage();
                ActivityReponse();
                break;
            case 3:     //Bonne réponse
                changerNumeroQuestion();
                changerAffichage();
                ActivityReponse();
                break;
            case 5:     //Mauvaise réponse
                perdreVie();
                break;
            case 7:     //Mauvaise réponse
                perdreVie();
                break;
            case 8:     //Mauvaise réponse
                perdreVie();
                break;
            case 9:     //Bonne réponse
                changerNumeroQuestion();
                changerAffichage();
                ActivityReponse();
                break;
            case 10:     //Mauvaise réponse
                perdreVie();
                break;
            default:
                break;
        }
        verifSiPerdu();
    }

    /**
     * ImageQ5Cliquable.
     * Réactive les boutons quand la question 5 est réussite.
     *
     * @param v View
     */
    public void imageQ5Cliquable(View v){
        changerNumeroQuestion();
        changerAffichage();
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);
        imageViewCamera.setVisibility(View.INVISIBLE);
        boutonPrendrePhoto.setVisibility(View.INVISIBLE);
        ActivityReponse();
    }

    /**
     * ReactiverBoutonQ5.
     * Réactive les boutons pour la question 5.
     */
    public void reactiverBoutonQ5(){
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);
    }

    /**
     * ActiverBoutonQ6.
     * Réactive les boutons pour la question 6.
     */
    public void activerBoutonQ6(){
        boutonPrendrePhoto.setVisibility(View.VISIBLE);
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        button4.setVisibility(View.INVISIBLE);
    }

    /**
     * VerifCoeur.
     * Transforme le coeur plein en coeur vide quand l'utilisateur perd une vie.
     */
    public void verifCoeur(){
        if (nbVie==2){
            imageCoeur1.setVisibility(View.INVISIBLE);
            imageCoeurvide1.setVisibility(View.VISIBLE);
        }else if (nbVie==1){
            imageCoeur1.setVisibility(View.INVISIBLE);
            imageCoeurvide1.setVisibility(View.VISIBLE);

            imageCoeur2.setVisibility(View.INVISIBLE);
            imageCoeurvide2.setVisibility(View.VISIBLE);
        }
    }

    /**
     * PerdreVie.
     * Décrémente le nombre de vie.
     */
    public void perdreVie(){
        if(nbVie==3){
            imageCoeur1.setVisibility(View.INVISIBLE);
            imageCoeurvide1.setVisibility(View.VISIBLE);
            nbVie--;
        } else if(nbVie==2){
            imageCoeur2.setVisibility(View.INVISIBLE);
            imageCoeurvide2.setVisibility(View.VISIBLE);
            nbVie--;
        } else if(nbVie==1){
            imageCoeur3.setVisibility(View.INVISIBLE);
            imageCoeurvide3.setVisibility(View.VISIBLE);
            nbVie--;
        }
    }

    /**
     * VerifSiPerd.
     * Vérifie si l'utilisateur à perdu (si oui, il est renvoyé à l'accueil).
     */
    public void verifSiPerdu(){
        if(nbVie==0){
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        }
    }

    /**
     * ChangerAffichageQ1.
     * Change l'affichage pour la question 1.
     */
    public void changerAffichageQ1(){
        numQuestionTextView.setText(getResources().getString(R.string.QuestionNumero)+numeroQuestion);
        titreQuestion.setText(getResources().getString(R.string.TitreQ1));
        button1.setText("Clash royal");
        button2.setText("Uber eat");
        button3.setText("Rocket Quizz");
        button4.setText("Tinder");
    }

    /**
     * ChangerAffichage.
     * Change l'affichage des questions et des réponses.
     */
    public void changerAffichage(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                numQuestionTextView.setText(getResources().getString(R.string.QuestionNumero)+numeroQuestion);
                switch(numeroQuestion){
                    case 2:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ2));
                        button1.setText("17");
                        button2.setText("18");
                        button3.setText("19");
                        button4.setText("20");
                        break;
                    case 3:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ3));
                        button1.setText("9");
                        button2.setText("1");
                        button3.setText("12");
                        button4.setText("6");
                        break;
                    case 4:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ4));
                        button1.setVisibility(View.INVISIBLE);
                        button2.setVisibility(View.INVISIBLE);
                        button4.setVisibility(View.INVISIBLE);
                        button3.setText(getResources().getString(R.string.Bt3Q4));
                        break;
                    case 5:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ5));
                        button1.setText(getResources().getString(R.string.Bt1Q5));
                        button2.setText(getResources().getString(R.string.Bt2Q5));
                        button3.setText(getResources().getString(R.string.Bt3Q5));
                        button4.setText(getResources().getString(R.string.Bt4Q5));
                        NombreCliqueTextView.setVisibility(View.INVISIBLE);
                        montionText.setVisibility(View.INVISIBLE);
                        break;
                    case 6:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ6));
                        boutonPrendrePhoto.setText(getResources().getString(R.string.Bt3Q6));
                        activerBoutonQ6();
                        break;
                    case 7:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ7));
                        button1.setText(getResources().getString(R.string.Bt1Q7));
                        button2.setText(getResources().getString(R.string.Bt2Q7));
                        button3.setText(getResources().getString(R.string.Bt3Q7));
                        button4.setText(getResources().getString(R.string.Bt4Q7));
                        break;
                    case 8:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ8));
                        button1.setText("0");
                        button2.setText("1");
                        button3.setText("49");
                        button4.setText("50");
                        break;
                    case 9:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ9));
                        button1.setText(getResources().getString(R.string.Bt1Q9));
                        button2.setText(getResources().getString(R.string.Bt2Q9));
                        button3.setText(getResources().getString(R.string.Bt3Q9));
                        button4.setText(getResources().getString(R.string.Bt4Q9));
                        break;
                    case 10:
                        titreQuestion.setText(getResources().getString(R.string.TitreQ10));
                        button1.setText("-20");
                        button2.setText("100");
                        button3.setText("40");
                        button4.setText("80");
                        break;
                    case 11:
                        titreQuestion.setText("Q11");
                        button1.setText("1");
                        button2.setText("2");
                        button3.setText("3");
                        button4.setText("4");
                        break;
                    default:
                        break;
                }
            }
        },300);
    }

    /**
     * ChangerNumroQuestion.
     * Incrémente de 1 le numéro de la question.
     */
    public void changerNumeroQuestion(){
        numeroQuestion++;
    }

    /**
     * ActivityReponse.
     * Envoye vers l'activité des réponses aux questions.
     */
    public void ActivityReponse(){
        Intent intent=new Intent(getApplicationContext(),ReponseActivity.class);
        intent.putExtra("numeroQuestion",numeroQuestion);
        startActivity(intent);
    }

}