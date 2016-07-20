package unegdevelop.paintfragments.aula8.Chat;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xavier on 03/07/16.
 */
public class Usuario
{

    //Si se cree conveniente agregar más info al usuario hacerlo aquí
    private int imagen;
    private String nombre;
    private String correo;
    private String idpregunta;
    private List<Respuesta> respuestas;

    public Usuario(int imagen, String nombre, String correo){
        this.imagen = imagen;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Usuario(int imagen, String nombre, String correo, String idPregunta) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.correo = correo;
        this.idpregunta = idPregunta;
        this.respuestas = new ArrayList();
    }


    public String getIDPregunta() {
        return idpregunta;
    }

    public List getPreguntas() {
        return respuestas;
    }

    public void setPreguntas(Respuesta pregunta) {
        respuestas.add(pregunta);
    }

   /* public void setRespuestasPreguna(Respuesta r){ this.preguntas.setRespuestas(r); }*/

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

}
