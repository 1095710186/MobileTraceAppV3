package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.JHFoodAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.RecyclerViewItemDecoration;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by hujie on 16/10/11.
 * 进货选择菜品
 */

public class JHChooseFoodActivity extends BaseActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    JHFoodAdapter mAdapter;

    @OnClick(R.id.a_foodentry_tv_xz)
    public void onXzOnClick(){ //新增
        Intent intent = new Intent(this, FoodnfoLIistChooseActivity.class);
        intent.putExtra("type",SimpleEventType.ON_JH_ADD_BACK);
        startActivity(intent);
    }
    @Override
    public void init() {


        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(
                RecyclerViewItemDecoration.MODE_GRID, getResources().getColor(R.color.t), DensityUtil.dip2px(this, 5), 0, 0));


        mAdapter = new JHFoodAdapter();

        addHeaderView();

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {


                startActivity(XZCDGYSActivityAutoBundle.createIntentBuilder((FoodInfo) baseQuickAdapter.getData().get(i)).build(mActivity));

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {
        if (event.type == SimpleEventType.ON_JH_BACK) {
            finish();
        }else if (event.type ==SimpleEventType.ON_JH_ADD_BACK){
            finish();
        }
    }


    FrameLayout pic1Layout;
    FrameLayout pic2Layout;
    ImageView iv1;
    ImageView iv2;
    TextView tv1;
    TextView tv2;

    TextView tv3;
    TextView tv4;
    View bottomSpaceIV;

    private void addHeaderView() {

        View view = LayoutInflater.from(this).inflate(R.layout.food_head_item_layout, null);
        pic1Layout = (FrameLayout) view.findViewById(R.id.pic1Layout);
        pic2Layout = (FrameLayout) view.findViewById(R.id.pic2Layout);
        bottomSpaceIV = view.findViewById(R.id.bottomSpaceIV);
        pic1Layout.setVisibility(View.GONE);
        pic2Layout.setVisibility(View.GONE);
        iv1 = (ImageView) view.findViewById(R.id.iv1);
        iv2 = (ImageView) view.findViewById(R.id.iv2);
        tv1 = (TextView) view.findViewById(R.id.name1TV);
        tv2 = (TextView) view.findViewById(R.id.name2TV);
        tv3 = (TextView) view.findViewById(R.id.zl1TV);
        tv4 = (TextView) view.findViewById(R.id.zl2TV);
        int sWidth = DensityUtil.getScreenW(this);
        int ww = (sWidth - DensityUtil.dip2px(this, 113)) / 2;

        pic1Layout.getLayoutParams().height = ww;
        pic1Layout.getLayoutParams().width = ww;
        pic2Layout.getLayoutParams().height = ww;
        pic2Layout.getLayoutParams().width = ww;

        mAdapter.addHeaderView(view);


    }

    private void handleData(List<FoodInfo> foodInfos) {
        int size = foodInfos.size();
        if (size == 1) {
            tv1.setText(foodInfos.get(0).getAlias());
            tv3.setText(foodInfos.get(0).getInvQty() + "斤");
            UserManager.getInstance().loadFoodHeadImage(this, iv1, foodInfos.get(0).getImgId());
            pic1Layout.setVisibility(View.VISIBLE);
            pic1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(XZCDGYSActivityAutoBundle.createIntentBuilder(foodInfos.get(0)).build(mActivity));

                }
            });
        } else {

            sortFoodinfo(foodInfos);
            pic1Layout.setVisibility(View.VISIBLE);
            pic2Layout.setVisibility(View.VISIBLE);

            tv3.setText(foodInfos.get(0).getInvQty() + "斤");
            tv4.setText(foodInfos.get(1).getInvQty() + "斤");


            tv1.setText(foodInfos.get(0).getAlias());
            UserManager.getInstance().loadFoodHeadImage(this, iv1, foodInfos.get(0).getImgId());
            tv2.setText(foodInfos.get(1).getAlias());
            UserManager.getInstance().loadFoodHeadImage(this, iv2, foodInfos.get(1).getImgId());
            pic1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(XZCDGYSActivityAutoBundle.createIntentBuilder(foodInfos.get(0)).build(mActivity));
                }
            });
            pic2Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(XZCDGYSActivityAutoBundle.createIntentBuilder(foodInfos.get(1)).build(mActivity));
                }
            });
            if (size <= 5) {
                bottomSpaceIV.setVisibility(View.VISIBLE);
            }
            mAdapter.setNewData(foodInfos.subList(2, foodInfos.size()));
        }

    }
    private List<FoodInfo> sortFoodinfo(List<FoodInfo> foodInfos) {

        for (FoodInfo foodInfo : foodInfos) {
            EntityManager.setFoodSortCount(foodInfo);
        }

        Collections.sort(foodInfos, new Comparator<FoodInfo>() {
            @Override
            public int compare(FoodInfo o1, FoodInfo o2) {
                if (o1.getSortCount() > o2.getSortCount()) {
                    return -1;
                } else if (o1.getSortCount() == o2.getSortCount()) {
                    return 0;
                }
                return 1;
            }
        });
        return foodInfos;


    }

    private void loadData() {
        Api.api().getFoodList(bindUntilEvent(ActivityEvent.DESTROY), null, "Y", new SimpleRequestListener<BaseEntity<List<FoodInfo>>>() {
            @Override
            public void onSuccess(BaseEntity<List<FoodInfo>> o) {
                if (o != null && o.getData().size() > 0) {
                    handleData(o.data);
                }
            }

            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud();
            }
        });
    }


    @Override
    public int layoutId() {
        return R.layout.a_jh_choose_food_layout;
    }
}
