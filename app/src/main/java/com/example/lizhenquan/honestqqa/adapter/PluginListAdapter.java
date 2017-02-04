package com.example.lizhenquan.honestqqa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lizhenquan.honestqqa.R;

/**
 * Created by lizhenquan on 2017/1/18.
 */

public class PluginListAdapter extends BaseAdapter {

    private  Context mContext;
    private  String[] mDataTitle;
    private  int[] mDataImage;

    public PluginListAdapter(Context context, String[] title, int[] image) {
        this.mContext = context;
        this.mDataTitle = title;
        this.mDataImage  = image;
    }

    @Override
    public int getCount() {
        return mDataImage.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHodler hodler;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.plugin_list_item,null);
            hodler.iv = (ImageView) convertView.findViewById(R.id.plugin_iv_image);
            hodler.tv = (TextView) convertView.findViewById(R.id.plugin_tv_title);
            convertView.setTag(hodler);
        }
        hodler = (ViewHodler) convertView.getTag();


        hodler.iv.setImageResource(mDataImage[i]);
        hodler.tv.setText(mDataTitle[i]);
        return convertView;
    }
    class ViewHodler{
        ImageView iv;
        TextView tv;
    }
}
