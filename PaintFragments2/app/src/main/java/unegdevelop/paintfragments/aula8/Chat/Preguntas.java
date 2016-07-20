package unegdevelop.paintfragments.aula8.Chat;


import java.util.List;

/**
 * Created by wuilkysb on 17/07/16.
 */
public class Preguntas {
    String usuario_pregunta;
    String usuario_respuesta;
    String pregunta;
    //List<Respuesta> respuestas = (List<Respuesta>) new Respuesta();
    String estado;
    String id;

    public Preguntas() {
    }

    public String getId() {return id;}

    public void setId(String id) { this.id = id;  }

    public Preguntas(String usuario_pregunta, String pregunta, String id) {
        this.usuario_pregunta = usuario_pregunta;
        this.pregunta = pregunta;
        this.id = id;
    }

    public String getUsuario_pregunta() {
        return usuario_pregunta;
    }

    public void setUsuario_pregunta(String usuario_pregunta) {
        this.usuario_pregunta = usuario_pregunta;
    }

    public String getUsuario_respuesta() {
        return usuario_respuesta;
    }

    public void setUsuario_respuesta(String usuario_respuesta) {
        this.usuario_respuesta = usuario_respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    /*public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Respuesta respuestas) {
        this.respuestas.add(respuestas);
    }*/

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
