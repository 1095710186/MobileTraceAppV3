package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MoneyView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MyListView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;


/**
 * Created by hujie on 16/10/11.
 */

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;


    @AutoBundleField
    int selectIndex = 1;

    @AutoBundleField
    Billbean billbean;


    @BindView(R.id.Tv_shouz_time)
    TextView Tv_shouz_time;//时间显示

    @BindView(R.id.billIdTV)
    TextView billIdTV;

    @BindView(R.id.yfjeTV)
    TextView yfjeTV;

    @BindView(R.id.retryBtn)
    Button retryBtn;

    @BindView(R.id.stateTV)
    TextView stateTV;


    @BindView(R.id.mc_mlvList)
    MyListView mc_mlvList;   //未支付  菜品列表

    @BindView(R.id.mcx_tvSum)
    TextView mcx_tvSum;//未支付  货品总额

    @BindView(R.id.mcx_kindsTV)
    TextView mcx_kindsTV;

    @BindView(R.id.mcx_mxlayout)
    LinearLayout mcx_mxlayout;

    @BindView(R.id.mcx_checkbox_ordermx)
    CheckBox mcx_checkbox_ordermx;  //订单详情  展开与隐藏

    @BindView(R.id.mcx_checkbox_text)
    TextView mcx_checkbox_text;



    DecimalFormat df = new java.text.DecimalFormat("#.##");


    private void addRightMenu() {
        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.removeAllViews();

        View view = LayoutInflater.from(this).inflate(R.layout.choose_food_right_menu_layout, null);


        menuLayout.addView(view);

        view.setOnClickListener(view1 -> {

            UtilsPopWindow.showMenuWindow(view1, mActivity);

        });

    }

    @Subscribe
    public void onEvent(SimpleEvent event) {
//        if (event.type == SimpleEventType.ON_CHOOSE_CYCP_BACK) {
//            finish();
//        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //删除订单
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }

    @Override
    public void init() {

        setToolbar();
        setToolbarColor(colors[selectIndex - 1]);
//        addRightMenu();

        initContext();

    }
    @OnClick(R.id.retryBtn)
    public void GoPay(){
        startActivity(PayActivityAutoBundle.createIntentBuilder(selectIndex, billbean).build(mActivity));
    }
    private void initContext() {
        //首先看有没有没有支付的
        Billbean billbean = EntityManager.getBillByType(selectIndex);
        if (billbean != null) {
            billIdTV.setText(billbean.getBillNo());
            Tv_shouz_time.setText(billbean.getCreateDate());
            DecimalFormat df = new DecimalFormat("#.##");
            yfjeTV.setText(df.format(billbean.getPayAmt()) + "元");

            mcx_kindsTV.setText("共" + billbean.getKinds() + "种/" + df.format(billbean.getSumQty()) + "斤");
            mcx_tvSum.setText("￥" + df.format(billbean.getSumAmt()));
            mcx_checkbox_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mcx_checkbox_ordermx.isChecked()) {
                        mcx_checkbox_ordermx.setChecked(false);
                        mcx_checkbox_text.setText("订单详细");
                        mcx_mxlayout.setVisibility(View.GONE);
                    } else {
                        mcx_checkbox_ordermx.setChecked(true);
                        mcx_checkbox_text.setText("收起");
                        mcx_mxlayout.setVisibility(View.VISIBLE);
                    }
                }
            });
            mcx_checkbox_ordermx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mcx_checkbox_ordermx.isChecked()) {
                        mcx_checkbox_ordermx.setText("收起");
                        mcx_mxlayout.setVisibility(View.VISIBLE);
                    } else {
                        mcx_checkbox_ordermx.setText("订单详细");
                        mcx_mxlayout.setVisibility(View.GONE);
                    }
                }
            });

            mcmxadapter = new MC_NoPayAdapter(billbean.getShopCarFoods());
            mc_mlvList.setAdapter(mcmxadapter);
            if (billbean.getPayState() == 1) {
                stateTV.setText("该订单未支付");
                retryBtn.setText("去支付");
                /*if (get_databendi.getorder_data(getContext(), selectIndex) != null) {
                    if (get_databendi.getorder_data(getContext(), selectIndex).getOridId().toString().equals(billbean.getId())) {
                        Tv_shouz_time.setVisibility(View.VISIBLE);//时间显示
                        sz_qujiaoyan.setVisibility(View.VISIBLE);//去校验
                        sz_qujiaoyan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                show_jianyan_Dialog();
                            }
                        });
                    }
                }*/
            } else {
                retryBtn.setText("重试");
                stateTV.setText("该订单支付失败");
            }
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(PayActivityAutoBundle.createIntentBuilder(selectIndex, billbean).build(mActivity));
                }
            });
        }
    }


    @Override
    protected boolean autoBindBundle() {
        return true;
    }

    int[] colors = {R.color.t1_s, R.color.t2_s, R.color.t3_s, R.color.t4_s, R.color.t5_s};

    private void setToolbarColor(int color) {
        mToolbar.setBackgroundColor(getResources().getColor(color));
        if (mStatusBarTintView != null) {
            mStatusBarTintView.setBackgroundColor(getResources().getColor(color));
        }
    }


    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // 删除订单
                showExitOrderDialog(new ArrayList() {
                    {
                        add(billbean.getId());
                    }
                });

            }
        });
//        mToolbar.setNavigationOnClickListener(v -> finish());
    }

    public void deleBill(List<String> ids){
        Api.api().deleteBill(bindUntilEvent(ActivityEvent.DESTROY), ids, new SimpleRequestListener() {
            @Override
            public void onSuccess(Object o) {

                       /* if (get_databendi.getorder_data(getContext(), selectIndex) != null) {
                            if (get_databendi.getorder_data(getContext(), selectIndex).getOridId().toString().equals(billbean.getId())) {
                                get_databendi.deletorder(getActivity(), selectIndex);
                            }
                        }*/
                EntityManager.deleteBill(billbean);
                finish();

            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud();
            }


            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }
            @Override
            public void onError(ApiError error) {
                super.onError(error);


            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
       if(keyCode == KeyEvent.KEYCODE_BACK) {//fanhui
            //监控/拦截back键
           showExitOrderDialog(new ArrayList() {
               {
                   add(billbean.getId());
               }
           });

            return true;
        }



        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
    @Override
    public boolean supportActionbar() {
        return false;
    }

    public void setStatusColor() {
        //5.0上面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addStatusView(colors[selectIndex - 1]);
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

    Display display;
    static AlertDialog dialog;
    public void dissclose() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
    //弹出提示框 退出
    private void showExitOrderDialog(List<String> ids) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_wxit_jh, null);

        View cd_back = (View) view1.findViewById(R.id.cd_layout_quxiao);
        View cd_sure = (View) view1.findViewById(R.id.cd_layout_sure);
        TextView tvNews = (TextView) view1.findViewById(R.id.tvNews);
        tvNews.setText("离开会删除订单" + "\r\n\r\n 确定要继续吗？");

        cd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();

            }
        });
        cd_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();
                deleBill(ids);

            }
        });
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new AlertDialog.Builder(mActivity)
                .create();
        dialog.show();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        WindowManager windowManager = mActivity.getWindowManager();
        display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth())*3/4; //设置宽度
//        lp.height = (int) (display.getHeight())*1/3;
//                (int) (DensityUtil.dip2px(mActivity, 550));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view1);

    }

    @Override
    public int layoutId() {
        return R.layout.no_pay_detail_layout;
    }



    private MC_NoPayAdapter mcmxadapter;

    public class MC_NoPayAdapter extends BaseAdapter {
        private List<ShopCarFood> mvpOrderDet;
        DecimalFormat df = new java.text.DecimalFormat("#.##");

        public MC_NoPayAdapter(List<ShopCarFood> mvpOrderDet) {
            this.mvpOrderDet = mvpOrderDet;
        }

        public void Update(List<ShopCarFood> mvpOrderDet) {
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
            MC_NoPayAdapter.Holder holder;
            if (null == convertView) {
                holder = new MC_NoPayAdapter.Holder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.w_tj_dzdetail_item, null); //mContext指的是调用的Activtty
                holder.tvFoodName = (TextView) convertView.findViewById(R.id.tvName);
                holder.tvPayPrice = (TextView) convertView.findViewById(R.id.tvPayPrice);
                holder.tvPayNum = (TextView) convertView.findViewById(R.id.tvPayNum);

                convertView.setTag(holder);
            } else {
                holder = (MC_NoPayAdapter.Holder) convertView.getTag();
            }
            ShopCarFood billDetBean = mvpOrderDet.get(position);
            holder.tvFoodName.setText(billDetBean.getItemName()); //billDetBean.getItemName()
            holder.tvPayPrice.setText("￥" + df.format(billDetBean.getPrice()) + "x" + df.format(billDetBean.getQty()) + "斤");
            holder.tvPayNum.setText("小计：￥" + df.format(billDetBean.getAmt()));
            return convertView;
        }

        class Holder {
            public TextView tvFoodName, tvPayNum, tvPayPrice;

        }
    }
}
