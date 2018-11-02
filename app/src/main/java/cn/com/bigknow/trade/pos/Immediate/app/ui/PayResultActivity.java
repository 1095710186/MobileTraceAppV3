package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yatatsu.autobundle.AutoBundleField;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayResult;

import static cn.com.bigknow.trade.pos.Immediate.R.id.tvPaySum;

/**
 * Created by hujie on 16/11/7.
 */

public class PayResultActivity extends BaseActivity {

    @AutoBundleField
    PayResult payResult;

    @AutoBundleField
    Billbean billbean;
    @BindView(R.id.ivStatus)
    ImageView mIvStatus;
    @BindView(R.id.tvResult)
    TextView mTvResult;
    @BindView(R.id.llHeadView)
    LinearLayout mLlHeadView;
    @BindView(R.id.viewLine)
    View mViewLine;
    @BindView(R.id.kindsTV)
    TextView mKindsTV;
    @BindView(R.id.totalzlTV)
    TextView mTotalzlTV;
    @BindView(tvPaySum)
    TextView mTvPaySum;
    @BindView(R.id.tvPayName)
    TextView mTvPayName;
    @BindView(R.id.tvPaySttaus)
    TextView mTvPaySttaus;
    @BindView(R.id.tvPayTime)
    TextView mTvPayTime;
    @BindView(R.id.tvPayType)
    TextView mTvPayType;
    @BindView(R.id.tvBillNO)
    TextView mTvBillNO;
    @BindView(R.id.tvWaterNO)
    TextView mTvWaterNO;



    @Override
    public void init() {
        setData();

    }

    private void setData() {
        mKindsTV.setText(billbean.getKinds() + "种");
        mTotalzlTV.setText(billbean.getSumQty() + "斤");
        if ("C".equals(payResult.getPayChanner())) {
            mTvPayType.setText("现金支付");
        }

        if ("Y".equals(payResult.getState())) {
            mTvPaySttaus.setText("支付成功");
        } else {
            mTvPaySttaus.setText("支付失败");
        }

        mTvPayTime.setText(payResult.getPayDate());
        mTvBillNO.setText(payResult.getBillNo());
        mTvPaySum.setText("￥  " + billbean.getPayAmt() + "元");

        String[] payNames = payResult.getPayCorrInfo().split(",");
        if (payNames != null && payNames.length > 1) {
            try {
                mTvPayName.setText(payNames[2]);
            } catch (Exception e) {
                mTvPayName.setText("非系统用户");
            }

        } else {
            mTvPayName.setText("非系统用户");
        }


    }

    @OnClick(R.id.tuichu)
    public void onTuichu()
    {
        finish();
    }


    @Override
    public void finish() {

        Intent intent = new Intent(mActivity,MainActivity.class);
        startActivity(intent);

        super.finish();
    }

    @Override
    protected boolean autoBindBundle() {
        return true;
    }

    @Override
    public int layoutId() {
        return R.layout.a_payresult_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
