package unegdevelop.paintfragments;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;




// Nombres de las Clases en Mayusculas la Primera Palabra (igual con las Interfaces)
public class AudioStream
{

    //Nombres de las Constantes TOTALMENTE_EN_MAYUSCULAS
    // y antecedido por "final static", puede ser public o private o protected

    // Las Variables y las constantes deben estar Agrupadas por un lado todas los constantes
    // y por otro lado todas las variables, sin mezclas
    private final static int   MINBUFFERSIZE = 15000;
    private final static int   SAMPLE_RATE = 44100;
    private final static int   STREAMING_TYPE = AudioManager.STREAM_MUSIC;
    private final static int   CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_DEFAULT;
    private final static int   ENCODE_AUDIO_TYPE = AudioFormat.ENCODING_PCM_16BIT;
    private final static int   NIVEL_DE_AMPLITUD_MINIMO = 33;

    //Nombres de las Variables y Objetos instanciados,
    //Comienzan en miniscula y las siguientes palabras, la 1era letra en mayuscula
    //NOTA: Los Nombres de las variables tienen que ser totalmente autodescriptivos
    //      es decir, tienen que hacer referencia total al valor que guardan

    private static int         bufferSize;
    private static boolean     recording = false;
    private static Thread      hiloGrabacion;
    private static AudioRecord grabador;
    private static AudioTrack  reproductor;

    private  static Emitter.Listener audioListener = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            JSONObject data = (JSONObject) args[0];
            try
            {
                addAudio(data);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    };

    private static Runnable runnableGrabar = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                grabar();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
    };

    public static void startRecording()
    {
        hiloGrabacion = new Thread(runnableGrabar);
        recording = true;
        hiloGrabacion.start();
    }

    public static  void stopRecording()
    {
        recording = false;
        hiloGrabacion = null;
        grabador.stop();
        grabador = null;
    }

    public static void startReceiver() throws Throwable
    {
        crearReproductor();
        Servidor.anadirEventoRecibidoAlSocket("get_audio",audioListener);
    }

    public static void stopReceiver()
    {
        reproductor.stop();
        reproductor = null;
        Servidor.eliminarEvento("get_audio");
    }

    public static boolean isRecording()
    {
        return recording;
    }

    private static void crearGrabador() throws Throwable
    {
        calcularBufferSize();

        grabador = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                ENCODE_AUDIO_TYPE,
                bufferSize);

    }

    private static void crearReproductor() throws Throwable
    {
        calcularBufferSize();
        reproductor = new AudioTrack(STREAMING_TYPE,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                ENCODE_AUDIO_TYPE,
                bufferSize,
                AudioTrack.MODE_STREAM
        );

    }


    private static void calcularBufferSize()
    {
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, ENCODE_AUDIO_TYPE);
        if(bufferSize < MINBUFFERSIZE)
            bufferSize = MINBUFFERSIZE;
    }

    private static void grabar() throws Throwable
    {
        crearGrabador();
        grabador.startRecording();
        byte[] audioBuffer = new byte[bufferSize];
        int bufferResult = 0;

        while (recording)
        {

            bufferResult = grabador.read(audioBuffer,0, bufferSize);
            //Se Verifica si hay sonido o no..
            if(isNoisy(bufferResult,audioBuffer))
                enviarStream(audioBuffer);
        }
    }

    private static boolean isNoisy(int bufferResult, byte[] audioBuffer)
    {
        double amplitudLevel = 0;
        double sumAmplitudLevel = 0;
        for (byte b : audioBuffer)
        {
            sumAmplitudLevel += Math.abs(b);
        }
        amplitudLevel = Math.abs(sumAmplitudLevel / bufferResult);
        return (amplitudLevel > NIVEL_DE_AMPLITUD_MINIMO);
    }

    private static void enviarStream(byte[] audioBuffer) throws JSONException
    {
        JSONObject obj = new JSONObject();
        //convertimos el Audio a String
        String audioString = Base64.encodeToString(audioBuffer,Base64.DEFAULT);
        obj.put("audio", audioString);
        Servidor.enviarEvento("send_audio",obj);
    }


    private static void addAudio(JSONObject data) throws JSONException
    {
        //Si el Reproductor no esta Reproduciendo lo inicia ..
        if (reproductor.getState() == AudioTrack.STATE_INITIALIZED)
            reproductor.play();
        escribirAudioEnReproductor(data.getString("audio"));
    }

    private static void escribirAudioEnReproductor(String audio)
    {
        byte[] audioDecodificado = Base64.decode(audio,Base64.DEFAULT);
        reproductor.write(audioDecodificado,0,audioDecodificado.length);
    }
}
