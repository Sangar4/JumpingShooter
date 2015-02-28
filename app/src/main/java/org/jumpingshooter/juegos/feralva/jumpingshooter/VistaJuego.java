package org.jumpingshooter.juegos.feralva.jumpingshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Vector;

public class VistaJuego extends View {
    ////// ROCA //////
    private Vector<Grafico> Rocas; // Vector con los Rocas del juego
    private Grafico Roca;
    ////// MONIGOTE //////
    private Grafico monigote;
    private boolean salto=false;

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
        Roca= new Grafico(this, drawableRoca);
        Roca.setIncX(-0.8*Roca.MAX_VELOCIDAD);
        Rocas = new Vector<Grafico>();

        Grafico roca = new Grafico(this, drawableRoca);
        roca.setIncX(-0.8*roca.MAX_VELOCIDAD);
        Rocas.add(roca);


    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto.

        monigote.setPosX((monigote.getAncho()) / 2);
        monigote.setPosY(alto - (monigote.getAlto() * 1.2));

        Roca.setPosX((ancho - Roca.getAncho()));
        Roca.setPosY(alto - (Roca.getAlto() * 1.2));

        for (Grafico roca : Rocas) {

            roca.setPosX((ancho - roca.getAncho()));
            roca.setPosY(alto - (roca.getAlto() * 1.2));
        }
        ultimoProceso = System.currentTimeMillis();
        thread.start();

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        monigote.dibujaGrafico(canvas);
        for (Grafico roca : Rocas) {
            roca.dibujaGrafico(canvas);

        }
    }

    synchronized protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        /*Si el tiempo que ha pasado es menor que el Periodo Proceso no hace nada*/
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return;
        }
        /*if(!Rocas.isEmpty() && Math.random()<0.1 && Rocas.capacity()!=Rocas.size()) {
            Rocas.setElementAt(Roca ,Rocas.size()-1);
        }*/
        // Para una ejecución en tiempo real calculamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
        if(salto){
            monigote.incrementaPos(retardo);
            if(monigote.getPosY()<(getHeight() - (monigote.getAlto() * 2.7))) {
                salto= false;
                descenso();
            }
          if(monigote.getPosY() > (getHeight() - (monigote.getAlto() * 1.2))){
              salto=false;
          }

        }
        if (!Rocas.isEmpty()) {
            for (int e=0;e<Rocas.size();e++) {
               Grafico roca= Rocas.get(e);
                roca.incrementaPos(retardo);
                if(roca.getPosX()+roca.getAncho()/2<0) {
                   Rocas.remove(e);
                    Rocas.add(Roca);
                }
            }
        }
    }
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_UP:{
                if(!salto) {
                    saltar();
                    salto = true;
                }
                break;
                }
        }
        return true;

    }

    class ThreadJuego extends Thread {
        @Override
        public void run() {
            while (true) {
                actualizaFisica();
            }
        }
    }
    private void saltar(){
        monigote.setIncY(-40);
        salto=true;
    }
    private void descenso(){
        monigote.setIncY(8);
        monigote.setPosY(getHeight() - (monigote.getAlto() * 2.7)-1);
        salto=true;
    }

}