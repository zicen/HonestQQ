package com.example.lizhenquan.honestqqa.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public interface ChatView {
    void onInitChatData(List<EMMessage> emMessageList);

    void onUpdateChatMessgae();

}
