package unegdevelop.paintfragments.aula8.StudyMaterial;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import unegdevelop.paintfragments.FilesController;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;
import unegdevelop.paintfragments.Utils;

/**
 * Created by wuilkysb on 21/07/16.
 */
public class DialogFileDownload extends DialogFragment {
        private View dialogView;
        public List<Material> material;
        public String link;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder sub = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);

            dialogView = inflater.inflate(R.layout.layout_downloads, null);

            final ListView materials = (ListView) dialogView.findViewById(R.id.downloads_lv);
            ArrayAdapter stringArray = new ArrayAdapter(getActivity(),
                    R.layout.support_simple_spinner_dropdown_item,
                    material);
            materials.setAdapter(stringArray);

            materials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                    final String file = materials.getItemAtPosition(i).toString();

                    final String dir = link  + "/" + file;

                    Thread t = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                System.out.println("Direccion Descarga " + dir);
                                FilesController.donwloadFile(dir, view.getContext());
                                while (!FilesController.getDownloadState());

                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();

                }
            });
            sub.setView(dialogView);
            return sub.create();
        }

}
