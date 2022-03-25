package com.example.biblioteca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class libros extends AppCompatActivity {
    EditText jetcodigo, jetitulo,jetautor,jetgenero;

    long sw = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros);

        getSupportActionBar().hide();
        jetcodigo = findViewById(R.id.etcodigo);
        jetitulo = findViewById(R.id.etitulo);
        jetautor = findViewById(R.id.etautor);
        jetgenero = findViewById(R.id.etgenero);

    }


    public void aMain(View view){
        Intent intmenu = new Intent(this,MainActivity.class);
        startActivity(intmenu);
    }


    public void consultar(View view) {
        String codigo = jetcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "Debes ingresar un codigo!", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }else{
            Conexion_Sqlite admin  = new Conexion_Sqlite(this,"biblioteca.db",null,1);
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor f = db.rawQuery("SELECT * FROM TblLibros WHERE cod_libro = '" + codigo + "'",null);
            if(f.moveToNext()){
                jetitulo.setText(f.getString(1));
                jetautor.setText(f.getString(2));
                jetgenero.setText(f.getString(3));
                Toast.makeText(this, "Codigo Encontrado!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Este Codigo No se Encontro En La Base De Datos!", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }


    public void adicionar(View view){
         String codigo,titulo,autor,genero;
        codigo = jetcodigo.getText().toString();
        titulo = jetitulo.getText().toString();
        autor = jetautor.getText().toString();
        genero  = jetgenero.getText().toString();

       if(codigo.isEmpty() || titulo.isEmpty() || autor.isEmpty() || genero.isEmpty()){
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
           Conexion_Sqlite admin  = new Conexion_Sqlite(this,"biblioteca.db",null,1);
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("cod_libro",codigo);
            registro.put("titulo",titulo);
            registro.put("nom_autor",autor);
            registro.put("genero",genero);

            long resp = db.insert("TblLibros",null,registro);

            if(resp > 0 ){
                Toast.makeText(this, "Libro Adicionado Correctamente!", Toast.LENGTH_SHORT).show();
                jetcodigo.setText("");
                jetgenero.setText("");
                jetitulo.setText("");
                jetautor.setText("");
            }else{
                Toast.makeText(this, "Error Al Adicionar Libro!", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }
}