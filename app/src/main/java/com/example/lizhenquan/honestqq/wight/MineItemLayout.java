package com.example.lizhenquan.honestqq.wight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;


/**
 * Created by lizhenquan on 2017/2/27.
 */

public class MineItemLayout extends LinearLayout {
    public MineItemLayout(Context context) {
        this(context,null);
    }

    public MineItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.ll_mine_item, null);
        this.addView(view);
        TextView tv_mine_item = (TextView) view
                .findViewById(R.id.tv_mine_item);
        ImageView iv_mine_item = (ImageView) view.findViewById(R.id.iv_mine_item);
        //获取xml中设置属性值，动态设置给文本控件
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MineItemLayout);
        String title = attributes.getString(R.styleable.MineItemLayout_minetitle);
        Drawable drawable = attributes.getDrawable(R.styleable.MineItemLayout_image);
        iv_mine_item.setImageDrawable(drawable);
        tv_mine_item.setText(title);

    }



}
