package com.example.user.meet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 2018-07-09.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>/* implements Filterable*/{

   /* private Context context;*/
    private List<GroupModel> mDataList;//원래 데이터

    private List<String> item,itemFiltered;
   /* private GroupAdapterListener listener;*/

   /* public GroupAdapter(Context context,List<GroupModel> gl,) {
        super();
    }*/

    public interface MyRecyclerViewClickListener {
        void onItemClicked(int position);//몇번째꺼가 클릭되었는지
    }

    private MyRecyclerViewClickListener mListener;

    public void setOnClickListener(MyRecyclerViewClickListener listener) {
        mListener = listener;
    }

    public GroupAdapter(List<GroupModel> dataList) {
        mDataList = dataList;//데이터를 외부에서 가져옴
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupModel item = mDataList.get(position);
        holder.group_name.setText(item.getGroup_name());
        holder.tutor_id.setText(item.getTutor_id());

        if (mListener != null) {
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView group_name, tutor_id;
        public ViewHolder(View itemView) {
            super(itemView);
            group_name = itemView.findViewById(R.id.group_name);
            tutor_id = itemView.findViewById(R.id.tutor_id);
        }
    }

    //필터

   /* @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query=charSequence.toString();

                if(query.isEmpty()){

                }
                List<GroupModel> filtered=new ArrayList<>();//필터된리스트

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                itemFiltered=(ArrayList<String>)results.values;
                notifyDataSetChanged();
            }
        };


    }*/

    //필터링에 필요한 리스너
    //연락처 선택할때마다 메소드 제공
    /*public interface GroupAdapterListener{
        void onContactSelectd(GroupModel groupModel);
    }*/
}
