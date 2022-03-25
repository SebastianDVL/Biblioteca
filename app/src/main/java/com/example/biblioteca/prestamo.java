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

public class prestamo extends AppCompatActivity {
    EditText jetcodPrestamo,jetfecha,jetcliente,jetcodLibro;
    long sw,resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamo);

        getSupportActionBar().hide();
        jetcodPrestamo = findViewById(R.id.etcodigoPrestamo);
        jetfecha = findViewById(R.id.etfecha);
        jetcliente = findViewById(R.id.etnombreCliente);
        jetcodLibro = findViewById(R.id.etgcodLibro);
        sw = 0;
    }

    public void aMain(View view){
        Intent intmenu = new Intent(this,MainActivity.class);
        startActivity(intmenu);
    }
    
    public Cursor consultarCodPrestamo(){
        String codPrestamo = jetcodPrestamo.getText().toString();

        Conexion_Sqlite admin  = new Conexion_Sqlite(this,"biblioteca.db",null,1);
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor f = db.rawQuery("SELECT * FROM TblPrestamos WHERE cod_prestamo = '" + codPrestamo + "'",null);

        if(f.moveToNext()){
            if(f.getString(4).equals("si")){
                sw = 1;
            }else {
                sw = 3;
            }
        }else{
            sw = 0;
        }

        db.close();
        return f;

    }

    public void consultarPrestamo(View view){
        String codPrestamo = jetcodPrestamo.getText().toString();

        if(codPrestamo.isEmpty()){
            Toast.makeText(this, "Debes Ingresar Un Codigo De Prestamo Para Consultar!", Toast.LENGTH_SHORT).show();
            jetcodPrestamo.requestFocus();
        }else {
            Cursor f = consultarCodPrestamo();
            if(sw == 1){
                    
                Toast.makeText(this, "Prestamo Encontrado!", Toast.LENGTH_SHORT).show();
                jetfecha.setText(f.getString(1));
                jetcliente.setText(f.getString(2));
                jetcodLibro.setText(f.getString(3));
                sw = 0;
            }else if(sw == 0){
                Toast.makeText(this, "Prestamo Disponible!", Toast.LENGTH_SHORT).show();
                jetfecha.requestFocus();
            }else{
                Toast.makeText(this, "Prestamo Actualmente Anulado!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void guardar(View view){
        String codPrestamo,fecha,cliente,codLibro;
        codPrestamo = jetcodPrestamo.getText().toString();
        fecha = jetfecha.getText().toString();
        cliente = jetcliente.getText().toString();
        codLibro = jetcodLibro.getText().toString();
        if(codPrestamo.isEmpty() || fecha.isEmpty() || cliente.isEmpty() || codLibro.isEmpty()){
            Toast.makeText(this, "Debes Ingresar Todos Los Campos", Toast.LENGTH_SHORT).show();
        }else{
            Conexion_Sqlite admin  = new Conexion_Sqlite(this,"biblioteca.db",null,1);
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fil = db.rawQuery("SELECT * FROM TblLibros WHERE cod_libro = '" + codLibro + "'",null);
            if(fil.moveToNext() && fil.getString(4).equals("si")){
                consultarCodPrestamo();
                SQLiteDatabase db2 = admin.getWritableDatabase();
                ContentValues regis =  new ContentValues();
                regis.put("cod_prestamo",codPrestamo);
                regis.put("fecha",fecha);
                regis.put("nom_cliente",cliente);
                regis.put("cod_libro",codLibro);

                if(sw == 1){
                    resp = db2.update("TblPrestamos",regis,"cod_prestamo = '" + codPrestamo + "'",null);

                    if(resp > 0){
                        Toast.makeText(this, "Prestamos Modificado Exitosamente!", Toast.LENGTH_SHORT).show();
                        limpiar();
                    }else{
                        Toast.makeText(this, "No Se Pudo Modificar El Prestamo!", Toast.LENGTH_SHORT).show();
                    }
                    sw = 0;
                }else if(sw ==0){
                    resp = db2.insert("TblPrestamos",null,regis);
                    if(resp > 0){
                        Toast.makeText(this, "Prestamo Guardado Exitosamente!", Toast.LENGTH_SHORT).show();
                        limpiar();

                    }else{
                        Toast.makeText(this, "Error Al Guardar Prestamo!", Toast.LENGTH_SHORT).show();
                    }       
                }else{
                    Toast.makeText(this, "Este Prestamo Esta Anulado, No Se Puede Modificar", Toast.LENGTH_SHORT).show();
                }
                db2.close();

            }else{
                Toast.makeText(this, "El Libro No Existe En La Base De Datos o Esta Anulado!", Toast.LENGTH_SHORT).show();
                jetcodLibro.requestFocus();
            }
            db.close();
        }
    }
    public void anular(View view){
        String codPrestamo = jetcodPrestamo.getText().toString();
        consultarCodPrestamo();
        if(codPrestamo.isEmpty()){
            Toast.makeText(this, "Debes Ingresar Un Codigo!", Toast.LENGTH_SHORT).show();
            jetcodPrestamo.requestFocus();
        }else{
            if(sw == 1){
                Conexion_Sqlite admin  = new Conexion_Sqlite(this,"biblioteca.db",null,1);
                SQLiteDatabase db = admin.getWritableDatabase();
                ContentValues anul = new ContentValues();
                anul.put("activo","no");
                resp = db.update("TblPrestamos",anul,"cod_prestamo = '" + codPrestamo + "'",null);
                if(resp  > 0){
                    Toast.makeText(this, "Prestamo Anulado Satisfactoriamente!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error Al Anular Prestamo", Toast.LENGTH_SHORT).show();
                }
                sw = 0;
            }else if(sw == 0){
                Toast.makeText(this, "Este Prestamo No Existe!", Toast.LENGTH_SHORT).show();
                jetcodPrestamo.requestFocus();
            }else{
                Toast.makeText(this, "Este Prestamo Ya Esta Anulado!", Toast.LENGTH_SHORT).show();
                jetcodPrestamo.requestFocus();

            }
        }

    }
    public void cancelar(View view){
        limpiar();
    }
    public void limpiar(){
        jetcodLibro.setText("");
        jetcliente.setText("");
        jetcodPrestamo.setText("");
        jetfecha.setText("");
    }

}