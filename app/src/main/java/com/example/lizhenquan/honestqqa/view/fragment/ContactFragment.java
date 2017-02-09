package com.example.lizhenquan.honestqqa.view.fragment;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import com.example.lizhenquan.honestqqa.R;
import com.example.lizhenquan.honestqqa.adapter.ContactAdapter;
import com.example.lizhenquan.honestqqa.event.ContactEvent;
import com.example.lizhenquan.honestqqa.presenter.ContactPresenter;
import com.example.lizhenquan.honestqqa.presenter.ContactPresenterImpl;
import com.example.lizhenquan.honestqqa.utils.ToastUtils;
import com.example.lizhenquan.honestqqa.view.BaseActivity;
import com.example.lizhenquan.honestqqa.view.ChatActivityEaseUI;
import com.example.lizhenquan.honestqqa.view.ContactView;
import com.example.lizhenquan.honestqqa.wight.ContactLayout;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
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
    public void onDelete(String contact, boolean isSuccess, String msg) {
        if (isSuccess) {
            Snackbar.make(mContactLayout,"删除"+contact+"成功",Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mContactLayout,"删除"+contact+"失败"+msg,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mContactLayout = null;
        mContactAdapter = null;
    }

    @Override
    public void onRefresh() {
        //更新数据
        mContactPresenter.updateDataFromServer();
    }

    @Override
    public void onClick(String contact) {
       /* Intent intent = new Intent();
        intent.setClass(mContext, ChatActivity.class);
        intent.putExtra("username",contact);
        startActivity(intent);*/
        Intent intent = new Intent();
        intent.setClass(mContext, ChatActivityEaseUI.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID,contact);
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
        startActivity(intent);
    }

    @Override
    public void onLongClick(final String contact) {
        Snackbar.make(mContactLayout,"你确定删除好友"+contact+"吗？",Snackbar.LENGTH_LONG)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO
                        //删除好友
                        mContactPresenter.deleteContact(contact);
                    }
                }).show();
    }
}
