package com.example.user.meet;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 2018-06-30.
 */

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://appmeet.dothome.co.kr/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String user_email, String user_pwd, String user_name, String user_pos, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("user_email",user_email);
        parameters.put("user_pwd",user_pwd);
        parameters.put("user_name",user_name);
        parameters.put("user_pos", user_pos);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
