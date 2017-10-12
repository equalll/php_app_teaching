package com.wiggins.teaching.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wiggins.teaching.R;
import com.wiggins.teaching.network.result.CommentListResult.DataBean.ListBean;
import com.wiggins.teaching.ui.view.SelectableRoundedImageView;
import com.wiggins.teaching.utils.GlideUtils;

import java.util.List;


/**
 * author ：wiggins on 2017/4/14 15:56
 * e-mail ：traywangjun@gmail.com
 * desc :  新闻评论
 * version :1.0
 */

public class NewsCommentAdapter extends BaseQuickAdapter<ListBean, BaseViewHolder> {

    private Context mContext;

    public NewsCommentAdapter(Context mContext, List<ListBean> dataList) {
        super(R.layout.item_dynamic_detail_comment, dataList);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, ListBean item) {
        if (item.getTo_user_id().equals("0")) {
            helper.setText(R.id.iv_dynamic_comment_name, item.getUsername());
        } else {
            textAppend((TextView) helper.getView(R.id.iv_dynamic_comment_name), item.getUsername(), item.getTousername());
        }

        helper.setText(R.id.iv_dynamic_comment_content, item.getContent());
        helper.setText(R.id.iv_dynamic_comment_time, item.getCreate_time());
        GlideUtils.loadingAsRoundedImageView(mContext, item.getImage(), (SelectableRoundedImageView) helper.getView(R.id.iv_dynamic_comment_avatar));
    }

    /**
     * TextView 回复变黑色
     *
     * @param tv           需要改变的 TextView
     * @param username     发布者昵称
     * @param rootusername 被回复者昵称
     */
    private void textAppend(TextView tv, String username, String rootusername) {
        String name = username + "回复" + rootusername;
        SpannableString spanString = new SpannableString(name);
        ForegroundColorSpan span = new ForegroundColorSpan(ResourcesCompat.getColor(mContext.getResources(),
                R.color.gray_7c7c7c, null));
        spanString.setSpan(span, username.length(), name.length() - rootusername.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spanString);
    }

}
