package unegdevelop.paintfragments.aula8.Chat;

public class Mensaje {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_IMAGE = 3;

    private int tipo;
    private String mensaje;
    private String usuario;
    private boolean isLocal;
    private Object file;

    public Mensaje() {}

    public int getTipo ()
    {
        return this.tipo;
    }

    public String getMensaje ()
    {
        return this.mensaje;
    }

    public String getUsuario ()
    {
        return this.usuario;
    }

    public boolean getIsLocal () { return this.isLocal; }

    public Object getFile() { return this.file; }

    public static class Builder
    {
        private final int tipo;
        private String mensaje;
        private String usuario;
        private boolean isLocal;
        private Object file;

        public Builder(int tipo, boolean isLocal) {
            this.tipo = tipo;
            this.isLocal = isLocal;
        }

        public Builder mensaje(String mensaje) {
            this.mensaje = mensaje;
            return this;
        }

        public Builder usuario(String usuario) {
            this.usuario = usuario;
            return this;
        }

        public Builder file(Object file) {
            this.file = file;
            return this;
        }

        public Mensaje build() {
            Mensaje msg = new Mensaje();
            msg.tipo = tipo;
            msg.mensaje = mensaje;
            msg.usuario = usuario;
            msg.isLocal = isLocal;
            msg.file = file;
            return msg;
        }
    }
}
