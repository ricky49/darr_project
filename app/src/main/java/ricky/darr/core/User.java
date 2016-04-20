package ricky.darr.core;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ricky.darr.core.webservice_controller.JsonParser;

public class User {

    String name, user, pass, mail, lastname,cedula;
    private JsonParser jsonParser;
    String users = "http://54.218.36.180:2000/api/users/";
    String authenticate="http://54.218.36.180:2000/authenticate";

    public User(){

    }
    public User(String user, String pass) {
        this.user = user;
        this.pass = pass;

    }

    public JSONObject register(String id,String fingerprint) {

        String url = users+id;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fingerprint", fingerprint));
        JSONObject json = JsonParser.putJSONFromUrl(url, params);
        return json;
    }

    public JSONObject loginUser(String user, String pass,String fingerprint) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("user", user));
        params.add(new BasicNameValuePair("pass", pass));
        params.add(new BasicNameValuePair("fingerprint",fingerprint));
        JSONObject json = JsonParser.postJSONFromUrl(authenticate, params);
        System.out.println(json);
        return json;
    }
}
