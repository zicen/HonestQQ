package com.example.lizhenquan.honestqq.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.AddFriendAdapter;
import com.example.lizhenquan.honestqq.presenter.AddFriendPresenter;
import com.example.lizhenquan.honestqq.presenter.AddFriendPresenterImpl;

import java.util.List;

public class AddFriendActivity extends BaseActivity implements AddFriendView, AddFriendAdapter.OnAddFriendClickLisener {

    private Toolbar            mAdd_friend_toolbar;
    private ImageView          mIv_nodata;
    private RecyclerView       mRecycleView_addfriend;
    private AddFriendPresenter mAddFriendPresenter;
    private InputMethodManager mInputMethodManager;
    private SearchView         mSearchView;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        mAdd_friend_toolbar = (Toolbar) findViewById(R.id.add_friend_toolbar);
        mIv_nodata = (ImageView) findViewById(R.id.iv_nodata);
        mRecycleView_addfriend = (RecyclerView) findViewById(R.id.recycleView_addfriend);
        mAdd_friend_toolbar.setTitle("");
        setSupportActionBar(mAdd_friend_toolbar);
        mAddFriendPresenter = new AddFriendPresenterImpl(this);

        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setQueryHint("用户名");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAddFriendPresenter.searchFriend(query);
                showDialog("正在查询...");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public void onSearchResult(List<AVUser> list, List<String> contactsList, boolean isSuccess, String msg) {
        hideDialog();
        //隐藏输入法程序
        mInputMethodManager.hideSoftInputFromInputMethod(mSearchView.getWindowToken(), 0);
        mSearchView.clearFocus();
        if (isSuccess) {

            if (list != null && list.size() > 0) {
                //成功了,并且有数据
                /**
                 * 1.隐藏nodata图片
                 * 2.显示RecycleView
                 */
                mIv_nodata.setVisibility(View.GONE);
                mRecycleView_addfriend.setVisibility(View.VISIBLE);
                mRecycleView_addfriend.setLayoutManager(new LinearLayoutManager(AddFriendActivity.this));

                AddFriendAdapter addFriendAdapter = new AddFriendAdapter(AddFriendActivity.this, list, contactsList);
                addFriendAdapter.setOnAddFriendClickLisener(this);
                mRecycleView_addfriend.setAdapter(addFriendAdapter);
            } else {
                //请求成功，但是没有数据
                /**
                 * 1.显示nodata图片
                 * 2.隐藏RecycleView
                 * 3.显示Snackbar提示用户
                 */
                mIv_nodata.setVisibility(View.VISIBLE);
                mRecycleView_addfriend.setVisibility(View.GONE);
                Snackbar.make(mAdd_friend_toolbar, "没有查询到指定结果", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            /**
             * 1.显示nodata图片
             * 2.隐藏RecycleView
             * 3.显示Snackbar提示用户
             */
            mIv_nodata.setVisibility(View.VISIBLE);
            mRecycleView_addfriend.setVisibility(View.GONE);
            Snackbar.make(mAdd_friend_toolbar, "出现异常" + msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddContact(String username, boolean isSuccess, String message) {
        if (isSuccess) {
            Snackbar.make(mAdd_friend_toolbar, "发送好友请求成功！", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mAdd_friend_toolbar, "发送好友请求失败！" + message, Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAddFriendClick(String username) {
        mAddFriendPresenter.addContact(username);
    }
}
