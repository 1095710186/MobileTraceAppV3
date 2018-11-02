package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.widget.PrinterModule;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.Contant;

/**
 * Created by wushwie on 2016/11/4.
 */

public class PayCuccessActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;

//    //打印
//    @BindView(R.id.)
//    TextView kaishi_yuyin;


    //种数
    @BindView(R.id.a_paycuccess_zhongs)
    TextView textview_zs;
    //斤数+件数
    @BindView(R.id.a_paycuccess_jinsjians)
    TextView textview_zsjs;
    //付款金额
    @BindView(R.id.a_paycuccess_zprice)
    TextView textview_fkje;
    //付款人
    @BindView(R.id.a_paycuccess_fukr)
    TextView textview_fkr;
    //支付状态
    @BindView(R.id.a_paycuccess_zhuangt)
    TextView textview_zfzt;
    //交易时间
    @BindView(R.id.a_paycuccess_time)
    TextView textview_zftime;
    //支付方式
    @BindView(R.id.a_paycuccess_zffs)
    TextView textview_payfs;
    //订单号
    @BindView(R.id.a_paycuccess_ordernumber)
    TextView textview_ordernum;

    EditText asd;
    private PrinterModule printerModule;
    HashMap<String, Object> json_map;
    ArrayList<HashMap<String, Object>> list_map_data;

    @Override
    public void init() {
        setToolbar();
        json_map = new HashMap<String, Object>();
        list_map_data = new ArrayList<HashMap<String, Object>>();
        getdata();
        init_dayin();


    }

    //得到listmap数据
    public void getdata() {
        String json_data = getIntent().getStringExtra("json_data");
//    textview_zs.setText(json_data);
        try {
            json_map = json_map(json_data);
            list_map_data = getjson(json_map.get("orderDetList").toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.makeToast("解析异常");
            return;
        }


        buildviewa();
    }

    //初始化打印功能
    public void init_dayin() {

        //初始化打印功能
        printerModule = new PrinterModule(this) {
            @Override
            protected void showMessage(String msg) {
//                ToastUtil.makeToast(msg);
            }

            @Override
            protected void onDeviceServiceCrash() {
                // Handle in 'PrinterActivity'
                PayCuccessActivity.this.onDeviceServiceCrash();
            }
        };

    }

    //往界面set数据
    public void buildviewa() {

        textview_zs.setText(json_map.get("kinds").toString() + "种"); //种数
        textview_zsjs.setText(json_map.get("sumQty").toString() + "斤"); //斤数
        textview_fkje.setText("￥" + json_map.get("amt").toString()); //付款金额


        String payCorrId = (String) json_map.get("payCorrId");
        if (payCorrId != null) {
            String[] payNames = json_map.get("payCorrInfo").toString().split(",");
            if (payNames != null && payNames.length > 1) {
                try {
                    textview_fkr.setText(payNames[2] + "\t" + payNames[3]);//付款人
                } catch (Exception e) {
                    textview_fkr.setText(Contant.NO_CORRNAME);//付款人
                }

            } else {
                textview_fkr.setText(Contant.NO_CORRNAME);//付款人
            }
        } else {
            textview_fkr.setText(Contant.NO_CORRNAME);
        }

        if (!TextUtils.isEmpty(json_map.get("state").toString())) {
            if (json_map.get("state").toString().equals("Y")) {
                textview_payfs.setText("支付成功");  //支付状态
            } else {
                textview_payfs.setText("支付失败");  //支付状态
            }

        } else {
            textview_zfzt.setText("支付成功"); //支付状态
        }


        textview_zftime.setText(json_map.get("payDate").toString());  //交易时间

        if (!TextUtils.isEmpty(json_map.get("payChanner").toString())) {
            if (json_map.get("payChanner").toString().equals("P")) {
                textview_payfs.setText("刷卡支付");  //支付方式
            } else {
                if (json_map.get("payChanner").toString().equals("B")) {
                    textview_payfs.setText("联名卡支付");  //支付方式
                } else {
                    if (json_map.get("payChanner").toString().equals("C")) {
                        textview_payfs.setText("现金支付");  //支付方式
                    }
                    if (json_map.get("payChanner").toString().equals("W")) {
                        textview_payfs.setText("微信支付");  //支付方式
                    }

                }
            }
        } else {
            textview_payfs.setText("刷卡支付");  //支付方式
        }

//    textview_payfs.setText(json_map.get("bankId").toString());  //支付银行
        textview_ordernum.setText(json_map.get("orderNo").toString());   //订单号

    }


    //点击打印票据
    @OnClick(R.id.a_paycuccess_dypj)
    public void onDaYingClick() {

        printerModule.print(list_map_data, json_map);

    }


    //点击退出
    @OnClick(R.id.a_paycuccess_tuichu)
    public void ontuiClick() {
//        Intent intenta = new Intent(PayCuccessActivity.this, CameraActivity.class);
//        startActivity(intenta);
        finish();

    }

    @Override
    public void finish() {

        Intent intent = new Intent(mActivity, MainActivity.class);
        startActivity(intent);

        super.finish();
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
        return R.layout.a_paycuccess_layout;
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

    public void unbindDeviceService() {

        DeviceService.logout();
        isDeviceServiceLogined = false;


    }


    public HashMap<String, Object> json_map(String json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            JSONObject jsonObject = new JSONObject(json);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                map.put(key, jsonObject.getString(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }


    //       json数据解析
    public ArrayList<HashMap<String, Object>> getjson(String jsondata) {
        ArrayList<HashMap<String, Object>> listmap = new ArrayList<HashMap<String, Object>>();
        try {
            JSONArray jsonArray = new JSONArray(jsondata);
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    map.put(key, jsonObject.getString(key));
                }
                listmap.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listmap;

    }


}

