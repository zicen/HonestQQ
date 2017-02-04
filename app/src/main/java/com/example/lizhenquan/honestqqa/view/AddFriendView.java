package com.example.lizhenquan.honestqqa.view;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public interface AddFriendView {
    void onSearchResult(List<AVUser> list, List<String> contactsList, boolean isSuccess, String msg);

    void onAddContact(String username, boolean isSuccess, String message);
}
