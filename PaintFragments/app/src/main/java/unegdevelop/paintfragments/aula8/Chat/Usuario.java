package unegdevelop.paintfragments.aula8.Chat;

/**
 * Created by xavier on 03/07/16.
 */
public class Usuario
{

    //Si se cree conveniente agregar más info al usuario hacerlo aquí
    private int imagen;
    private String nombre;
    private String correo;

    public Usuario(int imagen, String nombre, String correo) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.correo = correo;
    }

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
