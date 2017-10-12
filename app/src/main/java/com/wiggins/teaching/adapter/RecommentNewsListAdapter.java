package com.wiggins.teaching.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wiggins.teaching.R;
import com.wiggins.teaching.network.result.RecommentNewsListResult.DataBean;
import com.wiggins.teaching.utils.GlideUtils;

import java.util.List;


/**
 * author ：wiggins on 2017/4/14 15:56
 * e-mail ：traywangjun@gmail.com
 * desc : 推荐新闻列表
 * version :1.0
 */

public class RecommentNewsListAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {

    private Context mContext;

    public RecommentNewsListAdapter(Context mContext, List<DataBean> dataList) {
        super(R.layout.item_list_news, dataList);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        helper.setText(R.id.tv_news_title, item.getTitle());
        helper.setText(R.id.tv_news_readnum, item.getRead_count() + "人看过");
        GlideUtils.loadImageViewLoading(mContext, item.getImage(), (ImageView) helper.getView(R.id.iv_news_photo));
    }


}
