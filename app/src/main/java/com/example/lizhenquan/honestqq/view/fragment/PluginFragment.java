package com.example.lizhenquan.honestqq.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.PluginAdapter;
import com.example.lizhenquan.honestqq.model.HeaderBean;
import com.example.lizhenquan.honestqq.model.NewItemBean;
import com.example.lizhenquan.honestqq.presenter.PluginPresenter2;
import com.example.lizhenquan.honestqq.presenter.PluginPresenterImpl2;
import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.utils.ThreadUtils;
import com.example.lizhenquan.honestqq.view.PluginView2;
import com.example.lizhenquan.honestqq.view.activity.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhenquan on 2017/2/17.
 */

public class PluginFragment extends BaseFragment implements PluginView2 {


    private SliderLayout                           mSliderLayout;
    private RecyclerView                           mRecyclerView;
    private PluginPresenter2                       mPluginPresenter;
    private List<NewItemBean.ResultBean.ItemsBean> mShowItem;
    private PluginAdapter                          mPluginAdapter;
    public boolean hasMore       = true;    //默认有更多的数据
    public String  nextpageToken = "";
    public int lastVisibleItemPosition;  //最后一个条目的索引


    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_plugin, null);
        nextpageToken = "";
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.plugin_recycle_show);
        mPluginPresenter = new PluginPresenterImpl2(this);
        initSlider();
        initRecycle();
        return view;
    }




    private void initRecycle() {
        mShowItem = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mPluginAdapter = new PluginAdapter(mContext, mShowItem);
        mRecyclerView.setAdapter(mPluginAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShowItem.size() == (lastVisibleItemPosition + 1) && hasMore) {
                    mPluginPresenter.getBodyData(nextpageToken);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });

    }

    private void initSlider() {
        //使用默认的指示器
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setDuration(5000);
    }

    @Override
    public void onDestroy() {
        mSliderLayout.stopAutoCycle();
        super.onDestroy();
    }

    @Override
    protected void initData() {

        mPluginPresenter.getHeadData();
        mPluginPresenter.getBodyData(nextpageToken);
    }


    @Override
    public void onGetData(final List<NewItemBean.ResultBean.ItemsBean> newsItems, final String nextPageToken) {

        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {

                nextpageToken = nextPageToken;
                mShowItem.addAll(newsItems);
                mPluginAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onHeadData(final HeaderBean.ResultBean result) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                refreshHeadData(result);
            }
        });

    }

    /**
     * 更新头部广告条的数据
     *
     * @param listData
     */
    private void refreshHeadData(HeaderBean.ResultBean listData) {
        List<HeaderBean.ResultBean.ItemsBean> items = listData.getItems();
        for (int i = 0; i < items.size(); i++) {
            final HeaderBean.ResultBean.ItemsBean itemsBean = items.get(i);
            TextSliderView textSliderView = new TextSliderView(mContext);
            textSliderView.image(itemsBean.getImg());
            textSliderView.description(itemsBean.getName());
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Intent intent = new Intent(mContext, NewsDetailActivity.class);
                    intent.putExtra(Constant.URL, itemsBean.getHref());
                    startActivity(intent);
                }
            });
            mSliderLayout.addSlider(textSliderView);
        }
    }
}
