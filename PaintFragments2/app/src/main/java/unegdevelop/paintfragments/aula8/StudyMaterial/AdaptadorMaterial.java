package unegdevelop.paintfragments.aula8.StudyMaterial;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import unegdevelop.paintfragments.R;

/**
 * Created by wuilkysb on 20/07/16.
 */
public class AdaptadorMaterial extends RecyclerView.Adapter<AdaptadorMaterial.AdaptadorViewHolder> {

    private List<Material> materiales = new ArrayList<>();
    private String pregunta;

    public static class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private TextView material;

        public AdaptadorViewHolder(View v) {
            super (v);
            this.material = (TextView) v.findViewById(R.id.respuesta);
        }

    }

    public AdaptadorMaterial(List<Material> items) { this.materiales = items;}

    @Override
    public int getItemCount() { return materiales.size();  }

    @Override
    public AdaptadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.respuesta_card,parent,false);
        return new AdaptadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorViewHolder holder, int position) {
        holder.material.setText(materiales.get(position).getMaterialName());
    }
}
