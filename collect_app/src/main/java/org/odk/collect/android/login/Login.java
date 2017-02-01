package org.odk.collect.android.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.securepreferences.SecurePreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.odk.collect.android.R;
import org.odk.collect.android.activities.MainMenuActivity;

import cz.msebera.android.httpclient.Header;

public class Login extends Activity {

    //Composantes du formulaire de connexion
    private TextView _identifiant;
    private TextView _password;
    private Button _connexionButton;
    private ProgressDialog progressDialog;


    private String login;
    private String password;
    private ConnexionDetecteur connexion;


    //Pour l'accès à la base
    //private UserDB myDB;
    //URL du serveur
    private final String URL_SERVER = "http://genereservicescout.alwaysdata.net/projetoner/getUser.php";
    private final String SECRET ="!projet@onerbydevcorp";
    private SharedPreferences sharedPreferences;
    protected static final String MyPREFERENCES = "MyPrefs" ;
    protected static final String loginkey = "LoginKey";
    protected static final String passwordkey = "passwordKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //myDB = new UserDB(this);

        _identifiant = (TextView)findViewById(R.id.input_user);
        _password = (TextView)findViewById(R.id.input_password);
        _connexionButton =(Button) findViewById(R.id.button_connect);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Connexion en cours ...");
        progressDialog.setIndeterminate(false);
        connexion = new ConnexionDetecteur(this);
        sharedPreferences = new SecurePreferences(getBaseContext(), "userpassword", MyPREFERENCES+".xml");


        _connexionButton.setOnClickListener(connexionListener);


    }

    //Vraible de réception du click sur le bouton de connexion
    private View.OnClickListener connexionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            login = _identifiant.getText().toString();
            password = _password.getText().toString();

            if(login.isEmpty()){
                _identifiant.setError("Donnez votre identifiant !");
            }
            else if(password.isEmpty()){
                _password.setError("Renseigner le mot de passe !");
            }
            else{
                connect();
                progressDialog.show();
            }
        }
    };


    //connexion à l'application
    public void connect(){
        //myDB.open();

        /*Cursor cursor = myDB.getUser(login, password);
        cursor.moveToNext();*/
        try {

            if ((sharedPreferences.contains(loginkey) && sharedPreferences.contains(passwordkey))
                    &&(sharedPreferences.getString(loginkey,"").equals(_identifiant.toString())
                    && sharedPreferences.getString(passwordkey,"").equals(_password.toString()))) {
                progressDialog.dismiss();
                String prenom , nom ;


                prenom = sharedPreferences.getString("firstnamekey","");//cursor.getString(cursor.getColumnIndex("nom"));
                nom = sharedPreferences.getString("lastnamekey","");//cursor.getString(cursor.getColumnIndex("prenom"));

                System.out.println("Prénom: "+prenom+" Nom: "+nom);
                Intent intent = new Intent(Login.this, MainMenuActivity.class);
                intent.putExtra("Prenom", prenom);
                intent.putExtra("Nom", nom);
                //myDB.close();
                startActivity(intent);
                Login.this.finish();

            } else {
                if(connexion.isConnected()) {
                    login(login, password);
                }else {
                    Toast.makeText(Login.this, "Cet appareil n'êtes pas connecté à Internet", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }
        }catch (Exception e){
            progressDialog.dismiss();
            e.printStackTrace();
            Log.e(">>>>>>>> ","ERREUR DANS LA FONCTION connect");
        }
    }

    //connexion au serveur
    public void login(String login,String password){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("login", login);
        params.put("password", password);
        params.put("myproject",SECRET);

       client.setTimeout(20000);
        client.get( URL_SERVER, params, new AsyncHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                //myDB.close();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(),
                            "Ressource non trouvée", Toast.LENGTH_LONG)
                            .show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(),
                            "le serveur spécifier est indisponible",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Erreur de traitement! [Verifier: Si le PDA dispose de la connexion Internet]",
                            Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject repons = new JSONObject(response);
                    JSONArray user = repons.getJSONArray("result");
                    if(user.length() <1){
                        Toast.makeText(Login.this,"La connexion est rejetée, login ou password incorrect...",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }else{
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        for(int i=0;i<user.length();i++){
                            editor.putString("firstnamekey",user.getJSONObject(i).getString("prenom"));
                            editor.putString("lastnamekey",user.getJSONObject(i).getString("nom"));
                            editor.putString(loginkey,user.getJSONObject(i).getString("login"));
                            editor.putString(passwordkey,user.getJSONObject(i).getString("password"));
                            editor.putString("age",user.getJSONObject(i).getString("age"));
                            editor.commit();
                            //myDB.addUser(u);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Connexion réussie",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MainMenuActivity.class);
                            intent.putExtra("Prenom", user.getJSONObject(i).getString("prenom"));
                            intent.putExtra("Nom", user.getJSONObject(i).getString("nom"));
                            //myDB.close();
                            startActivity(intent);
                            Login.this.finish();

                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                    Log.e(">>>>>>>>> ","Erreur du traitement de la réponse");
                    progressDialog.dismiss();
                }
            }
        });

    }



}
