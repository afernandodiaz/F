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

public class BuscarProductoActivity extends AppCompatActivity {

    Spinner SpinCat;
    Button btnBuscarProd, btnElimProd, btnActProd;
    EditText ProdBus, nom, can, pres, prec, cat;
    SQLiteDatabase miBase;
    ArrayList<String> listaCategorias;
    ArrayList<Categoria> categorias;

    //creating constant keys for shared preferences.
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
        setContentView(R.layout.activity_buscar_producto);

        btnBuscarProd = findViewById(R.id.btnBuscarProd);
        btnElimProd = findViewById(R.id.BtnElimProd);
        btnActProd = findViewById(R.id.btnActProd);
        ProdBus = findViewById(R.id.txtBusProd);
        nom = findViewById(R.id.txtProdNom2);
        can = findViewById(R.id.txtProdCan2);
        pres = findViewById(R.id.txtProdPres2);
        prec = findViewById(R.id.txtProdPrec2);
        SpinCat = findViewById(R.id.SpinCatAct);

        consultarListaCategorias();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(
                this, android.R.layout.simple_list_item_1,listaCategorias);

        SpinCat.setAdapter(adaptador);


        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // getting data from shared prefs and
        // storing it in our string variable.
        email = sharedpreferences.getString(EMAIL_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY,null);
        name = sharedpreferences.getString(NAME_KEY, null);
        id = sharedpreferences.getString(ID_KEY,null);

        btnElimProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String prod = ProdBus.getText().toString().trim();

                    Modelo admin = new Modelo(getApplicationContext());
                    miBase = admin.getWritableDatabase();

                    Cursor fila = miBase.rawQuery("SELECT * FROM producto  JOIN categoria ON pro_cat_id = cat_id" +
                            " WHERE pro_nom LIKE "+"'%"+prod+"%' AND pro_us_id = " + id +"",null);

                    if(fila.moveToFirst()){
                        int cantidad = miBase.delete("producto","pro_id = " + fila.getInt(0), null);
                        Toast toast = Toast.makeText(getApplicationContext(),"Producto eliminado exitosamente!!!",Toast.LENGTH_SHORT);
                        toast.show();
                        nom.setText("");
                        can.setText("");
                        pres.setText("");
                        prec.setText("");
                        SpinCat.setSelection(0);
                        ProdBus.setText("");
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),"No se encuentra ningún producto!!!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (SQLiteException e){
                    Toast toast = Toast.makeText(getApplicationContext(),"Error: " + e,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnActProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String prod = ProdBus.getText().toString().trim();

                    Modelo admin = new Modelo(getApplicationContext());
                    miBase = admin.getWritableDatabase();

                    Cursor fila = miBase.rawQuery("SELECT * FROM producto  JOIN categoria ON pro_cat_id = cat_id" +
                            " WHERE pro_nom LIKE "+"'%"+prod+"%' AND pro_us_id = " + id +"",null);

                    if(fila.moveToFirst()){
                        String Nom = nom.getText().toString().trim();
                        String Can = can.getText().toString().trim();
                        String Pres = pres.getText().toString().trim();
                        String Prec = prec.getText().toString().trim();
                        String catSelected = SpinCat.getSelectedItem().toString().trim();

                        if(Nom.isEmpty() || Can.isEmpty() || Pres.isEmpty() ||
                                Prec.isEmpty() || catSelected.equals("Seleccione")){
                            Toast toast = Toast.makeText(getApplicationContext(),"No deben existir campos vacíos!!!",Toast.LENGTH_SHORT);
                            toast.show();
                        }else {
                            try {
                                Cursor fila2 = miBase.rawQuery("SELECT cat_id FROM categoria WHERE cat_nom = " + "'" + catSelected + "'", null);

                                if (fila2.moveToFirst()) {
                                    int idCat = fila2.getInt(0);

                                    ContentValues registro = new ContentValues();
                                    registro.put("pro_nom", Nom);
                                    registro.put("pro_can", Can);
                                    registro.put("pro_pres", Pres);
                                    registro.put("pro_prec", Prec);
                                    registro.put("pro_cat_id", idCat);

                                    int cantidad = miBase.update("producto", registro, "pro_id = " + fila.getInt(0), null);

                                    if(cantidad == 1){
                                        Toast toast = Toast.makeText(getApplicationContext(), "Registro actualizado exitosamente!!!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                    ProdBus.setText("");
                                    nom.setText("");
                                    can.setText("");
                                    pres.setText("");
                                    prec.setText("");
                                    SpinCat.setSelection(0);
                                }
                            } catch (SQLiteException e) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),"No se encuentra ningún producto!!!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (SQLiteException e){
                    Toast toast = Toast.makeText(getApplicationContext(),"Error: " + e,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnBuscarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String prod = ProdBus.getText().toString().trim();

                    Modelo admin = new Modelo(getApplicationContext());
                    miBase = admin.getWritableDatabase();

                    Cursor fila = miBase.rawQuery("SELECT * FROM producto  JOIN categoria ON pro_cat_id = cat_id" +
                            " WHERE pro_nom LIKE "+"'%"+prod+"%' AND pro_us_id = " + id +"",null);

                    if(fila.moveToFirst()){
                        nom.setText(fila.getString(1));
                        can.setText(fila.getString(2));
                        pres.setText(fila.getString(3));
                        prec.setText(fila.getString(4));

                        SpinCat.setSelection(Integer.parseInt(fila.getString(5)));
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),"No se encuentra ningún producto!!!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (SQLiteException e){
                    Toast toast = Toast.makeText(getApplicationContext(),"Error: " + e,Toast.LENGTH_SHORT);
                    toast.show();
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