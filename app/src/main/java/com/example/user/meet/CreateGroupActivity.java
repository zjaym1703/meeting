package com.example.user.meet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 2018-07-05.
 */

public class CreateGroupActivity extends AppCompatActivity implements GroupAdapter.MyRecyclerViewClickListener{

    FloatingActionButton add;
    RecyclerView.LayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
    List<GroupModel> my_group=new ArrayList<>();

    String group_tutor_id,group_name;
    GroupAdapter adapter;

    //이름담아두려는 변수
    List<String> title=new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Log.d("튜터일때","create");

        Intent it=getIntent();//intent는 oncreate 안에 써야 오류 안생김
        group_tutor_id=it.getStringExtra("user_id");//현재사용자의 아이디 받아오기

        add=findViewById(R.id.floatingActionButton2);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new GroupAdapter(my_group);
        recyclerView.setAdapter(adapter);

        GroupList();

        add.setOnClickListener(new View.OnClickListener() {//그룹생성버튼
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(CreateGroupActivity.this);

                alertDialogBuilder.setTitle("그룹생성");
                final EditText group_name_edt=new EditText(CreateGroupActivity.this);
                alertDialogBuilder.setView(group_name_edt);

                Log.d("2","2");

                alertDialogBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                   group_name=group_name_edt.getText().toString();
                    if(group_name_edt==null){
                       Toast.makeText(getApplicationContext(),"그룹이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                    }
                   Response.Listener<String> responseListener=new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           try{
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                              //성공했을때 현재 목록을 업데이트
                                my_group.clear();
                                GroupList();
                            }
                           }catch (JSONException e){
                                    e.printStackTrace();
                          }
                       }
                   };
                        GroupInsertRequest insertRequest = new GroupInsertRequest(group_name, group_tutor_id, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(CreateGroupActivity.this);
                        queue.add(insertRequest);

                        /*adapter.notifyDataSetChanged();//다시 로딩하려고 만듦*/
                    }
                });

                alertDialogBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialogBuilder.show();
            }
        });

        adapter.setOnClickListener(CreateGroupActivity.this);


    }

    //서버로부터 리스트 받아오기
    public void GroupList(){
        Response.Listener<String> responseList=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);//항목들을 불러옴
                    JSONArray jsonArray=jsonObject.getJSONArray("response");//배열형태로 만들어줌

                    int count=0;
                    String name,id;

                    while(count<jsonArray.length()){
                       JSONObject object=jsonArray.getJSONObject(count);//0번부터 받아옴
                        name=object.getString("group_name");
                        id=object.getString("group_tutor_id");

                        title.add(name);

                        //리사이클러뷰로 어댑터 써서 연결
                        my_group.add(new GroupModel(name,id));
                        count++;//다음꺼 가져오려고
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        GroupTutorListRequest TutorRequest = new GroupTutorListRequest(group_tutor_id,responseList);
        RequestQueue queue = Volley.newRequestQueue(CreateGroupActivity.this);
        queue.add(TutorRequest);

    }

    @Override
    public void onItemClicked(int position) {
        Intent intent=new Intent(CreateGroupActivity.this,GroupActivity.class);
        Log.d("그룹이름",title.get(position));
        intent.putExtra("그룹이름",title.get(position));
        intent.putExtra("아이디",group_tutor_id);//
        this.startActivity(intent);
    }


    class GroupInsertRequest extends StringRequest {
        final static private String URL = "http://appmeet.dothome.co.kr/GroupInsertRequest.php";
        private Map<String, String> parameters;

        public GroupInsertRequest(String group_name, String group_tutor_id, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            Log.d("오류가 php파일 안?", "ㄹㄹㄹ");
            parameters = new HashMap<>();
            parameters.put("group_name", group_name);
            parameters.put("group_tutor_id", group_tutor_id);
        }

        @Override
        protected Map<String, String> getParams() {
            return parameters;
        }
    }

    static class GroupTutorListRequest extends StringRequest {

        final static private String URL="http://appmeet.dothome.co.kr/GroupListRequest.php";
        private Map<String,String> parameter;

        public GroupTutorListRequest(String tutor_id, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            parameter=new HashMap<>();
            parameter.put("group_tutor_id",tutor_id);
        }

        protected Map<String, String> getParams(){
            return parameter;
        }
    }
}
