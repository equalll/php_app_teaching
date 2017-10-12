package com.wiggins.teaching.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.util.KeyboardUtils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.adapter.NewsCommentAdapter;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.CommentListResult;
import com.wiggins.teaching.network.result.CommentListResult.DataBean.ListBean;
import com.wiggins.teaching.ui.base.BaseActivity;
import com.wiggins.teaching.utils.KeyboardChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;

import static com.wiggins.teaching.activity.EditInformationActivity.getEditTextString;
import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.REFRESH_ANIMATION;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;

/**
 * author ：wiggins on 2017/7/13 16:04
 * e-mail ：traywangjun@gmail.com
 * desc :  新闻评论
 * version :1.0
 */

public class CommentActivity extends BaseActivity {

    NewsCommentAdapter newsCommentAdapter;
    List<ListBean> listBean = new ArrayList<>();
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_comment_content)
    EditText et_comment_content;
    private LayoutInflater inflater;
    private TextView news_title, news_time, tv_dynamic_like_num;
    private String to_user_id = "", parent_id = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_comment;
    }

    @Override
    protected boolean isOpenSideSlip() {
        return true;
    }

    @Override
    protected void init() {
        inflater = LayoutInflater.from(getApplicationContext());

        initAdapter();

        getCommentInfo();

        et_comment_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    setComment();
                }
                return false;
            }
        });

        new KeyboardChangeListener(this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (!isShow) {
                    et_comment_content.setHint("");
                    et_comment_content.setText("");
                    to_user_id = "";
                    parent_id = "";
                }
            }
        });
    }

    @OnClick({R.id.ibtn_back, R.id.close_comment})
    void close() {
        finish();
    }

    @OnClick(R.id.btn_send)
    void send() {
        setComment();
    }

    private void initAdapter() {
        View head = inflater.inflate(
                R.layout.include_comment_title, null);
        news_title = head.findViewById(R.id.tv_news_title);
        news_time = head.findViewById(R.id.tv_news_time);
        tv_dynamic_like_num = head.findViewById(R.id.tv_dynamic_like_num);

        news_title.setText(getIntent().getStringExtra("title"));
        news_time.setText(getIntent().getStringExtra("time"));
        newsCommentAdapter = new NewsCommentAdapter(getApplicationContext(), listBean);
        newsCommentAdapter.addHeaderView(head);
        newsCommentAdapter.openLoadAnimation(REFRESH_ANIMATION);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(newsCommentAdapter);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                to_user_id = listBean.get(position).getUser_id();
                parent_id = listBean.get(position).getParent_id();
                et_comment_content.setHint("回复" + listBean.get(position).getUsername() + "：");
                et_comment_content.requestFocus();
                et_comment_content.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(et_comment_content, 0);
                    }
                }, 200);
            }
        });
    }

    /**
     * 评论列表
     */
    private void getCommentInfo() {
        Map<String, String> map = HttpRequestParam.createGetMap(false);
        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_COMMENT_LIST + getIntent().
                getStringExtra("id"), createHeader(), HttpTypeConstant.HTTP_TYPE_COMMENT_LIST, getRequestTag(), map);
        RetrofitManager.getInstance().request(body, new OnRequestCallBack<CommentListResult>() {
            @Override
            public void onSuccess(final CommentListResult tClass, String type) {
                List<ListBean> tempList = tClass.getData().getList();
                listBean = tempList;
                tv_dynamic_like_num.setText("全部评论（" + tempList.size() + "）");
                newsCommentAdapter.setNewData(tempList);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
            }
        });
    }

    /**
     * 评论
     */
    private void setComment() {
        Map<String, RequestBody> map = HttpRequestParam.createComment(getIntent().getStringExtra("id")
                , getEditTextString(et_comment_content), to_user_id, parent_id);
        HttpParamBody<RequestBody> body = new HttpParamBody<>(HttpUrlConstant.URL_COMMENT, createHeader(),
                HttpTypeConstant.HTTP_TYPE_COMMENT, getRequestTag(), map);
        RetrofitManager.getInstance().requestPost(body, new OnRequestCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject tClass, String type) {
                et_comment_content.setText("");
                KeyboardUtils.hideSoftInput(CommentActivity.this);
                getCommentInfo();
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }
}
