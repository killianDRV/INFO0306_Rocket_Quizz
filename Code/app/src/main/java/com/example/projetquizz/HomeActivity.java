package com.example.projetquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * La class pour la page d'acceuil de l'application.
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * Button, pour le monde 1.
     */
    Button monde1,
    /**
     * Button, pour le monde 2.
     */
    monde2,
    /**
     * Button, pour le monde 3.
     */
    monde3;
    /**
     * int, le numéro de la question.
     */
    int numeroQuestion,
    /**
     * int, decoreco pour savoir si l'utilisateur se déconecte ou change juste d'activité.
     */
    decoReco = 0;
    /**
     * TextView, affiche la question maximal atteinte par l'utilisateur.
     */
    TextView textView2;
    /**
     * String, le shared preference
     */
    public static final String SHARED_PREFS = "sharedPrefs";
    /**
     * String, text
     */
    public static final String TEXT = "text";
    /**
     * Boolean, quitterApp pour savoir si l'utilisateur se déconecte ou change juste d'activité.
     */
    public boolean quitterApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createNotificationChannel();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        monde1 = findViewById(R.id.monde1);
        monde2 = findViewById(R.id.monde2);
        monde3 = findViewById(R.id.monde3);
        textView2 = findViewById(R.id.textView2);

        quitterApp = true;

        monde2.setEnabled(false);
        monde3.setEnabled(false);

        //récupère les données dans le SharedPreference
        numeroQuestion = loadData();

        //Récupère les données dans l'intent
        decoReco = getIntent().getIntExtra("decoReco", 0);
        if (decoReco == 2) {
            numeroQuestion = getIntent().getIntExtra("numeroQuestion", 1);
        }

        //rend enabled les mondes débloquer
        ouvrirMonde();
    }

    /**
     * createNotificationChannel
     * Crée une notification qui sera envoyé plus tard.
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description ="Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notificationRocketQuizz", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        //enregistre des données dans le SharedPreference et active l'envoie de notification.
        saveData();
        envoyerNotification();
    }

    protected void onStop(){
        super.onStop();
        //enregistre des données dans le SharedPreference et active l'envoie de notification.
        saveData();
        envoyerNotification();
    }

    /**
     * Open monde 1.
     * Lance le jeu (le monde 1).
     *
     * @param v View
     */
    public void openMonde1(View v){
        quitterApp=false;
        Intent intent=new Intent(getApplicationContext(),QuestionActivity.class);
        intent.putExtra("numeroQuestion",1);
        startActivity(intent);
    }

    /**
     * Open monde 2.
     * Lance le jeu (le monde 2).
     *
     * @param v View
     */
    public void openMonde2(View v){
        quitterApp=false;
        Intent intent=new Intent(getApplicationContext(),QuestionActivity.class);
        intent.putExtra("numeroQuestion",11);
        startActivity(intent);
    }

    /**
     * Open monde 3.
     * Lance le jeu (le monde 3).
     *
     * @param v View
     */
    public void openMonde3(View v){
        quitterApp=false;
        Intent intent=new Intent(getApplicationContext(),QuestionActivity.class);
        intent.putExtra("numeroQuestion",21);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_acceuil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.acceuil_parametre:
                quitterApp=false;
                Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.createur:
                quitterApp=false;
                Intent intent2 = new Intent(getApplicationContext(),CreateurActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Envoyer notification.
     * Permet d'envoyer des notification quand l'utilisateur quitte l'application.
     */
    public void envoyerNotification(){
        if(quitterApp==true) {

            Intent intent = new Intent(HomeActivity.this, ReminderBroadcast.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            long timeAtButtonClick = System.currentTimeMillis();
            long tenSecondsInMillis = 1000 * 20;

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    timeAtButtonClick + tenSecondsInMillis,
                    pendingIntent);
        }
    }

    /**
     * Ouvrir monde.
     * Débloque les mondes.
     */
    public void ouvrirMonde(){
        if(numeroQuestion==11){
            monde2.setEnabled(true);
        }
        if(numeroQuestion==21){
            monde3.setEnabled(true);
        }
    }

    /**
     * Save data.
     * Enregistre les données (le numéro de question maximum de l'utilisateur) quand il quitte l'application.
     */
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(TEXT, numeroQuestion);

        editor.apply();
    }

    /**
     * Load data int.
     * Récupère les données enregistrées dans le SharedPreference.
     *
     * @return int
     */
    public int loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(TEXT, 1);
    }

}