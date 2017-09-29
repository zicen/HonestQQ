package com.example.lizhenquan.honestqq.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.lizhenquan.honestqq.MyApplication;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.MyImgAdapter;
import com.example.lizhenquan.honestqq.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MyImgActivity extends AppCompatActivity {
    public static final String TAG = MyImgActivity.class.getSimpleName();
    private List<String> mImgUrl;
    private RecyclerView recyclerView;
    private MyImgAdapter mAdapter;
    private Toolbar my_img_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_img);
        my_img_toolbar = (Toolbar) findViewById(R.id.my_img_toolbar);
        ImageView img_add_img = (ImageView) findViewById(R.id.img_add_img);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        setSupportActionBar(my_img_toolbar);
        mImgUrl = new ArrayList<>();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initRecyclerView(recyclerView);


        initData();
    }

    /**
     * 初始化RecyclerView
     *
     * @param recyclerView 主控件
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true); // 设置固定大小
        initRecyclerLayoutManager(recyclerView); // 初始化布局
        initRecyclerAdapter(recyclerView); // 初始化适配器
    }

    private void initRecyclerLayoutManager(RecyclerView recyclerView) {
        // 网格布局
        recyclerView.setLayoutManager(new  StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.HORIZONTAL));
    }

    private void initRecyclerAdapter(RecyclerView recyclerView) {
        mAdapter = new MyImgAdapter(this, mImgUrl);
        recyclerView.setAdapter(mAdapter);
    }


    private void initData() {
        AVQuery<AVObject> avQuery = new AVQuery<>(Constant.UPLOADIMG);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        AVObject avObject = list.get(i);
                        Log.e(TAG, "i:" + avObject.toString());
                        if (avObject.get("userId").equals(MyApplication.mCurrentUser.getObjectId())) {
                            mImgUrl.add((String) avObject.get("ImgUrl"));
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        });

    }
}
