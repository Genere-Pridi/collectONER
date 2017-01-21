package org.odk.collect.android.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "user_profil";
    private static final String CREATE_QUERY=
            "CREATE TABLE "+TABLE_NAME+" ( "+" iduser INTEGER PRIMARY KEY AUTOINCREMENT , "
                    +"nom"+" TEXT , "
                    +"prenom"+" TEXT , "
                    +"age"+" INTEGER , "
                    +"login"+" TEXT , "
                    +"password"+" TEXT );";
    private static final String DATABASE_NAME = "onerLogin.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static MySQLite sInstance;

    public static synchronized MySQLite getInstance(Context context) {
        if (sInstance == null) { sInstance = new MySQLite(context); }
        return sInstance;
    }

    private MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Création de la base de données
        // on exécute ici les requêtes de création des tables
        sqLiteDatabase.execSQL(CREATE_QUERY); // création table "USER"
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Mise à jour de la base de données
        // méthode appelée sur incrémentation de DATABASE_VERSION
        // on peut faire ce qu'on veut ici, comme recréer la base :
        onCreate(sqLiteDatabase);
    }

}
