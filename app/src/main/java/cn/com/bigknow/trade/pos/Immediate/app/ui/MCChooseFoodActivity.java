package cn.com.bigknow.trade.pos.Immediate.app.ui;

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
import com.yatatsu.autobundle.AutoBundleField;

import java.util.List;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.JHFoodAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.RecyclerViewItemDecoration;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by hujie on 16/10/11.
 * 卖菜选择菜品
 */

public class MCChooseFoodActivity extends BaseActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    JHFoodAdapter mAdapter;

    @AutoBundleField
    int selectIndex = 1;

    @Override
    protected boolean autoBindBundle() {
        return true;
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
                showDialog((FoodInfo) baseQuickAdapter.getItem(i));
            }
        });

        loadData();


    }


    FrameLayout pic1Layout;
    FrameLayout pic2Layout;
    ImageView iv1;
    ImageView iv2;
    TextView tv1;
    TextView tv2;
    View bottomSpaceIV;

    private void addHeaderView() {

        View view = LayoutInflater.from(this).inflate(R.layout.foodinfo_head_item_layout, null);
        pic1Layout = (FrameLayout) view.findViewById(R.id.pic1Layout);
        pic2Layout = (FrameLayout) view.findViewById(R.id.pic2Layout);
        bottomSpaceIV = view.findViewById(R.id.bottomSpaceIV);
        pic1Layout.setVisibility(View.GONE);
        pic2Layout.setVisibility(View.GONE);
        iv1 = (ImageView) view.findViewById(R.id.iv1);
        iv2 = (ImageView) view.findViewById(R.id.iv2);
        tv1 = (TextView) view.findViewById(R.id.name1TV);
        tv2 = (TextView) view.findViewById(R.id.name2TV);

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
            UserManager.getInstance().loadFoodHeadImage(this, iv1, foodInfos.get(0).getImgId());
            pic1Layout.setVisibility(View.VISIBLE);
            pic1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(foodInfos.get(0));
                }
            });
        } else {
            pic1Layout.setVisibility(View.VISIBLE);
            pic2Layout.setVisibility(View.VISIBLE);
            tv1.setText(foodInfos.get(0).getAlias());
            UserManager.getInstance().loadFoodHeadImage(this, iv1, foodInfos.get(0).getImgId());
            tv2.setText(foodInfos.get(1).getAlias());
            UserManager.getInstance().loadFoodHeadImage(this, iv2, foodInfos.get(1).getImgId());
            pic1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(foodInfos.get(0));
                }
            });
            pic2Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(foodInfos.get(1));
                }
            });
            if (size <= 5) {
                bottomSpaceIV.setVisibility(View.VISIBLE);
            }
            mAdapter.setNewData(foodInfos.subList(2, foodInfos.size()));
        }


    }


    private void showDialog(FoodInfo food) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.weight_num_keyboard_layout, null);
        TextView nameTV = (TextView) view1.findViewById(R.id.nameTV);

        TextView showTV = (TextView) view1.findViewById(R.id.showTV);
        ImageView headIV = (ImageView) view1.findViewById(R.id.headIV);
        NumKeyboardView numkeyboardView = (NumKeyboardView) view1.findViewById(R.id.numkeyboardView);

        numkeyboardView.setShowTextView(showTV);

        nameTV.setText(food.getItemName());

        UserManager.getInstance().loadFoodHeadImage(this, headIV, food.getImgId());

        AlertDialogUtil.showNumKeyboardDialog(mActivity, view1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sureBtn) {
                    if (numkeyboardView.getNumValue() == 0) {
                        ToastUtil.makeToast("请输入重量");
                    } else {
                        if(numkeyboardView.getNumValue()<1)
                        {
                            ToastUtil.makeToast("重量不能低于1");
                            return;
                        }
                        ShopCarFood food1 = new ShopCarFood();
                        food1.setImgId(food.getImgId());
                        food1.setItemId(food.getId());
                        food1.setQty(numkeyboardView.getNumValue());
                        food1.setMainUnitId(food.getMainUnitId());
                        food1.setUnitId(food.getUnitId());
                        food1.setItemName(food.getItemName());
                        food1.setShopCarType(selectIndex);
                        EntityManager.saveShopCarFood(food1);
                        EventMamager.getInstance().postEvent(SimpleEventType.ON_CHOOSE_CYCP_BACK);
                        AlertDialogUtil.dissMiss();
                        finish();
                    }

                }
            }
        });
    }

    private void loadData() {
        Api.api().getFoodList(bindUntilEvent(ActivityEvent.DESTROY), "Y", "Y", new SimpleRequestListener<BaseEntity<List<FoodInfo>>>() {
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
