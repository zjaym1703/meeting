package com.example.user.meet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER on 2018-06-30.
 */

public class RegisterActivity extends AppCompatActivity {

    final static private String URL = "http://appmeet.dothome.co.kr/Register.php";
    String user_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText id_edt = (EditText)findViewById(R.id.register_email);
        final EditText pwd_edt = (EditText) findViewById(R.id.register_pwd);
        final EditText cpwd_edt = (EditText)findViewById(R.id.register_cpwd);
        final EditText name_edt = (EditText)findViewById(R.id.register_name);
      /*  final EditText email_edt = (EditText)findViewById(R.id.em);*/
        final RadioGroup radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        Button register_btn = (Button)findViewById(R.id.register_btn);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.pos_rd1){
                    user_pos="tutor";
                }else if(checkedId==R.id.pos_rd2){
                    user_pos="tutee";
            }
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_pwd = pwd_edt.getText().toString();
                String user_cpwd = cpwd_edt.getText().toString();
                String user_name = name_edt.getText().toString();
                String user_email = id_edt.getText().toString();//나중에 수정

                if(!user_pwd.equals(user_cpwd)){
                    Toast.makeText(getApplicationContext(),"비밀번호 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                    pwd_edt.setText("");
                    cpwd_edt.setText("");
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 등록에 성공했습니다.")
                                            .setPositiveButton("확인",null)
                                            .create()
                                            .show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 등록에 실패했습니다.")
                                            .setNegativeButton("다시 시도",null)
                                            .create()
                                            .show();
                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(user_email, user_pwd, user_name, user_pos, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                
                    //회원등록 파이어베이스
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(user_email,user_pwd)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //회원가입성공시
                                    Toast.makeText(RegisterActivity.this,"회원가입에 성공했습니다.",Toast.LENGTH_SHORT).show();
                                }
                            });
                }

        });
    }
}
