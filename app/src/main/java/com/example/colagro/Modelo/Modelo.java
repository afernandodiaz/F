package com.example.colagro.Modelo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Modelo extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "ColAgro";
    private  static final String TB_USUARIO = "usuario";
    private  static final String TB_CATEGORIA = "categoria";
    private  static final String TB_PRODUCTO = "producto";
    private  static final String TB_ANUNCIO = "anuncio";

    public Modelo(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase ColAgro) {
        ColAgro.execSQL("CREATE TABLE " + TB_USUARIO + "(" +
                "us_id INTEGER PRIMARY KEY," +
                "us_nom TEXT NOT NULL,"+
                "us_cor TEXT NOT NULL," +
                "us_tel TEXT NOT NULL," +
                "us_dir TEXT NOT NULL," +
                "us_con TEXT NOT NULL)");

        ColAgro.execSQL("INSERT INTO "+TB_USUARIO+" (us_nom, us_cor, us_tel, us_dir, us_con) " +
                "VALUES ('Alex','alexferdiazacero@gmail.com','3214934098','cll 14E # 12-61','Alex02*')");
        ColAgro.execSQL("INSERT INTO "+TB_USUARIO+" (us_nom, us_cor, us_tel, us_dir, us_con) " +
                "VALUES ('Pedro','pedro02@gmail.com','3214567890','cll 12E # 12-61','Pedro02*')");

        ColAgro.execSQL("CREATE TABLE "+TB_CATEGORIA+"(" +
                "cat_id INTEGER PRIMARY KEY," +
                "cat_nom TEXT NOT NULL)");

        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Cereales')");
        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Leguminosas')");
        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Oleaginosas')");
        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Hortalizas')");
        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Frutales')");
        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Ornamentales')");
        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Raíces y tubérculos')");
        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Cultivos para bebidas medicinales y aromáticas')");
        ColAgro.execSQL("INSERT INTO "+TB_CATEGORIA+" (cat_nom) VALUES ('Cultivos tropicales tradicionales')");

        ColAgro.execSQL("CREATE TABLE "+TB_PRODUCTO+"(" +
                "pro_id INTEGER PRIMARY KEY," +
                "pro_nom TEXT NOT NULL," +
                "pro_can INT NOT NULL," +
                "pro_pres TEXT NOT NULL,"+
                "pro_prec REAL NOT NULL," +
                "pro_cat_id INT REFERENCES categoria(cat_id)," +
                "pro_us_id INT REFERENCES usuario(us_id))");

        ColAgro.execSQL("INSERT INTO "+TB_PRODUCTO+" (pro_nom, pro_can, pro_pres, pro_prec, pro_cat_id, pro_us_id)" +
                "VALUES ('Mora',10,'Canastilla 25Kg',45.000,5,1)");

        ColAgro.execSQL("CREATE TABLE "+TB_ANUNCIO+"(" +
                "an_id INTEGER PRIMARY KEY," +
                "an_tit TEXT NOT NULL," +
                "an_des TEXT NOT NULL," +
                "an_prod_id INT REFERENCES producto(pro_id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TB_USUARIO);
        sqLiteDatabase.execSQL("DROP TABLE " + TB_CATEGORIA);
        sqLiteDatabase.execSQL("DROP TABLE " + TB_PRODUCTO);
        sqLiteDatabase.execSQL("DROP TABLE " + TB_ANUNCIO);
        onCreate(sqLiteDatabase);
    }
}
