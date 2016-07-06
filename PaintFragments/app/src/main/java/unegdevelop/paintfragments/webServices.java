package unegdevelop.paintfragments;


import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by wuilkysb on 05/07/16.
 */
public interface webServices {
    @GET("/sessions/{subject}")
    void getSessions(@Path("subject") String subject, Callback<List<Sessions>> response);

}
