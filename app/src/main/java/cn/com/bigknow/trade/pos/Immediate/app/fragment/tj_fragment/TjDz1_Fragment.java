package cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
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
import cn.com.bigknow.trade.pos.Immediate.app.ui.TjDzDetailActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.util.DateUtils;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.Contant;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzeDzBillinfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BillDetailbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.DzInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.YearMonthbean;

/**
 * Created by laixy on 2016/10/25.
 * 卖菜 对账
 */

public class TjDz1_Fragment extends BaseSupportFragment {
    @BindView(R.id.f_tj_dz1_radipgroup)
    RadioGroup rg;
    @BindView(R.id.f_tj_head_t)
    RadioButton rbtT;
    @BindView(R.id.f_tj_head_z)
    RadioButton rbtZ;
    @BindView(R.id.f_tj_head_y)
    RadioButton rbtY;
    @BindView(R.id.f_tj_head_chooseMonth)
    Button rbtCMonth;
    @BindView(R.id.f_tj_dz1_SRC)
    SimpleSwipeRefreshLayout srf;

    DzInfo dzInfo;

    private int curPage = 1;
    private int rows = 10;

    DecimalFormat df=new   java.text.DecimalFormat("#.##");
    private BaseQuickAdapter mAdapter;

    List<AnalyzeDzBillinfo> analyzeDzBillinfos;

    private String startBillDate;
    private String endBillDate;
    private String payChanner = null;
    @BindView(R.id.f_tj_dz1_layout_SRC)
    LinearLayout layoutSRC;
    @OnClick(R.id.btnRe)
    public void btnRe() {
        srf.startRefresh();
    }
    public static TjDz1_Fragment newInstance() {
        Bundle args = new Bundle();
        TjDz1_Fragment fragment = new TjDz1_Fragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initLazyView() {
        analyzeDzBillinfos = new ArrayList<>();
        dzInfo = (DzInfo) getActivity().getIntent().getSerializableExtra("dzInfo");//getArguments().getSerializable("dzInfo");
        setHeadView();
        mAdapter = new MyAdapter();
        srf.setAdapter(mAdapter);

        srf.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getDzList("Y"));
        mAdapter.openLoadMore(rows);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                appendOutList("Y");
            }
        });

        srf.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                startActivity(TjDzDetailActivA);

                getBillDetail(analyzeDzBillinfos.get(i).getBillNo(),null);

                analyzeDzBillinfos.get(i).getBillNo();
//               startActivity((new Intent(getContext(), TjDzDetailActivity.class)
//                                .putExtra("billNo", analyzeDzBillinfos.get(i).getBillNo())));

            }
        });
        if (dzInfo!=null) {
            startBillDate = dzInfo.getStartBillDate();
            endBillDate = dzInfo.getEndBillDate();
            payChanner = dzInfo.getPayChanner();
            if (payChanner!=null && payChanner.equals("S")){
                payChanner = null;
            }
            if (dzInfo.getType().equals("CM")) {

                rg.clearCheck();
                rbtCMonth.setText(dzInfo.getDate());
                rbtCMonth.setBackgroundResource(R.drawable.shape_tj_head_rbbg_m);

//                getDzList("Y");

            }else if (dzInfo.getType().equals("T")) { //今天
//                rg.check(R.id.f_tj_head_t);
                rbtT.setChecked(true);
            }else if (dzInfo.getType().equals("W")){  //周
//                rg.check(R.id.f_tj_head_z);
                rbtZ.setChecked(true);
            }else if (dzInfo.getType().equals("M")){  //月
//                rg.check(R.id.f_tj_head_y);
                rbtY.setChecked(true);
            }

        }else {
            rbtT.setChecked(true);
        }
        srf.startRefresh();

    }

    private void setHeadView() {
        startBillDate = DateUtils.getCurrentDateString();
        endBillDate = DateUtils.getCurrentDateString();
        rbtCMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<PopupWindow> popupWindowList = new ArrayList<>();
                RecyclerView recyclerView = (RecyclerView) UtilsPopWindow.showMonthPopupWindow(rbtCMonth,getActivity(),popupWindowList);
                RecyclerView.LayoutManager  manager = new LinearLayoutManager(getActivity());
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
                        startBillDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(stringList.get(i).getYear(),stringList.get(i).getMonth()-1));
                        endBillDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(stringList.get(i).getYear(),stringList.get(i).getMonth()-1));;
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
                switch (id){
                    case R.id.f_tj_head_t:// 今天
                        startBillDate = DateUtils.getCurrentDateString();
                        endBillDate = DateUtils.getCurrentDateString();
                        srf.startRefresh();
                        break;
                    case R.id.f_tj_head_z: //周
                        startBillDate = DateUtils.getFirstDayOfWeek();//getStringDateFromDate(DateUtils.getFirstDayOfWeek(new Date()));
                        endBillDate = DateUtils.getLastDayOfWeek();//getStringDateFromDate(DateUtils.getLastDayOfWeek(new Date()));
                        srf.startRefresh();
                        break;
                    case R.id.f_tj_head_y: //月
                        startBillDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(new Date()));
                        endBillDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(new Date()));
                        srf.startRefresh();
                        break;
                }
            }
        });

    }

    //获取订单详情
    public void getBillDetail(String billNo, String id) {
        Api.api().getDzBillDetail(bindUntilEvent(FragmentEvent.DESTROY),  billNo,id, new SimpleRequestListener<BaseEntity<List<BillDetailbean>>>() {
            @Override
            public void onSuccess(BaseEntity<List<BillDetailbean>> o) {
                if (o.success==1 && !o.data.isEmpty()) {
                    startActivity(TjDzDetailActivityAutoBundle.createIntentBuilder(o.getData().get(0)).build(getActivity()));

                } else {

//                    layoutAll.setVisibility(View.GONE);
                    ToastUtil.makeToast("查询数据为空");

                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onStart() {
//                progressHudUtil.showProgressHud();
            }
        });
    }
    public void getDzList(String balState) {
        curPage = 1;
        Api.api().getDzList(bindUntilEvent(FragmentEvent.DESTROY),curPage,rows, balState,startBillDate,endBillDate,payChanner, new SimpleRequestListener<BaseEntity<List<AnalyzeDzBillinfo>>>() {


            @Override
            public void onSuccess(BaseEntity<List<AnalyzeDzBillinfo>> listListResultWrap) {
                if (listListResultWrap.success==1 && !listListResultWrap.data.isEmpty()) {
                    layoutSRC.setVisibility(View.GONE);
                    srf.setVisibility(View.VISIBLE);
                    analyzeDzBillinfos = listListResultWrap.data;
                    mAdapter.setNewData(listListResultWrap.data);
                    curPage++;

                } else {
                    layoutSRC.setVisibility(View.VISIBLE);
                    srf.setVisibility(View.GONE);
//                    ToastUtil.makeToast("列表返回数据为空");
                    curPage =1;
                }

            }


            @Override
            public void onFinish() {
                srf.stopRefresh();
                mAdapter.loadComplete();
               /* if (srf.isRefreshing()) {
                    srf.setRefreshing(false);
                }*/

            }
        });

    }

    public void appendOutList(String balState) {
        Api.api().getDzList(bindUntilEvent(FragmentEvent.DESTROY),curPage,rows, balState,startBillDate,endBillDate,payChanner, new SimpleRequestListener<BaseEntity<List<AnalyzeDzBillinfo>>>() {

            @Override
            public void onSuccess(BaseEntity<List<AnalyzeDzBillinfo>> listListResultWrap) {
                srf.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listListResultWrap.data != null && !listListResultWrap.data.isEmpty()) {
                            analyzeDzBillinfos.addAll(listListResultWrap.data);
                            mAdapter.setNewData(analyzeDzBillinfos);
                            mAdapter.notifyDataSetChanged();
                            curPage++;
                        } else {
                            ToastUtil.makeToast("已经没有更多数据了！");
                        }
                    }
                });

            }


            @Override
            public void onFinish() {
                srf.stopRefresh();
               /* if (srf.isRefreshing()) {
                    srf.setRefreshing(false);
                }*/
                mAdapter.loadComplete();
            }
        });

    }
    @Override
    public int layoutId() {
        return R.layout.f_tj_dz1_layout;
    }

    public class MyAdapter extends BaseQuickAdapter <AnalyzeDzBillinfo>{

        public MyAdapter() {
            super(R.layout.w_dz1_list_item, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, AnalyzeDzBillinfo analyzeDzBillinfo) {
            baseViewHolder
                    .setText(R.id.w_mv_tvTime, DateUtils.parseDate(analyzeDzBillinfo.getBillDate()))    //时间
                    .setText(R.id.w_mv_tvPzNumber, analyzeDzBillinfo.getKinds()+"种")   //品种数量
                    .setText(R.id.w_mv_tvZlNumber, df.format(analyzeDzBillinfo.getSumQty())+"斤")  //总重量
                    .setText(R.id.w_mv_tvPrice, df.format(analyzeDzBillinfo.getBalAmt())+"元")  //金额
                    .setText(R.id.w_mv_tvtimeNews, analyzeDzBillinfo.getPayDate());  //时间详情
            TextView tvName =  baseViewHolder.getView(R.id.w_mv_tvName);
            TextView tvType =  baseViewHolder.getView(R.id.w_mv_tvZfType);

            if (analyzeDzBillinfo!=null){
                if (analyzeDzBillinfo.getPayChanner().equals("B")){//联名卡
                    tvType.setText("刷联名卡支付");
                }else if (analyzeDzBillinfo.getPayChanner().equals("P")){//银行卡
                    tvType.setText("刷银行卡支付");
                }else if (analyzeDzBillinfo.getPayChanner().equals("C")){//现金
                    tvType.setText("现金支付");
                }else if (analyzeDzBillinfo.getPayChanner().equals("W")){//现金
                    tvType.setText("微信支付");
                }
            }
            if (analyzeDzBillinfo.getCorrId()!=null) {
                String[] payNames = analyzeDzBillinfo.getCorrInfo().split(",");
                if (payNames != null && payNames.length > 1) {
                    try {
                        tvName.setText(payNames[2] + "\t" + payNames[3]);
                    } catch (Exception e) {
                        tvName.setText(Contant.NO_CORRNAME);
                    }

                } else {
                    tvName.setText(Contant.NO_CORRNAME);
                }
            }else {
                tvName.setText(Contant.NO_CORRNAME);
            }

        }

    }

}
