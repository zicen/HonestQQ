package com.example.lizhenquan.honestqq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lizhenquan.honestqq.R;
import com.example.lizhenquan.honestqq.model.NewItemBean;
import com.example.lizhenquan.honestqq.utils.ColorfulTextViewUtil;
import com.example.lizhenquan.honestqq.utils.Constant;
import com.example.lizhenquan.honestqq.utils.StringUtils;
import com.example.lizhenquan.honestqq.utils.ToastUtils;
import com.example.lizhenquan.honestqq.view.activity.NewsDetailActivity;
import com.hyphenate.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.lizhenquan.honestqq.R.id.tv_comment_count;
import static com.example.lizhenquan.honestqq.R.id.tv_time;

/**
 * Created by lizhenquan on 2017/3/25.
 */

public class PluginAdapter extends RecyclerView.Adapter<PluginAdapter.MyViewHolder> {
    private  List<NewItemBean.ResultBean.ItemsBean> mShowItem = new ArrayList<>();
    private Context mContext;
    public PluginAdapter(Context context, List<NewItemBean.ResultBean.ItemsBean> showItem) {
        this.mContext = context;
        this.mShowItem = showItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_news, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NewItemBean.ResultBean.ItemsBean itemsBean = mShowItem.get(position);
        holder.mTitleView.setText(itemsBean.getTitle());
        holder.mCommentCount.setText(itemsBean.getCommentCount()+"");
        holder.mDescriptionView.setText(itemsBean.getBody());
        holder.mTime.setText(StringUtils.getDate(itemsBean.getPubDate()));

        //根据上传时间判断是否 标题中添加"今"
        //根据上传时间判断是否 标题中添加"今"
        if (ColorfulTextViewUtil.publicDataIsToday(itemsBean.getPubDate())) {
            //填图"今"
            SpannableString spannableString = ColorfulTextViewUtil.addTagWord(itemsBean.getTitle(), R.mipmap.ic_item_today);
            //标题字体大小
            spannableString.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(mContext, 16)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mTitleView.setText(spannableString);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(mContext,"tiaomubei dianji e");

                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra(Constant.URL,itemsBean.getHref());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mShowItem.size()==0?0:mShowItem.size();
    }
    class MyViewHolder extends  RecyclerView.ViewHolder{


         TextView mTitleView;
         TextView mDescriptionView;
         TextView mTime;
         TextView mCommentCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
            mDescriptionView = (TextView) itemView.findViewById(R.id.tv_description);
            mTime = (TextView) itemView.findViewById(tv_time);
            mCommentCount = (TextView) itemView.findViewById(tv_comment_count);
        }
    }

}
