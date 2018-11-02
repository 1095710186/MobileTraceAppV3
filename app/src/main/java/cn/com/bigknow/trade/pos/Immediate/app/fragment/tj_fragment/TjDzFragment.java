package cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.TjMonthChooseAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.BaseSupportFragment;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TJActivity;
import cn.com.bigknow.trade.pos.Immediate.app.util.DateUtils;
import cn.com.bigknow.trade.pos.Immediate.app.widget.Pay_Data;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzeDzInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.DzInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.YearMonthbean;

/**
 * Created by laixy on 2016/10/25.
 * 卖菜 对账
 */

public class TjDzFragment extends BaseSupportFragment {
    @BindView(R.id.f_tj_dz_radipgroup)
    RadioGroup rg;
    @BindView(R.id.f_tj_head_chooseMonth)
    Button rbtCMonth;

    @BindView(R.id.f_tj_dz_layout_SRC)
    LinearLayout layoutSRC;

    @BindView(R.id.f_tj_dz_SRC)
    SimpleSwipeRefreshLayout srf;

    DecimalFormat df = new java.text.DecimalFormat("#.##");
    private List<AnalyzeDzInfo> analyzeMcInfo;//
    private BaseQuickAdapter mAdapter;

    private String startDate;
    private String endDate;
    private String type = "T";
    String date = null;

    @OnClick(R.id.btnRe)
    public void btnRe() {
        srf.startRefresh();
    }


    public static TjDzFragment newInstance() {
        Bundle args = new Bundle();
        TjDzFragment fragment = new TjDzFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initLazyView() {
        startDate = DateUtils.getCurrentDateString();
        endDate = DateUtils.getCurrentDateString();
        analyzeMcInfo = new ArrayList<>();
        mAdapter = new MyAdapter();
        srf.setAdapter(mAdapter);

        rbtCMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ArrayList<PopupWindow> popupWindowList = new ArrayList<>();
                RecyclerView recyclerView = (RecyclerView) UtilsPopWindow.showMonthPopupWindow(rbtCMonth, getActivity(), popupWindowList);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
//                    LogUtil.i(TAG,"------>"+"");
                List<YearMonthbean> stringList = DateUtils.getCurrentDateBefo3Month();
                TjMonthChooseAdapter mAdapter = new TjMonthChooseAdapter();
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(mAdapter);
                mAdapter.setNewData(stringList);

                recyclerView.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                        rg.clearCheck();
                        popupWindowList.get(0).dismiss();
                        rbtCMonth.setBackgroundResource(R.drawable.shape_tj_head_rbbg_m);
                        rbtCMonth.setText(stringList.get(i).getDate());
                        date = stringList.get(i).getDate();
                        type = "CM";
                        startDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(stringList.get(i).getYear(), stringList.get(i).getMonth() - 1));
                        endDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(stringList.get(i).getYear(), stringList.get(i).getMonth() - 1));
                        ;
                        srf.startRefresh();
                    }
                });
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                rbtCMonth.setText("选月份");
                rbtCMonth.setBackgroundResource(R.drawable.selector_tj_head_rbbg_m);

                switch (id) {
                    case R.id.f_tj_head_t:// 今天
                        type = "T";
                        startDate = DateUtils.getCurrentDateString();
                        endDate = DateUtils.getCurrentDateString();
                        srf.startRefresh();
                        break;
                    case R.id.f_tj_head_z: //周
                        type = "W";
                        startDate = DateUtils.getFirstDayOfWeek();//getStringDateFromDate(DateUtils.getFirstDayOfWeek(new Date()));
                        endDate = DateUtils.getLastDayOfWeek();//getStringDateFromDate(DateUtils.getLastDayOfWeek(new Date()));
                        srf.startRefresh();
                        break;
                    case R.id.f_tj_head_y: //月
                        type = "M";
                        startDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(new Date()));
                        endDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(new Date()));
                        srf.startRefresh();
                        break;


                }
            }
        });

        srf.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getDzList());
        srf.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                startActivity(TjDzDetailActivA);
                AnalyzeDzInfo info = analyzeMcInfo.get(i);
                DzInfo dzInfo = null;
                if (info != null) {
                    dzInfo = new DzInfo();
                    dzInfo.setBalState("Y");
                    dzInfo.setStartBillDate(startDate);
                    dzInfo.setEndBillDate(endDate);
                    dzInfo.setType(type);
                    if (type.equals("CM")) {
                        dzInfo.setDate(date);
                    }

                    if (info.getType().equals("S")) {
                        dzInfo.setPayChanner("S");

                    } else if (info.getType().equals("P")) {
                        dzInfo.setPayChanner("P,B");

                    } else if (info.getType().equals("C")) {
                        dzInfo.setPayChanner("C");
                    }else if (info.getType().equals("W")) {
                        dzInfo.setPayChanner("W");
                    }

                    if (dzInfo != null) {

                        startActivity((new Intent(getActivity(), TJActivity.class))
                                .putExtra("dzInfo", dzInfo));

                    } else {
                        ToastUtil.makeToast("没有对账信息！");
                    }
                } else {
                    ToastUtil.makeToast("没有对账信息！");
                }

            }
        });


        srf.startRefresh();
    }

    public void getDzList() {
        Api.api().getDzType(bindUntilEvent(FragmentEvent.DESTROY), startDate, endDate, new SimpleRequestListener<BaseEntity<List<AnalyzeDzInfo>>>() {

            @Override
            public void onSuccess(BaseEntity<List<AnalyzeDzInfo>> analyzeMcInfoBaseEntity) {
                if (analyzeMcInfoBaseEntity.data.size() > 0) {
                    analyzeMcInfo = analyzeMcInfoBaseEntity.data;
                    mAdapter.setNewData(analyzeMcInfo);
                    layoutSRC.setVisibility(View.GONE);
                    srf.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.makeToast("没有对账记录！");
                    analyzeMcInfo = new ArrayList<AnalyzeDzInfo>();
                    mAdapter.setNewData(analyzeMcInfo);
                    layoutSRC.setVisibility(View.VISIBLE);
                    srf.setVisibility(View.GONE);


                }
            }

            @Override
            public void onFinish() {
                if (srf != null) {
                    srf.stopRefresh();
                }
            }
        });

    }

    @Override
    public int layoutId() {
        return R.layout.f_tj_dz_layout;
    }

    public class MyAdapter extends BaseQuickAdapter<AnalyzeDzInfo> {

        public MyAdapter() {
            super(R.layout.w_dz_list_item, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, AnalyzeDzInfo analyzeDzInfo) {
            baseViewHolder
                    .setText(R.id.f_tj_dz_tvB, analyzeDzInfo.getOrderNum() + "笔")   //品种数量
                    .setText(R.id.f_tj_dz_tvT, df.format(analyzeDzInfo.getOrderQty()) + "斤")  //总重量
                    .setText(R.id.f_tj_dz_tvJye, df.format(analyzeDzInfo.getOrderAmt()) + "元"); //金额

            LinearLayout layout = baseViewHolder.getView(R.id.layout_dz);
            TextView tvType = baseViewHolder.getView(R.id.f_tj_dz_tvType);
            TextView tvTb = baseViewHolder.getView(R.id.f_tj_dz_tvTb);
            Pay_Data pay_data = new Pay_Data();
            pay_data.getpay_data(mContext);
            if (analyzeDzInfo != null) {
                if (analyzeDzInfo.getType().equals("P")) {//刷卡
                    tvType.setText("刷卡支付");
                    layout.setBackgroundColor(getResources().getColor(R.color.c_0DA34C));
                    if (pay_data.getpay_data(mContext) != null) {//判断本地是否有未提交的数据
                        tvTb.setVisibility(View.VISIBLE);
                        tvTb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToastUtil.makeToast("数据同步...");
                                pay_data.post_pay(pay_data.getpay_data(mContext), mContext);//得到本地数据进行提交。
                            }
                        });
                    } else {
                        tvTb.setVisibility(View.GONE);
                    }
                } else if (analyzeDzInfo.getType().equals("S")) {//总计
                    tvType.setText("总计");
                    layout.setBackgroundColor(getResources().getColor(R.color.c_0f8c44));
                } else if (analyzeDzInfo.getType().equals("C")) {//现金
                    tvType.setText("现金支付");
                    layout.setBackgroundColor(getResources().getColor(R.color.c_14b859));
                }
                else if (analyzeDzInfo.getType().equals("W")) {//现金
                    tvType.setText("微信支付");
                    layout.setBackgroundColor(getResources().getColor(R.color.common_green));
                }
            }


        }

    }

}
