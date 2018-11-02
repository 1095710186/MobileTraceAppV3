package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.TXJHDAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryDsMain;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.IdResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

public class Update_JHD_Activity extends BaseActivity {
    @BindView(R.id.actionbar)
    Toolbar mToolbar;

    @BindView(R.id.refreshLayouta)
    SimpleSwipeRefreshLayout refreshLayout;

    TXJHDAdapter mAdapter;

    @AutoBundleField
    TXJHDWrapper wrapper;

    @AutoBundleField(required = false)
    boolean isEdit = false;

    @BindView(R.id.cpTVa)
    TextView mCpTV;
    @BindView(R.id.tvZ)
    TextView tvZ;
    @BindView(R.id.kindsTVa)
    TextView mKindsTV;
    @BindView(R.id.hjTVa)
    TextView mHjTV;

    @BindView(R.id.kindsTV_)
    TextView mKindsTV_;
    @BindView(R.id.hcTV)
    TextView mHcTV;

    @BindView(R.id.hjLLa)
    LinearLayout mHjLL;
    @BindView(R.id.updata_jh_layoutdia)
    LinearLayout updata_jh_layoutdi;

    @BindView(R.id.addTV)
    TextView mAddTV;
    @BindView(R.id.submitTVa)
    TextView mSubmitTV;

    @BindView(R.id.updata_jhd_checkbox)
    CheckBox updata_jhd_checkbox;

    @BindView(R.id.ggcpBtna)
    TextView ggcpBtn;
    boolean updata_jhd=false;
    DecimalFormat df=new   java.text.DecimalFormat("#.##");
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean showBackNavication() {
        return super.showBackNavication();

    }

    @Override
    public void init() {
        setToolbar();
        if (wrapper== null){
            wrapper = (TXJHDWrapper) getIntent().getSerializableExtra("wrapper");
        }
        mAdapter = new TXJHDAdapter();
        mAdapter.setTest(false);
        refreshLayout.setAdapter(mAdapter);
        refreshLayout.setRefreshEnable(false);


        updata_jhd_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(updata_jhd_checkbox.isChecked()){
                    ggcpBtn.setBackgroundColor(Color.parseColor("#16a61e"));
                    ggcpBtn.setTextColor(Color.parseColor("#ffffff"));
                    ggcpBtn.setText("更改");
                    ggcpBtn.setVisibility(View.VISIBLE);
                    mHjLL.setVisibility(View.VISIBLE);
                    updata_jh_layoutdi.setVisibility(View.VISIBLE);
                    updata_jhd=true;
                    mAdapter.setTest(true);
                    mAdapter.notifyDataSetChanged();
                }else{
//                    ggcpBtn.setBackgroundColor(Color.parseColor("#e9ffea"));
//                    ggcpBtn.setTextColor(Color.parseColor("#16a61e"));
//                    ggcpBtn.setText(df.format(Float.valueOf(wrapper.getTotal()))+"吨");
                    ggcpBtn.setVisibility(View.GONE);
                    mHjLL.setVisibility(View.VISIBLE);
                    updata_jh_layoutdi.setVisibility(View.GONE);
                    updata_jhd=false;
                    mAdapter.setTest(false);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
//        ggcpBtn.setText(df.format(Float.valueOf(wrapper.getTotal()))+"吨");
//        ggcpBtn.setTextColor(Color.parseColor("#16a61e"));
//        ggcpBtn.setBackgroundColor(Color.parseColor("#e9ffea"));
        ggcpBtn.setBackgroundColor(Color.parseColor("#16a61e"));
        ggcpBtn.setTextColor(Color.parseColor("#ffffff"));
        ggcpBtn.setText("更改");
        ggcpBtn.setVisibility(View.GONE);
        setData();
    }



    private void setData() {
        if (wrapper.getChePai() != null) {
            mCpTV.setText(wrapper.getChePai().getChePai());
        }

        mAdapter.setNewData(wrapper.getFoodInfos());
        if (wrapper.getTotal()!=null) {
            tvZ.setText(df.format(Float.valueOf(wrapper.getTotal())) + "吨");
        }
        if (wrapper.getFoodInfos().size() > 0) {
            mHjLL.setVisibility(View.VISIBLE);
            mKindsTV.setText("共" + wrapper.getFoodInfos().size() + "种");
            mKindsTV_.setText("共" + wrapper.getFoodInfos().size() + "种");

            float total = 0;
            for (FoodInfo foodInfo : wrapper.getFoodInfos()) {
                total += foodInfo.getQty();
            }
            BigDecimal numHj = new BigDecimal(total);
            mHjTV.setText(df.format(numHj) + "");
            if (wrapper.getTotal()!=null && wrapper.getTotal().toString().trim().length()>0) {
                float zTotal = Float.valueOf(wrapper.getTotal())*2000;
                BigDecimal numZp = new BigDecimal(zTotal);
                mHcTV.setText(df.format(numZp.subtract(numHj)));
            }

        } else {
            mHjLL.setVisibility(View.GONE);
        }
    }

    //更改车牌
    @OnClick(R.id.ggcpBtna)
    public void ggcpBtna() {

        if(updata_jhd) {
            startActivity(TXJHDCPActivityAutoBundle.createIntentBuilder().wrapper(wrapper).build(this));
        }
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {

        if(updata_jhd) {

            if (event.type == SimpleEventType.ON_CHANGE_CHEPAI_BACK) {
                wrapper = (TXJHDWrapper) event.objectEvent;
            } else if (event.type == SimpleEventType.ON_JH_BACK) {
                FoodInfo foodInfoADD = (FoodInfo) event.objectEvent;
                foodInfoADD.setItemId(foodInfoADD.getId());
                wrapper.getFoodInfos().add(foodInfoADD);
            } else if (event.type == SimpleEventType.ON_EDIT_FOOD_ZL_CLICK) {
                FoodInfo foodInfo = (FoodInfo) event.objectEvent;
                showDialog(foodInfo);
            } else if (event.type == SimpleEventType.ON_EDIT_FOOD_CDGYS_CLICK) {
                selectFoodInfo = (FoodInfo) event.objectEvent;
//            wrapper.getFoodInfos().set((FoodInfo) event.objectEvent);
                startActivity(XZCDGYSActivityAutoBundle.createIntentBuilder(selectFoodInfo).isEdit(true).build(this));
            } else if (event.type == SimpleEventType.ON_JH_CDGYS_EDIT_BACK) {
                FoodInfo foodInfo = (FoodInfo) event.objectEvent;
                if (selectFoodInfo != null) {
                    for (int i = 0; i < wrapper.getFoodInfos().size(); i++) {
                        if (wrapper.getFoodInfos().get(i).getItemId().equals(foodInfo.getItemId())){
                            wrapper.getFoodInfos().set(i, foodInfo);
                        }
                    }
                    for (FoodInfo info: wrapper.getFoodInfos()){

                    }
                    /*int i = mAdapter.getData().indexOf(selectFoodInfo);
                    wrapper.getFoodInfos().set(i,foodInfo);
                    mAdapter.remove(i);
                    mAdapter.addData(i, foodInfo);*/
                }
            } else if (event.type == SimpleEventType.ON_JH_FOOD_DELETE_CLICK) {
                int i =(int)event.objectEvent;
                wrapper.getFoodInfos().remove(i);
            }
            setData();

        }
    }

    private FoodInfo selectFoodInfo;

    private void showDialog(FoodInfo foodInfo) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dd_weight_num_keyboard_layout, null);
        TextView nameTV = (TextView) view1.findViewById(R.id.nameTV);
        TextView cdTV = (TextView) view1.findViewById(R.id.gysTV);
        TextView gysTV = (TextView) view1.findViewById(R.id.cdTV);
        TextView showTV = (TextView) view1.findViewById(R.id.showTV);
        ImageView headIV = (ImageView) view1.findViewById(R.id.headIV);
        NumKeyboardView numkeyboardView = (NumKeyboardView) view1.findViewById(R.id.numkeyboardView);

        numkeyboardView.setShowTextView(showTV);

        nameTV.setText(foodInfo.getAlias());

        UserManager.getInstance().loadFoodHeadImage(this, headIV, foodInfo.getImgId());

        cdTV.setText(foodInfo.getArea());
        gysTV.setText(foodInfo.getVendor());

        AlertDialogUtil.showNumKeyboardDialog(mActivity, view1, view -> {
            if (view.getId() == R.id.sureBtn) {
                if (numkeyboardView.getNumValue() == 0) {
                    ToastUtil.makeToast("请输入重量");
                } else {
                    if(numkeyboardView.getNumValue()<1)
                    {
                        ToastUtil.makeToast("重量不能低于1");
                        return;
                    }

                    foodInfo.setQty(numkeyboardView.getNumValue());
                    AlertDialogUtil.dissMiss();
                    mAdapter.notifyDataSetChanged();
                    setData();
                }
            }
        });
    }


    @OnClick(R.id.addTV)
    public void onAddClick() {
        if(updata_jhd) {
            Intent intent = new Intent(this, JHChooseFoodActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.submitTVa)
    public void onSubmitClick() {
        if(updata_jhd) {

           /* if (wrapper.getChePai() == null) {
                ToastUtil.makeToast("请填写车牌");
                return;
            }*/
            if (wrapper.getFoodInfos().size() == 0) {
                ToastUtil.makeToast("请添加菜品");
                return;
            }
        /*if (wrapper.getFileId() == null) {
            ToastUtil.makeToast("请上传过磅单据");
            return;
        }*/
            requstSave();
        }
    }


    private void requstSave() {
        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        List<RequestWrap> requestWraps = new ArrayList<>();

        RequestWrap<FoodEntryDsMain> requestWrap = new RequestWrap<>();
        requestWrap.name = "dsMain";
        List<FoodEntryDsMain> list = new ArrayList<>();

        FoodEntryDsMain dsMain = new FoodEntryDsMain();


        if (isEdit) {
            dsMain.id = wrapper.getId();
            dsMain.transType = "C";
            dsMain.state = wrapper.getState();
            if (wrapper.getChePai() != null) {
                dsMain.searchNo = wrapper.getChePai().getChePai();
            }
            dsMain.kinds = wrapper.getFoodInfos().size() + "";
            dsMain.itemQty = mHjTV.getText().toString();
            dsMain.totalQty = wrapper.getTotal();
            dsMain.fileId = wrapper.getFileId();
            dsMain.updateDate = wrapper.getUpdateDate();
        } else {
            dsMain.transType = "C";
            if (wrapper.getChePai() != null) {
                dsMain.searchNo = wrapper.getChePai().getChePai();
            }
            dsMain.kinds = wrapper.getFoodInfos().size() + "";
            dsMain.itemQty = mHjTV.getText().toString();
            dsMain.totalQty = wrapper.getTotal();
            dsMain.fileId = wrapper.getFileId();

        }


        list.add(dsMain);

        requestWrap.data = list;
        requestWraps.add(requestWrap);

        RequestWrap<FoodEntryInfo> requestWrap1 = new RequestWrap<FoodEntryInfo>();
        requestWrap1.name = "dsDet";


        List list1 = new ArrayList<>();

        for (FoodInfo foodInfo : wrapper.getFoodInfos()) {
            if (foodInfo.getUnitId() == null) {
                foodInfo.setUnitId(foodInfo.getPurUnitId());
            }
//            foodInfo.setItemId(foodInfo.getId());
        }
        list1.addAll(wrapper.getFoodInfos());
        requestWrap1.data = list1;
        requestWraps.add(requestWrap1);

        simpleRequestWrap.dataset = requestWraps;


        String json = JSON.toJSONString(simpleRequestWrap);

        Api.api().reFoodEntry(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<IdResultWrap>() {
            @Override
            public void onStart() {
                super.onStart();
                progressHudUtil.showProgressHud("正在提交数据，请稍后", false);
            }

            @Override
            public void onSuccess(IdResultWrap o) {
                ToastUtil.makeToast("操作成功");
                EventMamager.getInstance().postEvent(SimpleEventType.ON_JH_REFRESH);
                EventMamager.getInstance().postEvent(SimpleEventType.ON_CLEANJ_KC_BACK);
                finish();
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressHudUtil.dismissProgressHud();
            }
        });
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
        mToolbar.setNavigationOnClickListener(v -> finish());
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
        return R.layout.update_jhd_xinlayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_HOME) {
            if (updata_jhd_checkbox.isChecked()){
                showExitJHDialog(4);
            }else {
               finish();
            }
            return true;
//            showExitJHDialog(4);
        }
        return super.onKeyDown(keyCode, event);

    }

    Display display;
    static AlertDialog dialog;

    public void dissclose() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
    //弹出提示框 退出
    private void showExitJHDialog(int type) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_wxit_jh, null);

        View cd_back = (View) view1.findViewById(R.id.cd_layout_quxiao);
        View cd_sure = (View) view1.findViewById(R.id.cd_layout_sure);
        TextView tvNews = (TextView) view1.findViewById(R.id.tvNews);
        if (type ==4){
            tvNews.setText("正在编辑，\r\n确定要离开？");
        }
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
                finish();
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
}