package com.example.colagro.Controladores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.colagro.Clases.Categoria;
import com.example.colagro.Modelo.Modelo;
import com.example.colagro.R;

import java.util.ArrayList;

public class RegistroProductoActivity extends AppCompatActivity {

    Spinner SpinCat;
    ArrayList<String> listaCategorias;
    ArrayList<Categoria> categorias;
    EditText nom, can, pres, prec;
    String catSelected;
    int us, cat;
    Button btnRegProd;

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
        setContentView(R.layout.activity_registro_producto);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        SpinCat = (Spinner) findViewById(R.id.Cat);
        btnRegProd = findViewById(R.id.btnRegProd);
        nom = findViewById(R.id.txtProdNom);
        can = findViewById(R.id.txtProdCan);
        pres = findViewById(R.id.txtProdPres);
        prec = findViewById(R.id.txtProdPrec);
        us = Integer.parseInt(sharedpreferences.getString(ID_KEY,null));

        consultarListaCategorias();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(
                this, android.R.layout.simple_list_item_1,listaCategorias);

        SpinCat.setAdapter(adaptador);

        btnRegProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Nom = nom.getText().toString().trim();
                String Can = can.getText().toString().trim();
                String Pres = pres.getText().toString().trim();
                String Prec = prec.getText().toString().trim();
                catSelected = SpinCat.getSelectedItem().toString().trim();

                if(Nom.isEmpty() || Can.isEmpty() || Pres.isEmpty() ||
                        Prec.isEmpty() || catSelected.equals("Seleccione")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Se deben llenar todos los campos!!!",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    try {
                        Modelo admin = new Modelo(getApplicationContext());
                        SQLiteDatabase miBase = admin.getWritableDatabase();

                        Cursor fila = miBase.rawQuery("SELECT cat_id FROM categoria WHERE cat_nom = "+"'"+catSelected+"'",null);

                        if(fila.moveToFirst()){
                            int idCat = fila.getInt(0);

                            ContentValues registro = new ContentValues();
                            registro.put("pro_nom", Nom);
                            registro.put("pro_can", Can);
                            registro.put("pro_pres", Pres);
                            registro.put("pro_prec", Prec);
                            registro.put("pro_cat_id", idCat);
                            registro.put("pro_us_id", us);

                            miBase.insert("producto", null, registro);

                            Toast toast = Toast.makeText(getApplicationContext(),"Registro exitoso!!!",Toast.LENGTH_SHORT);
                            toast.show();

                            nom.setText("");
                            can.setText("");
                            pres.setText("");
                            prec.setText("");
                            SpinCat.setSelection(0);
                        }
                    }catch (SQLiteException e){
                        Toast toast = Toast.makeText(getApplicationContext(),"Error: "+e,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    private void consultarListaCategorias() {
        try {
            Modelo admin = new Modelo(getApplicationContext());
            SQLiteDatabase miBase = admin.getWritableDatabase();

            Categoria categoria;
            categorias = new ArrayList<Categoria>();
            Cursor fila = miBase.rawQuery("SELECT * FROM categoria", null);

            while (fila.moveToNext()) {
                categoria = new Categoria();
                categoria.setId(fila.getInt(0));
                categoria.setNombre(fila.getString(1));

                categorias.add(categoria);
            }

            fila.close();
            obtenerLista();

        }catch (SQLiteException e){
            Toast toast = Toast.makeText(getApplicationContext(),"Error: "+e,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void obtenerLista() {
        listaCategorias = new ArrayList<String>();
        listaCategorias.add("Seleccione");

        for (int i = 0; i<categorias.size(); i++){
            listaCategorias.add(categorias.get(i).getNombre());
        }
    }
}