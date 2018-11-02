package cn.com.bigknow.trade.pos.Immediate.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.trello.rxlifecycle.android.FragmentEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.JHAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TXJHDCPActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TXJHD_Activity;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.Contant;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by hujie on 16/10/11.
 */

public class JHFragment extends BaseSupportFragment {
    public static JHFragment newInstance() {
        Bundle args = new Bundle();
        JHFragment fragment = new JHFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @BindView(R.id.refreshLayout)
    SimpleSwipeRefreshLayout refreshLayout;
    JHAdapter adapter;

    @BindView(R.id.noView)
    View noView;


    @Override
    public void initLazyView() {

        adapter = new JHAdapter();

        refreshLayout.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SimpleSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        refreshLayout.startRefresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter==null||adapter.getData() == null || adapter.getData().isEmpty()) {
            refreshLayout.startRefresh();
        }
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {
        if (event.type == SimpleEventType.ON_JH_REFRESH) {
            refreshLayout.startRefresh();
        }
    }

    private void loadData() {
        Api.api().getFoodEntryList(bindUntilEvent(FragmentEvent.DESTROY), new SimpleRequestListener<List<FoodEntryInfo>>() {
            @Override
            public void onSuccess(List<FoodEntryInfo> list) {
                if (list == null || list.size() == 0) {
                    noView.setVisibility(View.VISIBLE);
                } else {
                    noView.setVisibility(View.GONE);
                }
                adapter.setNewData(list);
            }

            @Override
            public void onFinish() {
                if (refreshLayout!=null) {
                    refreshLayout.stopRefresh();
                }
            }
        });
    }


    @OnClick(R.id.addView)
    public void onAddView() {
        Intent intent = new Intent(getContext(),TXJHDCPActivity.class);// TXJHD_Activity.class);
        intent.putExtra(Contant.WHERE,Contant.JH);
        startActivity(intent);
    }

    @Override
    public int layoutId() {
        return R.layout.f_jh_layout;
    }
}
