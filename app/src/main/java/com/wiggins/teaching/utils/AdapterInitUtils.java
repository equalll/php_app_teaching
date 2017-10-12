package com.wiggins.teaching.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lib.base.util.ConvertUtils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.appconstant.AppConstant;
import com.wiggins.teaching.ui.view.CustomLoadMoreView;

/**
 * <pre>
 *     author : jarylan
 *     e-mail : jarylan@foxmail.com
 *     time   : 2017/06/20
 *     desc   : 统一初始化 列表类 Adapter 的一些共用配置
 *                  {@link com.chad.library.adapter.base.BaseQuickAdapter}
 *     version: 1.0
 * </pre>
 */
public class AdapterInitUtils {

    public static void initAdapter(Activity context, BaseQuickAdapter mAdapter,
                                   RecyclerView recyclerView){
        //设置加载更多样式
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        //设置加载更多动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //剩余多少条时预加载(默认总条数的一半)。在下拉刷新后获取服务器每页总条数再设置。
        mAdapter.setAutoLoadMoreSize(AppConstant.REMAINING_NUMBER_PRELOADING);
        /* 没有数据 , 网络错误   占位View */
        View emptyView = context.getLayoutInflater().inflate(R.layout.view_load_empty, (ViewGroup) recyclerView.getParent(), false);
        View errorView = context.getLayoutInflater().inflate(R.layout.view_load_error, (ViewGroup) recyclerView.getParent(), false);
        TextView tvErrorMsg = errorView.findViewById(R.id.tv_error_msg);
        mAdapter.setNoDataEmptyView(emptyView);
        mAdapter.setNetworkErrorEmptyView(errorView,tvErrorMsg);
    }

    public static void initAdapter(Activity context, BaseQuickAdapter mAdapter,
                                   RecyclerView recyclerView, int paddingTop){
        //设置加载更多样式
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        //设置加载更多动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //剩余多少条时预加载(默认总条数的一半)。在下拉刷新后获取服务器每页总条数再设置。
        mAdapter.setAutoLoadMoreSize(AppConstant.REMAINING_NUMBER_PRELOADING);
        /* 没有数据 , 网络错误   占位View */
        View emptyView = context.getLayoutInflater().inflate(R.layout.view_load_empty, (ViewGroup) recyclerView.getParent(), false);
        emptyView.findViewById(R.id.linear_empty).setPadding(0, ConvertUtils.dp2px(context, paddingTop), 0, 0);
        View errorView = context.getLayoutInflater().inflate(R.layout.view_load_error, (ViewGroup) recyclerView.getParent(), false);
        errorView.findViewById(R.id.linear_error_empty).setPadding(0, ConvertUtils.dp2px(context, paddingTop), 0, 0);
        TextView tvErrorMsg = errorView.findViewById(R.id.tv_error_msg);
        mAdapter.setNoDataEmptyView(emptyView);
        mAdapter.setNetworkErrorEmptyView(errorView,tvErrorMsg);
    }
}
