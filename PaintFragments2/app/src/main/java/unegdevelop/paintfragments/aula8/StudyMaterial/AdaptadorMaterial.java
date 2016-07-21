package unegdevelop.paintfragments.aula8.StudyMaterial;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import unegdevelop.paintfragments.FilesController;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Utils;

/**
 * Created by wuilkysb on 20/07/16.
 */
public class AdaptadorMaterial extends RecyclerView.Adapter<AdaptadorMaterial.AdaptadorViewHolder> {

    private List<String> materiales = new ArrayList<>();

    public static class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private TextView material;

        public AdaptadorViewHolder(View v) {
            super (v);
            material = (TextView) v.findViewById(R.id.respuesta);
        }

    }

    public AdaptadorMaterial(List<String> items) {
        this.materiales = items;
    }

    @Override
    public int getItemCount() { return materiales.size();  }

    @Override
    public AdaptadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.respuesta_card,parent,false);
        return new AdaptadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AdaptadorViewHolder holder, final int position) {
        holder.material.setText(materiales.get(position));
    }
}
