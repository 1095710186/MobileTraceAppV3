package cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.BaseSupportFragment;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;

/**
 * Created by laixy on 2016/10/25.
 * 库存
 */

public class TjKcFragment extends BaseSupportFragment {
    @BindView(R.id.f_tj_kc_SRC)
    SimpleSwipeRefreshLayout srf;
    @BindView(R.id.f_tj_kc_layout_SRC)
    LinearLayout layoutSRC;

    private BaseQuickAdapter mAdapter;

    private List<FoodInfo> yFoodInfo;
    public static TjKcFragment newInstance() {
        Bundle args = new Bundle();
        TjKcFragment fragment = new TjKcFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initLazyView() {


        mAdapter = new MyAdapter();
        srf.setAdapter(mAdapter);

        srf.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getKcList());
        srf.startRefresh();
        layoutSRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKcList();
            }
        });
    }



    public void getKcList() {
        Api.api().getFoodList(bindUntilEvent(FragmentEvent.DESTROY),  "Y", null, new SimpleRequestListener<BaseEntity<List<FoodInfo>>>() {
            @Override
            public void onSuccess(BaseEntity<List<FoodInfo>> o) {
                if (o.success==1 && !o.data.isEmpty()) {
                    layoutSRC.setVisibility(View.GONE);
                    srf.setVisibility(View.VISIBLE);
                    yFoodInfo = convertData(o.getData());
                    mAdapter.setNewData(yFoodInfo);

                } else {
                    ToastUtil.makeToast(o.msg);
                    layoutSRC.setVisibility(View.VISIBLE);
                    srf.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFinish() {
                srf.stopRefresh();
            }
        });
    }

    private List<FoodInfo> convertData(List list) {

        try {
            FoodInfo task = (FoodInfo) list.get(0);
            return list;
        } catch (ClassCastException e) {
            List<FoodInfo> tasks = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof JSONObject) {
                    tasks.add(JSON.parseObject(((JSONObject) list.get(i)).toJSONString(), FoodInfo.class));
                } else if (list.get(i) instanceof FoodInfo) {
                    tasks.add((FoodInfo) list.get(i));
                }
            }
            return tasks;
        }


    }
    @Override
    public int layoutId() {
        return R.layout.f_tj_kc_layout;
    }

    public class MyAdapter extends BaseQuickAdapter <FoodInfo>{

        public MyAdapter() {
            super(R.layout.w_kc_list_item, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, FoodInfo foodInfo) {
            baseViewHolder
                    .setText(R.id.w_kc_item_tvName, foodInfo.getItemName())    //名字
                    .setText(R.id.w_kc_item_tvNum, foodInfo.getBalQty()+"") ;  //总重量
//                    .setText(R.id.w_kc_item_tvNum1, foodInfo.getBillDate())  //总重量


            UserManager.getInstance().loadFoodHeadImage(getActivity(), (ImageView) baseViewHolder.getView(R.id.w_kc_item_imvHead), foodInfo.getImgId());


            //删除
            baseViewHolder.setOnClickListener(R.id.w_kc_item_imvDel, (view) -> deleKc(baseViewHolder,  foodInfo));
        }



        /**
         * 展开详细
         */
        public void deleKc(BaseViewHolder baseViewHolder, FoodInfo foodInfo) {
            ToastUtil.makeToast("删除 接口开发中！");

        }
    }

}
