package com.example.lizhenquan.honestqq.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<EMMessage> emMessageList;

    public ChatAdapter(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = emMessageList.get(position);
        EMMessage.Direct direct = emMessage.direct();
        return direct == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return emMessageList == null ? 0 : emMessageList.size();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_receive, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_send, parent, false);
        }
        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        EMMessage emMessage = emMessageList.get(position);

        //设置时间的显示与隐藏
        long msgTime = emMessage.getMsgTime();
        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (position == 0) {
            holder.mTvTime.setVisibility(View.VISIBLE);
        } else {
            EMMessage preMsg = emMessageList.get(position - 1);
            long preTime = preMsg.getMsgTime();
            if (DateUtils.isCloseEnough(msgTime, preTime)) {
                //不显示时间
                holder.mTvTime.setVisibility(View.GONE);
            } else {
                holder.mTvTime.setVisibility(View.VISIBLE);
            }
        }

        //设置消息文本
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody textMessageBody = (EMTextMessageBody) body;
            String message = textMessageBody.getMessage();
            holder.mTvMsg.setText(message);
        }

        //处理消息的状态
        if (emMessage.direct() == EMMessage.Direct.SEND) {
            EMMessage.Status status = emMessage.status();
            switch (status) {
                case CREATE:
                case INPROGRESS:
                    holder.mIvMsgState.setVisibility(View.VISIBLE);
                    holder.mIvMsgState.setImageResource(R.drawable.msg_sending_anim);
                    Drawable drawable = holder.mIvMsgState.getDrawable();
                    AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
                    if (animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    animationDrawable.start();
                    break;
                case FAIL:
                    holder.mIvMsgState.setImageResource(R.mipmap.msg_error);
                    holder.mIvMsgState.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    holder.mIvMsgState.setVisibility(View.GONE);
                    break;

            }
        }

    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView  mTvTime;
        private final TextView  mTvMsg;
        private final ImageView mIvMsgState;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mIvMsgState = (ImageView) itemView.findViewById(R.id.iv_msg_state);
        }
    }
}
