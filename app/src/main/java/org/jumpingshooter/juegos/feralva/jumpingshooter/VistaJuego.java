package org.jumpingshooter.juegos.feralva.jumpingshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Vector;

public class VistaJuego extends View {
    // //// ASTEROIDES //////
    private Vector<Grafico> Rocas; // Vector con los Rocas
    private Grafico monigote;
    private int numRocas = 10; //Numero inicial de rocas

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableRoca, drawableMonigote, drawableMisil;
        drawableMonigote = context.getResources().getDrawable(R.drawable.monigoteapp);
        drawableRoca = context.getResources().getDrawable(R.drawable.roca);

        monigote = new Grafico(this, drawableMonigote);

        Rocas = new Vector<Grafico>();

        Grafico roca = new Grafico(this, drawableRoca);
        Rocas.add(roca);


    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto.

        monigote.setPosX((monigote.getAncho()) / 2);
        monigote.setPosY(alto - (monigote.getAlto() * 1.2));

        for (Grafico roca : Rocas) {

            roca.setPosX((ancho - roca.getAncho()));
            roca.setPosY(alto - (roca.getAlto() * 1.2));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        monigote.dibujaGrafico(canvas);
        for (Grafico roca : Rocas) {
            roca.dibujaGrafico(canvas);

        }
    }
}