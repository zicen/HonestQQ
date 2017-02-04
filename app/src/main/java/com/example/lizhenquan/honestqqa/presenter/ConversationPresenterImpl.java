package com.example.lizhenquan.honestqqa.presenter;

import com.example.lizhenquan.honestqqa.view.ConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public class ConversationPresenterImpl implements ConversationPresenter {

   private ConversationView mConversationView;
    private List<EMConversation> eMConversationList = new ArrayList<>();

    public ConversationPresenterImpl(ConversationView conversationView) {
        mConversationView = conversationView;
    }

    @Override
    public void initConversation() {
        eMConversationList.clear();
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        if (allConversations != null && allConversations.size() > 0) {
            eMConversationList.addAll(allConversations.values());
        }
        mConversationView.onInitConversation(eMConversationList);
    }

    @Override
    public void removeConversation(EMConversation emConversation) {
        //删除和某个user会话，如果需要保留聊天记录，传false
        String userName = emConversation.getUserName();
       EMClient.getInstance().chatManager().deleteConversation(userName, false);

    }


}
