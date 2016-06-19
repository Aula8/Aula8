package unegdevelop.paintfragments.aula8.lobby;

import java.util.Random;

public class Sala {
    private String tema;
    private String materia;
    private String owner;
    private int codigo;
    private int seccion;
    private int usuariosMaximos;
    private int usuariosConectados;
    private static final String materias[] = {"MATEMATICAS", "FISICA", "QUIMICA", "PROGRAMACION", "BASE DE DATOS II", "INGENIERIA DEL SOFTWARE I", "INFORMATICA INDUSTRIAL", "ESTRUCTURA DE DATOS"};
    private static final String profesores[] = {"Hector Flores", "José Rodriguez", "Noel Cuba", "Carlos Martínez", "Jorge Escalona", "Jesús Rondon", "Nelson Inojosa", "Andres Lillo"};
    private static final String temas[] = {"Es el servicio de banca por Internet para personas naturales, que le permite realizar sus transacciones de forma segura, confiable y fácil, desde cualquier lugar del mundo.", "Ecuaciones diferenciales de primer orden", "Programación orientada a objetos", "Operaciones con números binarios", "Recursividad", "Teoría de conjuntos", };
    private static final int capacidades[] = {25, 50, 75, 100};
    private static final Random rnd = new Random();


    public Sala() { //Constructor sin parametros que genera los datos de manera aleatoria
        this.codigo = rnd.nextInt(100);
        this.seccion = 1 + rnd.nextInt(5);
        this.tema = temas[rnd.nextInt(temas.length)];
        this.materia = materias[rnd.nextInt(materias.length)];
        this.owner = profesores[rnd.nextInt(profesores.length)];
        this.usuariosMaximos = capacidades[rnd.nextInt(capacidades.length)];
        this.usuariosConectados = rnd.nextInt(this.usuariosMaximos + 1);
    }
    //constructor con parametros
    public Sala(int codigo,int seccion, int usuariosConectados, int usuariosMaximos, String owner, String materia, String tema) {
        this.codigo = codigo;
        this.seccion = seccion;
        this.usuariosConectados = usuariosConectados;
        this.usuariosMaximos = usuariosMaximos;
        this.owner = owner;
        this.materia = materia;
        this.tema = tema;
    }

    //Metodos Set
    public void setSeccion(int seccion)    { this.seccion = seccion; }
    public void setCodigo(int codigo)      {
        this.codigo = codigo;
    }
    public void setTema(String tema)       {this.tema = tema;}
    public void setMateria(String materia) {
        this.materia = materia;
    }
    public void setUsuariosMaximos(int usuariosMaximos) {
        this.usuariosMaximos = usuariosMaximos;
    }
    public void setUsuariosConectados(int usuariosConectados) { this.usuariosConectados = usuariosConectados; }
    public void setOwner(String owner){this.owner = owner;}

    //Metodos Get
    public int getUsuariosMaximos() {
        return usuariosMaximos;
    }
    public String getOwner() {
        return owner;
    }
    public String getMateria() {
        return materia;
    }
    public int getUsuariosConectados() {
        return usuariosConectados;
    }
    public String getTema() {
        return tema;
    }
    public int getCodigo() {return codigo;}
    public int getSeccion() { return seccion; }

}
