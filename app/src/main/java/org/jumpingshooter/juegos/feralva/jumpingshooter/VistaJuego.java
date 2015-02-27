package org.jumpingshooter.juegos.feralva.jumpingshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class VistaJuego extends View {
    ////// ROCA //////
    private Grafico roca;
    private boolean rocaActivo = false;
    private int tiempoRoca;

    ////// MONIGOTE //////
    private Grafico monigote;
    private boolean monigoteActivo = false;
    private boolean descensoActivo = false;



    // Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;


    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableRoca, drawableMonigote, drawableMisil;
        drawableMonigote = context.getResources().getDrawable(R.drawable.monigoteapp);
        drawableRoca = context.getResources().getDrawable(R.drawable.roca);

        monigote = new Grafico(this, drawableMonigote);
        roca = new Grafico(this, drawableRoca);
//        roca = new Grafico(this, drawableRoca);

    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto.

        monigote.setPosX((monigote.getAncho()) / 2);
        monigote.setPosY(alto - (monigote.getAlto() * 1.2));

        ultimoProceso = System.currentTimeMillis();
        thread.start();

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        monigote.dibujaGrafico(canvas);
        if(rocaActivo) {
    roca.dibujaGrafico(canvas);
        }
    }

    synchronized protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        /*Si el tiempo que ha pasado es menor que el Periodo Proceso no hace nada*/
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return;
        }
        if((Math.random()*10)<0.1 && (rocaActivo == false)){ //Falta la otra condicion de que la roca no este activa
            ActivaRoca();


        }
        // Para una ejecución en tiempo real calculamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
        // Actualizamos posición de misil
        if (rocaActivo) {
            roca.incrementaPos(retardo);
            if (roca.getPosX()<=0) {
                rocaActivo = false;
                //   if (tiempoRoca < 0) {            tiempoRoca -= retardo;

            }

                        }
        if(monigoteActivo) {
            monigote.incrementaPos(retardo);
            if (monigote.getPosY() < (getHeight() - (monigote.getAlto() * 2.2))) {
                monigoteActivo = false;
                ActivaDescenso();

            }
        }
        if(descensoActivo) {
            monigote.incrementaPos(retardo);
            if (monigote.getPosY() > (getHeight() - (monigote.getAlto() * 1.2))) {
                descensoActivo = false;
            }
        } //   if (tiempoRoca < 0) {            tiempoRoca -= retardo;


    }

    class ThreadJuego extends Thread {
        @Override
        public void run() {
            while (true) {
                actualizaFisica();
            }
        }
    }

    private float mX=0, mY=0;
    private boolean disparo=false;

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo=true;
                break;

            case MotionEvent.ACTION_UP:

                if (disparo && (monigoteActivo==false) && (descensoActivo == false)){ // Los False ahi son para que si salta y vuleve a pulsar no se Raye
                    ActivaSalto();
                }
                break;
        }
        mX=x; mY=y;
        return true;
    }





    private void ActivaRoca() {

    roca.setIncX(-0.5*roca.MAX_VELOCIDAD);
         roca.setPosX((getWidth() - roca.getAncho()));
         roca.setPosY(getHeight() - (roca.getAlto() * 1.2));
        rocaActivo = true;
    }

    private void ActivaSalto() {

        monigote.setIncY(-0.5*monigote.MAX_VELOCIDAD);
        monigote.setPosX((monigote.getAncho()) / 2);
        monigote.setPosY(getHeight() - (monigote.getAlto() * 1.2));
        monigoteActivo = true;
    }
    private void ActivaDescenso() {

        monigote.setIncY(0.5*monigote.MAX_VELOCIDAD);
        monigote.setPosX((monigote.getAncho()) / 2);
        monigote.setPosY(getHeight() - (monigote.getAlto() * 2.2));
        descensoActivo = true ;
    }
}