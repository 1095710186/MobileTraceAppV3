package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

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

    BadgeView badgeView1;
    BadgeView badgeView2;
    BadgeView badgeView3;
    BadgeView badgeView4;
    BadgeView badgeView5;


    OnTabSelectedListener onTabSelectedListener;


    public int getTab1Num() {
        return badgeView1.getBadgeCount();
    }

    public int getTab2Num() {
        return badgeView2.getBadgeCount();
    }

    public int getTab3Num() {
        return badgeView3.getBadgeCount();
    }

    public int getTab4Num() {
        return badgeView4.getBadgeCount();
    }

    public int getTab5Num() {
        return badgeView5.getBadgeCount();
    }

    public void setTab1Num(String count) {
        badgeView1.setText(count + "");
    }

    public void setTab2Num(String count) {
        badgeView2.setText(count + "");
    }

    public void setTab3Num(String count) {
        badgeView3.setText(count + "");
    }

    public void setTab4Num(String count) {
        badgeView4.setText(count + "");
    }

    public void setTab5Num(String count) {
        badgeView5.setText(count + "");
    }

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

        badgeView1 = new BadgeView(getContext());
        badgeView2 = new BadgeView(getContext());
        badgeView3 = new BadgeView(getContext());
        badgeView4 = new BadgeView(getContext());
        badgeView5 = new BadgeView(getContext());

        badgeView1.setHideOnNull(true);
        badgeView2.setHideOnNull(true);
        badgeView3.setHideOnNull(true);
        badgeView4.setHideOnNull(true);
        badgeView5.setHideOnNull(true);



        badgeView1.setBackground(50, Color.parseColor("#d3321b"));

        badgeView2.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView2.setBadgeMargin(0, 10, 2, 0);
        badgeView2.setBackground(50, Color.parseColor("#d3321b"));
        badgeView3.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView3.setBadgeMargin(0, 10, 2, 0);
        badgeView3.setBackground(50, Color.parseColor("#d3321b"));
        badgeView4.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView4.setBadgeMargin(0, 10, 2, 0);
        badgeView4.setBackground(50, Color.parseColor("#d3321b"));
        badgeView5.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView5.setBadgeMargin(0, 10, 2, 0);
        badgeView5.setBackground(50, Color.parseColor("#d3321b"));


        badgeView1.setTargetView(mTLayouts[0]);
        badgeView2.setTargetView(mTLayouts[1]);
        badgeView3.setTargetView(mTLayouts[2]);
        badgeView4.setTargetView(mTLayouts[3]);
        badgeView5.setTargetView(mTLayouts[4]);

    }


    private void setTLayoutOnClick() {
        for (int i = 0; i < mTLayouts.length; i++) {
            int finalI = i;
            mTLayouts[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelectTab(finalI + 1);
                }
            });
        }
    }


    public void setSelectTab(int index) {

        mTLayouts[this.index - 1].setSelected(false);
        mTTVs[this.index - 1].setSelected(false);

        mTLayouts[index - 1].setSelected(true);
        mTTVs[index - 1].setSelected(true);

        this.index = index;

        if (onTabSelectedListener != null) {
            onTabSelectedListener.onTabSelected(index);
        }
    }


    public interface OnTabSelectedListener {
        void onTabSelected(int selectIndex);
    }
}
