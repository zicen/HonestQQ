package com.example.lizhenquan.honestqqa.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        this.mContext = activity;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        /*RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = initView();
        return view;
    }

    protected abstract View initView();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract void initData();
}
