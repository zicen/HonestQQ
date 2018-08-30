package com.example.lizhenquan.honestqq.view;

import com.example.lizhenquan.honestqq.model.HeaderBean;
import com.example.lizhenquan.honestqq.model.NewItemBean;

import java.util.List;

/**
 * Created by lizhenquan on 2017/2/18.
 */

public interface PluginView2 {

    void onGetData(List<NewItemBean.ResultBean.ItemsBean> items, String nextPageToken);

    void onHeadData(HeaderBean.ResultBean result);
}
