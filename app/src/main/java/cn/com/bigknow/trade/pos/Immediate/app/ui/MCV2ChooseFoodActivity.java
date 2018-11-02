package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.MCV2FoodAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.RecyclerViewItemDecoration;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by hujie on 16/10/11.
 */

public class MCV2ChooseFoodActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @AutoBundleField
    int selectIndex = 1;

    @AutoBundleField
    ArrayList<FoodInfo> foodInfoList;

    MCV2FoodAdapter mAdapter;


    private void addRightMenu() {
        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.removeAllViews();

        View view = LayoutInflater.from(this).inflate(R.layout.choose_food_right_menu_layout, null);


        menuLayout.addView(view);

        view.setOnClickListener(view1 -> {

            UtilsPopWindow.showMenuWindow(view1, mActivity);

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
        setToolbarColor(colors[selectIndex - 1]);
//        addRightMenu();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 3);

                recyclerView.setLayoutManager(layoutManager);

                recyclerView.addItemDecoration(new RecyclerViewItemDecoration(
                        RecyclerViewItemDecoration.MODE_GRID, getResources().getColor(R.color.t), DensityUtil.dip2px(mActivity, 5), 0, 0));


                mAdapter = new MCV2FoodAdapter();


                recyclerView.setAdapter(mAdapter);


                recyclerView.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                        EntityManager.saveFoodSortCount(mAdapter.getData().get(i).getId());
                        EventMamager.getInstance().postObjEvent(SimpleEventType.ON_CHOOSE_FOOD_V2_BACK, baseQuickAdapter.getData().get(i));
                        finish();
                    }
                });

                mAdapter.setNewData(sortFoodinfo(foodInfoList));

                for (FoodInfo foodInfo : foodInfoList) {
                    EntityManager.setFoodSortCount(foodInfo);
                }
            }
        }, 100);

    }


    private List<FoodInfo> sortFoodinfo(List<FoodInfo> foodInfos) {

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
    private void loadFoodsData() {

        Api.api().getFoodList(bindUntilEvent(ActivityEvent.DESTROY), "Y", "Y", new SimpleRequestListener<BaseEntity<List<FoodInfo>>>() {
            @Override
            public void onSuccess(BaseEntity<List<FoodInfo>> o) {
                if (o != null && o.getData().size() > 0) {
                    mAdapter.setNewData(sortFoodinfo(foodInfoList));
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
//        overridePendingTransition(0, 0);
    }

//    @Override
//    public void overridePendingTransition(int enterAnim, int exitAnim) {
//        super.overridePendingTransition(0, 0);
//    }

    @Override
    public int layoutId() {
        return R.layout.a_choose_food_v2_layout;
    }
}
