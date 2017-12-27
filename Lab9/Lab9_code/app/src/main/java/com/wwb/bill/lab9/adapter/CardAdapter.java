package com.wwb.bill.lab9.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wwb.bill.lab9.R;
import com.wwb.bill.lab9.model.Github;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15945 on 2017/12/21.
 */

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Github> list;
    private Context context;
    private LayoutInflater inflater;

    public CardAdapter(Context context, List<Github> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item, null);
        return new MyViewHolder(view);
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder holder1 = (MyViewHolder) holder;
        Github itemEntity = list.get(position);
        holder1.loginText.setText(String.format("login：%s",itemEntity.getLogin()));
        holder1.idText.setText(String.format("id：%d",itemEntity.getID()));
        holder1.blogText.setText(String.format("blog：%s",itemEntity.getBlog()));
        if (mOnItemClickListener != null)
        {
            holder1.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder1.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder1.itemView, pos);
                }
            });

            holder1.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder1.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder1.itemView, pos);
                    return false;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if(null == list || list.size() ==0 ){
            return 0;
        }
        else return list.size();
    }

    public void addData(Github github){
        list.add(github);
    }
    public Github getData(int pos){
        return list.get(pos);
    }
    public void removeData(int pos){
        list.remove(pos);
    }
    public void clear(){
        list.clear();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView loginText;
        private TextView idText;
        private TextView blogText;
        public MyViewHolder(View itemView){
            super(itemView);
            loginText = (TextView)itemView.findViewById(R.id.personLogin);
            idText = (TextView)itemView.findViewById(R.id.personID);
            blogText = (TextView)itemView.findViewById(R.id.personBlog);
        }
    }

}
