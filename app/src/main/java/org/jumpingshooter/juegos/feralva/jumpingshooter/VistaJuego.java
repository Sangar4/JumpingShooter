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
    ////// MONIGOTE //////
    private Grafico monigote;
    // variable booleana que permite saber si el monigote está saltando
    private boolean salto=false;
    // Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;
    //Vector de flags que indican si las rocas del vector están activas
    private boolean[] rocasactivas = new boolean[8];
    //Distancia que define el espacio mínimo que debe de haber entre rocas
    private double distancia_minima;


    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableRoca, drawableMonigote, drawableMisil;
        drawableMonigote = context.getResources().getDrawable(R.drawable.monigoteapp);
        drawableRoca = context.getResources().getDrawable(R.drawable.roca);
        monigote = new Grafico(this, drawableMonigote);
        Rocas = new Vector<Grafico>(8);
        //Añadimos 8 objetos roca al vector
        for (int i=0 ; i<8 ; i++) {
            Grafico  roca = new Grafico(this,drawableRoca );
            roca.setIncX(-0.8 * roca.MAX_VELOCIDAD);
            Rocas.add(roca);
        }

    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto.
        monigote.setPosX((monigote.getAncho()) / 2);
        monigote.setPosY(alto - (monigote.getAlto() * 1.2));

        //Establecemos el ancho y alto de todas las rocas del vector
        for (Grafico roca : Rocas) {

            roca.setPosX(ancho - roca.getAncho());
            roca.setPosY(alto - (roca.getAlto() * 1.2));
        }
        ultimoProceso = System.currentTimeMillis();
        thread.start();

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        monigote.dibujaGrafico(canvas);
        //Variable que sirve como indice del vector de rocas para saber que roca estamos en
        // el for y asociarla con si flag del vector de rocasactivas
        int count=0;
        //Dibujamos las rocas que tengan su flag de activo.
        for (Grafico roca : Rocas) {

            if(rocasactivas[count]) {
                roca.dibujaGrafico(canvas);
           }
            count++;
        }
    }

    synchronized protected void actualizaFisica() {
        //Variable que sirve como indice del vector de rocas para saber que roca estamos en
        // el for y asociarla con si flag del vector de rocasactivas
        int count=0;
        //Variable que nos dice si se cumple las condiciones necesarias para dibujar una nueva roca
        boolean flag=true;
        long ahora = System.currentTimeMillis();
        /*Si el tiempo que ha pasado es menor que el Periodo Proceso no hace nada*/
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return;
        }
        // Para una ejecución en tiempo real calculamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
        //Si la variable salto está activa significa que el monigote ha de moverse ya sea hacia arriba
        // o hacia abajo dependiendo de la posición donde se encuentre
        if(salto){
            monigote.incrementaPos(retardo);
            if(monigote.getPosY()<(getHeight() - (monigote.getAlto() * 2.7))) {
                salto= false;
                //Cuando llegamos a la posición máxima llamamos al metodo descenso
                descenso();
            }
          if(monigote.getPosY() > (getHeight() - (monigote.getAlto() * 1.2))){
              // Al llegar a la posición inicial después de saltar decimos que el salto es false
              salto=false;
          }

        }
        //En este for haremos el movimiento de cada roca
        for (Grafico roca : Rocas) {
            //La distancia mínima entre rocas debe de ser 5.5 veces el ancho de la roca para que el
            //monigote pueda subir y bajar.
            distancia_minima = getWidth() - 5.5 * roca.getAncho();
            //Si la roca llega al final se desactiva.
            if(roca.getPosX()+roca.getAncho()/2<0) {
                DesactivaRoca(count);

            }
            //Si la roca no ha sido desactivada debe actualizarse su posicion
            if(rocasactivas[count]){
                roca.incrementaPos(retardo);
            }
            //Suponemos en principio que la roca se puede dibujar y hemos de comprobar si se cumplen
            //todas las condiciones
            flag=true;
          /*En primer lugar para dibujar una roca esta ha de estar desactivada, y hacemos que esto
          lo calcule cada tiempo aleatorio
          */
            if(!rocasactivas[count] && Math.random()<0.017) {
                //Si entramos aquí suponemos que tya se puede dibujar la roca y la activamos
                rocasactivas[count] = true;
                /*Pero hemos de comprobar que el resto de rocas activas no estén cerca de ahi dos
                bucles for para excluir la roca que acabamos de activar para dibujar
                */
                for (int e = 0; e<count ; e++) {
                    double posx = (int) Rocas.get(e).getPosX();
                    /*Si la posicion de las otras rocas es menor a la distancia minima no podremos
                    dibujar la roca
                    */
                    if ( (posx > distancia_minima) && rocasactivas[e]) {
                        flag = false;
                        rocasactivas[count] = false;
                    }
                }
                //idem for anterior
                for (int e = count+1; e<Rocas.capacity() ; e++) {
                    double posx = (int) Rocas.get(e).getPosX();

                    if ( (posx > distancia_minima) && rocasactivas[e]) {
                        flag = false;
                        rocasactivas[count] = false;
                    }
                }
                /*Al final del proceso si flag está a true esq se cumplirarn todas las condiciones
                 * para poder dibujar la roca
                */
                if(flag){
                    rocasactivas[count] = true;
                }

            }
            count++;

        }
    }
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            /*Al pulsar la pantalla activamos el proceso salto*/
            case MotionEvent.ACTION_UP:{
                if(!salto ) {
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
    // metodo saltar activa la condición de salto y la velocidad del monigote para ir hacia arriba
    private void saltar(){
        monigote.setIncY(-40);
        salto=true;
    }
    /* el metodo descenso es activado una vez el monigote ha llegado a la maxima altura en su salto
    * entonces la velocidad del monigote se pone para ir hacia abajo y se situa un punto mas a bajo
    * de la posición máxima
    */
    private void descenso(){
        monigote.setIncY(15);
        monigote.setPosY(getHeight() - (monigote.getAlto() * 2.7)-1);
        salto=true;
    }
    /*El metodo desactiva roca es llamado cuando una roca sale de pantalla y lo que hace es desactivar
    * el flag correspondiente a esa roca y situar la roca desactivada en la posición inical para que
    * esté preparada para cuando sea activada
    */
    private void DesactivaRoca(int i){
        rocasactivas[i]=false;
        Rocas.get(i).setPosX(getWidth() - Rocas.get(i).getAncho());
        Rocas.get(i).setPosY(getHeight() - Rocas.get(i).getAlto()*1.2);
    }
}