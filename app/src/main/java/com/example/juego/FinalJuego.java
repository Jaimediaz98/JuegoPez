package com.example.juego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinalJuego extends AppCompatActivity {

    String puntos;
    int p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //llamamos a la pantalla final tras perder
        setContentView(R.layout.activity_final_juego);
        Intent intent = getIntent();
        //Mostramos la puntuacion obtenida en el juego
        p=getIntent().getIntExtra("Puntos",0);
        puntos=String.valueOf(p);
        TextView tv4;
        tv4= findViewById(R.id.textView4);
        tv4.setText(puntos);

    }

    public void empezar(View view) {
        Intent intentFinal= new Intent(this, MainActivity.class);
        this.startActivity(intentFinal);
    }
}
