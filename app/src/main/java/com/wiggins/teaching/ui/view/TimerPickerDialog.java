package com.wiggins.teaching.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wiggins.teaching.R;
import com.wiggins.teaching.ui.view.timepicker.Type;
import com.wiggins.teaching.ui.view.timepicker.WheelTime;

import java.util.Calendar;

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
public class TimerPickerDialog extends Dialog {

    @BindView(R.id.tv_title_dialog)
    TextView tvTitle;
    @BindView(R.id.timepicker)
    LinearLayout timepicker;

    private Context mContext;
    private String title;
    private OnPickerChangeListener onPickerChangeListener;
    private int defaultYear ;
    private int defaultMonth = 0;
    private int defaultDay = 1;
    private WheelTime wheelTime;

    public TimerPickerDialog(@NonNull Context context, String title, int year, int month, int day, OnPickerChangeListener onPickerChangeListener) {
        super(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;
        this.title = title;
        this.defaultYear = year;
        this.defaultMonth = month;
        this.defaultDay = day;
        this.onPickerChangeListener = onPickerChangeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_timer_picker);
        ButterKnife.bind(this);
        Window win = getWindow();
        win.setGravity(Gravity.BOTTOM);
        win.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        init();
    }

    private void init() {
        tvTitle.setText(title);
        initWheelTime(timepicker);
    }

    @OnClick({R.id.tv_cancel_dialog,R.id.tv_confirm_dialog})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_cancel_dialog:
                break;
            case R.id.tv_confirm_dialog:
                if(null != onPickerChangeListener){
                    onPickerChangeListener.onPickerChange(wheelTime.getTimeYMD(),true);
                }
                break;
        }
        dismiss();
    }

    View initWheelTime(View view) {
        wheelTime = new WheelTime(view.findViewById(R.id.timepicker), Type.YEAR_MONTH_DAY, Gravity.CENTER, 20);
        final int year, month, day, hours, minute, seconds;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        year = calendar.get(Calendar.YEAR);
        int endYear = year - 18;
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        seconds = calendar.get(Calendar.SECOND);
        wheelTime.setStartYear(1957);
        wheelTime.setEndYear(endYear);
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar startDate = Calendar.getInstance();
        startDate.set(1957, 0, 31);
        Calendar endDate = Calendar.getInstance();
        endDate.set(endYear, month, day);
        wheelTime.setRangDate(startDate, endDate);
        wheelTime.setPicker(defaultYear, defaultMonth - 1, defaultDay, hours, minute, seconds);
        wheelTime.setLabels(getLabelString(R.string.hint_year), getLabelString(R.string.hint_month), getLabelString(R.string.hint_day),
                getLabelString(R.string.hint_hour), getLabelString(R.string.hint_minute), getLabelString(R.string.hint_second));
        wheelTime.setCyclic(true);
        wheelTime.setYearCyclic(false);
//        wheelTime.setLineSpacingMultiplier(1.6F);
        wheelTime.isCenterLabel(false);
        wheelTime.setOnTimeChangeListener(new WheelTime.OnTimeChangeListener() {
            @Override
            public void onTimeChange(String time) {
                if(null != onPickerChangeListener){
                    onPickerChangeListener.onPickerChange(time,false);
                }
            }
        });
        return view;
    }

    private String getLabelString(int resId) {
        return mContext.getResources().getString(resId);
    }

    public interface OnPickerChangeListener{
        /**
         * @param time 1991-2-28
         * @param confirm 是否确认
         */
        void onPickerChange(String time, boolean confirm);
    }
}
