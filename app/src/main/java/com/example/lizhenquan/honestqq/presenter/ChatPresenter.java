package com.example.lizhenquan.honestqq.presenter;

import com.hyphenate.chat.EMMessage;

/**
 * Created by lizhenquan on 2017/1/24.
 */

public interface ChatPresenter {
    void initChatData(String conatct);

    void sendMessage(String msg, String contact);

    void receiveMessage(EMMessage emMessage);
}
