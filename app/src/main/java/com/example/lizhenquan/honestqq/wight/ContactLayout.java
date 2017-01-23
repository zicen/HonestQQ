package com.example.lizhenquan.honestqq.wight;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;

/**
 * Created by lizhenquan on 2017/1/19.
 */

public class ContactLayout extends RelativeLayout {

    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private SlideBar mSlideBar;

    public ContactLayout(Context context) {
        this(context,null);
    }


    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.contact_layout,this,true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_contact);
        mTextView = (TextView) findViewById(R.id.tv_float);
        mSlideBar = (SlideBar) findViewById(R.id.slidebar);
    }

    /**
     * 利用代理设计模式来将RecycleView的setAdapter方法设置出去
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        /**
         * 设置适配器之前不要忘了设置布局管理器
         */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }

}
