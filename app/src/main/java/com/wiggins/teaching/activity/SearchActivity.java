package com.wiggins.teaching.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.util.ConvertUtils;
import com.lib.base.util.KeyboardUtils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.adapter.NewsListAdapter;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.NewsListResult;
import com.wiggins.teaching.network.result.NewsListResult.DataBean.ListBean;
import com.wiggins.teaching.ui.base.BaseActivity;
import com.wiggins.teaching.utils.AdapterInitUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lib.base.util.Utils.getContext;
import static com.wiggins.teaching.activity.EditInformationActivity.getEditTextString;
import static com.wiggins.teaching.activity.MainActivity.createHeader;

/**
 * author ：wiggins on 2017/7/13 16:04
 * e-mail ：traywangjun@gmail.com
 * desc : 搜索
 * version :1.0
 */

public class SearchActivity extends BaseActivity implements
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.onClickEmptyViewListener {

    private NewsListAdapter newsListlAdapter;
    private ArrayList<ListBean> newsList = new ArrayList<>();
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_search_cn_et)
    EditText mEditText;
    private View noMoreDataFootView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_search_condition;
    }

    @Override
    protected boolean isOpenSideSlip() {
        return true;
    }

    @Override
    protected void init() {

        initNewsAdapter();

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    refresh();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.ibtn_back)
    void close() {
        finish();
    }


    @OnClick(R.id.id_search_iv_clear)
    void clean() {
        mEditText.setText("");
    }

    /**
     * 获取新闻
     */
    private void getNewsInfo() {
        Map<String, String> map = HttpRequestParam.createNewsList(getEditTextString(mEditText));
        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_NEWS, createHeader(),
                HttpTypeConstant.HTTP_TYPE_NEWS, getRequestTag(), map);
        RetrofitManager.getInstance().request(body, new OnRequestCallBack<NewsListResult>() {
            @Override
            public void onSuccess(NewsListResult tClass, String type) {
                setRefreshOtherEnable(true);
                KeyboardUtils.hideSoftInput(SearchActivity.this);
                List<ListBean> tempList = tClass.getData().getList();
                newsList.clear();
                if (tempList.size() == 0) {//说明没有数据，需显示没有数据占位图
                    newsListlAdapter.showNoDataEmptyView();
                }
                newsListlAdapter.setNewData(tempList);
                newsList.addAll(tempList);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                newsList.clear();
                newsListlAdapter.setNewData(newsList);
                newsListlAdapter.showNetworkErrorEmptyView(errorMsg);
                setRefreshOtherEnable(true);
            }
        });
    }


    private void initNewsAdapter() {
        newsListlAdapter = new NewsListAdapter(getContext(), newsList);
        AdapterInitUtils.initAdapter(SearchActivity.this, newsListlAdapter, mRecyclerView);
        newsListlAdapter.setOnClickEmptyViewListener(this);//设置点击 网络异常占位布局监听
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(newsListlAdapter);
        newsListlAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(SearchActivity.this, NewsDetailActivity.class).putExtra("id",
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


    /**
     * 刷新数据
     */
    private void refresh() {
        setNoMoreData(false);//刷新时去掉“没有更多数据”
        getNewsInfo();
    }

    private void setRefreshOtherEnable(boolean enable) {
        newsListlAdapter.setEnableLoadMore(enable);
    }


    @Override
    public void onClickNetErrorEmptyView() {
        refresh();
    }

    @Override
    public void onLoadMoreRequested() {
        getNewsInfo();
    }
}
