package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.FoodAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.RecyclerViewItemDecoration;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MCLeftTabView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by hujie on 16/10/11.
 */

public class ChooseFoodActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;


    @BindView(R.id.mcLeftTabView)
    MCLeftTabView mcLeftTabView;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @AutoBundleField
    int selectIndex = 1;

    private boolean isFirst = true;

    FoodAdapter mAdapter;


    private void addRightMenu() {
        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.removeAllViews();

        View view = LayoutInflater.from(this).inflate(R.layout.choose_food_right_menu_layout, null);


        menuLayout.addView(view);

        view.setOnClickListener(view1 -> {

            UtilsPopWindow.showMenuWindow(view1,mActivity);

        });

    }

    @Subscribe
    public void onEvent(SimpleEvent event) {
        if (event.type == SimpleEventType.ON_CHOOSE_CYCP_BACK) {
            finish();
        }
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }

    @Override
    public void init() {

        setToolbar();
        addRightMenu();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(
                RecyclerViewItemDecoration.MODE_GRID, getResources().getColor(R.color.t), DensityUtil.dip2px(this, 5), 0, 0));


        mAdapter = new FoodAdapter();

        recyclerView.setAdapter(mAdapter);

        addHeaderView();


        mAdapter.notifyDataSetChanged();


        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                showDialog((FoodInfo) baseQuickAdapter.getItem(i));

            }
        });


        mcLeftTabView.setOnTabSelectedListener(new MCLeftTabView.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int selectIndex) {
                EventMamager.getInstance().postIntEvent(SimpleEventType.ON_MC_LEFT_TAB_CHANGE, selectIndex);
                setToolbarColor(colors[selectIndex - 1]);
                if (isFirst) {
                    isFirst = false;
                } else {
                    finish();
                }
            }
        });
        mcLeftTabView.setSelectTab(selectIndex);
        loadData();

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
        int ww = (sWidth - DensityUtil.dip2px(this, 83)) / 2;

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

            pic1Layout.setOnClickListener(v -> showDialog(foodInfos.get(0)));
        } else {

           List<FoodInfo> newfoodInfos = sortFoodinfo(foodInfos);

            pic1Layout.setVisibility(View.VISIBLE);
            pic2Layout.setVisibility(View.VISIBLE);
            tv1.setText(newfoodInfos.get(0).getAlias());

            tv3.setText(newfoodInfos.get(0).getInvQty() + "斤");
            tv4.setText(newfoodInfos.get(1).getInvQty() + "斤");

            UserManager.getInstance().loadFoodHeadImage(this, iv1, newfoodInfos.get(0).getImgId());
            tv2.setText(newfoodInfos.get(1).getAlias());
            UserManager.getInstance().loadFoodHeadImage(this, iv2, newfoodInfos.get(1).getImgId());
            pic1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(newfoodInfos.get(0));
                }
            });
            pic2Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(newfoodInfos.get(1));
                }
            });
            if (size <= 5) {
                bottomSpaceIV.setVisibility(View.VISIBLE);
            }
            mAdapter.setNewData(newfoodInfos.subList(2, newfoodInfos.size()));
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

    private void showDialog(FoodInfo food) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.weight_num_keyboard_layout, null);
        TextView nameTV = (TextView) view1.findViewById(R.id.nameTV);

        TextView showTV = (TextView) view1.findViewById(R.id.showTV);
        ImageView headIV = (ImageView) view1.findViewById(R.id.headIV);
        NumKeyboardView numkeyboardView = (NumKeyboardView) view1.findViewById(R.id.numkeyboardView);

        numkeyboardView.setShowTextView(showTV);

        nameTV.setText(food.getAlias());//food.getItemName()

        UserManager.getInstance().loadFoodHeadImage(this, headIV, food.getImgId());


        AlertDialogUtil.showNumKeyboardDialog(mActivity, view1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sureBtn) {
                    if (numkeyboardView.getNumValue() == 0) {
                        ToastUtil.makeToast("请输入重量");
                    } else {
                        if (numkeyboardView.getNumValue() <1) {
                            ToastUtil.makeToast("重量不能低于1");
                            return;
                        }

                        EntityManager.saveFoodSortCount(food.getId());

                        ShopCarFood food1 = new ShopCarFood();
                        food1.setImgId(food.getImgId());
                        food1.setItemId(food.getId());
                        food1.setQty(numkeyboardView.getNumValue());
                        food1.setMainUnitId(food.getMainUnitId());
                        food1.setUnitId(food.getUnitId());
                        food1.setItemName(food.getAlias());//food.getItemName()
                        food1.setShopCarType(selectIndex);
                        EntityManager.saveShopCarFood(food1);
                        AlertDialogUtil.dissMiss();
                        finish();
                    }

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLeftTabNum();
    }


    private void setLeftTabNum() {
        mcLeftTabView.setTab1Num(EntityManager.getShopCarFoodsCountByType(1) + "");
        mcLeftTabView.setTab2Num(EntityManager.getShopCarFoodsCountByType(2) + "");
        mcLeftTabView.setTab3Num(EntityManager.getShopCarFoodsCountByType(3) + "");
        mcLeftTabView.setTab4Num(EntityManager.getShopCarFoodsCountByType(4) + "");
        mcLeftTabView.setTab5Num(EntityManager.getShopCarFoodsCountByType(5) + "");
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
            addStatusView(colors[selectIndex - 1]);
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
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(0, 0);
    }

    @Override
    public int layoutId() {
        return R.layout.a_choose_food_layout;
    }
}
