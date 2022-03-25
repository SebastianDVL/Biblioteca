package com.example.biblioteca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }


    public void aPrestamo(View view){
        Intent intPrestamo = new Intent(this,prestamo.class);
        startActivity(intPrestamo);
    }

    public void aLibros(View view){
        Intent intLibros = new Intent(this,libros.class);
        startActivity(intLibros);
    }

}