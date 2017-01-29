package com.example.lizhenquan.honestqq.view.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.ConversationAdapter;
import com.example.lizhenquan.honestqq.presenter.ConversationPresenter;
import com.example.lizhenquan.honestqq.presenter.ConversationPresenterImpl;
import com.example.lizhenquan.honestqq.view.ChatActivity;
import com.example.lizhenquan.honestqq.view.ConversationView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements ConversationView, ConversationAdapter.IonSlidingViewClickListener {

    private ConversationPresenter mConversationPresenter;
    private RecyclerView     mRecyclerView;
    private ConversationAdapter   mConversationAdapter;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_conversation, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL));
        mConversationPresenter = new ConversationPresenterImpl(this);

        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    protected void initData() {

    }

    /**
     * 在OnResume方法中进行会话的初始化，这样不仅第一次进入的时候回刷新UI，在从ChatActivity返回的时候也会
     * 刷新UI
     */
    @Override
    public void onResume() {
        super.onResume();
        mConversationPresenter.initConversation();
    }

    /**
     * 接收到消息重新调用P层的方法刷新UI即可
     * @param emMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        mConversationPresenter.initConversation();
    }
    @Override
    public void onInitConversation(List<EMConversation> eMConversationList) {
        mConversationAdapter = new ConversationAdapter(eMConversationList,this);
        mRecyclerView.setAdapter(mConversationAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //释放View资源
        mRecyclerView = null;
    }


    @Override
    public void onItemClick(View view, int position, EMConversation emConversation) {
        Intent intent = new Intent();
        intent.setClass(mContext, ChatActivity.class);
        intent.putExtra("username",emConversation.getUserName());
        startActivity(intent);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position,EMConversation emConversation) {
        //本地删除
        mConversationAdapter.removeData(position);
        //访问网络删除会话
        //TODO
        mConversationPresenter.removeConversation(emConversation);

    }

}
