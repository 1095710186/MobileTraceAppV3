package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.CustomerBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayInfoBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by hujie on 16/10/13.
 * 现金支付
 */

public class CashPayActivity extends BaseActivity {
    @BindView(R.id.cashTV)
    TextView mCashTV;
    @BindView(R.id.srsjhTV)
    TextView mSrsjhTV;

    @AutoBundleField
    public Billbean billbean;
    @AutoBundleField
    public float cash;

    @AutoBundleField
    int selectIndex = 1;

    public CustomerBean customerBean;

    @Override
    protected boolean autoBindBundle() {
        return true;
    }

    @Override
    public void init() {
        setTitle("现金支付");
        mCashTV.setText(cash + "");
    }


    @Override
    public int layoutId() {
        return R.layout.a_cash_pay_layout;
    }

    @OnClick(R.id.txl)
    public void onTXL() {
        startActivity(new Intent(this, CustomerActivity.class));
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {
        if (event.type == SimpleEventType.ON_CHOOSE_CUSTOMER_BACK) {
            customerBean = (CustomerBean) event.objectEvent;
            mSrsjhTV.setText(customerBean.getUserName() + "("
                    + customerBean.getUserNo() + ")");
        }
    }

    @OnClick(R.id.srsjhTV)
    public void onSRSJH() {
        View view = LayoutInflater.from(this).inflate(R.layout.phone_num_keyboard_layout, null);

        TextView showTV = (TextView) view.findViewById(R.id.showTV);
        NumKeyboardView numKeyboardView = (NumKeyboardView) view.findViewById(R.id.numkeyboardView);
        numKeyboardView.setShowTextView(showTV);
        numKeyboardView.setTypePhone(1);
        AlertDialogUtil.showNumKeyboardDialog(this, view, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sureBtn) {
                    if (showTV.getText().length() < 11 || showTV.getText().toString().contains(".")) {
                        ToastUtil.makeToast("请输入正确的手机号");
                        return;
                    }
                    customerBean = null;
                    String phone = showTV.getText().toString().trim();
                    if (phone.length()>11){
                        phone = phone.substring(0,11);
                    }
                    mSrsjhTV.setText(phone);
                    AlertDialogUtil.dissMiss();
                }
            }
        });
    }


    @OnClick(R.id.cancelBtn)
    public void onCancleBtnClick() {
        finish();
    }


    @OnClick(R.id.sureBtn)
    public void onSureBtnClick() {
//        if (customerBean == null && mSrsjhTV.getText().length() == 0) {
//            ToastUtil.makeToast("请输入客户手机号");
//            return;
//        }


        String phone = mSrsjhTV.getText().toString();

        if (phone.trim().length() > 0 && phone.trim().length() < 11) {
            ToastUtil.makeToast("请输入正确的手机号码");
            return;
        }

        if (customerBean != null) {
            phone = customerBean.getUserNo();
        }

        if (phone.trim().length() == 0) {
            phone = null;
        }


        pay(phone);

    }

    private void pay(String phone) {

        RequestWrap<PayInfoBean> payInfoBeanRequestWrap = new RequestWrap<>();

        payInfoBeanRequestWrap.name = "dsMain";

        PayInfoBean payInfoBean = new PayInfoBean();
        payInfoBean.setState("Y");
        payInfoBean.setOridId(billbean.getId());
        payInfoBean.setPayChanner("C");
        payInfoBean.setTransType("O");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        payInfoBean.setPayDate(simpleDateFormat.format(new Date()));
        payInfoBean.setAmt(billbean.getPayAmt() + "");
        payInfoBean.setPayCorrName(phone);
        payInfoBean.setPayAmt(billbean.getSumAmt() + "");
        if (String.valueOf(cash).length() > 0) {
            payInfoBean.setAmt(cash + "");
        } else {
            payInfoBean.setAmt(billbean.getSumAmt() + "");
        }
        List<PayInfoBean> payInfoBeanList = new ArrayList<>();
        payInfoBeanList.add(payInfoBean);
        payInfoBeanRequestWrap.data = payInfoBeanList;

        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        List<RequestWrap> requestWraps = new ArrayList<>();
        requestWraps.add(payInfoBeanRequestWrap);

        simpleRequestWrap.dataset = requestWraps;

       /*
        Api.api().pay(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<PayResult>() {
            @Override
            public void onSuccess(PayResult o) {
                ToastUtil.makeToast("支付成功");
                EntityManager.deleteBill(billbean);

                startActivity(PayResultActivityAutoBundle.createIntentBuilder(o, billbean).build(mActivity));
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
        });
        */

        Api.api().PaymentSubmitted(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<BaseEntity<String>>() {
            @Override
            public void onSuccess(BaseEntity<String> o) {
                EntityManager.deleteBill(billbean);//删除订单
                EntityManager.deleteShopCarFoodsByType(selectIndex);//删除对应得购物车信息
                Intent intenta = new Intent(mActivity, PayCuccessActivity.class);
                intenta.putExtra("json_data", o.data.toString());
                startActivity(intenta);
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
                ToastUtil.makeToast(error.toString());
            }

        });


    }


}
