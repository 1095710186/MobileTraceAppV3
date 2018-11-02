package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fyales.tagcloud.library.TagCloudLayout;
import com.google.android.flexbox.FlexboxLayout;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.TgbaseAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodAlias;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeDetailModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by laixiaoyang on 2016/10/12..
 * 改名字
 */


public class FoodNameChangeActivity extends BaseActivity {


    @BindView(R.id.container)
    TagCloudLayout mContainer;

    @BindView(R.id.flexbox)
    FlexboxLayout flexboxLayout;

    @BindView(R.id.a_food_namechange_tvName)
    TextView tvName;

    @BindView(R.id.a_food_namechange_imvHead)
    ImageView imvHead;
    @BindView(R.id.actionbar)
    Toolbar mToolbar;

    private TgbaseAdapter mAdapter;
    private List<FoodAlias> mList;
    private FoodAlias foodAlias;

    private FoodTypeDetailModel corrInfo;


    @OnClick(R.id.a_foof_namechange_submit)
    public void onFinish(){
        finish();
    }
    @Override
    public void init() {
        setToolbar();
        corrInfo = (FoodTypeDetailModel) getIntent().getSerializableExtra("foodtypedetail");
        if (corrInfo!=null) {
            UserManager.getInstance().loadFoodHeadImage(this, imvHead, corrInfo.getImgId());
            tvName.setText(corrInfo.getName());
        }
        mList = new ArrayList<>();
        mAdapter = new TgbaseAdapter(this,mList);
        mContainer.setAdapter(mAdapter);

        Api.api().changeFoodName(bindUntilEvent(ActivityEvent.DESTROY),  corrInfo.getId(), null, new SimpleRequestListener<List<FoodAlias>>() {
            @Override
            public void onSuccess(List<FoodAlias> o) {
                if (o!=null && o.size()>0) {
                    mList = o;
                    flexboxLayout.removeAllViews();
                    if (mList!=null && mList.size()>0) {
                        flexboxLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < mList.size(); i++) {

                            View tvView = LayoutInflater.from(mActivity).inflate(R.layout.rb_food, null);
                            RadioButton rbt = (RadioButton) tvView.findViewById(R.id.rb);
                            rbt.setText(mList.get(i).getAlias());
                            rbt.setChecked(false);
//                            rbt.setBackgroundResource(foodTVBGs[i % 4]);
                            flexboxLayout.addView(tvView);
                            final int position = i;
                            rbt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b){
                                        rbt.setChecked(true);
                                        update(position);
                                    }else {
                                        rbt.setChecked(false);
                                    }
                                }
                            });

                        }
                    }else {
                        flexboxLayout.setVisibility(View.GONE);
                    }
//                    mAdapter.update(convertData(mList));
//                    mAdapter.notifyDataSetChanged();
                } else {
//                    ToastUtil.makeToast();
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


        mContainer.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                mAdapter.setCheck(position);
                foodAlias = mAdapter.getItem(position);
                ToastUtil.makeToast(mList.get(position).getAlias());
            }
        });
    }

    void  update(int position){
        if (mList!=null && mList.size()>0) {
            for (int i = 0; i < mList.size(); i++) {
                View tvView = flexboxLayout.getChildAt(i);
                RadioButton rbt = (RadioButton) tvView.findViewById(R.id.rb);
                if (position == i) {
                    rbt.setChecked(true);
                    EventMamager.getInstance().postString_2Event(SimpleEventType.ON_FOODNAME_CHANGE, mList.get(i).getAlias(),mList.get(i).getId());
                }else {
                    rbt.setChecked(false);
                }
            }
        }
    }

    private List<FoodAlias> convertData(List list) {

        try {
            FoodAlias task = (FoodAlias) list.get(0);
            return list;
        } catch (ClassCastException e) {
            List<FoodAlias> tasks = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof JSONObject) {
                    tasks.add(JSON.parseObject(((JSONObject) list.get(i)).toJSONString(), FoodAlias.class));
                } else if (list.get(i) instanceof FoodAlias) {
                    tasks.add((FoodAlias) list.get(i));
                }
            }
            return tasks;
        }


    }
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> finish());
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
        return R.layout.a_food_namechange_layout;
    }

    @Override
    public boolean supportActionbar() {
        return false;
    }
    @Override
    protected boolean autoBindBundle() {
        return true;
    }



}
