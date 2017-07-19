package com.example.lizhenquan.honestqq.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.utils.ThreadUtils;

import java.util.List;

/**
 * @author Maj1nBuu
 * @data 2017/3/1 20:12.
 * @overView ${TODO}
 */

public abstract class LoadPager extends FrameLayout {

    private View mLoading;
    private View mError;
    private View mSuccess;
    private LoadStateUtil currentState = LoadStateUtil.LOADING;

    public LoadPager(Context context) {
        this(context, null);
    }

    public LoadPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPage();
    }

    private void initPage() {
        if (mLoading != null) {
            mLoading = View.inflate(getContext(), R.layout.loadanimation_detail, null);
            //加载动画
            loadAnimation();
        } else if (mError != null) {
            //失败界面
            mError = View.inflate(getContext(), R.layout.detail_page_error, null);
        } else if (mSuccess != null) {
            //成功界面
            mSuccess = createSuccessView();
            if (mSuccess == null) {
                throw new RuntimeException("小白来个界面吧!");
            }
        }

        //加入到布局
        addView(mLoading);
        addView(mError);
        addView(mSuccess);

        //切换页面
        changeView(currentState);

        //根据网络数据自动切换ui
        showPage();
    }

    private void showPage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object data = getNetData();
                //判断数据
                currentState = checkData(data);
                //当前的状态,去切换页面,主线程
                ThreadUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        changeView(currentState);
                    }
                });
            }
        });
    }

    protected LoadStateUtil checkData(Object data) {
        if (data == null) {
            return LoadStateUtil.ERROR;
        } else {
            if (data instanceof List) {
                List items = (List) data;
                if (items.size() > 0) {
                    return LoadStateUtil.SUCCESS;
                } else {
                    return LoadStateUtil.ERROR;
                }
            } else {
                return LoadStateUtil.SUCCESS;
            }
        }
    }

    protected abstract Object getNetData();

    private void changeView(LoadStateUtil currentState) {
        //先全部隐藏
        mLoading.setVisibility(GONE);
        mError.setVisibility(GONE);
        mSuccess.setVisibility(GONE);
        //后根据状态显示对应的界面
        switch (currentState) {
            case LOADING:
                //加载 中
                mLoading.setVisibility(VISIBLE);
                break;
            case ERROR:
                //加载 中
                mError.setVisibility(VISIBLE);
                break;
            case SUCCESS:
                //加载 中
                mSuccess.setVisibility(VISIBLE);
                break;

            default:
                break;
        }
    }

    protected abstract void loadAnimation();

    public abstract View createSuccessView();


}
