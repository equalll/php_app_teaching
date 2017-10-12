package com.wiggins.teaching.activity;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.util.KeyboardUtils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.adapter.RecommentNewsListAdapter;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.GetPraiseResult;
import com.wiggins.teaching.network.result.NewsDetailResult;
import com.wiggins.teaching.network.result.RecommentNewsListResult;
import com.wiggins.teaching.network.result.RecommentNewsListResult.DataBean;
import com.wiggins.teaching.ui.base.BaseActivity;
import com.wiggins.teaching.ui.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;

import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;

/**
 * author ：wiggins on 2017/7/13 16:04
 * e-mail ：traywangjun@gmail.com
 * desc :  新闻详情
 * version :1.0
 */

public class NewsDetailActivity extends BaseActivity {

    private RecommentNewsListAdapter recommentNewsListAdapter;
    private ArrayList<DataBean> newsList = new ArrayList<>();
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.web_activities)
    WebView webView;
    @BindView(R.id.news_title)
    TextView newsTitle;
    @BindView(R.id.news_time)
    TextView newsTime;
    @BindView(R.id.open_comment)
    TextView openComment;
    @BindView(R.id.btn_praise)
    TextView btnPraise;
    @BindView(R.id.et_comment_content)
    TextView etCommentContent;
    private int isPriase;
    private String title, time;

    @Override
    protected int getLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected boolean isOpenSideSlip() {
        return true;
    }

    @Override
    protected void init() {

        initAdapter();

        getNewsDetail();

        getPriaseInfo();
    }

    @OnClick(R.id.ibtn_back)
    void close() {
        finish();
    }

    @OnClick({R.id.open_comment, R.id.et_comment_content})
    void openComment() {
        startActivity(new Intent(getApplicationContext(), CommentActivity.class).putExtra("title", title)
                .putExtra("time", time).putExtra("id", getIntent().getStringExtra("id")));
    }

    @OnClick(R.id.btn_praise)
    void praise() {
        if (isPriase == 1) {
            canclePriase();
        } else {
            setPriase();
        }
    }

    private void initAdapter() {
        recommentNewsListAdapter = new RecommentNewsListAdapter(getApplicationContext(), newsList);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        mRecyclerView.setAdapter(recommentNewsListAdapter);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!KeyboardUtils.keyBoardState(getApplicationContext(), view)) {
                    startActivity(new Intent(getApplicationContext(), NewsDetailActivity.class).putExtra("id",
                            newsList.get(position).getId()));
                } else {
                    KeyboardUtils.hideSoftInput(getApplicationContext(), view);
                }
            }
        });

    }

    /**
     * 新闻详情
     */
    private void getNewsDetail() {
        showLoadingDialog("正在加载...");
        Map<String, String> map = HttpRequestParam.createGetMap(false);

        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_NEWS_DETAIL + getIntent().
                getStringExtra("id"), createHeader(), HttpTypeConstant.HTTP_TYPE_NEWSDETAIL,
                getRequestTag(), map);

        RetrofitManager.getInstance().request(body, new OnRequestCallBack<NewsDetailResult>() {
            @Override
            public void onSuccess(NewsDetailResult tClass, String type) {
                title = tClass.getData().getTitle();
                time = tClass.getData().getCreate_time() + "  " + tClass.getData().getRead_count() + "人看过";
                newsTitle.setText(title);
                newsTime.setText(time);
                openComment.setText(String.valueOf(tClass.getData().getComment_count()));
                btnPraise.setText(String.valueOf(tClass.getData().getUpvote_count()));
                webView.loadDataWithBaseURL(null, tClass.getData().getContent(), "text/html", "utf-8", null);
                dismissLoadingDialog();

            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                dismissLoadingDialog();
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
        getRecommentInfo();
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
                List<DataBean> tempList = tClass.getData();
                recommentNewsListAdapter.setNewData(tempList);
                newsList.addAll(tempList);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
            }
        });
    }

    /**
     * 获取是否点赞
     */
    private void getPriaseInfo() {
        Map<String, String> map = HttpRequestParam.createGetMap(false);
        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_NEWS_NEWSDETAIL_UPVOTE + getIntent().
                getStringExtra("id"), createHeader(), HttpTypeConstant.HTTP_TYPE_NEWSDETAIL_UPVOTE, getRequestTag(), map);
        RetrofitManager.getInstance().request(body, new OnRequestCallBack<GetPraiseResult>() {
            @Override
            public void onSuccess(final GetPraiseResult tClass, String type) {
                isPriase = tClass.getData().getIsUpVote();
                if (tClass.getData().getIsUpVote() == 1) {
                    btnPraise.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(
                            getResources(), R.drawable.ic_favorite_red_24dp, null), null, null, null);
                }
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
            }
        });
    }

    /**
     * 点赞
     */
    private void setPriase() {
        Map<String, RequestBody> map = HttpRequestParam.createSetPraise(getIntent().getStringExtra("id"));
        HttpParamBody<RequestBody> body = new HttpParamBody<>(HttpUrlConstant.URL_NEWS_UPVOTE, createHeader(),
                HttpTypeConstant.HTTP_TYPE_UPVOTE, getRequestTag(), map);
        RetrofitManager.getInstance().requestPost(body, new OnRequestCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject tClass, String type) {
                isPriase = 1;
                btnPraise.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(
                        getResources(), R.drawable.ic_favorite_red_24dp, null), null, null, null);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }

    /**
     * 取消点赞
     */
    private void canclePriase() {
        Map<String, String> map = HttpRequestParam.createCanclePraise(getIntent().getStringExtra("id"));
        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_NEWS_UPVOTE, createHeader(),
                HttpTypeConstant.HTTP_TYPE_CANCLE_UPVOTE, getRequestTag(), map);
        RetrofitManager.getInstance().requestDelete(body, new OnRequestCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject tClass, String type) {
                isPriase = 0;
                btnPraise.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(
                        getResources(), R.drawable.ic_favorite_black_24dp, null), null, null, null);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }
}
