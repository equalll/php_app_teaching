package com.wiggins.teaching.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wiggins.teaching.R;


/**
 * <pre>
 *     author : jarylan
 *     e-mail : jarylan@foxmail.com
 *     time   : 2017/05/12
 *     desc   : Dialog 工具
 *     version: 1.0
 * </pre>
 */
public class DialogUtils {

    /**
     * 通用进度弹窗
     * */
    public static Dialog showLoading(Context context, String tip){
        Dialog dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        View view =  View.inflate(context, R.layout.dialog_progress, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_dialog_msg);
        if(TextUtils.isEmpty(tip)){
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(tip);
        }
        dialog.setContentView(view);
        return dialog;
    }

    /**
     * 通用进度弹窗
     * */
    public static Dialog showLoading(Context context, @StringRes int tip){
        Dialog dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        View view =  View.inflate(context, R.layout.dialog_progress, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_dialog_msg);
        if( tip == 0 ){
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(tip);
        }
        dialog.setContentView(view);
        return dialog;
    }

}
