package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.StringUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MerchantInfo;

/**
 * Created by laixiaoyang
 */


public class MerchantInfoActivity extends BaseActivity {

    /**
     * 编号
     */
    @BindView(R.id.merchantinfo_shopid_TV)
    TextView shopid_TV;

    /**
     * 商户名
     */
    @BindView(R.id.merchantinfo_shopname_TV)
    TextView shopname_TV;


    /**
     * 经营类别
     */
    @BindView(R.id.merchantinfo_shoptype_TV)
    TextView shoptype_TV;


    /**
     * 登记者
     */
    @BindView(R.id.merchantinfo_idname_TV)
    TextView idname_TV;


    /**
     * 登记证书
     */
    @BindView(R.id.merchantinfo_idtype_TV)
    TextView idtype_TV;


    /**
     * 证件号
     */
    @BindView(R.id.merchantinfo_idnum_TV)
    TextView idnum_TV;


    /**
     * 所属市场
     */
    @BindView(R.id.merchantinfo_belong_TV)
    TextView belong_TV;


    /**
     * 商铺号
     */
    @BindView(R.id.merchantinfo_shopnum_TV)
    TextView shopnum_TV;

    /**
     * 联系人
     */
    @BindView(R.id.merchantinfo_contact_TV)
    TextView contact_TV;

    /**
     * 手机号码
     */
    @BindView(R.id.merchantinfo_phonenum_TV)
    TextView phonenum_TV;

    /**
     * 更改绑定
     */
    @BindView(R.id.merchantinfo_phonenum_IBTN)
    ImageButton phonenum_IBTN;


    @Override
    public void init() {
        setTitle("商户信息");
        String orgId = UserManager.getInstance().getUserInfo().getOrgId();
        if (!TextUtils.isEmpty(orgId)) {
            requestMerchantInfo(orgId);
        }
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
        return R.layout.a_merchantinfo_layout;
    }

    public void requestMerchantInfo(String orgId) {
        Api.api().getMerchantInfo(bindUntilEvent(ActivityEvent.DESTROY), orgId, new SimpleRequestListener<MerchantInfo>() {
            @Override
            public void onSuccess(MerchantInfo merchantInfo) {
                shopid_TV.setText(StringUtil.getString(merchantInfo.getMerchantCode()));
                shopname_TV.setText(StringUtil.getString(merchantInfo.getMerchantName()));
                shoptype_TV.setText(StringUtil.getString(merchantInfo.getTypeName()));
                idname_TV.setText(StringUtil.getString(merchantInfo.getResponer()));
                idtype_TV.setText(StringUtil.getString(merchantInfo.getCardTypeName()));
                idnum_TV.setText(StringUtil.getString(merchantInfo.getCardNo()));
                belong_TV.setText(StringUtil.getString(merchantInfo.getOrgName()));
                shopnum_TV.setText(StringUtil.getString(merchantInfo.getMerchantNo()));
                contact_TV.setText(StringUtil.getString(merchantInfo.getContacter()));
                phonenum_TV.setText(StringUtil.getString(merchantInfo.getTel()));
            }

            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud(false);
            }
        });
    }
}
