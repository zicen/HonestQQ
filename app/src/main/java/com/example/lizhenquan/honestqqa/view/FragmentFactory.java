package com.example.lizhenquan.honestqqa.view;

import com.example.lizhenquan.honestqqa.view.fragment.BaseFragment;
import com.example.lizhenquan.honestqqa.view.fragment.ContactFragment;
import com.example.lizhenquan.honestqqa.view.fragment.ConversationFragment;
import com.example.lizhenquan.honestqqa.view.fragment.PluginFragment;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class FragmentFactory {
    private static ConversationFragment conversationFragment ;
    private static ContactFragment      contactFragment ;
    private static PluginFragment       pluginFragment ;
    public static BaseFragment getFragmentByPosition(int position) {
        switch (position) {
            case 0:
                if (conversationFragment == null) {
                    conversationFragment = new ConversationFragment();
                }
                return  conversationFragment;
            case 1:
                if (contactFragment == null) {
                    contactFragment = new ContactFragment();
                }
                return  contactFragment;

            case 2:

                if (pluginFragment == null) {
                    pluginFragment = new PluginFragment();
                }
                return  pluginFragment;


        }
        return null;
    }
}
