package com.wiggins.teaching.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.util.ConvertUtils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.activity.NewsDetailActivity;
import com.wiggins.teaching.adapter.IndexListlAdapter;
import com.wiggins.teaching.adapter.NewsListAdapter;
import com.wiggins.teaching.appconstant.AppConstant;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.IndexListResult;
import com.wiggins.teaching.network.result.IndexListResult.DataBean.HeadsBean;
import com.wiggins.teaching.network.result.IndexListResult.DataBean.PositionsBean;
import com.wiggins.teaching.network.result.NewsListResult;
import com.wiggins.teaching.network.result.NewsListResult.DataBean.ListBean;
import com.wiggins.teaching.ui.base.BaseFragment;
import com.wiggins.teaching.ui.base.CSwipeRefreshLayout;
import com.wiggins.teaching.ui.view.banners.events.OnBannerClickListener;
import com.wiggins.teaching.ui.view.banners.views.BannerSlider;
import com.wiggins.teaching.utils.AdapterInitUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.FIRST_PAGE;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;


/**
 * 作者：wiggins on 2017/4/13 09:36
 * 邮箱：traywangjun@gmail.com
 */

public class NewsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.onClickEmptyViewListener {


    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private IndexListlAdapter indexListlAdapter;
    private NewsListAdapter newsListlAdapter;
    private ArrayList<PositionsBean> indexList = new ArrayList<>();
    private ArrayList<ListBean> newsList = new ArrayList<>();
    private BannerSlider bannerSlider;
    private LayoutInflater inflater;
    private String catid;
    private int currentPage = FIRST_PAGE;
    private View noMoreDataFootView;


    @Override
    protected int getLayout() {
        return R.layout.fragment_newslist;
    }

    public static NewsListFragment newInstance() {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init() {
        super.init();
        inflater = LayoutInflater.from(getContext());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        catid = String.valueOf(getArguments().getInt("catid"));
        if (getArguments().getInt("position") == 0) {
            initAdapter();
        } else {
            initNewsAdapter();
        }
        refresh();
    }


    /**
     * 首页
     */
    private void getIndexInfo() {
        Map<String, String> map = HttpRequestParam.createGetMap(false);
        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_INDEX, createHeader(),
                HttpTypeConstant.HTTP_TYPE_INDEX, getRequestTag(), map);
        RetrofitManager.getInstance().request(body, new OnRequestCallBack<IndexListResult>() {
            @Override
            public void onSuccess(final IndexListResult tClass, String type) {
                setRefreshOtherEnable(true);
                List<PositionsBean> tempList = tClass.getData().getPositions();
                addBanners(tClass.getData().getHeads());
                bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void onClick(int position) {
                        startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("id",
                                tClass.getData().getHeads().get(position).getId()));
                    }
                });
                indexListlAdapter.setNewData(tempList);
                indexList.addAll(tempList);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                setRefreshOtherEnable(true);
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }

    /**
     * 获取新闻
     */
    private void getNewsInfo(int page) {
        Map<String, String> map = HttpRequestParam.createNewsList(page, catid, "6");
        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_NEWS, createHeader(),
                HttpTypeConstant.HTTP_TYPE_NEWS, getRequestTag(), map);
        RetrofitManager.getInstance().request(body, new OnRequestCallBack<NewsListResult>() {
            @Override
            public void onSuccess(NewsListResult tClass, String type) {
                setRefreshOtherEnable(true);
                List<ListBean> tempList = tClass.getData().getList();
                if (currentPage == AppConstant.FIRST_PAGE) {//第一页说明是下拉刷新,需要先清空数据
                    newsList.clear();
                    setRefreshOtherEnable(true);
                    if (tempList.size() == 0) {//说明没有数据，需显示没有数据占位图
                        newsListlAdapter.showNoDataEmptyView();
                    } else {
                        newsListlAdapter.setAutoLoadMoreSize(tempList.size() / 2);//设置剩余多少条事预加载，为总条数的一半
                    }
                    newsListlAdapter.setNewData(tempList);
                } else {//加载更多
                    newsListlAdapter.addData(tempList);
                    newsListlAdapter.loadMoreComplete();
                    setLoadMoreOtherEnable(true);
                }
                newsList.addAll(tempList);
                if (currentPage != AppConstant.FIRST_PAGE && tClass.getData().getList().size() == 0) {//非第一页且获取的数据为0 ，说明没有更多数据了
                    setNoMoreData(true);
                    return;
                }
                currentPage += 1;//记录当前页数

            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                if (currentPage == AppConstant.FIRST_PAGE) {//第一页，需要显示网络错误占位图
                    newsList.clear();
                    newsListlAdapter.setNewData(newsList);
                    newsListlAdapter.showNetworkErrorEmptyView(errorMsg);
                    setRefreshOtherEnable(true);
                } else {//加载更多
                    newsListlAdapter.loadMoreComplete();
                    setLoadMoreOtherEnable(true);
                }
            }
        });
    }

    private void initAdapter() {
        indexListlAdapter = new IndexListlAdapter(getContext(), indexList);
        indexListlAdapter.setOnClickEmptyViewListener(this);//设置点击 网络异常占位布局监听
        indexListlAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(indexListlAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("id",
                        indexList.get(position).getId()));
            }
        });
        setupBannerSlider();
    }

    private void initNewsAdapter() {
        newsListlAdapter = new NewsListAdapter(getContext(), newsList);
        AdapterInitUtils.initAdapter(getActivity(), newsListlAdapter, mRecyclerView);
        newsListlAdapter.setOnLoadMoreListener(this, mRecyclerView);//设置加载更多监听
        newsListlAdapter.setOnClickEmptyViewListener(this);//设置点击 网络异常占位布局监听
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(newsListlAdapter);
        newsListlAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
            newsListlAdapter.removeAllFooterView();
            newsListlAdapter.setEnableLoadMore(true);
            return;
        }
        newsListlAdapter.setEnableLoadMore(false);
        if (noMoreDataFootView == null) {
            noMoreDataFootView = View.inflate(getContext(), R.layout.view_load_more, null);
            noMoreDataFootView.setPadding(0, ConvertUtils.dp2px(getContext(), 15), 0, ConvertUtils.dp2px(getContext(), 15));
            noMoreDataFootView.findViewById(R.id.load_more_loading_view).setVisibility(View.GONE);
            noMoreDataFootView.findViewById(R.id.load_more_load_fail_view).setVisibility(View.GONE);
            noMoreDataFootView.findViewById(R.id.load_more_load_end_view).setVisibility(View.VISIBLE);
        }
        newsListlAdapter.addFooterView(noMoreDataFootView);
    }

    View head;

    private void setupBannerSlider() {
        head = inflater.inflate(R.layout.include_banner, null);
        bannerSlider = head.findViewById(R.id.banner_slider);
        indexListlAdapter.addHeaderView(head);
    }

    private void addBanners(List<HeadsBean> heads) {
        bannerSlider.addBanners(heads);
    }

    @Override
    public void backKeyEvent() {

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
                    if (getArguments().getInt("position") == 0) {
                        getIndexInfo();
                    } else {
                        currentPage = FIRST_PAGE;
                        setNoMoreData(false);//刷新时去掉“没有更多数据”
                        getNewsInfo(currentPage);
                    }
                }
            }, CSwipeRefreshLayout.DELAY_MILLIS);
        }
    }

    private void setRefreshOtherEnable(boolean enable) {
        mSwipeRefreshLayout.setRefreshing(!enable);
        if (getArguments().getInt("position") == 0) {
            indexListlAdapter.setEnableLoadMore(enable);
        } else {
            newsListlAdapter.setEnableLoadMore(enable);
        }
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
        getNewsInfo(currentPage);
    }
}
