package com.example.projetquizz;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * La class pour la base de donnée.
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * String, le nom de la base de donnée.
     */
    public static final String DBNAME="login.db";

    /**
     * Instancie un nouveau Db helper.
     *
     * @param context the context
     */
    public DBHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
    }

    /**
     * insertData boolean.
     * insert des données dans la base de donnée.
     *
     * @param username the username
     * @param password the password
     * @return boolean
     */
    public Boolean insertData(String username, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("password", password);

        long result= db.insert("users", null, values);
        if(result==-1) return false;
        else return true;
    }

    /**
     * Checkusername boolean.
     * vérifie si le nom d'utilisateur correspond.
     *
     * @param username the username
     * @return the boolean
     */
    public Boolean checkusername(String username){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=?", new String[] {username});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    /**
     * Checkusernamepassword boolean.
     * vérifie si le nom d'utilisateur et le mot de passe corespondent.
     *
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?", new String[] {username,password});
        if(cursor.getCount()>0) return true;
        else return false;
    }

}
