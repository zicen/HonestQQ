package com.example.lizhenquan.honestqq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.utils.StringUtils;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/23.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContatctViewHolder> implements SlideBarAdapter {

    private List<String> contactList;

    public ContactAdapter(List<String> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    @Override
    public ContatctViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ContatctViewHolder contatctViewHolder = new ContatctViewHolder(view);
        return contatctViewHolder;
    }

    @Override
    public void onBindViewHolder(ContatctViewHolder holder, int position) {
        String contact = contactList.get(position);
        holder.mTvusername.setText(contact);
        String initial = StringUtils.getInitial(contact);
        holder.mTvsection.setText(initial);

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
    }

    @Override
    public List<String> getData() {
        return contactList;
    }


    class ContatctViewHolder extends RecyclerView.ViewHolder {

        TextView mTvsection;
        TextView mTvusername;

        public ContatctViewHolder(View itemView) {
            super(itemView);
            mTvsection = (TextView) itemView.findViewById(R.id.tv_section);
            mTvusername = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }
}
