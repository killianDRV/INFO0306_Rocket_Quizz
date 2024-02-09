package com.example.projetquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * La class pour la connexion à l'application.
 */
public class ConnexionActivity extends AppCompatActivity {

    /**
     * EditText, Nom de l'utilisateur.
     */
    EditText username,
    /**
     * EditText, Mot de passe de l'utilisateur.
     */
    password;
    /**
     * Button, bouton pour s'identifier.
     */
    Button signin;
    /**
     * DBHelper, la base de donnée.
     */
    DBHelper DB;
    /**
     * int,texte "Champs requis".
     */
    int ChampsRequis = R.string.FieldsRequired;
    /**
     * int, texte "Connexion reussie".
     */
    int ConnexionReussie = R.string.SuccessfulConnection;
    /**
     * int, texte "Echec Connexion".
     */
    int EchecConnexion = R.string.ConnectionFailed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        username=findViewById(R.id.username1);
        password=findViewById(R.id.password1);
        signin=findViewById(R.id.signin1);
        DB = new DBHelper(this);

        //Connexion
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user=username.getText().toString();
                String pass=password.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass))
                    Toast.makeText(ConnexionActivity.this, ChampsRequis, Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass= DB.checkusernamepassword(user,pass);
                    if(checkuserpass==true){
                        Toast.makeText(ConnexionActivity.this, ConnexionReussie, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(ConnexionActivity.this, EchecConnexion, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}