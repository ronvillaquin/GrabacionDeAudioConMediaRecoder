package com.example.grabaciondeaudioconmediarecoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private MediaRecorder grabacion;
    // una variable string para asignar un nombre a la pista que se va a crear
    //siempre se tiene que iniciar en null
    private String archivoSalida = null;
    private Button btn_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_record = (Button)findViewById(R.id.btn_recorder);

        //asi se establecen los permisos para usar los sensores y alojar audio o video en el dispositivo
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
    }

    public void recorder(View vista){
        // le preguntamos si la grabacion se esta ejecutando o no
        if (grabacion == null){
            //ahora le decimos donde queremos guardar el archivo y el nombre cn al extencion del mismo
            archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Grabacion.mp3";
            // ahora cremamos el objeto recoder para gramar y usar los sensores
            grabacion = new MediaRecorder();
            // en esta linea le decimos que queremos usar el sensor o microfono
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            //Ahora le diremos el formato de salida que tendra el audio
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //
            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            //ahora le diremos que se va  aguardar en la variable archivo salida
            grabacion.setOutputFile(archivoSalida);

            try {
                //aqui s eprepara el objeto apra comenzar a grabar
                grabacion.prepare();
                // aqui s eempiea a grabar
                grabacion.start();
            }catch (IOException e){

            }
            //aqui le decimos que cambie la imagen o boton cuando comience a grabar
            btn_record.setBackgroundResource(R.drawable.rec);
            Toast.makeText(this, "Grabando", Toast.LENGTH_SHORT).show();
        }else if (grabacion != null){
            //aqui le decimos que si la grabacion es diferente de null se detenga osea si esta grabano se detenga
            grabacion.stop();
            //una ves finalizada se coloca en estado finalizado con release
            grabacion.release();
            //ahora le decimos que grabacion va a contener d enuevo un valor de tipo null para reiniciar el objeto
            grabacion = null;
            // se cambi la imagen del boton d enuevo
            btn_record.setBackgroundResource(R.drawable.stop_rec);
            Toast.makeText(this, "Grabacion Finalizada", Toast.LENGTH_SHORT).show();

        }
    }

    public void reproducir(View vista){
        // cremos un objeto d ela clase media player para poder reporducir audio
        MediaPlayer mp = new MediaPlayer();

        try {
            //mediante la clase data souce reproducimos el archivo en la rita archivo salida que guardamos arriba
            mp.setDataSource(archivoSalida);
            //aqui para que prepare el audio que vamos a reproducir
            mp.prepare();
        }catch (IOException e){
        }
        //para que comience la reproduccion
        mp.start();
        Toast.makeText(this, "Reproduciendo", Toast.LENGTH_SHORT).show();
    }

    //luego que termine mi logica vamos al archivo manifest para darle los permisos
    // necesarios a la app de usar los sensores del celular
    // se agregan dos permisos para microfono o grabar y para guardar
    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    //    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
}
