package com.example.lizhenquan.honestqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.utils.StringUtils;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/23.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContatctViewHolder> implements SlideBarAdapter {

    private List<String> contactList;
    private Context      mContext;
    private AVUser       mCurrentUser;
    private AVQuery<AVUser> mUserQuery;

    public ContactAdapter(List<String> contactList) {
        this.contactList = contactList;
        mCurrentUser = AVUser.getCurrentUser();
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    @Override
    public ContatctViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ContatctViewHolder contatctViewHolder = new ContatctViewHolder(view);
        mUserQuery = new AVQuery<>("_User");

        return contatctViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContatctViewHolder holder, final int position) {
        final String contact = contactList.get(position);
        holder.mTvusername.setText(contact);
        String initial = StringUtils.getInitial(contact);
        holder.mTvsection.setText(initial);
        mUserQuery.whereEqualTo("username",contact);
        mUserQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (list.size()!=0&&list!=null){
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println("username:"+list.get(i).getUsername());
                        String portraitUrl = list.get(i).getString(Constant.PORTRAITURL);
                        if (portraitUrl!=null)
                            Glide.with(mContext).load(portraitUrl).into(holder.mIv_avatar);
                    }
                }
            }
        });




        /**
         * 如果position==0.则肯定显示，否则需要获取一下上一个条目的首字母
         * 如果当前首字母跟上一个首字母一样，则隐藏，否则显示
         */
        if (position == 0) {
            holder.mTvsection.setVisibility(View.VISIBLE);
        } else {
            String preContact = contactList.get(position - 1);
            String preInitial = StringUtils.getInitial(preContact);
            if (preInitial.equals(initial)) {
                holder.mTvsection.setVisibility(View.GONE);
            } else {
                holder.mTvsection.setVisibility(View.VISIBLE);
            }
        }

        //设置接口回调
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnContactListener != null) {
                    mOnContactListener.onClick(contact);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnContactListener != null) {
                    mOnContactListener.onLongClick(contact);
                }
                return true;
            }
        });
    }

    @Override
    public List<String> getData() {
        return contactList;
    }


    class ContatctViewHolder extends RecyclerView.ViewHolder {

        TextView mTvsection;
        TextView mTvusername;
        ImageView mIv_avatar;

        public ContatctViewHolder(View itemView) {
            super(itemView);
            mTvsection = (TextView) itemView.findViewById(R.id.tv_section);
            mTvusername = (TextView) itemView.findViewById(R.id.tv_username);
            mIv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

    /**
     * 接口回调，条目点击事件，以及长按点击事件的实现
     */
    public interface OnContactListener{
        void onClick(String contact);
        void onLongClick(String contact);
    }
    private OnContactListener mOnContactListener;
    public void setOnContactListener(OnContactListener onContactListener){
        this.mOnContactListener = onContactListener;
    }
}
