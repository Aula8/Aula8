package unegdevelop.paintfragments.aula8.Chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wuilkysb on 17/07/16.
 */
public class Preguntas {
    @SerializedName("user_question") String usuario_pregunta;
    @SerializedName("user_responses") String usuario_respuesta;
    @SerializedName("question") String pregunta;
    @SerializedName("responses") List<Respuesta> respuestas;
    @SerializedName("status") String estado;

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

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
