package unegdevelop.paintfragments.aula8.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import unegdevelop.paintfragments.R;

/**
 * Created by xavier on 03/07/16.
 */
public class Adaptador extends RecyclerView.Adapter<Adaptador.AdaptadorViewHolder> {

    private List<Usuario> items;

    public static class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagen;
        public TextView nombre;
        public TextView correo;

        public AdaptadorViewHolder(View v) {
            super (v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            correo = (TextView) v.findViewById(R.id.correo);
        }

    }

    public Adaptador(List<Usuario> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.usuario_card,viewGroup,false);
        return new AdaptadorViewHolder(v);
    }

    public void onBindViewHolder(AdaptadorViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.correo.setText("Correo: "+String.valueOf(items.get(i).getCorreo()));
    }
}
