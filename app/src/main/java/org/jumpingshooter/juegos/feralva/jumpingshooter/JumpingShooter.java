package org.jumpingshooter.juegos.feralva.jumpingshooter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class JumpingShooter extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jumping_shooter);
    }
    public void lanzarJuego(View view) {
        Intent i = new Intent(this, Juego.class);
        startActivity(i);
    }
    public void lanzarAyuda(View view) {
        Intent i = new Intent(this, Ayuda.class);
        startActivity(i);
    }
}
