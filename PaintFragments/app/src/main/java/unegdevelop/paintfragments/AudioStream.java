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


public class AudioStream
{
    //Min BUFFERSIZE
    private final static int   MINBUFFERSIZE = 15000;
    //CONSTANTES DEL STREAMING
    private final static int   SAMPLE_RATE = 44100; //Frecuencia en hertz de la calidad del audio
    private final static int   streamingType = AudioManager.STREAM_MUSIC; // Tipo de Streaming
    private final static int   channelConfig = AudioFormat.CHANNEL_IN_DEFAULT;
    private final static int   encodeAudioType = AudioFormat.ENCODING_PCM_16BIT; // Tipo de Formato De Codificacion de audio
    private final static int   NIVEL_DE_AMPLITUD_MINIMO = 33;

    private static int         buffersize;
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

    public static void startRecording()
    {
        hiloGrabacion = new Thread(new Runnable() 
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
        });
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
                                      channelConfig,
                                      encodeAudioType,
                                      buffersize);

    }

    private static void crearReproductor() throws Throwable
    {
        calcularBufferSize();
        reproductor = new AudioTrack(streamingType,
                                     SAMPLE_RATE,
                                     channelConfig,
                                     encodeAudioType,
                                     buffersize,
                                     AudioTrack.MODE_STREAM
                                    );

    }


    private static void calcularBufferSize()
    {
        buffersize = AudioRecord.getMinBufferSize(SAMPLE_RATE, channelConfig, encodeAudioType);
        if(buffersize < MINBUFFERSIZE)
            buffersize = MINBUFFERSIZE;
    }

    private static void grabar() throws Throwable
    {
        crearGrabador();
        grabador.startRecording();
        byte[] audioBuffer = new byte[buffersize];
        int bufferResult = 0;

        while (recording)
        {
            bufferResult = grabador.read(audioBuffer,0, buffersize);
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
