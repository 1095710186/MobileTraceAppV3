package cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.google.android.flexbox.FlexboxLayout;
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
import cn.com.bigknow.trade.pos.Immediate.app.ui.JhDetaiActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.util.DateUtils;
import cn.com.bigknow.trade.pos.Immediate.base.util.RegularUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzeDzBillinfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzeJhInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ChePai;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.bean.YearMonthbean;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by laixy on 2016/10/25.
 * 进货
 */

public class TjJhFragment extends BaseSupportFragment {
    @BindView(R.id.f_tj_jh_SRC)
    SimpleSwipeRefreshLayout srf;

    private TextView tvJhcs; //近货车数量
    private TextView tvZzl;// 总重量
    private TextView tvRjjhl;// 日均进货量
    private RadioGroup rg;  //
    private RadioButton rbToday;  //
    Button rbtCMonth;

    private BaseQuickAdapter mAdapter;

    private AnalyzeJhInfo analyzeJhInfo;

    List<FoodEntryInfo>  list;

    private int curPage = 1;
    private int rows = 10;

    private String startDate;
    private String endDate;
    DecimalFormat df = new java.text.DecimalFormat("#.##");

    @BindView(R.id.f_tj_jh_layout_SRC)
    LinearLayout layoutSRC;

    @OnClick(R.id.btnRe)
    public void btnRe() {
        getJhList("Y");
    }

    public static TjJhFragment newInstance() {
        Bundle args = new Bundle();
        TjJhFragment fragment = new TjJhFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initLazyView() {
        list = new ArrayList<>();
        startDate = DateUtils.getCurrentDateString();
        endDate = DateUtils.getCurrentDateString();
        mAdapter = new MyAdapter();
        srf.setAdapter(mAdapter);
//        mAdapter.addHeaderView();
        setHeadView();

        srf.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getJhList("Y"));
        mAdapter.openLoadMore(rows);
        mAdapter.setOnLoadMoreListener((BaseQuickAdapter.RequestLoadMoreListener) () -> appendOutList("Y"));
        srf.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                FoodEntryInfo info = (FoodEntryInfo) mAdapter.getItem(i);
                TXJHDWrapper wrapper = new TXJHDWrapper();
                wrapper.setFoodInfos(info.getDetList());

                wrapper.setTotal(info.getTotalQty());

                String serachNo = info.getSearchNo();
                if (serachNo != null) {
                    serachNo = serachNo.replaceAll("\\s*", "");
                    if (RegularUtil.isChePai(serachNo)) {
                        ChePai chePai = new ChePai();
                        chePai.setProvince(serachNo.substring(0, 1));
                        chePai.setCity(serachNo.substring(1, 2));
                        chePai.setNumber(serachNo.substring(2));
                        wrapper.setChePai(chePai);
                    }
                }


                wrapper.setId(info.getId());
                wrapper.setUpdateDate(info.getUpdateDate());
                wrapper.setFileId(info.getFileId());
                wrapper.setState(info.getState());

                startActivity(JhDetaiActivityAutoBundle.createIntentBuilder(wrapper).build(getActivity()));

            }
        });
        srf.startRefresh();
    }

    private void setHeadView() {


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.f_tj_head_jh_layout, null);
        rg = (RadioGroup) view.findViewById(R.id.f_tj_head_radipgroup);
        rbToday = (RadioButton) view.findViewById(R.id.f_tj_head_t);
        tvJhcs = (TextView) view.findViewById(R.id.f_tj_head_tvJhcs);
        tvZzl = (TextView) view.findViewById(R.id.f_tj_head_tvZzl);
        tvRjjhl = (TextView) view.findViewById(R.id.f_tj_head_tvRjjhl);
        rbtCMonth = (Button) view.findViewById(R.id.f_tj_head_chooseMonth);
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
                        popupWindowList.get(0).dismiss();
                        rg.clearCheck();
                        rbtCMonth.setBackgroundResource(R.drawable.shape_tj_head_rbbg_m);
                        rbtCMonth.setText(stringList.get(i).getDate());

                        startDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(stringList.get(i).getYear(), stringList.get(i).getMonth() - 1));
                        endDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(stringList.get(i).getYear(), stringList.get(i).getMonth() - 1));
                        ;

//                        getJh(startDate, endDate);
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
                        startDate = DateUtils.getCurrentDateString();
                        endDate = DateUtils.getCurrentDateString();

                        srf.startRefresh();
                        break;
                    case R.id.f_tj_head_z: //周
                        startDate = DateUtils.getFirstDayOfWeek();//getStringDateFromDate(DateUtils.getFirstDayOfWeek(new Date()));
                        endDate = DateUtils.getLastDayOfWeek();//getStringDateFromDate(DateUtils.getLastDayOfWeek(new Date()));
//                        getJh(startDate, endDate);
                        srf.startRefresh();
                        break;
                    case R.id.f_tj_head_y: //月
                        startDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(new Date()));
                        endDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(new Date()));
//                        getJh(startDate, endDate);
                        srf.startRefresh();
                        break;
                }


            }
        });

        mAdapter.addHeaderView(view);

        rbToday.setChecked(true);
        startDate = DateUtils.getCurrentDateString();
        endDate = DateUtils.getCurrentDateString();
//        getJh(startDate, endDate);
    }

    public void getJh(String startDate, String endDate) {
        Api.api().getJhAnaluze(bindUntilEvent(FragmentEvent.DESTROY), startDate, endDate, new SimpleRequestListener<BaseEntity<AnalyzeJhInfo>>() {
            @Override
            public void onError(ApiError error) {
                super.onError(error);
                tvJhcs.setText("--车");
                tvZzl.setText("--斤");
                tvRjjhl.setText("--斤");
            }

            @Override
            public void onSuccess(BaseEntity<AnalyzeJhInfo> jhInfoBaseEntity) {
                if (jhInfoBaseEntity.success == 1 && jhInfoBaseEntity.data != null) {
                    analyzeJhInfo = jhInfoBaseEntity.data;
                    tvJhcs.setText(analyzeJhInfo.getEntryBat() + "车");
                    tvZzl.setText(df.format(analyzeJhInfo.getEntryQty()) + "斤");
                    tvRjjhl.setText(df.format(analyzeJhInfo.getAvgQty()) + "斤");

                } else {
                    tvJhcs.setText("--车");
                    tvZzl.setText("--斤");
                    tvRjjhl.setText("--斤");
                    ToastUtil.makeToast(jhInfoBaseEntity.getMsg());
                }
            }


            @Override
            public void onFinish() {

            }
        });

    }

    public void getJhList(String state) {
        getJh(startDate, endDate);
        curPage =1;
        Api.api().getJhList(bindUntilEvent(FragmentEvent.DESTROY),curPage,rows, state, startDate, endDate, new SimpleRequestListener<BaseEntity<List<FoodEntryInfo>>>() {
            @Override
            public void onSuccess(BaseEntity<List<FoodEntryInfo>> foodEntryInfoListResultWrap) {
                if (!foodEntryInfoListResultWrap.data.isEmpty()) {
                    layoutSRC.setVisibility(View.GONE);
                    srf.setVisibility(View.VISIBLE);
                    list = foodEntryInfoListResultWrap.data;
                    mAdapter.setNewData(foodEntryInfoListResultWrap.data);
                    mAdapter.notifyDataSetChanged();
                    curPage++;

                } else {
                    mAdapter.setNewData(foodEntryInfoListResultWrap.data);
                    curPage =1;
//                    layoutSRC.setVisibility(View.VISIBLE);
//                    srf.setVisibility(View.GONE);
                    ToastUtil.makeToast("进货列表没有记录！");
                }
            }


            @Override
            public void onFinish() {
                if (srf != null) {
                    srf.stopRefresh();
                }
                if (mAdapter!=null){
                    mAdapter.loadComplete();
                }

            }
        });
    }

    public void appendOutList(String state) {
        Api.api().getJhList(bindUntilEvent(FragmentEvent.DESTROY),curPage,rows, state, startDate, endDate, new SimpleRequestListener<BaseEntity<List<FoodEntryInfo>>>() {

            @Override
            public void onSuccess(BaseEntity<List<FoodEntryInfo>>  listListResultWrap) {
                srf.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listListResultWrap.data != null && !listListResultWrap.data.isEmpty()) {
                            list.addAll(listListResultWrap.data);
                            mAdapter.setNewData(list);
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
                if (mAdapter!=null){
                    mAdapter.loadComplete();
                }

            }
        });

    }

    @Override
    public int layoutId() {
        return R.layout.f_tj_jh_layout;
    }


    public class MyAdapter extends BaseQuickAdapter<FoodEntryInfo> {

        private final int[] foodTVBGs = {R.drawable.tv1, R.drawable.tv2, R.drawable.tv3, R.drawable.tv4};

        public MyAdapter() {
            super(R.layout.w_jh_list_item, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, FoodEntryInfo foodInfo) {
            FlexboxLayout flexboxLayout = baseViewHolder.getView(R.id.flexbox);
            flexboxLayout.removeAllViews();
            if (foodInfo.getFoodList() != null && foodInfo.getFoodList().size() > 0) {
                flexboxLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < foodInfo.getFoodList().size(); i++) {

                    View tvView = LayoutInflater.from(mContext).inflate(R.layout.jh_food_tv, null);
                    TextView textView = (TextView) tvView.findViewById(R.id.tv);
                    textView.setText(foodInfo.getFoodList().get(i));
                    textView.setBackgroundResource(foodTVBGs[i % 4]);
                    flexboxLayout.addView(tvView);

                }
            } else {
                flexboxLayout.setVisibility(View.GONE);
            }
            baseViewHolder
                    .setText(R.id.w_mv_tvCph, foodInfo.getSearchNo())   //车牌号
                    .setText(R.id.w_mv_tvTime, DateUtils.parseDate(foodInfo.getBillDate()))    //时间
                    .setText(R.id.w_mv_tvPzNumber, foodInfo.getKinds() + "种")   //品种数量
                    .setText(R.id.w_mv_tvZlNumber, df.format(Float.valueOf(foodInfo.getItemQty())) + "斤")  //总重量
                    .setText(R.id.w_jh_tvTimeNews, foodInfo.getCreateTime());  //时间详情


            //详情按钮
//            baseViewHolder.setOnClickListener(R.id.w_mv_tvDetail, (view) -> openDetail(baseViewHolder, (Objects) foodInfo));
        }


        /**
         * 展开详细
         */
        public void openDetail(BaseViewHolder baseViewHolder, FoodEntryInfo foodInfo) {
//            ((SwipeMenuLayout) baseViewHolder.getConvertView()).smoothCloseMenu();

        }
    }

}
