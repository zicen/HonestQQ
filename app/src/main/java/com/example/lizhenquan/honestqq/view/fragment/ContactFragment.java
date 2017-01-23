package com.example.lizhenquan.honestqq.view.fragment;


import android.support.v4.app.Fragment;
import android.view.View;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.ContactAdapter;
import com.example.lizhenquan.honestqq.presenter.ContactPresenter;
import com.example.lizhenquan.honestqq.presenter.ContactPresenterImpl;
import com.example.lizhenquan.honestqq.view.BaseActivity;
import com.example.lizhenquan.honestqq.view.ContactView;
import com.example.lizhenquan.honestqq.wight.ContactLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment implements ContactView{

    private ContactPresenter mContactPresenter;
    private ContactLayout mContactLayout;
    private ContactAdapter mContactAdapter;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_contact, null);
        mContactLayout = (ContactLayout) view.findViewById(R.id.contact_layout);

        mContactPresenter = new ContactPresenterImpl(this);
        return view;
    }

    @Override
    protected void initData() {
        /**
         * 获取联系人数据
         */
        mContactPresenter.initContact();
    }

    @Override
    public void oninitContacts(List<String> contactsList) {
        mContactAdapter = new ContactAdapter(contactsList);
        mContactLayout.setAdapter(mContactAdapter);

    }

    @Override
    public void onUpdateContacts(boolean isSuccess, String msg) {
        if (isSuccess) {
            mContactAdapter.notifyDataSetChanged();
        } else {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.showToast("更新联系人失败，"+msg);
        }
    }
}
