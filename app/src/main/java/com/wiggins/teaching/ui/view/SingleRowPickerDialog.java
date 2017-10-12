package com.wiggins.teaching.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.base.widget.NumberPickerView;
import com.wiggins.teaching.R;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *     author : jarylan
 *     e-mail : jarylan@foxmail.com
 *     time   : 2017/04/19
 *     desc   : 通用单列选择器 dialog
 *     version: 1.0
 * </pre>
 */
public class SingleRowPickerDialog extends Dialog {

    @BindView(R.id.tv_cancel_dialog)
    TextView tvCancel;
    @BindView(R.id.tv_confirm_dialog)
    TextView tvConfirm;
    @BindView(R.id.tv_title_dialog)
    TextView tvTitle;
    @BindView(R.id.fl_picker)
    FrameLayout frameLayoutPicker;

    NumberPickerView numberPicker;

    private Context mContext;
    private String title;
    private String[] datas;
    private String[] dataIds;
    private Map<String,String> dataIdMap = new ArrayMap<>();//key 为内容， value 为 id
    private int defaultIndex = 0;
    OnPickerChangeListener onPickerChangeListener;

    public SingleRowPickerDialog(@NonNull Context context, String title, String[] datas, String[] dataIds, String currentId, OnPickerChangeListener onPickerChangeListener) {
        super(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;
        this.title = title;
        this.datas = datas;
        this.dataIds = dataIds;
        this.onPickerChangeListener = onPickerChangeListener;
        if( currentId == null ){
            currentId = "";
        }
        for (int i = 0; i < datas.length; i++) {
            if( dataIds[i].equals(currentId) ){
                /* 计算 初始下标 */
                this.defaultIndex = i;
            }
              /* 初始化 数据源与id 的绑定关系 */
            dataIdMap.put(datas[i],dataIds[i]);
        }
    }

    public SingleRowPickerDialog(@NonNull Context context, String title, Map<String,String> map, String currentId, OnPickerChangeListener onPickerChangeListener) {
        super(context, R.style.ActionSheetDialogStyle);
        if( currentId == null ){
            currentId = "";
        }
        this.mContext = context;
        this.title = title;
        this.onPickerChangeListener = onPickerChangeListener;
        //将 map 转换成 id 跟 data 两个数组
        dataIds = new String[map.size()];
        datas = new String[map.size()];
        Set<String> keys = map.keySet();
        Iterator iterator = keys.iterator();
        int i = 0;
        while ( iterator.hasNext() ){
            String next = iterator.next() + "";
            dataIds[i] = next;
            datas[i] = map.get(next);
            dataIdMap.put(map.get(next),next);
            if( currentId.equals(next) ){
                this.defaultIndex = i;
            }
            i ++ ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picker);
        ButterKnife.bind(this);
        Window win = getWindow();
        win.setGravity(Gravity.BOTTOM);
        win.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        init();
    }

    private void init() {
        tvTitle.setText(title);
        frameLayoutPicker.addView(getPickerView(mContext));
        setNumberPicker(numberPicker,datas,defaultIndex);
    }

    @OnClick({R.id.tv_cancel_dialog,R.id.tv_confirm_dialog})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_cancel_dialog:
                break;
            case R.id.tv_confirm_dialog:
                if( numberPicker != null & onPickerChangeListener != null){
                    String content = numberPicker.getContentByCurrValue();
                    onPickerChangeListener.onPickerChange(content,dataIdMap.get(content),true);
                }
                break;
        }
        dismiss();
    }

    private void setNumberPicker(NumberPickerView picker, String[] datas, int index){
        picker.updateContentAndIndex(datas);
        picker.setMinValue(0);//下标从 "0" 开始算
        picker.setMaxValue(datas.length - 1);//下标结束值， max - min 不能大于 datas.length
        if(index >= datas.length){
            index = datas.length -1;
        }
        picker.setValue(index);
        picker.setOnValueChangeListenerInScrolling(new NumberPickerView.OnValueChangeListenerInScrolling() {
            @Override
            public void onValueChangeInScrolling(NumberPickerView picker, int oldVal, int newVal) {
                //下标  picker.getValue() ； 内容 picker.getContentByCurrValue()
                if(onPickerChangeListener != null){
                    String content = picker.getContentByCurrValue();
                    onPickerChangeListener.onPickerChange(content,dataIdMap.get(content),false);
                }
            }
        });
    }

    /**
     * 在任意要显示该控件的父容器 .addView(getCityPickerView()); 即可显示该选择器
     */
    private View getPickerView(Context context) {
        View view = View.inflate(context, R.layout.layout_single_row_picker, null);
        numberPicker = (NumberPickerView) view.findViewById(R.id.npv_layout);
        return view;
    }

    public interface OnPickerChangeListener{
        /**
         * @param confirm 是否确认
         */
        void onPickerChange(String content, String contentId, boolean confirm);
    }
}
