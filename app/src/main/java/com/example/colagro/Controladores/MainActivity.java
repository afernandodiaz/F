package com.example.colagro.Controladores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.Context;

import com.example.colagro.Modelo.Modelo;
import com.example.colagro.R;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String EMAIL_KEY = "email_key";

    // key for storing password.
    public static final String PASSWORD_KEY = "password_key";

    // key for storing name
    public static final String NAME_KEY = "name_key";

    // key for storing id
    public static final String ID_KEY = "id_key";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email, password, name, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtCorreo = findViewById(R.id.txtCorreoI);
        EditText txtContra = findViewById(R.id.txtContra);
        Button btnEntrar = findViewById(R.id.btnEntrar);
        Button btnCrear = findViewById(R.id.btnCrear);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // in shared prefs inside het string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
        email = sharedpreferences.getString(EMAIL_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY, null);
        name = sharedpreferences.getString(NAME_KEY,null);
        id = sharedpreferences.getString(ID_KEY,null);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Correo = txtCorreo.getText().toString().trim();
                String Contra = txtContra.getText().toString().trim();

                if(Correo.isEmpty() || Contra.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Se deben llenar todos los campos!!!",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    try{
                        Modelo admin = new Modelo(getApplicationContext());
                        SQLiteDatabase miBase = admin.getWritableDatabase();

                        if(validarCorreo(Correo)){
                            Cursor fila = miBase.rawQuery("SELECT * FROM usuario WHERE us_cor = "+"'"+Correo+"'",null);

                            if(fila.moveToFirst()){
                                Toast toast = Toast.makeText(getApplicationContext(),"Bienvenido a ColAgro!!!",Toast.LENGTH_SHORT);
                                toast.show();

                                Cursor fila2 = miBase.rawQuery("SELECT us_id FROM usuario WHERE us_cor = "+"'"+Correo+"' AND us_con="+"'"+Contra+"'",null);

                                if(fila2.moveToFirst()){
                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    // below two lines will put values for
                                    // email and password in shared preferences.
                                    editor.putString(EMAIL_KEY, Correo);
                                    editor.putString(PASSWORD_KEY, Contra);
                                    editor.putString(ID_KEY, String.valueOf(fila2.getInt(0)));

                                    // to save our data with key and value.
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), InicioActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast toast2 = Toast.makeText(getApplicationContext(),"Contraseña incorrecta!!!",Toast.LENGTH_SHORT);
                                    toast2.show();
                                }
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(),"El correo no se encuentra registrado!!!",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(),"El correo ingresado no es válido!!!",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }catch (SQLiteException e){
                        Toast toast = Toast.makeText(getApplicationContext(),"Error: "+e+"",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (email != null && password != null) {
            Intent i = new Intent(MainActivity.this, InicioActivity.class);
            startActivity(i);
        }
    }

    public boolean validarCorreo(String Correo) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(Correo).matches();
    }
}