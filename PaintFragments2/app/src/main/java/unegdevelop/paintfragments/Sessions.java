package unegdevelop.paintfragments;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuilkysb on 05/07/16.
 */
public class Sessions {
    public String theme;
    public String questions;
    public String status;
    public String filesFolder;
    public String boadr;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilesFolder() {
        return filesFolder;
    }

    public void setFilesFolder(String filesFolder) {
        this.filesFolder = filesFolder;
    }

    public String getBoadr() {
        return boadr;
    }

    public void setBoadr(String boadr) {
        this.boadr = boadr;
    }

    @Override
    public String toString() {
        return theme;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject session = null;
        session.put("theme", theme);
        session.put("questions", questions);
        session.put("status", status);
        session.put("files_folder", filesFolder);
        session.put("boadr", boadr);
        return session;
    }
}
