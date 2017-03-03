package com.example.lizhenquan.honestqq.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.lizhenquan.honestqq.db.DBUtils;
import com.example.lizhenquan.honestqq.model.ContactBean;
import com.example.lizhenquan.honestqq.utils.Constant;
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
    private ContactView     mContactView;
    private AVQuery<AVUser> mUserQuery  = new AVQuery<>("_User");;
    private List<String> contactsList = new ArrayList<>();
    private  List<ContactBean> contactBeanList = new ArrayList<>();
    private  List<ContactBean> contactBeanList2 = new ArrayList<>();
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
        List<ContactBean> contacts = DBUtils.getContacts(currentUser);
        contactBeanList.clear();
        contactBeanList.addAll(contacts);
        System.out.println("contactBeanList initSize"+contactBeanList.size());
        for (int i = 0; i < contactBeanList.size(); i++) {
            ContactBean contactBean = contactBeanList.get(i);
            System.out.println("contactBeanList initData"+contactBean.username+","+contactBean.avatarUrl);
        }

        /**
         * 初始化的时候应该是到缓存的数据库中去拿List<ContactBean>数据
         */
        mContactView.oninitContacts(contactBeanList);
        //2.获取服务器上的联系人数据，保存到缓存
        updateFromServer(currentUser);



    }

    private ContactBean  queryAvatar(final String username) {
        mUserQuery.whereEqualTo("username", username);
         final ContactBean contactBean = new ContactBean();
        //这是一个在子线程请求的方法，所以最后如果直接就返回contactBean的话，会是一个空的对象，如何解决？

                try {
                    List<AVUser> avUsers = mUserQuery.find();
                    if (avUsers.size() != 0 && avUsers != null) {
                        String portraitUrl = avUsers.get(0).getString(Constant.PORTRAITURL);
                        System.out.println("username:" + avUsers.get(0).getUsername()+"portraitUrl:"+portraitUrl);
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

    private ContactBean  queryAvatar2(final String username) {
        mUserQuery.whereEqualTo("username", username);
        final ContactBean contactBean = new ContactBean();
        //这是一个在子线程请求的方法，所以最后如果直接就返回contactBean的话，会是一个空的对象，如何解决？
        mUserQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (list != null && list.size() > 0) {
                    String portraitUrl = list.get(0).getString(Constant.PORTRAITURL);
                    if (portraitUrl == null) {
                        contactBean.avatarUrl = null;
                        contactBean.username = username;
                    } else {
                        contactBean.avatarUrl = portraitUrl;
                        contactBean.username = username;
                    }

                }
            }
        });

            return contactBean;

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
                            mContactView.onDelete(contact, true, null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    //删除失败
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDelete(contact, false, e.getMessage());
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
                    final List<String> allContactsFromServer =   EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //获取成功，将网络上的最新数据保存到缓存
                    contactBeanList2.clear();
                    for (int i = 0; i < allContactsFromServer.size(); i++) {
                        String username = allContactsFromServer.get(i);
                        //查询并添加到ContactBean集合中
                        ContactBean contactBean = queryAvatar(username);
                        contactBeanList2.add(contactBean);

                    }
                    contactBeanList.clear();
                    contactBeanList.addAll(contactBeanList2);
                    System.out.println("contactBeanList LastSize:"+contactBeanList.size());
                    DBUtils.UpdateContactDB(currentUser, contactBeanList);
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
                            mContactView.onUpdateContacts(true, contactsList, null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    //获取失败，通知UI
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onUpdateContacts(false, contactsList, e.getMessage());
                        }
                    });
                }
            }
        });
    }
}
