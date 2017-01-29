package com.example.lizhenquan.honestqq.presenter;

import com.example.lizhenquan.honestqq.db.DBUtils;
import com.example.lizhenquan.honestqq.utils.ThreadUtils;
import com.example.lizhenquan.honestqq.view.ContactView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lizhenquan on 2017/1/21.
 */

public class ContactPresenterImpl implements ContactPresenter {
    private ContactView mContactView;
    private List<String> contactsList = new ArrayList<>();
    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
    }

    @Override
    public void initContact() {
        /**
         * 1.先从缓存的数据库中读取联系人数据
         * 2.获取服务器上的联系人数据，保存到缓存
         * 3.刷新UI
         */
            //1.从缓存的数据库中读取联系人数据
            String currentUser = EMClient.getInstance().getCurrentUser();
            List<String> contacts = DBUtils.getContacts(currentUser);
            contactsList.clear();
            contactsList.addAll(contacts);
            mContactView.oninitContacts(contactsList);
            //2.获取服务器上的联系人数据，保存到缓存
                updateFromServer(currentUser);


    }

    @Override
    public void updateDataFromServer() {
        String currentUser = EMClient.getInstance().getCurrentUser();
        updateFromServer(currentUser);
    }

    @Override
    public void deleteContact(final String contact) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(contact);
                    //删除成功，通知UI线程刷新
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDelete(contact,true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    //删除失败
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDelete(contact,false,e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void updateFromServer(final String currentUser) {

        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //获取成功，将网络上的最新数据保存到缓存

                    DBUtils.UpdateContactDB(currentUser, allContactsFromServer);
                    contactsList.clear();
                    contactsList.addAll(allContactsFromServer);
                    Collections.sort(contactsList, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    //通知UI
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onUpdateContacts(true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    //获取失败，通知UI
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onUpdateContacts(false,e.getMessage());
                        }
                    });
                }
            }
        });
    }
}
