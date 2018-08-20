package com.example.user.meet;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText login_id,login_pwd;
    Button login_btn;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id=(EditText)findViewById(R.id.login_email);
        login_pwd=(EditText)findViewById(R.id.login_pwd);

        login_btn=(Button)findViewById(R.id.login_btn);

        register = (TextView)findViewById(R.id.register_txt);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String login_user_id=login_id.getText().toString();
                final String login_user_pwd=login_pwd.getText().toString();

                Response.Listener<String> responseListener =new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success=jsonObject.getBoolean("success");

                            if(success){
                                String user_id=jsonObject.getString("user_email");
                                String user_pos=jsonObject.getString("user_pos");

                                if(user_pos.equals("tutor")){//튜터일때
                                    Intent intent=new Intent(LoginActivity.this,CreateGroupActivity.class);
                                    intent.putExtra("user_id",user_id);
                                    LoginActivity.this.startActivity(intent);
                                }else if(user_pos.equals("tutee")){//튜티일때
                                    Intent intent=new Intent(LoginActivity.this,JoinGroupActivity.class);
                                    intent.putExtra("user_id",user_id);
                                    LoginActivity.this.startActivity(intent);
                                }

                            }else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다.")
                                        .setNegativeButton("다시 시도",null)
                                        .create()
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest=new LoginRequest(login_user_id,login_user_pwd,responseListener);
                RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

}