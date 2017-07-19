package com.example.lizhenquan.honestqq.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.example.lizhenquan.honestqq.model.ContactBean;
import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.view.ConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public class ConversationPresenterImpl implements ConversationPresenter {

    private ConversationView mConversationView;
    private AVQuery<AVUser> mUserQuery = new AVQuery<>("_User");
    ;
    private List<String>         mAvatarUrl         = new ArrayList<>();
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
        if (eMConversationList != null && eMConversationList.size() > 0) {
            Collections.sort(eMConversationList, new Comparator<EMConversation>() {
                @Override
                public int compare(EMConversation emConversation, EMConversation emConversation1) {
                    EMMessage lastMessage = emConversation.getLastMessage();
                    EMMessage lastMessage1 = emConversation1.getLastMessage();
                    if (emConversation != null && lastMessage != null && lastMessage1!=null) {
                        long msgTime = lastMessage.getMsgTime();
                        long msgTime1 = lastMessage1.getMsgTime();
                        int num = (int) (msgTime1 - msgTime);
                        return num == 0 ? (int) msgTime : num;
                    }
                    return 0;
                }
            });
        }

        mConversationView.onInitConversation(eMConversationList);
    }

    @Override
    public void removeConversation(EMConversation emConversation) {
        //删除和某个user会话，如果需要保留聊天记录，传false
        String userName = emConversation.getUserName();
        EMClient.getInstance().chatManager().deleteConversation(userName, false);

    }

    private ContactBean queryAvatar(final String username) {
        mUserQuery.whereEqualTo("username", username);
        final ContactBean contactBean = new ContactBean();
        //这是一个在子线程请求的方法，所以最后如果直接就返回contactBean的话，会是一个空的对象，如何解决？

        try {
            List<AVUser> avUsers = mUserQuery.find();
            if (avUsers.size() != 0 && avUsers != null) {
                String portraitUrl = avUsers.get(0).getString(Constant.PORTRAITURL);
                System.out.println("username:" + avUsers.get(0).getUsername() + "portraitUrl:" + portraitUrl);
                //将数据封装到Bean中
                if (portraitUrl == null) {
                    contactBean.avatarUrl = null;
                    contactBean.username = username;
                } else {
                    contactBean.avatarUrl = portraitUrl;
                    contactBean.username = username;
                }
            }
        } catch (AVException e) {
            e.printStackTrace();
        }

        return contactBean;
    }
}
