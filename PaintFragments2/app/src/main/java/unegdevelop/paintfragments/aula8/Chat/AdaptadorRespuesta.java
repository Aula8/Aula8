package unegdevelop.paintfragments.aula8.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;

/**
 * Created by wuilkysb on 16/07/16.
 */
public class AdaptadorRespuesta extends RecyclerView.Adapter<AdaptadorRespuesta.AdaptadorViewHolder> {

    private List<Respuesta> responses = new ArrayList<>();
    private String pregunta;

    public static class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private TextView respuesta;

        public AdaptadorViewHolder(View v) {
            super (v);
            this.respuesta = (TextView) v.findViewById(R.id.respuesta);
        }

    }

    public AdaptadorRespuesta(List<Respuesta> items) { this.responses = items;}

    @Override
    public int getItemCount() { return responses.size();  }

    @Override
    public AdaptadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.respuesta_card,parent,false);
        return new AdaptadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorViewHolder holder, int position) {
        holder.respuesta.setText(responses.get(position).getRespuesta());
    }
}
