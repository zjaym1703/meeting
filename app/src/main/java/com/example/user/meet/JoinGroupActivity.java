package com.example.user.meet;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 정연미 on 2018-08-10.
 */

//튜티용 화면
public class JoinGroupActivity extends AppCompatActivity implements GroupAdapter.MyRecyclerViewClickListener{

    //메뉴아이콘 추가하게 검색은 새창으로 이동해서 할것
   // private MenuItem searchItem;

    //내가 가입된 목록 띄우기
    RecyclerView.LayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
    List<GroupModel> dataList=new ArrayList<>();
    GroupAdapter adapter;
    RecyclerView recyclerView;

    //사용자 id받을 변수
    String id;
    List<String> title=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        Intent it=getIntent();
        id=it.getStringExtra("user_id");

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new GroupAdapter(dataList);
        recyclerView.setAdapter(adapter);

        GroupList();

        adapter.setOnClickListener(JoinGroupActivity.this);
    }

    public void GroupList(){
        Response.Listener<String> stringListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("response");

                    int count=0;
                    String name,id;

                    while(count<jsonArray.length()){
                        JSONObject object=jsonArray.getJSONObject(count);//0번부터 받아옴
                        name=object.getString("group_name");
                        id=object.getString("group_tutor_id");

                        title.add(name);//이름만 저장*/

                        //리사이클러뷰로 어댑터 써서 연결
                        dataList.add(new GroupModel(name,id));
                        count++;//다음꺼 가져오려고
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        GroupTuteeListRequest TuteeRequest=new GroupTuteeListRequest(id,stringListener);
        RequestQueue queue= Volley.newRequestQueue(JoinGroupActivity.this);
        queue.add(TuteeRequest);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_search){
            Intent it=new Intent(JoinGroupActivity.this,SearchGroupActivity.class);
            startActivity(it);
        }
        //나중에 사용자 계정 or 메뉴 추가했을때 바꾸기
        return true;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent=new Intent(JoinGroupActivity.this,GroupActivity.class);
        Log.d("그룹이름",title.get(position));
        intent.putExtra("그룹이름",title.get(position));
        intent.putExtra("아이디",id);
        this.startActivity(intent);
    }

    //tutee로 요청 --> 나중에 하나 클래스로 받아서 if문 처리로 변경
    class GroupTuteeListRequest extends StringRequest {

        final static private String URL="http://appmeet.dothome.co.kr/GroupListRequest.php";
        private Map<String,String> parameter;

        public GroupTuteeListRequest(String id, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            parameter=new HashMap<>();
            parameter.put("user_id",id);
        }

        protected Map<String, String> getParams(){
            return parameter;
        }
    }
}
