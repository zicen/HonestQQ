package com.example.lizhenquan.honestqq.view.fragment;


import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.ContactAdapter;
import com.example.lizhenquan.honestqq.event.ContactEvent;
import com.example.lizhenquan.honestqq.presenter.ContactPresenter;
import com.example.lizhenquan.honestqq.presenter.ContactPresenterImpl;
import com.example.lizhenquan.honestqq.utils.ToastUtils;
import com.example.lizhenquan.honestqq.view.BaseActivity;
import com.example.lizhenquan.honestqq.view.ContactView;
import com.example.lizhenquan.honestqq.wight.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment implements ContactView, SwipeRefreshLayout.OnRefreshListener, ContactAdapter.OnContactListener {

    private ContactPresenter mContactPresenter;
    private ContactLayout mContactLayout;
    private ContactAdapter mContactAdapter;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_contact, null);
        mContactLayout = (ContactLayout) view.findViewById(R.id.contact_layout);

        mContactPresenter = new ContactPresenterImpl(this);
        mContactLayout.setOnRefreshListener(this);

        EventBus.getDefault().register(this);
        return view;
    }


    @Override
    protected void initData() {
        /**
         * 获取联系人数据
         */
        mContactPresenter.initContact();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ContactEvent contactEvent){
        ToastUtils.showToast(mContext,contactEvent.isAdded?"添加了"+contactEvent.username:"删除了"+contactEvent.username);
        mContactPresenter.updateDataFromServer();
    }

    @Override
    public void oninitContacts(List<String> contactsList) {
        mContactAdapter = new ContactAdapter(contactsList);
        mContactLayout.setAdapter(mContactAdapter);
        //设置条目点击事件以及长按点击事件
        mContactAdapter.setOnContactListener(this);
    }

    @Override
    public void onUpdateContacts(boolean isSuccess, String msg) {
        if (isSuccess) {
            mContactAdapter.notifyDataSetChanged();
        } else {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.showToast("更新联系人失败，"+msg);
        }
        mContactLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        //更新数据
        mContactPresenter.updateDataFromServer();
    }

    @Override
    public void onClick(String contact) {
        ToastUtils.showToast(mContext,contact+"被点击了，进入聊天页面");
        //TODO
    }

    @Override
    public void onLongClick(final String contact) {
        Snackbar.make(mContactLayout,"你确定删除好友"+contact+"吗？",Snackbar.LENGTH_LONG)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO
                        //删除好友
                        ToastUtils.showToast(mContext,"删除好友"+contact);
                    }
                }).show();
    }
}
