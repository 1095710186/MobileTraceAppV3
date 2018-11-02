package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yatatsu.autobundle.AutoBundleField;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.Logger;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.Pay_Data;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.CommonUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.RequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayReques_isorNo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayRequestCode;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayRequestCodeInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayRequestCode_;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PaymentInformationBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import cn.jpush.android.api.JPushInterface;
import me.grantland.widget.AutofitTextView;

/**
 * Created by hujie on 16/10/13.
 */

public class PayActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;
    @AutoBundleField
    int selectIndex = 1;

    @AutoBundleField
    Billbean billbean;

    @BindView(R.id.toolbarTitleTV)
    TextView titleView;
    @BindView(R.id.menuView)
    LinearLayout mMenuView;
    @BindView(R.id.billNo)
    AutofitTextView mBillNo;
    @BindView(R.id.kindsTV)
    TextView mKindsTV;
    @BindView(R.id.totalzlTV)
    TextView mTotalzlTV;
    @BindView(R.id.totaljgTV)
    TextView mTotaljgTV;
    @BindView(R.id.gjTV)
    TextView mGjTV;
    @BindView(R.id.carshPay)
    LinearLayout mCarshPay;


    @BindView(R.id.wx_saoma_paylayout)
    LinearLayout wx_saoma_paylayout;

    @BindView(R.id.wx_shuaka_paylayout)
    LinearLayout wx_shuaka_paylayout;

    @BindView(R.id.image_pay_wxurl)
    ImageView image_pay_wxurl;


    PaymentInformationBean pay_bean=new PaymentInformationBean();
    Pay_Data bendi=new Pay_Data();
    boolean yic_pd=false;
    String szImei;
    String    out_trade_no;

    public static boolean isForeground = false;

    protected void onResume() {
        isForeground = true;
        super.onResume();
//        JPushInterface.onResume(this);
    }

    protected void onPause() {
        isForeground = false;
        super.onPause();
//        JPushInterface.onPause(this);
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
    @Override
    public void init() {
        setToolbar();
        setToolbarColor(colors[selectIndex - 1]);
        titleView.setText("手账" + selectIndex);
        setData();
        if(bendi.getorder_data(this,selectIndex)!=null) {
            if (bendi.getorder_data(this,selectIndex).getOridId().toString().equals(billbean.getId())) {
                yic_pd=true;
            }
        }
        String deviceId  = JPushInterface.getUdid(getApplicationContext());//ExampleUtil.getDeviceId(getApplicationContext());
        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        szImei = TelephonyMgr.getDeviceId();
        registerMessageReceiver();

    }

    private void setData() {

        mBillNo.setText(billbean.getBillNo());
        mKindsTV.setText(billbean.getKinds() + "种");
        mTotalzlTV.setText(billbean.getSumQty() + "斤");
        if (billbean.getSumAmt() != billbean.getPayAmt() && billbean.getPayAmt() != 0) {
            mGjTV.setText(df.format(billbean.getPayAmt()) + "");
        }
        mTotaljgTV.setText("￥   " + df.format(billbean.getSumAmt()) + "元");

        if(bendi.getpay_data(this)!=null){

            bendi.post_pay(bendi.getpay_data(mActivity),mActivity);//得到本地数据进行提交。断网数据

        }


    }


    @OnClick(R.id.carshPay)
    public void onCashPayClick() {
        if(pay_state.equals("Y")){
            ToastUtil.makeToast("此订单已支付,请查看对账信息！");

        }else{
            xianjin_pay();

        }
    }


    public void xianjin_pay(){

        if (mGjTV.getText().length() > 0) {
            Api.api().changePayAmt(bindUntilEvent(ActivityEvent.DESTROY), billbean.getId(), mGjTV.getText().toString(), new SimpleRequestListener() {
                @Override
                public void onSuccess(Object o) {
                    startActivity(CashPayActivityAutoBundle.createIntentBuilder(billbean, mGjTV.getText().length() == 0 ? billbean.getSumAmt() : Float.parseFloat(mGjTV.getText().toString()),selectIndex).build(mActivity));
                }

                @Override
                public void onStart() {
                    progressHudUtil.showProgressHud();
                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }
            });
        } else {
            startActivity(CashPayActivityAutoBundle.createIntentBuilder(billbean, mGjTV.getText().length() == 0 ? billbean.getSumAmt() : Float.parseFloat(mGjTV.getText().toString()),selectIndex).build(this));
        }

    }


    /**
     * 进行刷卡支付即将吊起工行app支付
     */
    @OnClick(R.id.card_Pay)
    public void onCard_PayClick() {
        checkBattery();
    }


    String pay_state="Z";
    int aa=1;
    int bb=1;
    public void pay_card(){
        if(CommonUtil.isNetWorkConnected(this)){

            if(pay_state.equals("Y")){
                ToastUtil.makeToast("此订单已支付,请查看对账信息！");

            }else{
//                if(bendi.getpay_data(this)!=null){
                    //提示上传支付信息
//                    aa=2;
//                    bb=1;
//                    MaterialDialog dialog = new MaterialDialog(this);
//                    dialog.setTitle("提示");
//                    dialog.setMessage("你有没提交成功的支付订单信息，请提交成功后再继续操作！");
//                    dialog.setNegativeButton("取消");
//                    dialog.setPositiveButton("确定提交", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(bendi.getpay_data(PayActivity.this).getOridId().toString().equals(billbean.getId())){
//                                bb=2;
//                            }
//                            post_pay(bendi.getpay_data(PayActivity.this));
//                        }
//                    });
//                    dialog.show();
//                }else {
                    if(pay_state.equals("Y")){
                        ToastUtil.makeToast("此订单已支付,请查看对账信息！");
                    }else {
                        bb = 1;

                        Intent intent = new Intent("ICBCScript");
                        Bundle bundle = new Bundle();

                        if (mGjTV.getText().length() > 0) {

                            float money = Float.parseFloat(mGjTV.getText().toString());

                            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                            String zfp = decimalFormat.format(money);//format 返回的是字符串
                            String regEx = "[^0-9]";
                            Pattern p = Pattern.compile(regEx);
                            Matcher m = p.matcher(zfp);
                            String zifu = m.replaceAll("").trim();
                            int strLen = zifu.length();
                            StringBuffer sb = null;
                            while (strLen < 12) {
                                sb = new StringBuffer();
                                sb.append("0").append(zifu);// 左(前)补0
                                // sb.append(str).append("0");//右(后)补0
                                zifu = sb.toString();
                                strLen = zifu.length();
                            }
//                            bundle.putString("CallStr", "1001|004000000000001");
            bundle.putString("CallStr", "1001|004"+zifu);
                        } else {

                            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                            String zfp = decimalFormat.format(billbean.getSumAmt());//format 返回的是字符串
                            String regEx = "[^0-9]";
                            Pattern p = Pattern.compile(regEx);
                            Matcher m = p.matcher(zfp);
                            String zifu = m.replaceAll("").trim();
                            int strLen = zifu.length();
                            StringBuffer sb = null;
                            while (strLen < 12) {
                                sb = new StringBuffer();
                                sb.append("0").append(zifu);// 左(前)补0
                                // sb.append(str).append("0");//右(后)补0
                                zifu = sb.toString();
                                strLen = zifu.length();
                            }
//                            bundle.putString("CallStr", "1001|004000000000001");
                           bundle.putString("CallStr", "1001|004"+zifu);
                        }
                        try {
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 456);
                            PaymentInformationBean order_bean=new PaymentInformationBean();
                            if (mGjTV.getText().length() > 0) {
                                order_bean.setAmt(mGjTV.getText().toString());
                            }else{
                                order_bean.setAmt(billbean.getPayAmt()+"");
                            }
                            order_bean.setOridId(billbean.getId());
                            order_bean.setPayCorrName("18328502735");
                            order_bean.setPayAmt(billbean.getSumAmt()+"");
                            order_bean.setTransType("O");
                            order_bean.setPayChanner("P");
                            bendi.saveorder_data(this,order_bean,selectIndex);
                        } catch (ActivityNotFoundException aa) {
                            ToastUtil.makeToast("没有安装插件！");
                        }
                    }
//                }
            }
        }else{
            ToastUtil.makeToast("当前网络不可用，请检查网络");
        }




    }
    DecimalFormat df = new java.text.DecimalFormat("#.##");
    @OnClick(R.id.gjTV)
    public void ongj() {

        View view1 = LayoutInflater.from(this).inflate(R.layout.change_price_num_keyboard_layout, null);

        TextView showTV = (TextView) view1.findViewById(R.id.showTV);

        TextView totalzjTV = (TextView) view1.findViewById(R.id.totalzjTV);

        totalzjTV.setText(df.format(billbean.getSumAmt()) + "元");

        NumKeyboardView numkeyboardView = (NumKeyboardView) view1.findViewById(R.id.numkeyboardView);

        numkeyboardView.setShowTextView(showTV);

        AlertDialogUtil.showNumKeyboardDialog(this, view1, view -> {
            if (view.getId() == R.id.sureBtn) {
                if (numkeyboardView.getNumValue() > 0) {


                    DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    String zfp = decimalFormat.format(numkeyboardView.getNumValue());//format 返回的是字符串

                    billbean.setPayAmt(numkeyboardView.getNumValue());
                    EntityManager.saveBill(billbean);
                    mGjTV.setText(zfp);
                }

                AlertDialogUtil.dissMiss();
            }
        });
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
        return R.layout.a_pay_layout;
    }

    /**
     * 工行app支付的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //获取返回字符串，格式及内容说明见“脚本交易返回字符串说明”
        if (requestCode == 456) {
            if(data!=null){
                if(yic_pd){}else{bendi.deletorder(this,selectIndex);}

            String retStr = data.getExtras().getString("ReturnStr");
            Log.d("6666666666666666", retStr);
            parsing_data(retStr);
            }
        }


        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 10001) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    payCodeResult(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }




    }


    /**
     * 刷卡支付调用工行app消费返回数据处理
     */
    public void parsing_data(String data) {
        aa=1;
        bb=1;
        String[] sourceStrArray = data.split("\\|");

        for (int i = 0; i < sourceStrArray.length; i++) {

            if (sourceStrArray[i].toString().length() > 2) {

                if (sourceStrArray[i].toString().substring(0, 3).equals("039")) {
                  if(sourceStrArray[i].toString().length()>3){
                    String pay_v = sourceStrArray[i].toString().substring(3, sourceStrArray[i].toString().length());

                    if (pay_v.equals("00")) {
                        if(bendi.getorder_data(this,selectIndex)!=null) {
                            bendi.deletorder(this,selectIndex);
                        }
                            aa=1;

                        if (mGjTV.getText().length() > 0) {
                            pay_bean.setAmt(mGjTV.getText().toString());
                        }else{
                            pay_bean.setAmt(billbean.getPayAmt()+"");
                        }
                        pay_bean.setOridId(billbean.getId());
                        pay_bean.setPayCorrName("18328502735");
                        pay_bean.setPayAmt(billbean.getSumAmt()+"");
                        pay_bean.setTransType("O");
                        pay_bean.setPayChanner("P");
                        EntityManager.deleteBill(billbean);
                        tag_data(sourceStrArray);
                        ToastUtil.makeToast("支付成功");

                    } else {

                        ToastUtil.makeToast("支付失败");

                    }
                  }else{
                      ToastUtil.makeToast("支付失败");
                  }
                }
            }
        }

    }


    public void tag_data(String[] paydataStrArray) {

        HashMap<String, String> pay_map = new HashMap<String, String>();
        String data_tian="";
        String data_time="";
        String data_jiansuohao="";
        String data_liushuihao="";
        String shanghuhao="";

        for (int i = 0; i < paydataStrArray.length; i++) {

            if (paydataStrArray[i].toString().length() > 2) {


                if (paydataStrArray[i].toString().substring(0, 3).equals("039")) {
                    // 支付状态
                    String pay_zt = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    if (pay_zt.equals("00")) {
                        pay_bean.setState("Y");
                        pay_state="Y";
                    }else{
                        pay_bean.setState("N");
                    }

                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("013")) {
                    // 交易日期（例如：(YYYYMMDD)）
                    String pay_time = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("payData", pay_time);
                    pay_bean.setPayDate(pay_time);
                    data_tian=pay_time;
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("907")) {
                    // 卡类型（例如：中国银行）
                    String pay_bank = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("bankId", pay_bank);
                    pay_bean.setBankId(pay_bank);
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("002")) {
                    // 卡号
                    String pay_card_number = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("accountNo", pay_card_number);
                    pay_bean.setAccountNo(pay_card_number);
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("037")) {
                    // 检索号
                    String pjiansuo = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    data_jiansuohao=pjiansuo;
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("042")) {
                    // 商户号
                    String shanghu = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    shanghuhao=shanghu;
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("012")) {
                    // 交易时间（例如：HHMMSS）
                    String pay_time = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("pay_time", pay_time);
                    pay_bean.setResponseTime(pay_time);
                    data_time=pay_time;
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("902")) {
                    // 结算响应流水ID
                    String pay_ls_id = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("responseId", pay_ls_id);
                    data_liushuihao=pay_ls_id;
                }

            }
        }
        pay_bean.setResponseId(data_liushuihao+","+data_jiansuohao+","+shanghuhao);
        try {
            pay_bean.setPayDate(changeTime(data_tian+data_time));
        } catch (java.lang.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bendi.savepay_data(this,pay_bean);
        post_pay(pay_bean);
    }





    public void post_pay(PaymentInformationBean pay_bean) {

//        pay_bean.setResponseTime("14:22:33");
//        pay_bean.setAccountNo("123456789236890");
//        pay_bean.setBankId("中国银行");
//        pay_bean.setPayDate("2016-11-09");
//        pay_bean.setState("Y");
//        pay_bean.setResponseId("001825");



        List<PaymentInformationBean> pay_bean_list = new ArrayList<>();


        pay_bean_list.add(pay_bean);

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
                bendi.delet(PayActivity.this);
                EntityManager.deleteBill(billbean);//删除订单
                EntityManager.deleteShopCarFoodsByType(selectIndex);//删除对应得购物车信息
                Log.d("6666666666666666", o.data.toString());
                if(aa==1) {
                    Intent intenta = new Intent(PayActivity.this, PayCuccessActivity.class);
                    intenta.putExtra("json_data", o.data.toString());
                    startActivity(intenta);
                    finish();
                }else{
                  if(bb==2){
                      Intent intenta = new Intent(PayActivity.this, PayCuccessActivity.class);
                      intenta.putExtra("json_data", o.data.toString());
                      startActivity(intenta);
                      finish();
                   }else{
                       ToastUtil.makeToast("提交成功，可以开始付款！");
                   }
                }
            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud("结算中...", false);
            }


            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onError(ApiError error) {
//                ToastUtil.makeToast(error.errorMsg);
                if(!TextUtils.isEmpty(error.errorCode)){
                    if(error.errorCode.equals("common.data.ood.err_code")||error.errorCode.equals("mvp.pay.used.response.err_code")){
                        bendi.delet(PayActivity.this);
                    }else  if (error.isApiError() && ("common.access.err_code".equals(error.errorCode)
                            ||("common.connect.err_code").equals(error.errorCode))
                            ||("common.conncect.err_code").equals(error.errorCode)) {
                        toLogin(BootstrapApp.get());
                    }

                }

            }

        });


    }
    // 重新登录
    private static void toLogin(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("reLogin", true);
        context.startActivity(intent);
    }

    public String changeTime(String time)throws Exception{

        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = oldFormat.parse(time);
        return newFormat.format(date);
    }



    /**
     * 通过粘性广播检测电量
     */
    private void checkBattery()
    {
        //通过粘性广播读取电量
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intentBattery = registerReceiver(null, intentFilter);//注意，粘性广播不需要广播接收器
        if(intentBattery!=null)
        {
            //获取当前电量
            int batteryLevel = intentBattery.getIntExtra("level", 0);
            //电量的总刻度
            int batterySum = intentBattery.getIntExtra("scale", 100);
            float rotatio = 100*(float)batteryLevel/(float)batterySum;
            LogUtils.d("currentBattery="+rotatio+"%");
            if(rotatio<15)
            {
                int status=intentBattery.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                if(status==BatteryManager.BATTERY_STATUS_CHARGING)
                {
                    pay_card();
                }
                else
                {
                    getWindow().getDecorView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.makeToast("电量已低于15%,请充电后再刷卡支付！");

                        }
                    }, 100);
                }

            }else{
                pay_card();
            }
        }
    }




    @OnClick(R.id.wx_saoma_paylayout)
    public void onsaomahPayClick() {

        Intent intent = new Intent(mActivity,SMActivity.class);
        startActivityForResult(intent,10001);


    }

    @OnClick(R.id.wx_shuaka_paylayout)
    public void onshuakaPayClick() {
        getIsPay(billbean);
//        payGetCode();


    }

    private void getIsPay(Billbean billBean) {

        PayReques_isorNo payReques_isorNo = new PayReques_isorNo();
        payReques_isorNo.setOrder_no(billBean.getBillNo());
        Api.api().payIsNo(ModelConfig.GET_FROM_ORDERNO,payReques_isorNo, new RequestListener<PayReques_isorNo>() {
            @Override
            public void onSuccess(PayReques_isorNo s) { //付了钱未结算
                ToastUtil.makeToast("该订单已支付，未结算！");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                String time=format.format(new Date());
                //结算
                wx_post_pay(s.getOpenid(),time,s.getOut_trade_no(),s.getTotal_fee(),0);
            }

            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud(false);
            }

            @Override
            public void onError(ApiError error) {
                payGetCode();
            }
        });
    }
    ArrayList<PopupWindow> popupWindowList;
    private void payGetCode() {
        UserInfo userInfo = UserManager.getInstance().getUserInfo();
        PayRequestCode wrap = new PayRequestCode();
        wrap.order_no = billbean.getBillNo();
        wrap.body = "蔬菜购买";//(billBean.getBalAmt() == 0 ? billBean.getPayAmt()*100 : billBean.getBalAmt()*100) + ""
        wrap.total_fee =(int)(mGjTV.getText().length() == 0 ? billbean.getSumAmt()*100 : Float.parseFloat(mGjTV.getText().toString())*100) + "";
        wrap.user_id =  userInfo.getUserId();
        wrap.push_id = userInfo.getPushId();
        wrap.order_id = billbean.getId();
        wrap.order_type="P";
        if (szImei!=null){
            wrap.device_info = szImei;
        }else {
            wrap.device_info = "143333";
        }

        Api.api().payCode(ModelConfig.PAY_CODE_PAY,wrap, new SimpleRequestListener<PayRequestCodeInfo>() {
            @Override
            public void onSuccess(PayRequestCodeInfo o) {

                String url =  o.getCode_img_url().replaceAll("\\\\","");
                LogUtil.v("AAAA",url);
                out_trade_no = o.getOut_trade_no();
                popupWindowList = new ArrayList<>();
                ImageView imvCode = (ImageView) UtilsPopWindow.showCodePopupWindow(image_pay_wxurl,mActivity,popupWindowList);
                Glide.with(mActivity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imvCode);

            }


            @Override
            public void onStart() {
                progressHudUtil.showProgressHud("二维码生成中...", false);
            }


            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }


        });

    }





    private void payCodeResult(String auth_code) {
        UserInfo userInfo = UserManager.getInstance().getUserInfo();
        PayRequestCode_ wrap = new PayRequestCode_();
        wrap.out_trade_no = billbean.getBillNo();
        wrap.body = "蔬菜购买";
        wrap.total_fee =(int)( mGjTV.getText().length() == 0 ? (billbean.getSumAmt()*100) : Float.parseFloat(mGjTV.getText().toString())*100) + "";//
        wrap.user_id =  userInfo.getUserId();
        wrap.auth_code = auth_code;
        if (szImei!=null){
            wrap.device_info = szImei;
        }else {
            wrap.device_info = "143333";
        }
//        ModelConfig.PAY_CODE_

        Api.api().payCodeResult(ModelConfig.PAY_CODE_,wrap, new SimpleRequestListener<PayRequestCodeInfo>() {
            @Override
            public void onSuccess(PayRequestCodeInfo o) {
                EntityManager.deleteBill(billbean);
                LogUtil.v("AAAA",o.toString());
                ToastUtil.makeToast("微信支付成功！");
                wx_post_pay(o.getOpen_id(),o.getTime_end(),o.getOut_trade_no(),o.getTotal_fee(),0);
            }


            @Override
            public void onStart() {
                progressHudUtil.showProgressHud("结算中...", false);
            }


            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }


        });

    }


//微信支付提交
    public void wx_post_pay(String openid,String time,String order_id,String shipay,int qqq) {

        PaymentInformationBean wx_pay_bean=new PaymentInformationBean();
        String     amt_zhi="";
        if (shipay!=null) {
             amt_zhi = (Float.parseFloat(shipay))/100 + "";
        }
        LogUtil.d("微信支付！openid==",openid);
        LogUtil.d("微信支付！orderid==",order_id);
        wx_pay_bean.setResponseId(openid);//流水号
        wx_pay_bean.setPayAmt(billbean.getPayAmt()+"");//应付
        wx_pay_bean.setPayCorrName("");//  客户手机号payCorrNa
        if(qqq==1) {
            wx_pay_bean.setOridId(order_id);//订单id
        }else{
            wx_pay_bean.setOridId(billbean.getId());//订单id
        }
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
                EntityManager.deleteBill(billbean);//删除订单
                EntityManager.deleteShopCarFoodsByType(selectIndex);//删除对应得购物车信息
                    Intent intenta = new Intent(PayActivity.this, PayCuccessActivity.class);
                    intenta.putExtra("json_data", o.data.toString());
                    startActivity(intenta);
                    finish();


            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud("结算中...", false);
            }


            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onError(ApiError error) {
                Logger.writeLog("PayActivity_error", error.errorCode + ":" + error.errorMsg);
                ToastUtil.makeToast(error.errorMsg);
                if(!TextUtils.isEmpty(error.errorCode)){
                    if(error.errorCode.equals("common.data.ood.err_code")||error.errorCode.equals("mvp.pay.used.response.err_code")){
                        bendi.delet(PayActivity.this);
                    }else  if (error.isApiError() && ("common.access.err_code".equals(error.errorCode)
                            ||("common.connect.err_code").equals(error.errorCode))
                            ||("common.conncect.err_code").equals(error.errorCode)) {
                        toLogin(BootstrapApp.get());
                    }

                }

            }

        });


    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//                ToastUtil.makeToast("收到推送消息！");
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                JSONObject object = null;
                if (extras != null) {
                    try {
                        object = new JSONObject(extras);
                        String type = object.getString("type");
                        if (type.equals("weixin.pay.qrCode.result")) { //跳转详情  支付成功后返回
                            String openId = object.getString("openid");
                            String amt = object.getString("total_fee");
                            String order_id = object.getString("order_id");
                            LogUtil.d(mActivity.getClass().getSimpleName() + "", object.toString());
                            if (out_trade_no != null && out_trade_no.equals(object.getString("out_trade_no"))) {
                                if (popupWindowList!=null && popupWindowList.size()>0){
                                    popupWindowList.get(0).dismiss();
                                }
                                Logger.writeLog("PayActivity_openId_amt", openId + ":" + amt);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                String time=format.format(new Date());
                                wx_post_pay(openId,time,order_id,amt,1);
                                //结算
                            } else {
                                ToastUtil.makeToast("订单号不匹配，支付失败！");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
               /* StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!extras.isEmpty()) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());*/
                }
            }
        }

    }

}