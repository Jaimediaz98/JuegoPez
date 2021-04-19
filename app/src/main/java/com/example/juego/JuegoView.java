package com.example.juego;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class JuegoView extends View {
    //Declaracion de todas las variables que se van a usar en este juego
    int velocidad =0;
    int gravedad=3;
    int pezX;
    int pezY;
    int milisegundos=30;
    Runnable rb;
    Handler hn;
    int rojaX;
    int rojaY;
    int verdeX;
    int verdeY;
    int amarilloX;
    int amarilloY;
    int altoPez;
    int largoPez;
    int altoBola;
    int largoBola;
    int puntoss=0;
    int corazones;
    public SoundPool sp;
    int bolaBuena;
    int bolaMala;

    public JuegoView(Context context){
        super(context);
        //llamada al runable
        rb=new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        //llamada al handler
        hn= new Handler();
        //inicializamos el SoundPool y se lo asignamos a las variables de la bola buena y la bola mala
        sp = new SoundPool( 2, AudioManager.STREAM_MUSIC , 0);
        bolaBuena = sp.load(getContext(), R.raw.bueno, 0);
        bolaMala = sp.load(getContext(), R.raw.malo, 0);
        //Inicializamos la variable corazones a 3, que son las vias que tenemos
        corazones=3;
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //Pintamos el fondo del juego
        Bitmap fondo= BitmapFactory.decodeResource(getResources(), R.drawable.fondojuego);
        canvas.drawBitmap(fondo, 0, 0, null);

        velocidad+=gravedad;
        pezY += velocidad;

        //Este es el bot del juego, el pez no puede bajar mas de ahi
        if(pezY>1500){
            pezY=1500;
        }

        //Este es el top del juego, el pez no puede subir mas de ahi
        if(pezY<300){
            pezY=300;
        }

        //Pintamos el pez
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.pezclic);
        canvas.drawBitmap(b, 25, pezY, null);

        //Pintamos la puntuacion
        Paint puntuacion = new Paint();
        puntuacion.setTextSize(50);
        puntuacion.setColor(Color.WHITE);
        canvas.drawText("Puntuacion: "+puntoss, 20, 50, puntuacion);

        //Pintamos los corazones
        Bitmap[] vidas = {BitmapFactory.decodeResource(getResources(), R.drawable.vidas),BitmapFactory.decodeResource(getResources(), R.drawable.vidamenos)};

        //Este es el tope maximo por la parte derecha por el que se pueden pintar corazones
        int left=980;

        //mientras que tengamos vidas, nos pinta corazones rojos, a la que perdamos una vida, deja de pintar un corazon por cada vida perdida
            for(int i=0; i<corazones; i++){
                //le restamos 110 a cada corazon que pinta porque es el hueco entre cada uno de ellos
                canvas.drawBitmap(vidas[0], left-(110*i), 45, null);
            }

            //Dependiendo del numero de vidas que nos quedan, pinta 1, 2 o los 3 corazones vacios
            if(corazones==2){
                canvas.drawBitmap(vidas[1], 760, 45, null);
            }else if(corazones ==1){
                canvas.drawBitmap(vidas[1], 760, 45, null);
                canvas.drawBitmap(vidas[1], 870, 45, null);
            }else if( corazones==0){
                canvas.drawBitmap(vidas[1], 760, 45, null);
                canvas.drawBitmap(vidas[1], 870, 45, null);
                canvas.drawBitmap(vidas[1], 980, 45, null);
            }


        hn.postDelayed(rb, milisegundos);

        //Generaxion aleatoria de las bolas amarillas
        amarilloX-=20;
        if(amarilloX < 0){
            amarilloX = canvas.getWidth() + 20;
            amarilloY = (int) Math.floor(Math.random() * (canvas.getHeight()));
        }

        //pintamos las bolas amarillas
        Paint bolaAmarilla= new Paint();
        bolaAmarilla.setStyle(Paint.Style.FILL);
        bolaAmarilla.setColor(Color.YELLOW);
        canvas.drawCircle(amarilloX, amarilloY, 25, bolaAmarilla);

        //Generaxion aleatoria de las bolas verdes
        verdeX-=15;
        if(verdeX < 0){
            verdeX = canvas.getWidth() + 20;
            verdeY = (int) Math.floor(Math.random() * (canvas.getHeight()));
        }

        //Pintamos las bolas verdes
        Paint bolaVerde= new Paint();
        bolaVerde.setStyle(Paint.Style.FILL);
        bolaVerde.setColor(Color.GREEN);
        canvas.drawCircle(verdeX, verdeY, 25, bolaVerde);

        //Generaxion aleatoria de las bolas rojas
        rojaX-=25;
        if(rojaX < 0){
            rojaX = canvas.getWidth() + 20;
            rojaY = (int) Math.floor(Math.random() * (canvas.getHeight()));
        }

        //Pintamos las bolas verdes
        Paint bolaRoja= new Paint();
        bolaRoja.setStyle(Paint.Style.FILL);
        bolaRoja.setColor(Color.RED);
        canvas.drawCircle(rojaX, rojaY, 25, bolaRoja);

        //Declaramos el alto y largo del pez
        altoPez=pezY+b.getHeight();
        largoPez=pezX+b.getWidth();

        //Detectamos las colisiones del pez con las bolas verdes
        if(pezX<verdeX && verdeX < largoPez && largoPez < verdeY && verdeY < altoPez){
            //desplazamos la bola verde a fuera del movil
            verdeX= -15;
            //Sumamos los puntos por colisionar con la verde
            puntoss += 30;
            //Sonido de la bola buena
            sp.play(bolaBuena,1,1,1,0,0);

        }

        //Detectamos las colisiones del pez con las bolas amrilla
        if(pezX<amarilloX && amarilloX < largoPez && largoPez < amarilloY && amarilloY < altoPez){
            //desplazamos la bola amariila fuera del movil
            amarilloX= -15;
            //sumamos los puntos por colisionar con la bola amarilla
            puntoss += 20;
            //Sonido de la bola buena
            sp.play(bolaBuena,1,1,1,0,0);
        }

        //Detectamos las colisiones del pez con las bolas rojas
        if(pezX<rojaX && rojaX < largoPez && largoPez < rojaY && rojaY < altoPez){
            //desplazamos la bola roja fuera del movil
            rojaX= -15;
            //Si colisionamos con bola roja, restamos una vida
            corazones--;
            //Sonido de colision con bola mala
            sp.play(bolaMala,1,1,1,0,0);
            //Si nos hemos quedado sin vidas nos lleva a la pantalla final en la que se muestra la puntuacion y nos permite empezar un juego nuevo
            if (corazones == 0){
                Intent intentFinal= new Intent(getContext(), FinalJuego.class);
                intentFinal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentFinal.putExtra("Puntos", puntoss);
                getContext().startActivity(intentFinal);
            }
        }


    }

    //En el metodo onTouchEvent hacemos que se mueva el pez por cada click que hacemos
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int accion = event.getAction();
        if(accion == MotionEvent.ACTION_DOWN){
            velocidad = -40;
        }
        return true;
    }


}
