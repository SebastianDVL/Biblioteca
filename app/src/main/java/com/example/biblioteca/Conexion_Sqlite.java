package com.example.biblioteca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conexion_Sqlite extends SQLiteOpenHelper {
    public Conexion_Sqlite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE TblLibros(cod_libro text primary key,titulo text not null," +
                " nom_autor text not null,genero text not null, activo text not null default 'si' )");
        sqLiteDatabase.execSQL("CREATE TABLE TblPrestamos(cod_prestamo text primary key,fecha text not null,"+
                "nom_cliente text not null, cod_libro text not null,activo text not null default 'si',"+"" +
                "constraint pk_prestamo foreign key (cod_libro) references TblLibros(cod_libro))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE TblLibros");{
                onCreate(sqLiteDatabase);
            }
        sqLiteDatabase.execSQL("DROP TABLE TblPrestamos");{
            onCreate(sqLiteDatabase);

        }
    }
}
