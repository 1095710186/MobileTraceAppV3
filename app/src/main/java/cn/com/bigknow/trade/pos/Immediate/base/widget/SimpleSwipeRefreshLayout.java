package cn.com.bigknow.trade.pos.Immediate.base.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import cn.com.bigknow.trade.pos.Immediate.R;

/**
 * Created by hujie on 16/6/18.
 */
public class SimpleSwipeRefreshLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    public Runnable delayRunnable;
    OnRefreshListener onRefreshListener;

    public final int LAYOUT_LINEAR_MANAGER = 1;

    private int layoutManager = LAYOUT_LINEAR_MANAGER;


    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public SimpleSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public SimpleSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public void setLayoutManager(int layoutMode) {
        this.layoutManager = layoutMode;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (layoutManager == LAYOUT_LINEAR_MANAGER) {
            getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
        }
        getRecyclerView().setAdapter(adapter);
    }

    public void setRefreshEnable(boolean enable) {
        setEnabled(enable);
    }

    private void init() {
        this.setColorSchemeResources(R.color.colorPrimary);
        recyclerView = new RecyclerView(this.getContext());
        setOnRefreshListener(this);
        addView(recyclerView, 0, new RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(delayRunnable);
    }


    public synchronized void startRefresh() {
        if (isRefreshing() || !isEnabled()) {
            return;
        }
        post(() -> {
            setRefreshing(true);
            onRefresh();
        });
    }


    public void onRefresh() {

        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        } else {
            setRefreshing(false);
            return;
        }

        delayRunnable = () -> {
            if (isRefreshing()) {
                setRefreshing(false);
            }
        };
        //1分钟后自动收起来避免某些情况下导致不能收回
        postDelayed(delayRunnable, 30 * 1000);

    }
    public void stopRefresh() {
        if (isRefreshing()) {
            setRefreshing(false);
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
