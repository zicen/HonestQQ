package com.example.lizhenquan.honestqq.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.ChatAdapter;
import com.example.lizhenquan.honestqq.presenter.ChatPresenter;
import com.example.lizhenquan.honestqq.presenter.ChatPresenterImpl;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ChatActivity extends BaseActivity implements TextWatcher,ChatView, View.OnClickListener {

    private String mContact;
    private Button mBtnSend;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private EditText mEtMsg;
    private TextView mTvTitle;
    private ChatPresenter mChatPresenter;
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        mContact = intent.getStringExtra("username");

        if (TextUtils.isEmpty(mContact)){
            showToast("没有获取到聊天对象");
            finish();
            return;
        }
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mToolbar = (Toolbar) findViewById(R.id.toolBar_chat);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEtMsg = (EditText) findViewById(R.id.et_msg);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText(mContact);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String msg = mEtMsg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)){
            mBtnSend.setEnabled(false);
        }else {
            mBtnSend.setEnabled(true);
        }
        mEtMsg.addTextChangedListener(this);

        mChatPresenter = new ChatPresenterImpl(this);
        mChatPresenter.initChatData(mContact);
        mBtnSend.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().length() > 0) {
            mBtnSend.setEnabled(true);
        } else {
            mBtnSend.setEnabled(false);
        }
    }

    /**
     * EventBus接收到消息时，调用P层方法
     * @param emMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        if (emMessage.getFrom().equals(mContact)) {
            mChatPresenter.receiveMessage(emMessage);
        }

    }

    @Override
    public void onInitChatData(List<EMMessage> emMessageList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new ChatAdapter(emMessageList);
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.scrollToPosition(emMessageList.size()-1);
    }

    /**
     * 1.接收到消息后刷新适配器
     * 2.将RecycleView滚动到最后一个条目
     */
    @Override
    public void onUpdateChatMessgae() {
        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mChatAdapter.getItemCount()-1);
    }

    @Override
    public void onClick(View view) {
        String msg = mEtMsg.getText().toString();
        if (TextUtils.isEmpty(msg)){
            showToast("不能发送空消息！");
            return;
        }
        mChatPresenter.sendMessage(msg,mContact);
        //发送出去消息后，清空EditText
        mEtMsg.getText().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
