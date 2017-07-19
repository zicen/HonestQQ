package com.example.lizhenquan.honestqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.model.ContactBean;
import com.example.lizhenquan.honestqq.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder> implements View.OnClickListener {
    private  List<ContactBean> mContactList;
    private List<String> NameList = new ArrayList<>();

    private Context mContext;
    private    List<AVUser> mData;
    private  String TAG = "AddFriendAdapter";


    public AddFriendAdapter(Context context, List<AVUser> list, List<ContactBean> contactsList) {
        this.mContext = context;
        this.mData = list;
        this.mContactList = contactsList;
    }

    @Override
    public AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.add_friend_item, parent,false);
        AddFriendViewHolder holder = new AddFriendViewHolder(view);
        view.setOnClickListener(this);
        for (int i = 0; i < mContactList.size(); i++) {
            NameList.add(mContactList.get(i).username);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(AddFriendViewHolder holder, int position) {
        AVUser avUser = mData.get(position);
        final String username = avUser.getUsername();
        Date createdAt = avUser.getCreatedAt();
        holder.mTv_time.setText(StringUtils.getDateString(createdAt));
        holder.mTvUsername.setText(username);

        if (NameList.contains(username)) {
            holder.mBtn_add.setEnabled(false);
            holder.mBtn_add.setText("已添加好友");
        } else {
            holder.mBtn_add.setEnabled(true);
            holder.mBtn_add.setText("添加");
        }

        //给holder的btn设置点击事件的接口回调
        holder.mBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnAddFriendClickLisener != null) {
                    mOnAddFriendClickLisener.onAddFriendClick(username);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG,"点击了条目");
    }

    public interface  OnAddFriendClickLisener{
        void onAddFriendClick(String username);
    }
    private OnAddFriendClickLisener mOnAddFriendClickLisener;
    public void setOnAddFriendClickLisener(OnAddFriendClickLisener onAddFriendClickLisener){
        this.mOnAddFriendClickLisener = onAddFriendClickLisener;
    }
    class AddFriendViewHolder extends RecyclerView.ViewHolder{

         TextView mTvUsername;
         TextView mTv_time;
         Button mBtn_add;

        public AddFriendViewHolder(View itemView) {
            super(itemView);

            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTv_time = (TextView) itemView.findViewById(R.id.tv_time);
            mBtn_add = (Button) itemView.findViewById(R.id.btn_add);
        }
    }
}
