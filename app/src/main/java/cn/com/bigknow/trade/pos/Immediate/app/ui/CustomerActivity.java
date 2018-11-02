package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.List;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.CustomerAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.CustomerBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by hujie on 16/10/13.
 */

public class CustomerActivity extends BaseActivity {


    @BindView(R.id.refreshLayout)
    SimpleSwipeRefreshLayout refreshLayout;
    CustomerAdapter adapter;
    int page = 1;

    @Override
    public void init() {
        setTitle("选择客户");
        adapter = new CustomerAdapter();

        refreshLayout.setAdapter(adapter);
        adapter.openLoadMore(20);
        refreshLayout.setOnRefreshListener(new SimpleSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        refreshLayout.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_CHOOSE_CUSTOMER_BACK, baseQuickAdapter.getItem(i));
                finish();
            }
        });
        refreshLayout.startRefresh();
    }

    private void loadData() {

        Api.api().getCustomerDetialList(bindUntilEvent(ActivityEvent.DESTROY), null, 1, 20, new SimpleRequestListener<ListResultWrap<List<CustomerBean>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<CustomerBean>> listResultWrap) {
                if (listResultWrap.rows != null && !listResultWrap.rows.isEmpty()) {
                    adapter.setNewData(listResultWrap.rows);
                }
                page = 1;
            }

            @Override
            public void onFinish() {
                refreshLayout.stopRefresh();
            }
        });
    }


    private void loadMore() {

        Api.api().getCustomerDetialList(bindUntilEvent(ActivityEvent.DESTROY), null, page + 1, 20, new SimpleRequestListener<ListResultWrap<List<CustomerBean>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<CustomerBean>> listResultWrap) {

                if (listResultWrap.rows != null && !listResultWrap.rows.isEmpty()) {
                    adapter.addData(listResultWrap.rows);
                    page++;
                } else {
                    ToastUtil.makeToast("已经没有更多数据了！");
                }
            }

            @Override
            public void onFinish() {
                adapter.loadComplete();
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.a_customer_layout;
    }
}
