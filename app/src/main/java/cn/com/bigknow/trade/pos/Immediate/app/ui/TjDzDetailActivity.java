package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MyListView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.PrinterModule_Bill;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.model.Contant;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BillDetailbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BillFoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;

/**
 * Created by laixiaoyang
 * 对账详情
 */

public class TjDzDetailActivity extends BaseActivity {
    @BindView(R.id.actionbar)
    Toolbar mToolbar;
    @BindView(R.id.kindsTV)
    TextView mKindsTV;
    @BindView(R.id.tvPay)
    TextView mTvPay; //实际支付
    @BindView(R.id.tvSum)
    TextView mTvSum; //总额
    @BindView(R.id.tvPayName)  //people
    TextView mTvPayName;
    @BindView(R.id.tvPaySttaus)   //state pay
    TextView mTvPaySttaus;
    @BindView(R.id.tvPayTime)  //
    TextView mTvPayTime;
    @BindView(R.id.tvPayType) //type pay
    TextView mTvPayType;
    @BindView(R.id.tvBillNO)
    TextView mTvBillNO;
    @BindView(R.id.mlvList)
    MyListView mlvList;
    @BindView(R.id.a_tjdz_detail_layout)
    LinearLayout layoutDY;
    @BindView(R.id.a_tjdz_layout)
    LinearLayout layoutAll;

   /* @AutoBundleField
    String billNo;

    @AutoBundleField
    String id;*/

    @AutoBundleField
    BillDetailbean detailbean;

    DecimalFormat df = new java.text.DecimalFormat("#.##");
    private DzDetialAdapter mAdapter;
    private List<BillFoodInfo> billFoodInfoList;
    private PrinterModule_Bill printerModule;

    //点击打印票据
    @OnClick(R.id.a_tjdz_detail_tvDypj)
    public void onDaYingClick() {
        printerModule.print(detailbean);
    }

    //点击退出
    @OnClick(R.id.a_tjdz_detail_tvBack)
    public void onBackClick() {
        finish();
    }

    @Override
    public void init() {
        setToolbar();
        billFoodInfoList = new ArrayList<>();
        mAdapter = new DzDetialAdapter(billFoodInfoList);
        mlvList.setAdapter(mAdapter);
//        getBillDetail();
        initData();
        init_dayin();
    }

    private void initData() {
        layoutDY.setVisibility(View.VISIBLE);
        billFoodInfoList = detailbean.getMvpOrderDet();
        mAdapter.Update(billFoodInfoList);
        mKindsTV.setText("共" + detailbean.getKinds() + "种/" + df.format(detailbean.getSumQty()) + "斤");
        mTvPay.setText("￥" + df.format(detailbean.getBalAmt()));
        mTvSum.setText("￥" + df.format(detailbean.getSumAmt()));
        if (detailbean.getCorrId() != null) {
            String[] payNames = detailbean.getCorrInfo().split(",");
            if (payNames != null && payNames.length > 1) {
                try {
                    mTvPayName.setText(payNames[2] + "\t" + payNames[3]);
                } catch (Exception e) {
                    mTvPayName.setText(Contant.NO_CORRNAME);
                }
            } else {
                mTvPayName.setText(Contant.NO_CORRNAME);
            }
        } else {
            mTvPayName.setText(Contant.NO_CORRNAME);
        }
        mTvPaySttaus.setText(detailbean.getBalState().equals("Y") ? "支付完成" : "支付完成");
        mTvPayTime.setText(detailbean.getCreateDate());
        String type = null;  //P-刷卡，B-联名卡，C-现金
        if (detailbean.getPayment().equals("P")) {
            type = "刷银行卡支付";
        } else if (detailbean.getPayment().equals("B")) {
            type = "刷联名卡支付";
        } else if (detailbean.getPayment().equals("C")) {
            type = "现金支付";
        }else if (detailbean.getPayment().equals("W")) {
            type = "微信支付";
        }
        mTvPayType.setText(type);
        mTvBillNO.setText(detailbean.getBillNo());

    }

    //初始化打印功能
    public void init_dayin() {
        //初始化打印功能
        printerModule = new PrinterModule_Bill(this) {
            @Override
            protected void showMessage(String msg) {
                ToastUtil.makeToast(msg);
            }

            @Override
            protected void onDeviceServiceCrash() {
                // Handle in 'PrinterActivity'
                ToastUtil.makeToast("打印失败");
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindDeviceService();
    }

    // Sometimes you need to release the right of using device before other application 'onStart'.
    @Override
    protected void onPause() {
        super.onPause();
        unbindDeviceService();
    }

    /**
     * If device service crashed, quit application or try to relogin service again.
     */
    public void onDeviceServiceCrash() {
        bindDeviceService();
    }

    private boolean isDeviceServiceLogined = false;

    protected boolean isDeviceServiceLogined() {
        return isDeviceServiceLogined;
    }

    public void bindDeviceService() {

        try {
            isDeviceServiceLogined = false;
            DeviceService.login(this);
            isDeviceServiceLogined = true;
        } catch (RequestException e) {
            // 延迟若干毫秒后尝试重新绑定,若想继续占有设备服务
            e.printStackTrace();
        } catch (ServiceOccupiedException e) {
            e.printStackTrace();
        } catch (ReloginException e) {
            e.printStackTrace();
        } catch (UnsupportMultiProcess e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    public void unbindDeviceService() {
        DeviceService.logout();
        isDeviceServiceLogined = false;
    }

   /* public void getBillDetail() {
        Api.api().getDzBillDetail(bindUntilEvent(ActivityEvent.DESTROY), billNo, id, new SimpleRequestListener<BaseEntity<List<BillDetailbean>>>() {
            @Override
            public void onSuccess(BaseEntity<List<BillDetailbean>> o) {
                if (o.success == 1 && !o.data.isEmpty()) {
                    layoutDY.setVisibility(View.VISIBLE);
                    detailbean = o.data.get(0);
                    billFoodInfoList = detailbean.getMvpOrderDet();
                    mAdapter.Update(billFoodInfoList);
                    mKindsTV.setText("共" + detailbean.getKinds() + "种/" + df.format(detailbean.getSumQty()) + "斤");
                    mTvPay.setText("￥" + df.format(detailbean.getPayAmt()));
                    mTvSum.setText("￥" + df.format(detailbean.getSumAmt()));

                    if (detailbean.getCorrId() != null) {
                        String[] payNames = detailbean.getCorrInfo().split(",");
                        if (payNames != null && payNames.length > 1) {
                            try {
                                mTvPayName.setText(payNames[2] + "\t" + payNames[3]);
                            } catch (Exception e) {
                                mTvPayName.setText(Contant.NO_CORRNAME);
                            }

                        } else {
                            mTvPayName.setText(Contant.NO_CORRNAME);
                        }
                    } else {
                        mTvPayName.setText(Contant.NO_CORRNAME);
                    }

                    mTvPaySttaus.setText(detailbean.getBalState().equals("Y") ? "支付完成" : "支付完成");
                    mTvPayTime.setText(detailbean.getCreateDate());
                    String type = null;  //P-刷卡，B-联名卡，C-现金
                    if (detailbean.getPayment().equals("P")) {
                        type = "刷银行卡支付";
                    } else if (detailbean.getPayment().equals("B")) {
                        type = "刷联名卡支付";
                    } else if (detailbean.getPayment().equals("C")) {
                        type = "现金支付";
                    }
                    mTvPayType.setText(type);
                    mTvBillNO.setText(detailbean.getBillNo());

                } else {
                    layoutDY.setVisibility(View.GONE);
//                    layoutAll.setVisibility(View.GONE);
                    ToastUtil.makeToast("查询数据为空");

                }
            }

            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud();
            }
        });
    }*/

    private List<FoodInfo> convertData(List list) {
        try {
            FoodInfo task = (FoodInfo) list.get(0);
            return list;
        } catch (ClassCastException e) {
            List<FoodInfo> tasks = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof JSONObject) {
                    tasks.add(JSON.parseObject(((JSONObject) list.get(i)).toJSONString(), FoodInfo.class));
                } else if (list.get(i) instanceof FoodInfo) {
                    tasks.add((FoodInfo) list.get(i));
                }
            }
            return tasks;
        }

    }

    public class DzDetialAdapter extends BaseAdapter {
        private List<BillFoodInfo> mvpOrderDet;
        DecimalFormat df = new java.text.DecimalFormat("#.##");

        public DzDetialAdapter(List<BillFoodInfo> mvpOrderDet) {
            this.mvpOrderDet = mvpOrderDet;
        }

        public void Update(List<BillFoodInfo> mvpOrderDet) {
            this.mvpOrderDet = mvpOrderDet;
            notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return mvpOrderDet.size();
        }

        @Override
        public Object getItem(int position) {
            return mvpOrderDet.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (null == convertView) {
                holder = new Holder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.w_tj_dzdetail_item, null); //mContext指的是调用的Activtty
                holder.tvFoodName = (TextView) convertView.findViewById(R.id.tvName);
                holder.tvPayPrice = (TextView) convertView.findViewById(R.id.tvPayPrice);
                holder.tvPayNum = (TextView) convertView.findViewById(R.id.tvPayNum);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            BillFoodInfo billDetBean = mvpOrderDet.get(position);
            holder.tvFoodName.setText(billDetBean.getAlias()); //billDetBean.getItemName()
            holder.tvPayPrice.setText("￥" + df.format(billDetBean.getPrice()) + "x" + df.format(billDetBean.getQty()) + "斤");
            holder.tvPayNum.setText("小计：￥" + df.format(billDetBean.getAmt()));
            return convertView;
        }
        class Holder {
            public TextView tvFoodName, tvPayNum, tvPayPrice;
        }
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> finish());

        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
        menuLayout.setVisibility(View.GONE);
        menuLayout.removeAllViews();
        ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.index_1_right_menu, null);
        view.setBackgroundResource(R.drawable.ico_gd);
        menuLayout.addView(view);
        view.setOnClickListener(view1 -> {
            UtilsPopWindow.showMenuWindow(view1, mActivity);
        });
    }

    @Override
    public boolean supportActionbar() {
        return false;
    }

    @Override
    protected boolean autoBindBundle() {
        return true;
    }

    public void setStatusColor() {
        //5.0上面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addStatusView(R.color.colorAccent);
        }
    }

    View mStatusBarTintView;

    public void addStatusView(int color) {

        getRootView().setFitsSystemWindows(false);
        int statusHeight = getStatusHeight();
        mStatusBarTintView = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusHeight);
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(getResources().getColor(color));
        mStatusBarTintView.setVisibility(View.VISIBLE);
        ((LinearLayout) getRootView()).addView(mStatusBarTintView, 0);
    }
    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusHeight() {
        return getInternalDimensionSize(Resources.getSystem(), "status_bar_height");
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        //status_bar_height
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public int layoutId() {
        return R.layout.a_tjdz_detaillayout;
    }
}
