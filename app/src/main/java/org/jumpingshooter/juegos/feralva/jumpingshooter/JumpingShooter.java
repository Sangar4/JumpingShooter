package org.jumpingshooter.juegos.feralva.jumpingshooter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;



public class JumpingShooter extends ActionBarActivity {
    private MediaPlayer mp;
   public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jumping_shooter);
        //faltaria conicion para iniciar

    }
    public void lanzarJuego(View view) {
        Intent i = new Intent(this, Juego.class);
        startActivityForResult(i,1234);


    }
    @Override protected void onActivityResult(int requestCode,int resultCode, Intent data){

        super.onActivityResult(requestCode , resultCode , data );
            if(requestCode==1234 && resultCode==RESULT_OK && data!=null){
                double puntuacion = data.getExtras().getDouble("puntuacion");
                lanzarNombreJugador(null);
                String nombre= "Jugador 1";
                //Mejor leerlo desde un AlertDialog.Builder
                almacen.guardarPuntuacion(puntuacion , nombre);
                lanzarPuntuaciones(null);
            }
    }
    public void lanzarAyuda(View view) {
        Intent i = new Intent(this, Ayuda.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }

    public void lanzarPuntuaciones(View view) {
        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
    }
    public void lanzarNombreJugador(View view) {
        Intent i = new Intent(this, NombreJugador.class);
        startActivity(i);
    }




}
