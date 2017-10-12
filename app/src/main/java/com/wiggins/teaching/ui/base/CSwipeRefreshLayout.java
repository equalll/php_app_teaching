package com.wiggins.teaching.ui.base;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.wiggins.teaching.R;


/**
 * <pre>
 *     作者：wiggins on 2017/4/13 09:36
 *     邮箱：traywangjun@gmail.com
 *     desc   : 继承原生的刷新控件
 *     version: 1.0
 * </pre>
 */
public class CSwipeRefreshLayout extends SwipeRefreshLayout {
    public static final int DELAY_MILLIS = 500;

    public CSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeColors(ResourcesCompat.getColor(getResources(), R.color.dialog_blue, null));

    }

}
