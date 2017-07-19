package com.example.lizhenquan.honestqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.model.ContactBean;
import com.example.lizhenquan.honestqq.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lizhenquan on 2017/1/23.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContatctViewHolder> implements SlideBarAdapter {

    private List<ContactBean> contactList;
    private Context           mContext;

    public ContactAdapter(List<ContactBean> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }



    public List<ContactBean> getContactList() {
        if (contactList != null && contactList.size() > 0) {
            return contactList;
        }
        return null;
    }

    @Override
    public ContatctViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ContatctViewHolder contatctViewHolder = new ContatctViewHolder(view);

        return contatctViewHolder;
    }

   /* @Override
    public void onViewAttachedToWindow(ContatctViewHolder holder) {
        //设置条目加载动画
        holder.itemView.setScaleX(.6F);
        holder.itemView.setScaleY(.6f);
        ViewCompat.animate(holder.itemView).scaleX(1).scaleY(1).setDuration(300).setInterpolator(new OvershootInterpolator())
                .start();
        super.onViewAttachedToWindow(holder);
    }*/

    @Override
    public void onBindViewHolder(final ContatctViewHolder holder, final int position) {


        final String contact = contactList.get(position).username;
        holder.mTvusername.setText(contact);
        String initial = StringUtils.getInitial(contact);
        holder.mTvsection.setText(initial);

        String avatarUrl = contactList.get(position).avatarUrl;
        if (avatarUrl != null) {
            Glide.with(mContext).load(avatarUrl).into(holder.mIv_avatar);
        } else {
            holder.mIv_avatar.setImageResource(R.mipmap.avatar3);
        }
        /**
         * 如果position==0.则肯定显示，否则需要获取一下上一个条目的首字母
         * 如果当前首字母跟上一个首字母一样，则隐藏，否则显示
         */
        if (position == 0) {
            holder.mTvsection.setVisibility(View.VISIBLE);
        } else {
            String preContact = contactList.get(position - 1).username;
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
       List<String> data = new ArrayList<>();
        for (int i = 0; i < contactList.size(); i++) {
            data.add(contactList.get(i).username);
        }
        return data;
    }


    class ContatctViewHolder extends RecyclerView.ViewHolder {

        TextView        mTvsection;
        TextView        mTvusername;
        CircleImageView mIv_avatar;
        View mItemView;
        public ContatctViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            mTvsection = (TextView) itemView.findViewById(R.id.tv_section);
            mTvusername = (TextView) itemView.findViewById(R.id.tv_username);
            mIv_avatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
        }


    }

    /**
     * 接口回调，条目点击事件，以及长按点击事件的实现
     */
    public interface OnContactListener {
        void onClick(String contact);

        void onLongClick(String contact);
    }

    private OnContactListener mOnContactListener;

    public void setOnContactListener(OnContactListener onContactListener) {
        this.mOnContactListener = onContactListener;
    }

}
