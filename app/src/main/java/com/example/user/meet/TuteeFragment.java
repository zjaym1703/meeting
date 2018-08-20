package com.example.user.meet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 정연미 on 2018-08-12.
 */

public class TuteeFragment extends Fragment {

    List<UserModel> user=new ArrayList<>();

    TuteeRecyclerViewAdapter adapter;

    public TuteeFragment() {
        //super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Bundle bundle=getArguments();
        group_name=bundle.getString("group");
        Log.d("group_name",group_name);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tutee,container,false);

        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.fragment_tutee_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        adapter=new TuteeRecyclerViewAdapter(user);
        recyclerView.setAdapter(adapter);

        Request_list();
        return view;
    }

    //프래그먼트 adapter 실현한부분
    class TuteeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        //그룹이름 받아와서 group_user_id 테이블에서 찾기
        /*public TuteeRecyclerViewAdapter() { //기본생성자 --
            Request_list(this,group_name);
        }*/

        public TuteeRecyclerViewAdapter(List<UserModel> user_m){
            user=user_m;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //레이아웃 화면을 어댑터에서 사용가능하도록 뷰를 최초 생성하는 부분
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutee_list,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).nameTxt.setText(user.get(position).user_name);
            ((CustomViewHolder)holder).idTxt.setText(user.get(position).user_email);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //안해
                }
            });

        }

        @Override
        public int getItemCount() {//화면에 띄워줄 뷰를
            return user.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{

            //public ImageView imageView;
            public TextView nameTxt;
            public TextView idTxt;

            public CustomViewHolder(View view) {
                super(view);
                //imageView=(ImageView)view.findViewById(R.id.imageView);
                nameTxt=(TextView)view.findViewById(R.id.name_txt);
                idTxt=(TextView)view.findViewById(R.id.email_txt);
            }
        }
    }
    private void Request_list() {

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try { //여기에 응답리스너에 썻던걸 쓰면 됨
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    Log.d("응답", "66660");

                    int count = 0;
                    String email, name;

                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);//0번부터 받아옴
                        email = object.getString("user_email");
                        name = object.getString("user_name");

                        Log.d("dfd", "dfd");
                        //리사이클러뷰로 어댑터 써서 연결
                        user.add(new UserModel(name, email));
                        count++;//다음꺼 가져오려고
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        TuteeListRequest tuteeListRequest=new TuteeListRequest(GroupActivity.group_n,stringListener);
        RequestQueue queue=Volley.newRequestQueue(getActivity());
        /*RequestQueue queue=Volley.newRequestQueue(getContext());*/
        queue.add(tuteeListRequest);
    }

    class TuteeListRequest extends StringRequest{

        final static private String URL="http://appmeet.dothome.co.kr/TuteeListFragment.php";
        private Map<String,String> paramters;

        public TuteeListRequest(String group_name_s, Response.Listener<String> listener) {
            super(Method.POST,URL, listener, null);
            paramters=new HashMap<>();
            paramters.put("group_name",group_name_s);
        }
        @Override
        protected Map<String, String> getParams() {
            return paramters;
        }

        /*@Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            String str=null;
            try {
                str=new String(response.data,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
        }*/
    }
}
