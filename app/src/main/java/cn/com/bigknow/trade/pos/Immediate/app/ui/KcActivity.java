package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MaterialDialog;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import icepick.Injector;

/**
 * Created by laixiaoyang
 * 库存
 */

public class KcActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;




    @BindView(R.id.f_tj_kc_SRC)
    SimpleSwipeRefreshLayout srf;
    @BindView(R.id.f_tj_kc_layout_SRC)
    LinearLayout layoutSRC;

    private BaseQuickAdapter mAdapter;

    private List<FoodInfo> yFoodInfo;


    @OnClick(R.id.btnRe)
    public void btnRe() {
        getKcList();
    }
    @Override
    public void init() {
        setToolbar();

        mAdapter = new MyAdapter();
        srf.setAdapter(mAdapter);

        srf.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getKcList());


    }

    @Override
    protected void onResume() {
        super.onResume();
        srf.startRefresh();
    }

    public void getKcList() {
        Api.api().getFoodList(bindUntilEvent(ActivityEvent.DESTROY),  "Y", null, new SimpleRequestListener<BaseEntity<List<FoodInfo>>>() {
            @Override
            public void onSuccess(BaseEntity<List<FoodInfo>> o) {
                if (o.success==1 && !o.data.isEmpty()) {
                    layoutSRC.setVisibility(View.GONE);
                    srf.setVisibility(View.VISIBLE);
                    yFoodInfo = convertData(o.getData());
                    mAdapter.setNewData(yFoodInfo);
                } else {
                    ToastUtil.makeToast("列表为空");
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
    public void getKcOut(List<String> ids) {
        Api.api().getFoodOut(bindUntilEvent(ActivityEvent.DESTROY), ids, new SimpleRequestListener<BaseEntity<Object>>() {
            @Override
            public void onError(ApiError error) {
                super.onError(error);
            }

            @Override
            public void onSuccess(BaseEntity<Object>  o) {
                ToastUtil.makeToast(o.getMsg());
//                srf.startRefresh();
                deleteListKc(ids);
                EventMamager.getInstance().postEvent(SimpleEventType.ON_CLEANJ_KC_BACK);
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
    private void deleteListKc(List<String> ids) {
        List<FoodInfo> foodInfos = mAdapter.getData();

        if (foodInfos != null) {
            for (String id : ids) {
                for (FoodInfo foodInfo : foodInfos) {
                    if (id.equals(foodInfo.getId())) {
                        foodInfos.remove(foodInfo);
                        break;
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
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
    protected boolean autoBindBundle() {
        return false;
    }
    DecimalFormat df=new   java.text.DecimalFormat("#.##");
    public class MyAdapter extends BaseQuickAdapter <FoodInfo>{

        public MyAdapter() {
            super(R.layout.w_kc_list_item, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, FoodInfo foodInfo) {
            TextView tvNme = baseViewHolder.getView(R.id.w_kc_item_tvName);
            TextView tvNum = baseViewHolder.getView(R.id.w_kc_item_tvNum);
            TextView tvNum1 = baseViewHolder.getView(R.id.w_kc_item_tvNum1);
            String balQty = String.valueOf(foodInfo.getBalQty());

            String[] ss = (df.format(foodInfo.getInvQty()) + "").split("\\.");
            if (ss.length != 0) {
                if (ss.length == 1) {
                    tvNum.setText(ss[0]);
                    tvNum1.setText(".00");
                } else {
                    tvNum.setText(ss[0]);
                    tvNum1.setText("." + ss[1]);
                }
            }
            tvNme.setText(foodInfo.getAlias());

//            baseViewHolder
//                    .setText(R.id.w_kc_item_tvName, foodInfo.getItemName());    //名字


            UserManager.getInstance().loadFoodHeadImage(mContext, (ImageView) baseViewHolder.getView(R.id.w_kc_item_imvHead), foodInfo.getImgId());


            //删除
            baseViewHolder.setOnClickListener(R.id.w_kc_item_imvDel, (view) -> deleKc(baseViewHolder,  foodInfo));

        }



        /**
         * 展开详细
         */
        public void deleKc(BaseViewHolder baseViewHolder, FoodInfo foodInfo) {
            MaterialDialog dialog = new MaterialDialog(mActivity);
            dialog.setTitle("提示");
            dialog.setMessage("确定清除该菜品的库存？");
            dialog.setNegativeButton("取消");
            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getKcOut(new ArrayList(){
                        {
                            add(foodInfo.getId());
                        }

                    });
                    dialog.dismiss();
                }
            });
            dialog.show();

        }
    }


    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> finish());

        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.index_1_right_menu, null);
        menuLayout.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FoodInfo> foodInfos = mAdapter.getData();
                if (foodInfos != null) {

                    List<String> ids = new ArrayList<String>();
                    for (FoodInfo foodInfo : foodInfos) {
                        ids.add(foodInfo.getId());
                    }

                    MaterialDialog dialog = new MaterialDialog(mActivity);
                    dialog.setTitle("提示");
                    dialog.setMessage("确定清除当前所有库存？");
                    dialog.setNegativeButton("取消");
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getKcOut(ids);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
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

    @Override
    public int layoutId() {
        return R.layout.f_tj_kc_layout;
    }
}
