package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.BuildConfig;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.app.util.DataCleanManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.FileSizeUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.MainUiManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MerchantInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Version;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

import static cn.com.bigknow.trade.pos.Immediate.app.util.MainUiManager.downLoad;

/**
 * Created by laixiaoyang on 2016/
 * 关于
 */


public class AboutInfoActivity extends BaseActivity {

    /**
     * 版本
     */
    @BindView(R.id.activity_about_tvVerson)
    TextView tvVerson;

    /**
     * 更新
     */
    @BindView(R.id.activity_about_tvUpdadte)
    TextView tvUpdate;


    /**
     * 清除缓存
     */
    @BindView(R.id.activity_about_tvClean)
    TextView tvClean;

    private boolean isUpdate = false;  //false  可更新，true 更新过

    public AlertDialog.Builder builder;
    public AlertDialog dialog;
    private String size;

    @OnClick(R.id.activity_about_tvClean)
    public void clean() { //清除缓存
        try {
            size = DataCleanManager.getTotalCacheSize(BootstrapApp.get());
            if (size != null) {
//                createDialog(this);
//                dialog.show();
            } else {
                ToastUtil.makeToast("没有可清理的了！");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        progressHudUtil.showProgressHud("正在清除缓存...", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileSizeUtil.clearAllCacheSize(mActivity);
                progressHudUtil.dismissProgressHud();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.makeToast("清除成功");
                    }
                });

            }
        }).start();

    }

    @OnClick(R.id.activity_about_tvUpdadte)
    public void update() { //更新
        if (!isUpdate) {//可更新


        }
        if (URL != null) {
            downLoad(mActivity, URL);
            tvUpdate.setBackgroundResource(R.drawable.shape_tv_update_pre);
            tvUpdate.setText("更新中...");
        }
//        CheckVersionUpdate.instanceFragment().show(getSupportFragmentManager(), "");
//        MainUiManager.checkUpdate(BootstrapApp.get());
    }


    @Override
    public void init() {
        setTitle("关于");
        String orgId = UserManager.getInstance().getUserInfo().getOrgId();
        if (!TextUtils.isEmpty(orgId)) {
//            requestMerchantInfo(orgId);
        }
       /* if (!isUpdate) {//可更新
            tvUpdate.setBackgroundResource(R.drawable.shape_tv_upda);
        } else {
            tvUpdate.setBackgroundResource(R.drawable.shape_tv_update_pre);
        }*/

        tvVerson.setText("当前版本  " + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");

        checkUpdate(BootstrapApp.get());
    }

    public static String DOWNLOAD_URL = ModelConfig.MVP_URL + "servlet/fileupload?oper_type=download&fileId=";
    public static String URL;

    public void checkUpdate(Context context) {

        String version = BuildConfig.VERSION_NAME;
        Api.api().checkUpdate(version + "", new SimpleRequestListener<Version>() {
            @Override
            public void onSuccess(Version o) {
//                downLoad(context, DOWNLOAD_URL + o.getFileId());
                URL = DOWNLOAD_URL + o.getFileId();
                tvUpdate.setText("更新版本(" + o.getVersion() + ")");
                tvUpdate.setFocusable(true);
                tvUpdate.setBackgroundResource(R.drawable.shape_tv_upda);
                isUpdate = false;
                createDialog(BootstrapApp.get(),o);
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
                if (tvUpdate!=null) {
                    tvUpdate.setText("更新版本");
                    tvUpdate.setFocusable(false);
                    tvUpdate.setBackgroundResource(R.drawable.shape_tv_update_pre);
                    isUpdate = true;
                }
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
    public void createDialog(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage("检测到" + size + "大小的缓存，确定清除？");
        builder.setPositiveButton("确定", (dialog, id) -> {
            DataCleanManager.clearAllCache(this);
            ToastUtil.makeToast("清除完毕！");
            dialog.dismiss();

        });
        builder.setNegativeButton("取消", (dialog, id) -> {
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }

    public void setStatusColor() {
        //5.0上面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addStatusView(R.color.colorAccent);
        }

    }

    public static int getAPPVersionCodeFromAPP(Context ctx) {
        int currentVersionCode = 0;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
            LogUtil.d("AboutInfoActivity", currentVersionCode + " " + appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch blockd
            e.printStackTrace();
        }
        return currentVersionCode;
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
        return R.layout.a_about_layout;
    }

    public void requestMerchantInfo(String orgId) {
        Api.api().getMerchantInfo(bindUntilEvent(ActivityEvent.DESTROY), orgId, new SimpleRequestListener<MerchantInfo>() {
            @Override
            public void onSuccess(MerchantInfo merchantInfo) {
              /*  shopid_TV.setText(StringUtil.getString(merchantInfo.getMerchantCode()));
                shopname_TV.setText(StringUtil.getString(merchantInfo.getMerchantName()));
                shoptype_TV.setText(StringUtil.getString(merchantInfo.getTypeName()));
                idname_TV.setText(StringUtil.getString(merchantInfo.getResponer()));
                idtype_TV.setText(StringUtil.getString(merchantInfo.getCardTypeName()));
                idnum_TV.setText(StringUtil.getString(merchantInfo.getCardNo()));
                belong_TV.setText(StringUtil.getString(merchantInfo.getOrgName()));
                shopnum_TV.setText(StringUtil.getString(merchantInfo.getMerchantNo()));
                contact_TV.setText(StringUtil.getString(merchantInfo.getContacter()));
                phonenum_TV.setText(StringUtil.getString(merchantInfo.getTel()));*/
            }
        });
    }
}
