package com.example.lizhenquan.honestqq.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.lizhenquan.honestqq.db.DBUtils;
import com.example.lizhenquan.honestqq.utils.ThreadUtils;
import com.example.lizhenquan.honestqq.view.AddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {
    private AddFriendView mAddFriendView;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        mAddFriendView = addFriendView;
    }

    @Override
    public void searchFriend(String keyword) {
        final List<String> contactsList = DBUtils.getContacts(EMClient.getInstance().getCurrentUser());
        /**
         * 到AVCloud上面查询好友
         */
        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        userQuery.whereContains("username", keyword);
        //搜索结果不包含当前用户
        String currentUser = EMClient.getInstance().getCurrentUser();
        userQuery.whereNotEqualTo("username", currentUser);
        userQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        //真正成功，有数据
                        mAddFriendView.onSearchResult(list,contactsList,true,null);
                    } else {
                        //没有数据
                        mAddFriendView.onSearchResult(null,contactsList,true,null);
                    }
                } else {
                    //出现异常
                    mAddFriendView.onSearchResult(null,null,false,e.getMessage());
                }
            }
        });
    }

    @Override
    public void addContact(final String username) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username,"request reason...");
                    //成功请求，通知UI
                    mAddFriendView.onAddContact(username,true,null);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //请求失败，通知UI
                    mAddFriendView.onAddContact(username,false,e.getMessage());
                }
            }
        });
    }
}
