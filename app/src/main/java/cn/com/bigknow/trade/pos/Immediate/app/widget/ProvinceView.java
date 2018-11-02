package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.bigknow.trade.pos.Immediate.R;

/**
 * Created by hujie on 16/10/23.
 */

public class ProvinceView extends LinearLayout {


    private String[] porviceItems = {"京", "津", "冀", "渝", "川", "贵", "云", "藏", "粤", "桂", "青", "宁", "新", "陕", "甘", "晋", "蒙", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "琼"};
    private String[] letterItems = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String[] letterNumItems = {"1","2","3","4","5","6","7","8","9","0","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private String[] items;


    GridLayout gridLayout;
    boolean hasViewSW = false;

    OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public ProvinceView(Context context) {
        super(context);
        init();
    }

    public ProvinceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProvinceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProvinceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        gridLayout = new GridLayout(getContext());
        addView(gridLayout);
        gridLayout.setColumnCount(5);
        gridLayout.getLayoutParams().width = GridLayout.LayoutParams.MATCH_PARENT;
        gridLayout.getLayoutParams().height = GridLayout.LayoutParams.MATCH_PARENT;
        ViewTreeObserver vto = getViewTreeObserver();


        if ("province".equals(getTag())) {
            items = porviceItems;
        } else if ("letter".equals(getTag())){
            items = letterItems;
        }else {
            items = letterNumItems;
        }

        vto.addOnGlobalLayoutListener(() -> {
                    int height = getMeasuredHeight();
                    int width = getMeasuredWidth();
                    if (!hasViewSW && height != 0 && width != 0) {
                        hasViewSW = true;
                        setGridItemWidthAndHeight(width, height);
                    }
                }

        );
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setGridItemWidthAndHeight(int width, int height) {

        if (items != null && items.length > 0) {
            for (int i = 1; i <= items.length; i++) {


                TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.provice_tv_layout, null);
                textView.setText(items[i - 1]);
                gridLayout.addView(textView);
                MarginLayoutParams lp = (MarginLayoutParams) textView.getLayoutParams();
                if (i % 5 != 0) {
                    lp.rightMargin = dip2px(15);
                }
                if (i >= 6) {
                    lp.topMargin = dip2px(10);
                }
                int finalI = i;
                textView.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(items[finalI - 1]);
                    }
                });
            }
        }

        int childCount = gridLayout.getChildCount();
        gridLayout.setVisibility(View.VISIBLE);
        MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
        int wspace = getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin;

        int everyW = (width - wspace - dip2px(60)) / 5;

        int hspace = getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin;
        int everyH = (height - hspace - dip2px(60)) / 5;

        for (int i = 0; i < childCount; i++) {
            View view = gridLayout.getChildAt(i);
            view.getLayoutParams().height = everyW;
            view.getLayoutParams().width = everyW;
            view.invalidate();

        }
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}
