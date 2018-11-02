package cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tubb.smrv.SwipeMenuLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.BaseSupportFragment;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.TJFragment;
import cn.com.bigknow.trade.pos.Immediate.app.ui.FoodEntryActivity;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;

/**
 * Created by laixy on 2016/10/25.
 * 卖菜
 */

public class TjMcFragment extends BaseSupportFragment {
    @BindView(R.id.f_tj_mc_SRC)
    SimpleSwipeRefreshLayout srf;

    private TextView tvRank; //排名
    private TextView tvXsje; //销售金额
    private TextView tvZzl;// 总重量
    private TextView tvBs;// 笔数
    private RadioGroup rg;  //

    private BaseQuickAdapter mAdapter;

    public static TjMcFragment newInstance() {
        Bundle args = new Bundle();
        TjMcFragment fragment = new TjMcFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initLazyView() {


        mAdapter = new MyAdapter("上报");
        srf.setAdapter(mAdapter);
//        mAdapter.addHeaderView();
        setHeadView();
        mAdapter.addData(new Object());
        mAdapter.addData(new Object());
        mAdapter.addData(new Object());
        mAdapter.addData(new Object());
        mAdapter.addData(new Object());

        mAdapter.notifyDataSetChanged();
       /* srf.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getMcList("N"));
        srf.startRefresh();*/
    }

    String rankStr = null;
    private void setHeadView() {


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.f_tj_head_layout, null);
        rg = (RadioGroup) view.findViewById(R.id.f_tj_head_radipgroup);
        tvRank = (TextView) view .findViewById(R.id.f_tj_head_tvRank);
        tvXsje = (TextView) view .findViewById(R.id.f_tj_head_tvXsje);
        tvZzl = (TextView) view .findViewById(R.id.f_tj_head_tvZzl);
        tvBs = (TextView) view .findViewById(R.id.f_tj_head_tvBs);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.f_tj_head_t:// 今天
                        rankStr = "排名第一！";
                        tvRank.setText("您目前得成绩\n\t"+rankStr+"\n大神签个名呗");
                        tvXsje.setText("100元");
                        tvZzl.setText("60斤");
                        tvBs.setText("2笔");
                        break;
                    case R.id.f_tj_head_z: //周
                        rankStr = "排名第三！";
                        tvRank.setText("您目前得成绩\n\t"+rankStr+"\n大神签个名呗");
                        tvXsje.setText("600元");
                        tvZzl.setText("150斤");
                        tvBs.setText("10笔");
                        break;
                    case R.id.f_tj_head_by: //半月
                        rankStr = "排名第二！";
                        tvRank.setText("您目前得成绩\n\t"+rankStr+"\n大神签个名呗");
                        tvXsje.setText("3600元");
                        tvZzl.setText("1150斤");
                        tvBs.setText("80笔");
                        break;
                    case R.id.f_tj_head_y: //月
                        rankStr = "排名第一！";
                        tvRank.setText("您目前得成绩\n\t"+rankStr+"\n大神签个名呗");
                        tvXsje.setText("66600元");
                        tvZzl.setText("5150斤");
                        tvBs.setText("110笔");
                        break;
                }
            }
        });

        mAdapter.addHeaderView(view);
        rg.check(R.id.f_tj_head_t);
    }

    public void getMcList(String state) {
        Api.api().getFoodEntryList(bindUntilEvent(FragmentEvent.DESTROY), state, new SimpleRequestListener<ListResultWrap<List<FoodEntryInfo>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<FoodEntryInfo>> foodEntryInfoListResultWrap) {
                if (foodEntryInfoListResultWrap.total > 0 && !foodEntryInfoListResultWrap.rows.isEmpty()) {
                        mAdapter.setNewData(foodEntryInfoListResultWrap.rows);
                        mAdapter.notifyDataSetChanged();


                } else {

                    ToastUtil.makeToast("服务器返回数据为空");
                }
            }


            @Override
            public void onFinish() {
                    srf.onRefresh();

            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.f_tj_mc_layout;
    }

    public class MyAdapter extends BaseQuickAdapter {
        public String state;

        public MyAdapter(String state) {
            super(R.layout.w_mc_list_item, null);
            this.state = state;
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, Object foodInfo) {
            /*baseViewHolder
                    .setText(R.id.w_mv_tvName, foodInfo.getItemName())   //买方信息
                    .setText(R.id.w_mv_tvTime, foodInfo.getVendor())    //时间
                    .setText(R.id.w_mv_tvPzNumber, foodInfo.getArea())   //品种数量
                    .setText(R.id.w_mv_tvZlNumber, foodInfo.getBillDate())  //总重量
                    .setTag(R.id.w_mv_tvDdh, foodInfo)  //订单号
                    .setText(R.id.w_mv_tvPrice, state)  //金额
                     .setTag(R.id.w_mv_tvtimeNews, foodInfo);  //时间详情*/



            //详情按钮
            baseViewHolder.setOnClickListener(R.id.w_mv_tvDetail, (view) -> openDetail(baseViewHolder, (Objects) foodInfo));
        }



        /**
         * 展开详细
         */
        public void openDetail(BaseViewHolder baseViewHolder, Objects foodInfo) {
//            ((SwipeMenuLayout) baseViewHolder.getConvertView()).smoothCloseMenu();

        }
    }

}
