package com.example.lizhenquan.honestqq.view;

import com.avos.avoscloud.AVUser;
import com.example.lizhenquan.honestqq.model.ContactBean;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public interface AddFriendView {
    void onSearchResult(List<AVUser> list, List<ContactBean> contactsList, boolean isSuccess, String msg);

    void onAddContact(String username, boolean isSuccess, String message);
}
