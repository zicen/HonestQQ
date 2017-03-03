package com.example.lizhenquan.honestqq.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.db.DBUtils;
import com.example.lizhenquan.honestqq.utils.Utils;
import com.example.lizhenquan.honestqq.view.fragment.ConversationFragment;
import com.example.lizhenquan.honestqq.wight.SlidingButtonView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> implements SlidingButtonView.IonSlidingButtonListener {
    private  Context mContext;
    private List<EMConversation> eMConversationList;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private SlidingButtonView mMenu = null;
    public ConversationAdapter(List<EMConversation> eMConversationList, ConversationFragment context) {
        this.eMConversationList = eMConversationList;
        mContext = context.getContext();
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
    }



    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        view.setScaleX(.6F);
        view.setScaleY(.6f);
        ViewCompat.animate(view).scaleX(1).scaleY(1).setDuration(300).setInterpolator(new OvershootInterpolator())
                .start();
        ConversationViewHolder conversationViewHolder = new ConversationViewHolder(view);
        return conversationViewHolder;
    }

    @Override
    public void onViewAttachedToWindow(ConversationViewHolder holder) {
        //设置条目加载动画
        holder.mItemView.setScaleX(.6F);
        holder.mItemView.setScaleY(.6f);
        ViewCompat.animate(holder.mItemView).scaleX(1).scaleY(1).setDuration(300).setInterpolator(new OvershootInterpolator())
                .start();
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onBindViewHolder(final ConversationViewHolder holder, final int position) {
        final EMConversation emConversation = eMConversationList.get(position);
        String userName = emConversation.getUserName();
        holder.mTvUsername.setText(userName);
        EMMessage lastMessage = emConversation.getLastMessage();
        if (lastMessage != null) {
            long msgTime = lastMessage.getMsgTime();
            holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));

            EMMessageBody body = lastMessage.getBody();
            if (body instanceof EMTextMessageBody) {
                EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
                String message = emTextMessageBody.getMessage();
                holder.mTvMsg.setText(message);
            } else {
                holder.mTvMsg.setText("");
            }
        } else {
            holder.mTvTime.setText("");
            holder.mTvMsg.setText("");
        }


        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if (unreadMsgCount > 99) {
            holder.mTvUnread.setText("99+");
           holder.mTvUnread.setVisibility(View.VISIBLE);
        }else if (unreadMsgCount>0){
            holder.mTvUnread.setText(unreadMsgCount+"");
            holder.mTvUnread.setVisibility(View.VISIBLE);
        }else{
            holder.mTvUnread.setVisibility(View.INVISIBLE);
        }
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n,emConversation);
                }
            }
        });
        //设置内容布局的宽为屏幕宽度
        holder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);

        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n,emConversation);
            }
        });

        String avatarUrl = DBUtils.getAvatarUrl(userName);
        if (avatarUrl != null) {
            Glide.with(mContext).load(avatarUrl).into(holder.mCircleImageView);
        } else {
            holder.mCircleImageView.setImageResource(R.mipmap.avatar3);
        }


    }
    @Override
    public int getItemCount() {
        return eMConversationList==null?0:eMConversationList.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder{
        private final TextView mTvMsg;
        private final TextView mTvUnread;
        private final TextView mTvTime;
        private final TextView mTvUsername;
        private final ViewGroup layout_content;
        private final TextView btn_Delete;
        private final CircleImageView mCircleImageView;
        View mItemView;
        public ConversationViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvUnread = (TextView) itemView.findViewById(R.id.tv_unread);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
             btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            mCircleImageView = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            ((SlidingButtonView) itemView).setSlidingButtonListener(ConversationAdapter.this);

        }
    }

    public void removeData(int position){
        eMConversationList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if(menuIsOpen()){
            if(mMenu != slidingButtonView){
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }
    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if(mMenu != null){
            return true;
        }
        Log.i("asd","mMenu为null");
        return false;
    }

    public interface IonSlidingViewClickListener {
        void onItemClick(View view,int position,EMConversation emConversation);
        void onDeleteBtnCilck(View view,int position,EMConversation emConversation);
    }
}
