package com.example.lizhenquan.honestqq.view;

import com.example.lizhenquan.honestqq.model.ContactBean;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/21.
 */

public interface ContactView {


    void oninitContacts(List<ContactBean> contactsList);

    void onUpdateContacts(boolean isSuccess, List<String> contactsList, String msg);

    void onDelete(String contact, boolean isSuccess, String msg);
}
