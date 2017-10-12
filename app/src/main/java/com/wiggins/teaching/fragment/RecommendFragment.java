package com.wiggins.teaching.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.util.ConvertUtils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.activity.NewsDetailActivity;
import com.wiggins.teaching.adapter.RecommentNewsListAdapter;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.RecommentNewsListResult;
import com.wiggins.teaching.network.result.RecommentNewsListResult.DataBean;
import com.wiggins.teaching.ui.base.BaseFragment;
import com.wiggins.teaching.ui.base.CSwipeRefreshLayout;
import com.wiggins.teaching.utils.AdapterInitUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;


/**
 * 作者：wiggins on 2017/4/13 09:36
 * 邮箱：traywangjun@gmail.com
 */

public class RecommendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.onClickEmptyViewListener {

    private RecommentNewsListAdapter recommentNewsListAdapter;
    private ArrayList<DataBean> newsList = new ArrayList<>();
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    private View noMoreDataFootView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getLayout() {
        return R.layout.fragment_recommend;
    }

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        initNewsAdapter();
        refresh();
    }

    @Override
    public void backKeyEvent() {

    }

    /**
     * 推荐
     */
    private void getRecommentInfo() {
        Map<String, String> map = HttpRequestParam.createGetMap(false);
        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_NEWS_DETAIL_RECOMMENT, createHeader(),
                HttpTypeConstant.HTTP_TYPE_NEWSDETAIL_RECOMMENT, getRequestTag(), map);
        RetrofitManager.getInstance().request(body, new OnRequestCallBack<RecommentNewsListResult>() {
            @Override
            public void onSuccess(final RecommentNewsListResult tClass, String type) {
                setRefreshOtherEnable(true);
                List<DataBean> tempList = tClass.getData();
                recommentNewsListAdapter.setNewData(tempList);
                newsList.addAll(tempList);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                setRefreshOtherEnable(true);
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }

    private void initNewsAdapter() {
        recommentNewsListAdapter = new RecommentNewsListAdapter(getContext(), newsList);
        AdapterInitUtils.initAdapter(getActivity(), recommentNewsListAdapter, mRecyclerView);
        recommentNewsListAdapter.setOnLoadMoreListener(this, mRecyclerView);//设置加载更多监听
        recommentNewsListAdapter.setOnClickEmptyViewListener(this);//设置点击 网络异常占位布局监听
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(recommentNewsListAdapter);
        recommentNewsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("id",
                        newsList.get(position).getId()));
            }
        });
    }

    /**
     * 服务器的数据都加载完了，显示“没有更多数据”
     *
     * @param show true 添加显示，false 去掉显示
     */
    private void setNoMoreData(boolean show) {
        if (!show) {
            recommentNewsListAdapter.removeAllFooterView();
            recommentNewsListAdapter.setEnableLoadMore(true);
            return;
        }
        recommentNewsListAdapter.setEnableLoadMore(false);
        if (noMoreDataFootView == null) {
            noMoreDataFootView = View.inflate(getContext(), R.layout.view_load_more, null);
            noMoreDataFootView.setPadding(0, ConvertUtils.dp2px(getContext(), 15), 0, ConvertUtils.dp2px(getContext(), 15));
            noMoreDataFootView.findViewById(R.id.load_more_loading_view).setVisibility(View.GONE);
            noMoreDataFootView.findViewById(R.id.load_more_load_fail_view).setVisibility(View.GONE);
            noMoreDataFootView.findViewById(R.id.load_more_load_end_view).setVisibility(View.VISIBLE);
        }
        recommentNewsListAdapter.addFooterView(noMoreDataFootView);
    }

    /**
     * 刷新数据
     */
    private void refresh() {
        if (!isDetached()) {
            setRefreshOtherEnable(false);
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setNoMoreData(false);//刷新时去掉“没有更多数据”
                    getRecommentInfo();
                }
            }, CSwipeRefreshLayout.DELAY_MILLIS);
        }
    }

    private void setRefreshOtherEnable(boolean enable) {
        mSwipeRefreshLayout.setRefreshing(!enable);
        recommentNewsListAdapter.setEnableLoadMore(enable);
    }

    /**
     * 上拉加载时， 屏蔽一些控件
     */
    private void setLoadMoreOtherEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    @Override
    public void onClickNetErrorEmptyView() {
        refresh();
    }

    @Override
    public void onRefresh() {
        //TODO 发起网络请求
        refresh();
    }

    @Override
    public void onLoadMoreRequested() {
        setLoadMoreOtherEnable(false);
        getRecommentInfo();
    }

}
