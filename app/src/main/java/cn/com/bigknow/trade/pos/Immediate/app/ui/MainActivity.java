package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.BuildConfig;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.app.di.DBManager;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.JHFragment;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.MCFragmentV2;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.TJFragment;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.WDFragment;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.MainUiManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.activitymanager.TheActivityManager;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.base.util.ViewHelper;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeModelDao;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PaymentInformationBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UnitModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UnitModelDao;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Version;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import icepick.Icepick;
import icepick.State;
import me.yokeyword.fragmentation.SupportFragment;

import static cn.com.bigknow.trade.pos.Immediate.app.util.MainUiManager.downLoad;

/**
 * Created by hujie on 16/10/10.
 */

public class MainActivity extends AFragmentSupport {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;


    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;


    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.headIV)
    RoundedImageView mHeadIV;

    @BindView(R.id.tab1Layout)
    FrameLayout mTab1Layout;

    @BindView(R.id.tab2Layout)
    FrameLayout mTab2Layout;

    @BindView(R.id.tab3Layout)
    FrameLayout mTab3Layout;

    @BindView(R.id.tab4Layout)
    FrameLayout mTab4Layout;


    @BindView(R.id.toolbarTitleTV)
    TextView titleTV;
    @BindView(R.id.nameTV)
    TextView nameTV;
    @BindView(R.id.shopTV)
    TextView shopTV;


    @BindViews(value = {R.id.s1IV, R.id.s2IV, R.id.s3IV, R.id.s4IV})
    ImageView[] msIVs;


    @State
    int index = 0;

    String[] titles = {"手账", "统计分析", "进货管理", "我的"};


    SupportFragment[] mFragments = new SupportFragment[4];


    @Override
    public boolean supportActionbar() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {

        if (event.type == SimpleEventType.ON_MC_LEFT_TAB_CHANGE) {
            if (index == 1) {
                setToolbarColor(colors[event.intEvent - 1]);
                int i = event.intEvent;
                int count = EntityManager.getShopCarFoodsCountByType(i);
                if (count != 0) {
                    titleTV.setText(i + "篮(" + count + ")");
                } else {
                    Billbean billbean1 = EntityManager.getBillByType(i);
                    if (billbean1 != null) {
                        titleTV.setText(i + "篮(未支付)");
                    } else {
                        titleTV.setText(i + "篮");
                    }
                }

            }
        }
        if (event.type == SimpleEventType.ON_NEW_UPDATE) {

        }
        if(event.type == SimpleEventType.ON_POST_WXPAY){//推送信息提交支付数据
            String [] arr =  event.strEvent.split(",");//使用一个或多个空格分割字符串

            wx_post_pay(arr[0],arr[1],arr[2]);

        }
        if (event.type == SimpleEventType.ON_KEY_HOME) {//按Home键回到主页
//            openMenu();
            selectTab(1);
        }


    }

    int[] colors = {R.color.t1_s, R.color.t2_s, R.color.t3_s, R.color.t4_s, R.color.t5_s};

    private void setToolbarColor(int color) {
        mToolbar.setBackgroundColor(getResources().getColor(color));
        if (mStatusBarTintView != null) {
            mStatusBarTintView.setBackgroundColor(getResources().getColor(color));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //后台异步登陆下
        UserManager.getInstance().asyncLogin();
        UserManager.getInstance().loadHeadImage(this, mHeadIV);
    }


    private void setUserInfo() {
        UserInfo userInfo = UserManager.getInstance().getUserInfo();
        nameTV.setText(userInfo.getUserName());
        shopTV.setText(userInfo.getAdminOrgName());

//        MainUiManager.checkUpdate(BootstrapApp.get()); //检查更新并下载
        checkUpdate(BootstrapApp.get());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setToolbar();
        RequestBaseData();
        initDrawLayout();
        setStatusColor();
        initFragments(savedInstanceState);

        setUserInfo();
    }


    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mFragments[0] = MCFragmentV2.newInstance();
            mFragments[1] = TJFragment.newInstance();
            mFragments[2] = JHFragment.newInstance();
            mFragments[3] = WDFragment.newInstance();
            loadMultipleRootFragment(R.id.contentLayout, index, mFragments[0], mFragments[1], mFragments[2], mFragments[3]);
            selectTab(1);
        } else {
            // 这里库已经做了Fragment恢复工作，不需要额外的处理
            // 这里我们需要拿到mFragments的引用，用下面的方法查找更方便些，也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些)
            mFragments[0] = findFragment(MCFragmentV2.class);
            mFragments[1] = findFragment(TJFragment.class);
            mFragments[2] = findFragment(JHFragment.class);
            mFragments[3] = findFragment(WDFragment.class);
            selectTab(index);
        }
    }

    @Override
    public void onBackPressedSupport() {
        moveTaskToBack(true);
    }


    private void openMenu() {
        if (!mDrawerLayout.isDrawerOpen(findViewById(R.id.leftMenuView))) {
            mDrawerLayout.openDrawer(findViewById(R.id.leftMenuView));
        }
    }

    private void closeMenu() {
        if (mDrawerLayout.isDrawerOpen(findViewById(R.id.leftMenuView))) {
            mDrawerLayout.closeDrawer(findViewById(R.id.leftMenuView));
        }
    }

    private void initDrawLayout() {

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                ViewHelper.setTranslationX(mContent,
                        mMenu.getMeasuredWidth() * (1 - scale));
                mContent.invalidate();
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {


            }
        });


    }


    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationContentDescription("菜单");
        mToolbar.setNavigationOnClickListener(v -> openMenu());
    }


    public void setStatusColor() {
        //5.0上面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addStatusView(R.color.colorAccent);
        }

    }


    private void addRightMenu() {
        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.removeAllViews();
        if (index == 1) {
            View view = LayoutInflater.from(this).inflate(R.layout.index_1_right_menu, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventMamager.getInstance().postEvent(SimpleEventType.ON_MENU_DELETE_CLICK);
                }
            });

            menuLayout.addView(view);

            ImageView view1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.index_1_right_menu, null);

            view1.setBackgroundResource(R.drawable.ico_gd);
            menuLayout.addView(view1);


            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view1.getLayoutParams();
            lp.leftMargin = DensityUtil.dip2px(getApplicationContext(), 10);
            view1.setOnClickListener(v -> {
                UtilsPopWindow.showMenuWindow(view1, mActivity);
            });


        } else if (index == 2) {
            ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.index_1_right_menu, null);
            view.setBackgroundResource(R.drawable.ico_gd);
            menuLayout.addView(view);
            view.setOnClickListener(view1 -> {
                UtilsPopWindow.showMenuWindow(view1, mActivity);

            });
        } else if (index == 3) {
            ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.index_1_right_menu, null);
            view.setBackgroundResource(R.drawable.ico_gd);
            menuLayout.addView(view);
            view.setOnClickListener(view1 -> {
                UtilsPopWindow.showMenuWindow(view1, mActivity);

            });
        }


    }


    @OnClick(value = {R.id.tab1Layout, R.id.tab2Layout, R.id.tab3Layout, R.id.tab4Layout})
    public void onMenuClick(View v) {
        if (v == mTab1Layout) {
            selectTab(1);
        } else if (v == mTab2Layout) {
            selectTab(2);
        } else if (v == mTab3Layout) {
            selectTab(3);
        } else {
            selectTab(4);
        }
    }

    private void selectTab(int i) {
        closeMenu();
        if (index == i) {
            return;
        }
        if (index == 0) {
            msIVs[0].setVisibility(View.INVISIBLE);
            showHideFragment(mFragments[0], mFragments[0]);
        } else {
            showHideFragment(mFragments[i - 1], mFragments[index - 1]);
            msIVs[index - 1].setVisibility(View.INVISIBLE);
        }
        index = i;
        msIVs[index - 1].setVisibility(View.VISIBLE);
        titleTV.setText(titles[index - 1]);
        addRightMenu();

        if (index != 1) {
            setToolbarColor(R.color.colorAccent);
        } else {
            EventMamager.getInstance().postStickyEvent(SimpleEventType.ON_MENUC_SELECT_MC);
        }

    }


    View mStatusBarTintView;

    public void addStatusView(int color) {

        mainLayout.setFitsSystemWindows(false);
        int statusHeight = getStatusHeight();
        mStatusBarTintView = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusHeight);
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(getResources().getColor(color));
        mStatusBarTintView.setVisibility(View.VISIBLE);
        mainLayout.addView(mStatusBarTintView, 0);

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }

    @Override
    public int layoutId() {
        return R.layout.main_layout;
    }

    public void RequestBaseData() {

        //获取菜品类型列表
        Api.api().getFoodType(bindUntilEvent(ActivityEvent.DESTROY), null, null, null, null, new SimpleRequestListener<ListResultWrap<List<FoodTypeModel>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<FoodTypeModel>> listListResultWrap) {
                FoodTypeModelDao foodTypeModelDao = (FoodTypeModelDao) DBManager.getInstance(MainActivity.this).getDao(FoodTypeModel.class);
                //如果数据库不为空，则说明有旧数据，重新从服务器拉去
                if (foodTypeModelDao.loadAll() != null && foodTypeModelDao.loadAll().size() > 0) {
                    foodTypeModelDao.deleteAll();
                    LogUtils.d("菜品类型数据清除成功");
                }
                if (listListResultWrap.total > 0) {
                    foodTypeModelDao.insertInTx(listListResultWrap.rows);
                    LogUtils.d("菜品类型数据插入成功");
                }
            }
        });

        //获取菜品类型详情列表
      /*  Api.api().getFoodTypeDetail(bindUntilEvent(ActivityEvent.DESTROY), null, null, null, null, null, new SimpleRequestListener<ListResultWrap<List<FoodTypeDetailModel>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<FoodTypeDetailModel>> listListResultWrap) {
                FoodTypeDetailModelDao detailModelDao = (FoodTypeDetailModelDao) DBManager.getInstance(MainActivity.this).getDao(FoodTypeDetailModel.class);
                //如果数据库不为空，则说明有旧数据，重新从服务器拉去
                if (detailModelDao.loadAll() != null && detailModelDao.loadAll().size() > 0) {
                    detailModelDao.deleteAll();
                    LogUtils.d("菜品类型详情数据清除成功");
                }
                if (listListResultWrap.total > 0) {
                    detailModelDao.insertInTx(listListResultWrap.rows);
                    LogUtils.d("菜品类型详情数据插入成功");
                }
            }
        });
*/
        //获取计量单位列表
        Api.api().getUnit(bindUntilEvent(ActivityEvent.DESTROY), null, null, new SimpleRequestListener<ListResultWrap<List<UnitModel>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<UnitModel>> listListResultWrap) {
                UnitModelDao unitModelDao = (UnitModelDao) DBManager.getInstance(MainActivity.this).getDao(UnitModel.class);
                //如果数据库不为空，则说明有旧数据，重新从服务器拉去
                if (unitModelDao.loadAll() != null && unitModelDao.loadAll().size() > 0) {
                    unitModelDao.deleteAll();
                    LogUtils.d("菜品计量单位数据清除成功");
                }
                if (listListResultWrap.total > 0) {
                    unitModelDao.insertInTx(listListResultWrap.rows);
                    LogUtils.d("菜品计量单位数据插入成功");
                }
            }
        });
    }


    private int mSecretNumber = 0;
    private static final long MIN_CLICK_INTERVAL = 600;//间隔时间0.6秒内
    private long mLastClickTime;

    private long jianClickTime;
    private long jiaClickTime;


    private long firstTime = 0;
    private int Secretzt = 0;
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
            if(index==1){
            EventMamager.getInstance().postEvent(SimpleEventType.ON_FOOD_BACK);
            }
            return true;//不执行父类点击事件
        }
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_VOLUME_UP) {//音量减小增大


            if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    jianClickTime=System.currentTimeMillis();
                }
            }
                if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        jiaClickTime=System.currentTimeMillis();
                    }
                }


            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 100) {                                         //如果两次按键时间间隔大于0.1秒，则不操作
                firstTime = secondTime;//更新firstTime
            } else {

                if(jianClickTime>=jiaClickTime){
                    if(jianClickTime-jiaClickTime<100){
                        //两次按键小于0.1秒时，打开设置界面
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                }else{
                    if(jiaClickTime-jianClickTime<100){
                        //两次按键小于0.1秒时，打开设置界面
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                }



            }

        }


        if (keyCode == KeyEvent.KEYCODE_HOME) {//后门
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - mLastClickTime;
            mLastClickTime = currentClickTime;

            if (elapsedTime < MIN_CLICK_INTERVAL&&elapsedTime>100) {
                ++mSecretNumber;
                if (6 == mSecretNumber) {//点击次数等于6次后做后门操作

                    TheActivityManager.getInstance().finishAll();
                    UserManager.getInstance().onLoginOut();
                    return true;

                }
            } else {
                mSecretNumber = 0;
            }
        }


        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }





    Billbean  bean;
    String number;
    //微信支付提交
    public void wx_post_pay(String openid,String order_id,String pay) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String time=format.format(new Date());

        PaymentInformationBean wx_pay_bean=new PaymentInformationBean();
        String     amt_zhi="";
        if (pay!=null) {
            amt_zhi = (Float.parseFloat(pay)) + "";
        }

        wx_pay_bean.setResponseId(openid);//流水号
        wx_pay_bean.setPayAmt(amt_zhi+"");//应付
        wx_pay_bean.setPayCorrName("");//  客户手机号payCorrNa
        wx_pay_bean.setOridId(order_id);//订单id
        wx_pay_bean.setAccountNo("");//付款账号
        wx_pay_bean.setResponseTime(time); //  结算响应时间
        wx_pay_bean.setAmt(amt_zhi);//实际付款
        wx_pay_bean.setBankId("");//  付款银行
        wx_pay_bean.setPayChanner("W");// 交易渠道，C-现金，N-网银，P-刷卡
        wx_pay_bean.setState("Y");//交易状态，Y-成功，N-失败
        wx_pay_bean.setTransType("O");//业务类型，O-订单结算，A-赊销结算
        wx_pay_bean.setPayDate(time); //结算日期




        List<PaymentInformationBean> pay_bean_list = new ArrayList<>();


        pay_bean_list.add(wx_pay_bean);

        RequestWrap<PaymentInformationBean> foodBeanRequestWrap = new RequestWrap<>();

        foodBeanRequestWrap.name = "dsMain";

        foodBeanRequestWrap.data = pay_bean_list;

        List<RequestWrap> requestWraps = new ArrayList<>();

        requestWraps.add(foodBeanRequestWrap);

        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        simpleRequestWrap.dataset = requestWraps;


        Api.api().PaymentSubmitted(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<BaseEntity<String>>() {
            @Override
            public void onSuccess(BaseEntity<String> o) {
//                EntityManager.deleteBill(billbean);
//                ToastUtil.makeToast("微信支付提交回调成功！");
              for(int i=0;i<5;i++){
                  bean=  EntityManager.getBillByType(i+1);
                  if (bean!=null) {
                      if (order_id.equals(bean.getId())) {
                          int asd = i + 1;
                          number = String.valueOf(asd);
                          EventMamager.getInstance().postStringEvent(SimpleEventType.ON_CLEAR_WXPAY, number);
                          break;
//                     EntityManager.deleteBill(bean);
                      }
                  }

              }

            }

            @Override
            public void onStart() {
//                progressHudUtil.showProgressHud("结算中...", false);
            }


            @Override
            public void onFinish() {
//                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onError(ApiError error) {
                ToastUtil.makeToast(error.errorMsg);
                if(!TextUtils.isEmpty(error.errorCode)){
                    if(error.errorCode.equals("common.data.ood.err_code")||error.errorCode.equals("mvp.pay.used.response.err_code")){
                        for(int i=0;i<5;i++){
                            bean=  EntityManager.getBillByType(i+1);
                            if(order_id.equals(bean.getId())){
                                int  asd=i+1;
                                number=String.valueOf(asd);
                                EventMamager.getInstance().postStringEvent(SimpleEventType.ON_CLEAR_WXPAY, number);
//                     EntityManager.deleteBill(bean);
                            }

                        }
//                        EntityManager.deleteBill(bean);
                    }
                }

            }

        });


    }


    public AlertDialog.Builder builder;
    public AlertDialog dialog;
    public static String DOWNLOAD_URL = ModelConfig.MVP_URL + "servlet/fileupload?oper_type=download&fileId=";
    public static String URL;
    public void checkUpdate(Context context) {

        String version = BuildConfig.VERSION_NAME;
        Api.api().checkUpdate(version + "", new SimpleRequestListener<Version>() {
            @Override
            public void onSuccess(Version o) {
//                downLoad(context, DOWNLOAD_URL + o.getFileId());
                URL = DOWNLOAD_URL + o.getFileId();
                createDialog(BootstrapApp.get(),o);
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);

            }
        });

    }

    public  void createDialog(Context context,Version o) {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("发现最新版本(V"+o.getVersion()+"),\r\n确定要更新吗？");
        builder.setPositiveButton("确定", (dialog, id) -> {
            downLoad(context,DOWNLOAD_URL + o.getFileId());
        });
        builder.setNegativeButton("取消", (dialog, id) -> {
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }




}
