package unegdevelop.paintfragments.aula8.Chat;

/**
 * Created by wuilkysb on 16/07/16.
 */
public class Respuesta {
    public String respuesta;

    public Respuesta(String r){this.respuesta = r;}

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public String toString() {
        return respuesta;
    }
}
