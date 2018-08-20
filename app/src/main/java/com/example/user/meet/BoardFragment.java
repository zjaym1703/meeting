package com.example.user.meet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.meet.GroupActivity.group_n;

public class BoardFragment extends Fragment{

    List<BoardModel> board=new ArrayList<>();
    BoardRecyclerViewAdapter adapter;
    final static String url="http://appmeet.dothome.co.kr/BoardListFragment.php";

    public BoardFragment() {
        // Required empty public constructor
    }

   /* public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_board,container,false);

        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.fragment_board_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        //버튼
        Button write_btn=(Button)view.findViewById(R.id.write_btn);
        Button qa_btn=(Button)view.findViewById(R.id.qa_btn);
        adapter=new BoardRecyclerViewAdapter(board);
        recyclerView.setAdapter(adapter);

        Board_list();//디비에서 받아오는것

        //글쓰기
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //글쓰기로 인텐트 넘어감
            }
        });

        //질문모아보기
        qa_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }


    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        public BoardRecyclerViewAdapter(List<BoardModel> board_m){
            board=board_m;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //레이아웃 화면을 어댑터에서 사용가능하도록 뷰를 최초 생성하는 부분
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_list,parent,false);
            return new BoardFragment.BoardRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).boardName.setText(board.get(position).board_name);
            ((CustomViewHolder)holder).boardId.setText(board.get(position).user_id);
            ((CustomViewHolder)holder).boardDate.setText(board.get(position).board_date);
        }

        @Override
        public int getItemCount() {//화면에 띄워줄 뷰를
            return board.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{

            public TextView boardName;
            public TextView boardId;
            public TextView boardDate;

            public CustomViewHolder(View view) {
                super(view);
                boardName=(TextView)view.findViewById(R.id.name);
                boardId=(TextView)view.findViewById(R.id.id);
                boardDate=(TextView)view.findViewById(R.id.date);
            }
        }
    }

   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

   /*
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }*/

   private void Board_list(){
       StringRequest BoardRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {
                   JSONObject jsonObject=new JSONObject(response);
                   JSONArray jsonArray=jsonObject.getJSONArray("response");

                   int count=0;
                   String name,id,date;

                   while (count < jsonArray.length()) {
                       JSONObject object = jsonArray.getJSONObject(count);//0번부터 받아옴
                       id = object.getString("user_id");
                       name= object.getString("fboard_title");
                       date=object.getString("fboard_date");

                       //리사이클러뷰로 어댑터 써서 연결
                      board.add(new BoardModel(name,id,date));
                      count++;//다음꺼 가져오려고
                   }
                   adapter.notifyDataSetChanged();
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> param=new HashMap<>();
               param.put("group_name",group_n);
               return param;
           }
       };
       RequestQueue queue=Volley.newRequestQueue(getActivity());
       queue.add(BoardRequest);

   }
}
