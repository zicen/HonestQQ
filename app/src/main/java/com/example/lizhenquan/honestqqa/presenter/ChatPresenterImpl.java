package com.example.lizhenquan.honestqqa.presenter;

import com.example.lizhenquan.honestqqa.adapter.MyEMCallBack;
import com.example.lizhenquan.honestqqa.view.ChatView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public class ChatPresenterImpl implements ChatPresenter {
    private ChatView mChatView;
    private List<EMMessage> mEMMessageList = new ArrayList<>();

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
    }

    @Override
    public void initChatData(String conatct) {
        /**
         * 1.如果曾经有过会话，则返回一个最多20条数据的集合
         * 2.没有过会话，则返回一个空的集合
         */
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(conatct);
        if (conversation != null) {//1.如果曾经有过会话，则返回一个最多20条数据的集合
            /**
             * 设置消息为已读
             */
            conversation.markAllMessagesAsRead();
            //获取此会话的所有消息
            EMMessage lastMessage = conversation.getLastMessage();
            //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
            //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
            List<EMMessage> messages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), 19);
            mEMMessageList.clear();
            mEMMessageList.addAll(messages);
            mEMMessageList.add(lastMessage);
            mChatView.onInitChatData(mEMMessageList);
        } else {//2.没有过会话，则返回一个空的集合
            mEMMessageList.clear();
            mChatView.onInitChatData(mEMMessageList);
        }

    }

    @Override
    public void sendMessage(String msg, String contact) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        final EMMessage message = EMMessage.createTxtSendMessage(msg, contact);
        /**
         * 立即将当前消息添加到集合中，然后更新View
         */
        mEMMessageList.add(message);
        mChatView.onUpdateChatMessgae();
        //发送消息，异步的方法没有回调
        EMClient.getInstance().chatManager().sendMessage(message);
        //给message添加回调监听
        message.setMessageStatusCallback(new MyEMCallBack() {
            @Override
            public void onMainSuccess() {
                //消息发送成功
                mChatView.onUpdateChatMessgae();
            }

            @Override
            public void onMainError(int i, String s) {
                //消息发送失败
                mChatView.onUpdateChatMessgae();
            }
        });
    }

    @Override
    public void receiveMessage(EMMessage emMessage) {
        /**
         * 当再次接收到消息的时候也要设置为已读
         */
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(emMessage.getFrom());
        if (conversation != null) {
            conversation.markAllMessagesAsRead();
        }
        mEMMessageList.add(emMessage);
        mChatView.onUpdateChatMessgae();
    }
}
