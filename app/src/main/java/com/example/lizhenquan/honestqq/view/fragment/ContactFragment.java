package com.example.lizhenquan.honestqq.view.fragment;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.ContactAdapter;
import com.example.lizhenquan.honestqq.event.ContactEvent;
import com.example.lizhenquan.honestqq.model.ContactBean;
import com.example.lizhenquan.honestqq.presenter.ContactPresenter;
import com.example.lizhenquan.honestqq.presenter.ContactPresenterImpl;
import com.example.lizhenquan.honestqq.utils.ToastUtils;
import com.example.lizhenquan.honestqq.view.activity.AddFriendActivity;
import com.example.lizhenquan.honestqq.view.activity.BaseActivity;
import com.example.lizhenquan.honestqq.view.activity.ChatActivityEaseUI;
import com.example.lizhenquan.honestqq.view.ContactView;
import com.example.lizhenquan.honestqq.wight.ContactLayout;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment implements ContactView, SwipeRefreshLayout.OnRefreshListener, ContactAdapter.OnContactListener{

    private ContactPresenter mContactPresenter;
    private ContactLayout mContactLayout;
    private ContactAdapter mContactAdapter;
    private RelativeLayout mRl_addnew;
    private View mView;

    @Override
    protected View initView() {
        mView = View.inflate(mContext, R.layout.fragment_contact, null);
        mContactLayout = (ContactLayout) mView.findViewById(R.id.contact_layout);
        mRl_addnew = (RelativeLayout) mView.findViewById(R.id.rl_addnew);
        Button btn_add = (Button) mView.findViewById(R.id.btn_addnewfriend);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AddFriendActivity.class));
            }
        });
        mContactPresenter = new ContactPresenterImpl(this);
        mContactLayout.setOnRefreshListener(this);


        EventBus.getDefault().register(this);
        return mView;
    }


    @Override
    protected void initData() {
        /**
         * 获取初始化联系人数据
         */

        mContactPresenter.initContact();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ContactEvent contactEvent){
        ToastUtils.showToast(mContext,contactEvent.isAdded?"添加了"+contactEvent.username:"删除了"+contactEvent.username);
        mContactPresenter.updateDataFromServer();
    }

    @Override
    public void oninitContacts(List<ContactBean> contactsList) {
        if (contactsList.size() <= 0) {
            mRl_addnew.setVisibility(View.VISIBLE);
        } else {
            mRl_addnew.setVisibility(View.GONE);
        }

        mContactAdapter = new ContactAdapter(contactsList);
        mContactLayout.setAdapter(mContactAdapter);
        //设置条目点击事件以及长按点击事件
        mContactAdapter.setOnContactListener(this);
    }

    @Override
    public void onUpdateContacts(boolean isSuccess, List<String> contactsList, String msg) {

        if (isSuccess) {
            if (contactsList.size() <= 0) {
                mRl_addnew.setVisibility(View.VISIBLE);
            } else {
                mRl_addnew.setVisibility(View.GONE);
            }
           // mContactAdapter.notifyItemRangeInserted(0, locationData.size()+1);
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
        List<ContactBean> contactList = mContactAdapter.getContactList();
        if (contactList != null && contactList.size() > 0) {
            mContactPresenter.updateDataFromServer();
        }

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
