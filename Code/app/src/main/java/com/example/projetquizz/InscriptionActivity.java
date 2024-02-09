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
 * La class pour l'inscription à l'application.
 */
public class InscriptionActivity extends AppCompatActivity {

    /**
     * EditText, Nom de l'utilisateur.
     */
    EditText username,
    /**
     * EditText, Mot de passe de l'utilisateur.
     */
    password,
    /**
     * EditText, Confirmation du mot de passe de l'utilisateur.
     */
    repassword;
    /**
     * Button, bouton pour se s'inscire.
     */
    Button signup,
    /**
     * Button, bouton pour se connecter.
     */
    signin;
    /**
     * DBHelper, la base de donnée.
     */
    DBHelper DB;
    /**
     * int,texte "Champs requis".
     */
    int ChampsRequis = R.string.FieldsRequired;
    /**
     * int,texte "Enregistre Succes".
     */
    int EnregistreSucces = R.string.SuccessfullyRegistered;
    /**
     * int,texte "Echec Enregistrement".
     */
    int EchecEnregistrement = R.string.FailedRegister;
    /**
     * int,texte "Utilisateur Existe".
     */
    int UtilisateurExiste = R.string.UserExists;
    /**
     * int,texte "Utilisateur Existe".
     */
    int MDPInegale = R.string.PasswordsNotMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        signin=findViewById(R.id.signin);
        signup=findViewById(R.id.signup);
        DB = new DBHelper(this);

        //Inscription
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user= username.getText().toString();
                String pass= password.getText().toString();
                String repass= repassword.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass))
                    Toast.makeText(InscriptionActivity.this, ChampsRequis, Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser==false){
                            Boolean insert = DB.insertData(user,pass);
                            if(insert==true){
                                Toast.makeText(InscriptionActivity.this, EnregistreSucces, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(InscriptionActivity.this, EchecEnregistrement, Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(InscriptionActivity.this, UtilisateurExiste, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(InscriptionActivity.this, MDPInegale, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //redirige vers la page d'inscription
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), ConnexionActivity.class);
                startActivity(intent);
            }
        });

    }
}