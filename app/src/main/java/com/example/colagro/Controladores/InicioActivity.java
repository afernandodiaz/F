package com.example.colagro.Controladores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.colagro.Modelo.Modelo;
import com.example.colagro.Clases.Producto;
import com.example.colagro.R;
import com.example.colagro.adaptadores.AdaptadorProductos;

import java.util.ArrayList;

public class InicioActivity extends AppCompatActivity {

    ArrayList<Producto> listProductos;
    RecyclerView recyclerViewProductos;
    Button AgProd, btnBus;

    SQLiteDatabase miBase;

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
        setContentView(R.layout.activity_inicio);

        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // getting data from shared prefs and
        // storing it in our string variable.
        email = sharedpreferences.getString(EMAIL_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY,null);
        name = sharedpreferences.getString(NAME_KEY, null);
        id = sharedpreferences.getString(ID_KEY,null);

        AgProd = (Button) findViewById(R.id.btnAgProd);
        btnBus = (Button) findViewById(R.id.btnBuscar);

        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BuscarProductoActivity.class);
                startActivity(intent);
            }
        });

        AgProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistroProductoActivity.class);
                startActivity(intent);
            }
        });

        Modelo admin = new Modelo(getApplicationContext());
        miBase = admin.getWritableDatabase();

        listProductos = new ArrayList<>();

        recyclerViewProductos = findViewById(R.id.ListadoProductos);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        consultarListaProductos();

        AdaptadorProductos adaptador  = new AdaptadorProductos(listProductos);
        recyclerViewProductos.setAdapter(adaptador);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cerrar_sesion:
                cerrarSesion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cerrarSesion() {
        // calling method to edit values in shared prefs.
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // below line will clear
        // the data in shared prefs.
        editor.clear();

        // below line will apply empty
        // data to shared prefs.
        editor.apply();

        // starting mainactivity after
        // clearing values in shared preferences.
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void consultarListaProductos() {
        try{
            Producto producto;

            Cursor cursor = miBase.rawQuery("SELECT * FROM producto", null);

            while(cursor.moveToNext()){
                producto = new Producto();
                producto.setId(cursor.getInt(0));
                producto.setNombre(cursor.getString(1));
                producto.setCantidad(cursor.getInt(2));
                producto.setPresentacion(cursor.getString(3));
                producto.setPrecio(cursor.getDouble(4));
                producto.setCategoria(cursor.getInt(5));
                producto.setUsuario(cursor.getInt(6));

                listProductos.add(producto);
            }

            cursor.close();
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(getApplicationContext(),"Error!!! "+ e,Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}