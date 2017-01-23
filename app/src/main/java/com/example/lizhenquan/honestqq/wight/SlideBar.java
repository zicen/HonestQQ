package com.example.lizhenquan.honestqq.wight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.adapter.SlideBarAdapter;
import com.example.lizhenquan.honestqq.utils.StringUtils;
import com.hyphenate.util.DensityUtil;

import java.util.List;

/**
 * Created by lizhenquan on 2017/1/19.
 */

public class SlideBar extends View {
    private static final String[] arr = {"搜", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private float mX;
    private float mY;
    private Paint mPaint;
    private TextView mTv_float;
    private RecyclerView mRecycleview_contact;

    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.parseColor("#9c9c9c"));
        int sp2px = DensityUtil.sp2px(context, 10);
        mPaint.setTextSize(sp2px);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        mX = measuredWidth / 2;
        int measuredHeight = getMeasuredHeight();
        mY = measuredHeight / arr.length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //改变背景，创建一个shape
                setBackgroundResource(R.drawable.slidebar_shape);
                //显示floatView,改变floatview的文字
                showFloatViewBySection(event.getY());

                break;
            case MotionEvent.ACTION_MOVE:
                showFloatViewBySection(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                //隐藏FloatView
                mTv_float.setVisibility(INVISIBLE);
                //背景设置为透明
                setBackgroundColor(Color.TRANSPARENT);
                break;

        }
        return true;
    }

    private void showFloatViewBySection(float height) {
        if (mTv_float == null) {
            ViewGroup parent = (ViewGroup) getParent();
            mTv_float = (TextView) parent.findViewById(R.id.tv_float);
            mRecycleview_contact = (RecyclerView) parent.findViewById(R.id.recycleview_contact);
        }
        mTv_float.setVisibility(VISIBLE);
        int index = (int) (height / mY);
        if (index < 0) {
            index = 0;
        } else if (index > arr.length - 1) {
            index = arr.length-1;
        }
        String section = arr[index];
        mTv_float.setText(section);
        //获取RecycleView中的所有数据
        RecyclerView.Adapter adapter = mRecycleview_contact.getAdapter();
        if (adapter instanceof SlideBarAdapter) {
            SlideBarAdapter slideBarAdapter = (SlideBarAdapter) adapter;
            List<String> dataList = slideBarAdapter.getData();
            for (int i = 0; i < dataList.size(); i++) {
                String contact = dataList.get(i);
                //遍历当前联系人的首字母是否等于当前FloatView上的字母
                String initial = StringUtils.getInitial(contact);
                if (initial.equals(section)) {
                    mRecycleview_contact.smoothScrollToPosition(i);
                    return;
                }
            }
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < arr.length; i++) {
            canvas.drawText(arr[i], mX, mY * (i + 1), mPaint);
        }
    }
}
