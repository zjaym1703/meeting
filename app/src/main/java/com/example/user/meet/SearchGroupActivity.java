package com.example.user.meet;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
 * Created by 정연미 on 2018-08-12.
 */

public class SearchGroupActivity extends AppCompatActivity implements GroupAdapter.MyRecyclerViewClickListener{

    //내가 가입된 목록 띄우기
    RecyclerView.LayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
    List<GroupModel> dataList=new ArrayList<>();
    GroupAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;

    //이름을 받을 title변수 설정
    List<String> title=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);
        setTitle("검색");

        //검색
        searchView=(SearchView)findViewById(R.id.searchview);

        //리스트
        adapter=new GroupAdapter(dataList);
        recyclerView = (RecyclerView)findViewById(R.id.rcv);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        searchView.setActivated(true);
        searchView.setQueryHint("튜터의 아이디 또는 그룹명을 입력하세요");
        searchView.clearFocus();

        SearchManager searchManager= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dataList.clear();
                Response.Listener<String> stringListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");

                            int count = 0;
                            String name, id;

                            while (count < jsonArray.length()) {
                                JSONObject object = jsonArray.getJSONObject(count);//0번부터 받아옴
                                name = object.getString("group_name");
                                id = object.getString("group_tutor_id");

                                Log.d("그룹이름", String.valueOf(title.add(name)));
                                title.add(name);


                                //리사이클러뷰로 어댑터 써서 연결
                                dataList.add(new GroupModel(name, id));
                                count++;//다음꺼 가져오려고
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                CreateGroupActivity.GroupTutorListRequest TutorRequest=new CreateGroupActivity.GroupTutorListRequest(query,stringListener);
                RequestQueue queue= Volley.newRequestQueue(SearchGroupActivity.this);
                queue.add(TutorRequest);
                /*adapter.getFilter().filter(query);*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                /*adapter.getFilter().filter(query);*/
                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onItemClicked(int position) {//각각 그룹을 저장했을때
        //만약 유저아이디가 그룹디비에 없을 경우
        //그룹ㅇ


        Intent it=new Intent(SearchGroupActivity.this,GroupActivity.class);

        Log.d("그룹이름", title.get(position));
        it.putExtra("그룹이름",title.get(position));
        this.startActivity(it);
    }

    private void SearchInsertList(final String group_name){//insert
        final String url="http://appmeet.dothome.co.kr/Search.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        },null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("",group_name);//그룹이름
                return params;
            }
        };
    }

}
