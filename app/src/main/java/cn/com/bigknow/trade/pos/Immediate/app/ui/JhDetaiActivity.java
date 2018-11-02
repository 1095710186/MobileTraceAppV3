package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yatatsu.autobundle.AutoBundleField;

import java.text.DecimalFormat;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.JhDAdapter;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;

/**
 * Created by laixy
 *
 *
 * 进货详情 (已通过)
 */

public class JhDetaiActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SimpleSwipeRefreshLayout refreshLayout;

    JhDAdapter mAdapter;

    @AutoBundleField
    TXJHDWrapper wrapper;

    @BindView(R.id.tvZ)
    TextView tvZ;
    @BindView(R.id.cpTV)
    TextView mCpTV;
    @BindView(R.id.kindsTV)
    TextView mKindsTV;
    @BindView(R.id.hjTV)
    TextView mHjTV;
    @BindView(R.id.hjLL)
    LinearLayout mHjLL;

    DecimalFormat df=new   java.text.DecimalFormat("#.##");
    String Where=null;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Where!=null && Where.equals("PUSH")){
            startActivity(new Intent(mActivity,MyMsgTypeActivity.class));
            finish();
        }
    }
    @Override
    public void init() {
        Where = getIntent().getStringExtra("WHERE");
        if (Where!=null && Where.equals("PUSH")){
            wrapper = (TXJHDWrapper) getIntent().getSerializableExtra("wrapper");
        }
        setTitle("进货详情");
        mAdapter = new JhDAdapter();
        refreshLayout.setAdapter(mAdapter);
        refreshLayout.setRefreshEnable(false);
        setData();
    }

    @Override
    protected boolean autoBindBundle() {
        return true;
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

            float total = 0;
            for (FoodInfo foodInfo : wrapper.getFoodInfos()) {
                total += foodInfo.getQty();
            }
            mHjTV.setText(df.format(total) + "");

        } else {
            mHjLL.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean autoBindEvent() {
        return false;
    }



    private FoodInfo selectFoodInfo;



    @Override
    public int layoutId() {
        return R.layout.a_jhd_layout;
    }

}
