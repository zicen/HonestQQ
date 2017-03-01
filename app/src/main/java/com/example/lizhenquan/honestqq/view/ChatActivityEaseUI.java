package com.example.lizhenquan.honestqq.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lizhenquan.honestqq.R;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class ChatActivityEaseUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chateaseui);

        EaseChatFragment easeChatFragment = new EaseChatFragment();
        easeChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, easeChatFragment).commit();
    }

}
