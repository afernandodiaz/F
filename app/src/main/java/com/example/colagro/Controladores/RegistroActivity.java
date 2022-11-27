package com.example.colagro.Controladores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Patterns;
import android.view.View;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.colagro.Modelo.Modelo;
import com.example.colagro.R;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        EditText Nombre = findViewById(R.id.txtNombre);
        EditText Correo = findViewById(R.id.txtCorreo);
        EditText Telefono = findViewById(R.id.txtTelefono);
        EditText Direccion = findViewById(R.id.txtDireccion);
        EditText Contra1 = findViewById(R.id.txtCon1);
        EditText Contra2 = findViewById(R.id.txtCon2);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Nom = Nombre.getText().toString().trim();
                String Cor = Correo.getText().toString().trim();
                String Tel = Telefono.getText().toString().trim();
                String Dir = Direccion.getText().toString().trim();
                String Con1 = Contra1.getText().toString().trim();
                String Con2 = Contra2.getText().toString().trim();

                if(Nom.isEmpty() || Cor.isEmpty()  || Tel.isEmpty() || Dir.isEmpty() || Con1.isEmpty()
                || Con2.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Se deben llenar todos los campos!!!",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    try{
                        Modelo admin = new Modelo(getApplicationContext());
                        SQLiteDatabase miBase = admin.getWritableDatabase();

                        if(validarCorreo(Cor)){
                            Cursor fila = miBase.rawQuery("SELECT * FROM usuario WHERE us_cor = "+"'"+Cor+"'", null);

                            if(fila.moveToFirst()){
                                Toast toast = Toast.makeText(getApplicationContext(),"El correo ingresado ya se encuentra en uso!!!",Toast.LENGTH_SHORT);
                                toast.show();
                            }else{
                                if(Con1.equals(Con2)){
                                    ContentValues registro = new ContentValues();
                                    registro.put("us_nom", Nom);
                                    registro.put("us_cor", Cor);
                                    registro.put("us_tel", Tel);
                                    registro.put("us_dir", Dir);
                                    registro.put("us_con", Con1);

                                    miBase.insert("usuario", null, registro);

                                    Toast toast = Toast.makeText(getApplicationContext(),"Registro exitoso!!!",Toast.LENGTH_SHORT);
                                    toast.show();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast toast = Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden!!!",Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(),"El correo ingresado no es válido!!!",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }catch (SQLiteException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

    }

    public boolean validarCorreo(String Correo) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(Correo).matches();
    }
}