package org.odk.collect.android.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by generepridi on 1/20/17.
 */

public class UserDB  {
    private static final String TABLE_NAME = "user_profil";
    private static final String USER_NAME = "nom";
    private static final String USER_PRENOM = "prenom";
    private static final String USER_AGE = "age";
    private static final String USER_LOGIN ="login";
    private static final String USER_PASS = "password";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    //Création de la base de données
    public UserDB(Context context){
        maBaseSQLite = MySQLite.getInstance(context);
        Log.e("DATABASE OPERATIONS", "Database created / opened ...");
    }

    public void open()
    {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close()
    {
        //on ferme l'accès à la BDD
        db.close();
    }


    public long addUser(User user) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(USER_NAME, user.getNom());
        values.put(USER_PRENOM, user.getPrenom());
        values.put(USER_AGE,user.getAge());
        values.put(USER_LOGIN, user.getLogin());
        values.put(USER_PASS, user.getMot_de_passe());


        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

   public Cursor getUser(String login, String password){

       Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+USER_LOGIN+" = '"+login+"' AND "+USER_PASS+" = '"+password+"'", null);
       return c;
   }
}
