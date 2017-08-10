package com.example.lizhenquan.honestqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lizhenquan.honestqq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhenquan on 2017/8/10.
 */

public class MyImgAdapter extends RecyclerView.Adapter<MyImgAdapter.MyImgViewHolder> {
    private List<String> mDataModels;
    private Context mContext;

    public MyImgAdapter(Context context,List<String> dataModels) {
        if (dataModels == null) {
            throw new IllegalArgumentException("DataModel must not be null");
        }
        mContext = context;
        mDataModels = dataModels;
    }

    @Override
    public MyImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_view, parent, false);
        return new MyImgViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyImgViewHolder holder, int position) {
        String dataModel = mDataModels.get(position);

        Picasso.with(mContext).load(dataModel).into(holder.imgItem);


    }

    @Override
    public int getItemCount() {
        return mDataModels.size();
    }


    public void removeData(int position) {
        mDataModels.remove(position);
        notifyItemRemoved(position);
    }


    public class MyImgViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgItem; // 日期

        public MyImgViewHolder(View itemView) {
            super(itemView);
            imgItem = (ImageView) itemView.findViewById(R.id.img_item);
        }

    }

}
