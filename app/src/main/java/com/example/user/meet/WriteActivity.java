package com.example.user.meet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by USER on 2018-08-18.
 */

public class WriteActivity extends AppCompatActivity {

    Button cancel, write;
    Spinner board_list;
    EditText title_edt, content_edt;
    String board = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        cancel = (Button)findViewById(R.id.cancel_btn);
        write = (Button)findViewById(R.id.write_btn);
        board_list = (Spinner)findViewById(R.id.board_list);
        title_edt = (EditText)findViewById(R.id.title_edt);
        content_edt = (EditText)findViewById(R.id.content_edt);

        board_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                board = (String)adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = title_edt.getText().toString();
                String content = content_edt.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                WriteRequest writeRequest = new WriteRequest(board, title, content, responseListener);
                RequestQueue queue = Volley.newRequestQueue(WriteActivity.this);
                queue.add(writeRequest);
                WriteActivity.this.finish();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteActivity.this.finish();
            }
        });
    }
}
