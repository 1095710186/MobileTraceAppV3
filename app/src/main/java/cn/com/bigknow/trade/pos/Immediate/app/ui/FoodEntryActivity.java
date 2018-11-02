package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.FoodinfoAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.RecyclerViewItemDecoration;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.CommonUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import cn.jpush.android.a;
import cn.jpush.android.data.m;

/**
 * Created by laixiaoyang 10/11.
 * 常用菜品
 */

public class FoodEntryActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    FoodinfoAdapter mAdapter;
    List<FoodInfo> yFoodInfo;

    @BindView(R.id.caipin_layout_sc)
    CheckBox caipin_layout_sc;


    FrameLayout pic1Layout;
    FrameLayout pic2Layout;
    ImageView iv1;
    ImageView iv2;
    TextView tv1;
    TextView tv2;
    TextView sc_one;
    TextView sc_two;

    View bottomSpaceIV;

    boolean chock_boolean=false;

    @OnClick(R.id.a_foodentry_tv_xz)
    public void onXzOnClick(){ //新增
        Intent intent = new Intent(this, FoodnfoLIistChooseActivity.class);
        startActivity(intent);
    }

    @Override
    public void init() {
        setToolbar();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(
                RecyclerViewItemDecoration.MODE_GRID, getResources().getColor(R.color.t), DensityUtil.dip2px(this, 5), 0, 0));
        mAdapter = new FoodinfoAdapter(this);
        recyclerView.setAdapter(mAdapter);

        addHeaderView();


        caipin_layout_sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int test=yFoodInfo.size();
                if(caipin_layout_sc.isChecked()){
                    if(test==1){
                        sc_one.setVisibility(View.VISIBLE);
                    }else{
                        sc_one.setVisibility(View.VISIBLE);
                        sc_two.setVisibility(View.VISIBLE);
                    }
                    chock_boolean=true;
                    mAdapter.setxianshi(true);
                    mAdapter.notifyDataSetChanged();
                }else{
                    if(test==1){
                        sc_one.setVisibility(View.GONE);
                    }else{
                        sc_one.setVisibility(View.GONE);
                        sc_two.setVisibility(View.GONE);
                    }
                    chock_boolean=false;
                    mAdapter.setxianshi(false);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

             if(!chock_boolean){
                 startActivity(FoodInfoUpdateActivityAutoBundle.createIntentBuilder(yFoodInfo.get(i+2)).build(mActivity));
//                Intent intent = new Intent(FoodEntryActivity.this,FoodInfoUpdateActivity.class);
//                intent.putExtra("FoodInfo",  yFoodInfo.get(i+2));
//                startActivity(intent);
         }

            }
        });
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String food_id= yFoodInfo.get(i+2).getItemId();//菜品id
//                ToastUtil.makeToast(yFoodInfo.get(i+2).getItemName()+"");
                show_Dialog(yFoodInfo.get(i+2));

            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();
        showFoodList();
    }
    private void showFoodList(){
        Api.api().getFoodList(bindUntilEvent(ActivityEvent.DESTROY),  null, "Y", new SimpleRequestListener<BaseEntity<List<FoodInfo>>>() {

            @Override
            public void onSuccess(BaseEntity<List<FoodInfo>> o) {
                if (o!=null && o.getData().size()>0) {
                    yFoodInfo = convertData(o.getData());
                    handleData(yFoodInfo);
//                    mAdapter.setNewData(yFoodInfo);
//                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.makeToast(o.msg);
                }
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

    private void handleData(List<FoodInfo> foodInfos) {
        int size = foodInfos.size();
        if (size == 1) {

            tv1.setText(foodInfos.get(0).getItemName());
            UserManager.getInstance().loadFoodHeadImage(this, iv1, foodInfos.get(0).getImgId());
            pic1Layout.setVisibility(View.VISIBLE);
           /* pic1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FoodEntryActivity.this,FoodInfoUpdateActivity.class);
                    intent.putExtra("FoodInfo",  foodInfos.get(0));
                    startActivity(intent);

                }
            });*/
        } else {

            pic1Layout.setVisibility(View.VISIBLE);
            pic2Layout.setVisibility(View.VISIBLE);
            tv1.setText(foodInfos.get(0).getAlias());
            UserManager.getInstance().loadFoodHeadImage(this, iv1, foodInfos.get(0).getImgId());
            tv2.setText(foodInfos.get(1).getAlias());
            UserManager.getInstance().loadFoodHeadImage(this, iv2, foodInfos.get(1).getImgId());
           /* pic1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FoodEntryActivity.this,FoodInfoUpdateActivity.class);
                    intent.putExtra("FoodInfo",  foodInfos.get(0));
                    startActivity(intent);
                }
            });
            pic2Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FoodEntryActivity.this,FoodInfoUpdateActivity.class);
                    intent.putExtra("FoodInfo",  foodInfos.get(1));
                    startActivity(intent);
                }
            });*/
            if (size <= 5) {
                bottomSpaceIV.setVisibility(View.VISIBLE);
            }
            mAdapter.setNewData(foodInfos.subList(2, foodInfos.size()));
        }


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


    private void addHeaderView()
    {

        View view = LayoutInflater.from(this).inflate(R.layout.foodinfo_head_item_layout,null);
         pic1Layout = (FrameLayout) view.findViewById(R.id.pic1Layout);
        pic2Layout = (FrameLayout) view.findViewById(R.id.pic2Layout);
        bottomSpaceIV = view.findViewById(R.id.bottomSpaceIV);
        pic1Layout.setVisibility(View.GONE);
        pic2Layout.setVisibility(View.GONE);
        iv1 = (ImageView) view.findViewById(R.id.iv1);
        iv2 = (ImageView) view.findViewById(R.id.iv2);
        tv1 = (TextView) view.findViewById(R.id.name1TV);
        tv2 = (TextView) view.findViewById(R.id.name2TV);

        sc_one = (TextView) view.findViewById(R.id.foodinNmwe_one);
        sc_two = (TextView) view.findViewById(R.id.foodinNmwe_two);

        int sWidth = DensityUtil.getScreenW(this);
        int ww = (sWidth - DensityUtil.dip2px(this, 113)) / 2;

        pic1Layout.getLayoutParams().height = ww;
        pic1Layout.getLayoutParams().width = ww;
        pic2Layout.getLayoutParams().height = ww;
        pic2Layout.getLayoutParams().width = ww;

        mAdapter.addHeaderView(view);

        sc_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String food_id= yFoodInfo.get(1).getItemId();//菜品id
                show_Dialog(yFoodInfo.get(0));
            }
        });
        sc_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String food_id= yFoodInfo.get(1).getItemId();//菜品id
                show_Dialog(yFoodInfo.get(1));
            }
        });

        pic1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chock_boolean) {
                    startActivity(FoodInfoUpdateActivityAutoBundle.createIntentBuilder(yFoodInfo.get(0)).build(mActivity));
//                Intent intent = new Intent(FoodEntryActivity.this,FoodInfoUpdateActivity.class);
//                intent.putExtra("FoodInfo",  yFoodInfo.get(0));
//                startActivity(intent);
                }
            }
        });
        pic2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chock_boolean) {
                    startActivity(FoodInfoUpdateActivityAutoBundle.createIntentBuilder(yFoodInfo.get(1)).build(mActivity));
//                Intent intent = new Intent(FoodEntryActivity.this,FoodInfoUpdateActivity.class);
//                intent.putExtra("FoodInfo",  yFoodInfo.get(1));
//                startActivity(intent);
                }
            }
        });



    }

    @Override
    protected boolean autoBindBundle() {
        return false;
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



    private void show_Dialog(FoodInfo foodInfo) {
        String food_id = foodInfo.getId();

//        ToastUtil.makeToast(foodInfo.getAlias()+"");
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.a_foodentry_clear_dialog, null);


        View janyan_back = (View) view1.findViewById(R.id.foodshanchu_layout_quxiao);
        View janyan_jinru = (View) view1.findViewById(R.id.foodshanchu_layout_chechu);

        View layout_chechu_zhi = (View) view1.findViewById(R.id.layout_chechu_zhi);

        janyan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();
            }
        });
        janyan_jinru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CommonUtil.isNetWorkConnected(mActivity)) {
//                    ToastUtil.makeToast("删除菜品id为="+food_id);
                    DeleteFood(food_id,foodInfo.getAlias());
                    dissclose();
                } else {
                    ToastUtil.makeToast("当前网络不可用，请检查网络");
                }


            }
        });

        if (dialog != null && dialog.isShowing()) {
            return;
        }

        dialog = new AlertDialog.Builder(mActivity)
                .create();
        dialog.show();
        WindowManager windowManager = mActivity.getWindowManager();
         display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth())*3/4; //设置宽度
//        lp.height = (int) (display.getHeight())*1/2;
//                (int) (DensityUtil.dip2px(mActivity, 550));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view1);



    }

    Display display;
    static AlertDialog dialog;

    public void dissclose() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }


    private void DeleteFood(String food_id, String foodName){
        Api.api().deleteFoodEntry(bindUntilEvent(ActivityEvent.DESTROY),  food_id, new SimpleRequestListener<BaseEntity<String>>() {

            @Override
            public void onSuccess(BaseEntity<String> o) {
                showFoodList();
                ToastUtil.makeToast(foodName+"删除成功");
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
        return R.layout.a_foodentry_layout;
    }
}
