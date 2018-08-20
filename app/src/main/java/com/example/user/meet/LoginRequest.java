package com.example.user.meet;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String url="http://appmeet.dothome.co.kr/Login.php";
    private Map<String,String> parameters;

    public LoginRequest(String user_id, String user_pwd, Response.Listener<String> listener) {
        super(Method.POST, url, listener,null);
        parameters=new HashMap<>();
        parameters.put("user_email",user_id);
        parameters.put("user_pwd",user_pwd);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}