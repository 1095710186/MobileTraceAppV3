package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.com.bigknow.trade.pos.Immediate.R;

/**
 * Created by hujie on 16/10/11.
 */

public class MCLeftTabView extends LinearLayout {

    @BindViews(value = {R.id.t1Layout, R.id.t2Layout, R.id.t3Layout, R.id.t4Layout, R.id.t5Layout})
    FrameLayout[] mTLayouts;

    @BindViews(value = {R.id.t1TV, R.id.t2TV, R.id.t3TV, R.id.t4TV, R.id.t5TV})
    TextView[] mTTVs;

    int index = 1;


    OnTabSelectedListener onTabSelectedListener;


    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.onTabSelectedListener = onTabSelectedListener;
    }

    public MCLeftTabView(Context context) {
        super(context);
        init();
    }

    public MCLeftTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MCLeftTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MCLeftTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.mc_left_layout, null);
        ButterKnife.bind(this, view);
        addView(view);
        setTLayoutOnClick();
        setSelectTab(1);
    }


    private void setTLayoutOnClick() {
        for (int i = 0; i < mTLayouts.length; i++) {
            int finalI = i;
            mTLayouts[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelectTab(finalI +1);
                }
            });
        }
    }


    public void setSelectTab(int index) {

        mTLayouts[this.index-1].setSelected(false);
        mTTVs[this.index-1].setSelected(false);

        mTLayouts[index-1].setSelected(true);
        mTTVs[index-1].setSelected(true);

        this.index = index;

        if (onTabSelectedListener != null) {
            onTabSelectedListener.onTabSelected(index);
        }
    }


    public interface OnTabSelectedListener {
        void onTabSelected(int selectIndex);
    }
}
