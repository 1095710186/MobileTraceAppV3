package cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

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
import cn.com.bigknow.trade.pos.Immediate.app.util.DateUtils;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AnalyzePzInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.YearMonthbean;

/**
 * Created by laixy on 2016/10/25.
 * 品种
 */

public class TjPzFragment extends BaseSupportFragment {
    @BindView(R.id.f_tj_head_radipgroup)
    RadioGroup rg;
    @BindView(R.id.f_tj_pz_jt_SRC)
    SimpleSwipeRefreshLayout jt_SRC;
    @BindView(R.id.f_tj_head_chooseMonth)
    Button rbtCMonth;

    @BindView(R.id.f_tj_pz_chooseM_SRC)
    SimpleSwipeRefreshLayout chooseM_SRC;
    @BindView(R.id.f_tj_pz_z_SRC)
    SimpleSwipeRefreshLayout z_SRC;
    @BindView(R.id.f_tj_pz_y_SRC)
    SimpleSwipeRefreshLayout y_SRC;

    DecimalFormat df=new   java.text.DecimalFormat("#.##");
    private BaseQuickAdapter jt_mAdapter;
    private BaseQuickAdapter chooseM_mAdapter;
    private BaseQuickAdapter z_mAdapter;
    private BaseQuickAdapter y_mAdapter;

    public List<AnalyzePzInfo> jtAnalyzePzInfos;
    public List<AnalyzePzInfo> zAnalyzePzInfos;
    public List<AnalyzePzInfo> yAnalyzePzInfos;
    public List<AnalyzePzInfo> chooseMonthAnalyzePzInfos;

    private String startDate;
    private String endDate;
    int state = 1;
    boolean isCheckM = true;
    @BindView(R.id.f_tj_kc_layout_SRC)
    LinearLayout layoutSRC;
    @OnClick(R.id.btnRe)
    public void btnRe() {
        getPzList(state);
    }
    public static TjPzFragment newInstance() {
        Bundle args = new Bundle();
        TjPzFragment fragment = new TjPzFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initLazyView() {


        setRefreshLayout();
        startDate = DateUtils.getCurrentDateString();
        endDate = DateUtils.getCurrentDateString();
        /*jt_SRC.setRefreshEnable(false);
        z_SRC.setRefreshEnable(false);
        by_SRC.setRefreshEnable(false);
        y_SRC.setRefreshEnable(false);*/
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
                        popupWindowList.get(0).dismiss();
                        rg.clearCheck();
                        rbtCMonth.setBackgroundResource(R.drawable.shape_tj_head_rbbg_m);
                        rbtCMonth.setText(stringList.get(i).getDate());
                        isCheckM = true;

                        startDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(stringList.get(i).getYear(),stringList.get(i).getMonth()-1));
                        endDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(stringList.get(i).getYear(),stringList.get(i).getMonth()-1));
                        showSRF(chooseM_SRC);
                        if (chooseMonthAnalyzePzInfos ==null||chooseMonthAnalyzePzInfos.isEmpty()) {
                            chooseM_SRC.startRefresh();
                        }
                    }
                });
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                if (isCheckM){
                    rbtCMonth.setText("选月份");
                    rbtCMonth.setBackgroundResource(R.drawable.selector_tj_head_rbbg_m);
                    isCheckM = false;
                }

                switch (id){
                   /* case R.id.f_tj_head_t:// 今天
                        startDate = DateUtils.getCurrentDateString();
                        endDate = DateUtils.getCurrentDateString();
                        break;
                    case R.id.f_tj_head_z: //周
                        startDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfWeek(new Date()));
                        endDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfWeek(new Date()));
                        break;
                    case R.id.f_tj_head_y: //月
                        startDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(new Date()));
                        endDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(new Date()));
                        break;*/
                    case R.id.f_tj_head_t:// 今天
                        showSRF(jt_SRC);
                        startDate = DateUtils.getCurrentDateString();
                        endDate = DateUtils.getCurrentDateString();
                        if (jtAnalyzePzInfos == null || jtAnalyzePzInfos.isEmpty()) {

                            jt_SRC.startRefresh();
                        }
                        break;
                    case R.id.f_tj_head_z: //近7天
                        showSRF(z_SRC);
                        startDate = DateUtils.getFirstDayOfWeek();//getStringDateFromDate(DateUtils.getFirstDayOfWeek(new Date()));
                        endDate = DateUtils.getLastDayOfWeek();//getStringDateFromDate(DateUtils.getLastDayOfWeek(new Date()));
                        if (zAnalyzePzInfos == null || zAnalyzePzInfos.isEmpty()) {

                            z_SRC.startRefresh();
                        }

                        break;
                    case R.id.f_tj_head_y: //yue
                        showSRF(y_SRC);
                        startDate = DateUtils.getStringDateFromDate(DateUtils.getFirstDayOfMonth(new Date()));
                        endDate = DateUtils.getStringDateFromDate(DateUtils.getLastDayOfMonth(new Date()));
                        if (yAnalyzePzInfos == null || yAnalyzePzInfos.isEmpty()) {

                            y_SRC.startRefresh();
                        }
                        break;
                }
            }
        });

        jt_SRC.startRefresh();
    }

    private void setRefreshLayout() {
        //今天
        jt_mAdapter = new MyAdapter("今天");

        jt_SRC.setAdapter(jt_mAdapter);
        jt_SRC.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getPzList(1));
        //周
        z_mAdapter = new MyAdapter("周");
        z_SRC.setAdapter(z_mAdapter);
        z_SRC.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getPzList(2));

        //
        chooseM_mAdapter = new MyAdapter("月份");
        chooseM_SRC.setAdapter(chooseM_mAdapter);
        chooseM_SRC.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getPzList(3));

        //月
        y_mAdapter = new MyAdapter("月");
       y_SRC.setAdapter(y_mAdapter);
        y_SRC.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getPzList(4));
    }

    public void getPzList(int state) {
        this.state = state;
        Api.api().getPzList(bindUntilEvent(FragmentEvent.DESTROY), startDate,endDate, new SimpleRequestListener<BaseEntity<List<AnalyzePzInfo>>>() {
            @Override
            public void onSuccess(BaseEntity<List<AnalyzePzInfo>> listBaseEntity) {
                if ( !listBaseEntity.data.isEmpty()) {
                    layoutSRC.setVisibility(View.GONE);
                    if (state ==1) {
                        jt_SRC.setVisibility(View.VISIBLE);
                        jtAnalyzePzInfos = listBaseEntity.data;
                        jt_mAdapter.setNewData(jtAnalyzePzInfos);
                        jt_mAdapter.notifyDataSetChanged();
                    } else if (state==2) {
                        z_SRC.setVisibility(View.VISIBLE);
                        zAnalyzePzInfos = listBaseEntity.data;
                        z_mAdapter.setNewData(zAnalyzePzInfos);
                        z_mAdapter.notifyDataSetChanged();
                    } else if (state==4) {
                        y_SRC.setVisibility(View.VISIBLE);
                        yAnalyzePzInfos = listBaseEntity.data;
                        y_mAdapter.setNewData(yAnalyzePzInfos);
                        y_mAdapter.notifyDataSetChanged();
                    } else if (state==3) {
                        chooseM_SRC.setVisibility(View.VISIBLE);
                        chooseMonthAnalyzePzInfos = listBaseEntity.data;
                        chooseM_mAdapter.setNewData(chooseMonthAnalyzePzInfos);
                        chooseM_mAdapter.notifyDataSetChanged();
                    }


                } else {
                    if (state==1) {
                        jtAnalyzePzInfos = new ArrayList<AnalyzePzInfo>();
                        jt_mAdapter.setNewData(jtAnalyzePzInfos);
                        jt_SRC.setVisibility(View.GONE);
                    } else if (state==2) {
                        zAnalyzePzInfos = new ArrayList<AnalyzePzInfo>();
                        z_mAdapter.setNewData(zAnalyzePzInfos);
                        z_SRC.setVisibility(View.GONE);
                    } else if (state==4) {
                        yAnalyzePzInfos = new ArrayList<AnalyzePzInfo>();
                        y_mAdapter.setNewData(yAnalyzePzInfos);
                        y_SRC.setVisibility(View.GONE);
                    } else if (state==3) {
                        chooseM_SRC.setVisibility(View.GONE);
                        chooseMonthAnalyzePzInfos =  new ArrayList<AnalyzePzInfo>();
                        chooseM_mAdapter.setNewData(chooseMonthAnalyzePzInfos);
                    }

                    layoutSRC.setVisibility(View.VISIBLE);
                    ToastUtil.makeToast("当前没有记录！");
                }
            }


            @Override
            public void onFinish() {
                if(getActivity()!=null&&!getActivity().isFinishing()) {
                    if (state == 1) {
                        jt_SRC.stopRefresh();
                    } else if (state == 2) {
                        z_SRC.stopRefresh();
                    } else if (state == 4) {
                        y_SRC.stopRefresh();
                    } else if (state == 3) {
                        chooseM_SRC.stopRefresh();
                    }
                }

            }
        });
    }

    private void showSRF(SimpleSwipeRefreshLayout SRF) {
        jt_SRC.setVisibility(View.GONE);
        z_SRC.setVisibility(View.GONE);
        y_SRC.setVisibility(View.GONE);
        chooseM_SRC.setVisibility(View.GONE);
        SRF.setVisibility(View.VISIBLE);
    }
    @Override
    public int layoutId() {
        return R.layout.f_tj_pz_layout;
    }

    public class MyAdapter extends BaseQuickAdapter<AnalyzePzInfo> {
        public String state;

        public MyAdapter(String state) {
            super(R.layout.w_pz_list_item, null);
            this.state = state;
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, AnalyzePzInfo analyzePzInfo) {
            baseViewHolder
                    .setText(R.id.w_pz_item_tvNmwe, analyzePzInfo.getAlias().toString().trim())   //名称
                    .setText(R.id.w_pz_item_tvJhsl, analyzePzInfo.getEntryQty()+"斤，"
                            +analyzePzInfo.getEntryBat()+"车")    //进货数量
                    .setText(R.id.w_pz_item_tvYssl, df.format(analyzePzInfo.getSaleQty())+"斤")   //已售数量
                    .setText(R.id.w_pz_item_tvQkc, df.format(analyzePzInfo.getTrashQty())+"斤")  //清库存量
                    .setText(R.id.w_pz_item_tvPjsj, df.format(analyzePzInfo.getAvgPrice())+"元/斤") ; //平均售价

            UserManager.getInstance().loadFoodHeadImage(getActivity(), (ImageView) baseViewHolder.getView(R.id.w_pz_item_imvHead), analyzePzInfo.getImgId());


            baseViewHolder.getView(R.id.w_pz_item_imvHead);// 头像

            //详情按钮
//            baseViewHolder.setOnClickListener(R.id.w_mv_tvDetail, (view) -> openDetail(baseViewHolder, (Objects) foodInfo));
        }



        /**
         * 展开详细
         */
        public void openDetail(BaseViewHolder baseViewHolder, AnalyzePzInfo foodInfo) {
//            ((SwipeMenuLayout) baseViewHolder.getConvertView()).smoothCloseMenu();

        }
    }

}
