package com.example.lizhenquan.honestqq.view;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/21.
 */

public interface ContactView {


    void oninitContacts(List<String> contactsList);

    void onUpdateContacts(boolean isSuccess, String msg);

    void onDelete(String contact, boolean isSuccess, String msg);
}
