package com.example.lizhenquan.honestqq.presenter;

import com.example.lizhenquan.honestqq.cachemanager.LoadData;
import com.example.lizhenquan.honestqq.model.HeaderBean;
import com.example.lizhenquan.honestqq.model.NewItemBean;
import com.example.lizhenquan.honestqq.utils.ThreadUtils;
import com.example.lizhenquan.honestqq.utils.Urls;
import com.example.lizhenquan.honestqq.view.PluginView2;

/**
 * Created by lizhenquan on 2017/2/18.
 */

public class PluginPresenterImpl2 implements PluginPresenter2 {
    private final PluginView2 mPluginView;


    public PluginPresenterImpl2(PluginView2 pluginView) {
        this.mPluginView = pluginView;

    }


    @Override
    public void getBodyData(final String nextpage) {

        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {

                NewItemBean newItemBean = LoadData.getInstance().getBeanData(Urls.APIV2_BASE+"news?pageToken="+nextpage, NewItemBean.class);
                String nextPageToken = newItemBean.getResult().getNextPageToken();
                mPluginView.onGetData(newItemBean.getResult().getItems(),nextPageToken);
            }
        });


    }

    @Override
    public void getHeadData() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                HeaderBean beanData = LoadData.getInstance().getBeanData(Urls.APIV2_BASE + "banner?catalog=1", HeaderBean.class);
                mPluginView.onHeadData(beanData.getResult());
            }
        });
    }
}
