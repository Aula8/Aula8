package unegdevelop.paintfragments;


import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by wuilkysb on 05/07/16.
 */
public interface webServices {
    @GET("/check_connection")
    void getConnection();

    @GET("/sessions/{subject}")
    void getSessions(@Path("subject") String subject, Callback<List<Sessions>> callback);

    @FormUrlEncoded
    @POST("/sessions/create")
    void CreateSession(@Field("subject") String subject, @Field("session") String session, Callback<JSONObject> success);

}
