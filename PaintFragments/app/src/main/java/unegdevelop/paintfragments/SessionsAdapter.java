package unegdevelop.paintfragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wuilkysb on 07/07/16.
 */
public class SessionsAdapter extends ArrayAdapter<Sessions> {
    Context myContext;
    int myLayoutResourceID;
    List<Sessions> sessions = null;

    public SessionsAdapter(Context context, int layoutResourceID, List<Sessions> data){
        super(context, layoutResourceID, data);
        this.myContext = context;
        this.myLayoutResourceID = layoutResourceID;
        this.sessions = data;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        SessionsHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            row = inflater.inflate(myLayoutResourceID, parent, false);
            holder = new SessionsHolder();

            holder.theme = (TextView) row.findViewById(R.id.session_tv);
            row.setTag(holder);


        }else{
            holder = (SessionsHolder) row.getTag();
        }

        Sessions s = sessions.get(position);
        holder.theme.setText(s.getTheme());
        if(s.getStatus().equals("active")) {
            holder.theme.setTextColor(Color.GREEN);
        }else{
            holder.theme.setTextColor(Color.RED);
        }

        return row;
    }

    static class SessionsHolder{
        TextView theme;
    }
}
