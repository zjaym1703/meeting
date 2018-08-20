package com.example.user.meet;

import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 2018-06-30.
 */

public class WriteRequest extends StringRequest {

    final static private String URL = "http://appmeet.dothome.co.kr/BoardWrite.php";
    private Map<String, String> parameters;

    public WriteRequest(String user_id, String board, String title, String content, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("user_id", user_id);
        parameters.put("board", board);
        parameters.put("board_title", title);
        parameters.put("board_content", content);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
