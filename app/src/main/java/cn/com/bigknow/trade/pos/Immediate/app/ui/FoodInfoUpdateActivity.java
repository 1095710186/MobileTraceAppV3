package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.FoodInfoAreaAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.CommonUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AreaBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FileImgWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeDetailModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodUpdateAddInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

/**
 * Created by laixy on 2016/10/13.
 * x编辑菜品信息界面（基本信息）
 */


public class FoodInfoUpdateActivity extends BaseActivity {
    @BindView(R.id.a_foodinfo_editor_actionbar)
    Toolbar mToolbar;

    @BindView(R.id.food_updata_sc)
    ImageView shanchu;

    @BindView(R.id.a_foodinfo_editor_imvhead)
    ImageView imvHead;
    @BindView(R.id.a_foodinfo_editor_tvname)
    TextView tvName;
    @BindView(R.id.a_foodinfo_editor_tvChangename)
    TextView tvChangename;
    @BindView(R.id.a_foodinfo_editor_recyclerView)
    RecyclerView mRecyclerView;

    @AutoBundleField
    FoodInfo corrInfo;

    @AutoBundleField(required = false)
    boolean isEdit = false;

    public AlertDialog.Builder builder;
    public AlertDialog dialog;

    private FoodInfoAreaAdapter mAdapter;
//    private FoodInfo corrInfo;
    private int where = 1;  //1head;2 item

   /* List<VendorAreaBean> areaBeanList = new ArrayList<>();
    AreaBean areaBean;
    int itemPosition = -1;*/
   private String aliasId = null;

    @OnClick(R.id.a_foodinfo_editor_tvChangename)
    public void onChangeNameOnClick() {
//        ToastUtil.makeToast("修改菜名");
        Intent intent = new Intent(this,FoodNameChangeActivity.class);
        FoodTypeDetailModel foodTypeDetailModel = new FoodTypeDetailModel();
        foodTypeDetailModel.setId(corrInfo.getItemId());
        foodTypeDetailModel.setName(corrInfo.getItemName());
        foodTypeDetailModel.setImgId(corrInfo.getImgId());
        intent.putExtra("foodtypedetail",foodTypeDetailModel);
        startActivity(intent);
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {

        if (event.type == SimpleEventType.ON_FOODNAME_CHANGE) {
                corrInfo.setAlias(event.strEvent);
              aliasId = event.idEvent;
                tvName.setText(event.strEvent);

        }
    }
    @Override
    public void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//把整个Layout顶上去露出获得焦点的EditText,不压缩多余空间.

        areaBeanList = new ArrayList<>();
        if (isEdit) {
//             corrInfo = (FoodInfo) getIntent().getSerializableExtra("foodInfo");
            shanchu.setVisibility(View.GONE);
        }
        if (corrInfo!=null) {
            UserManager.getInstance().loadFoodHeadImage(this, imvHead, corrInfo.getImgId());
            tvName.setText(corrInfo.getAlias());
        }
        setToolbar();
//        addRightMenu();
        shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Dialog(corrInfo.getId());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(
//                RecyclerViewItemDecoration.MODE_GRID, getResources().getColor(R.color.t), DensityUtil.dip2px(this, 5), 0, 0));
        mAdapter = new FoodInfoAreaAdapter();
        mRecyclerView.setAdapter(mAdapter);
        addHeaderView();
        mAdapter.setItemClick(new FoodInfoAreaAdapter.OnListItemChildClick() {
            @Override
            public void OnClick(int position, int type, String vendor) {  //  type :0选择地区 ;1 change;2，dele
                if (type == 0){
                    where =2;
                    itemPosition =  position;
                    Intent intent = new Intent(FoodInfoUpdateActivity.this,FoodAreaLIistChooseActivity.class);
                    startActivityForResult(intent,125);
                }else if (type ==1){
                    itemPosition =  position;
                    areaBeanList.get( itemPosition-1 ).setVendor(vendor);
                    mAdapter.setNewData(areaBeanList);
                    mAdapter.notifyDataSetChanged();

                }else if (type ==2){
//                    ToastUtil.makeToast( "del");
                    itemPosition =  position;
                    areaBeanList.remove( itemPosition-1);
                    mAdapter.setNewData(areaBeanList);
                    mAdapter.notifyDataSetChanged();
                }else if (type == 3){
                    itemPosition =  position;
                    areaBeanList.get( itemPosition-1 ).setVendor(vendor);
//                    mAdapter.setNewData(areaBeanList);
                }
            }
        });

        if (corrInfo!=null) {
            Api.api().getVendorList(bindUntilEvent(ActivityEvent.DESTROY), isEdit?corrInfo.getItemId():corrInfo.getId(), corrInfo.getMerchantId(), new SimpleRequestListener<BaseEntity<List<VendorAreaBean>>>() {
                @Override
                public void onSuccess(BaseEntity<List<VendorAreaBean>> o) {
                    if (o != null && o.getData().size() > 0) {
                        areaBeanList = convertData(o.getData());
                        mAdapter.setNewData(areaBeanList);
                        mAdapter.notifyDataSetChanged();

                    } else {
                        ToastUtil.makeToast(o.msg);
                    }
                }

                @Override
                public void onFinish() {
                    ToastUtil.cancleToast();
                }
            });
        }

    }

    private List<VendorAreaBean> convertData(List list) {

        try {
            VendorAreaBean task = (VendorAreaBean) list.get(0);
            return list;
        } catch (ClassCastException e) {
            List<VendorAreaBean> tasks = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof JSONObject) {
                    tasks.add(JSON.parseObject(((JSONObject) list.get(i)).toJSONString(), VendorAreaBean.class));
                } else if (list.get(i) instanceof VendorAreaBean) {
                    tasks.add((VendorAreaBean) list.get(i));
                }
            }
            return tasks;
        }


    }


    EditText edtArea;
    EditText edtGys;
    List<VendorAreaBean> areaBeanList = new ArrayList<>();
    AreaBean areaBean;
    int itemPosition = -1;
    private void addHeaderView() {

        View view = LayoutInflater.from(this).inflate(R.layout.food_area_item_layout, null);
        edtArea = (EditText) view.findViewById(R.id.food_area_item_edtArea);
        ImageView imvChooseArea = (ImageView) view.findViewById(R.id.food_area_item_imvChooseArea);
        edtGys = (EditText) view.findViewById(R.id.food_area_item_edtGys);
        RelativeLayout relayoutSureAdd = (RelativeLayout) view.findViewById(R.id.food_area_item_relayoutSureAdd);
        relayoutSureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((edtArea.getText().toString().trim()!=null&&!"".equals(edtArea.getText().toString().trim()))
                        ){
//                    ToastUtil.makeToast("添加");
                    if (areaBeanList!=null) {
                        VendorAreaBean vendorAreaBean = new VendorAreaBean();
                        vendorAreaBean.setProdId(String.valueOf(areaBean.getId()));
                        vendorAreaBean.setProdArea(areaBean.getAreaName());
                        vendorAreaBean.setVendor(edtGys.getText().toString().trim());
                        areaBeanList.add(0,vendorAreaBean);
                        mAdapter.setNewData(areaBeanList);
                        mAdapter.notifyDataSetChanged();
                        edtArea.setText("");
                        edtGys.setText("");
                    }
                }else {
                    ToastUtil.makeToast("产地不能为空！");
                }
            }
        });
        imvChooseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                where = 1;
                Intent intent = new Intent(mActivity,FoodAreaLIistChooseActivity.class);
                startActivityForResult(intent,125);
            }
        });

        mAdapter.addHeaderView(view);


    }
    @Override
    public int layoutId() {
        return R.layout.a_foodinfo_update_layout;
    }
    @Override
    public boolean supportActionbar() {
        return false;
    }

    @Override
    protected boolean autoBindBundle() {
        return true;
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
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }
    private void addRightMenu() {
        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.removeAllViews();

        View view =  LayoutInflater.from(this).inflate(R.layout.choose_food_right_menu_layout, null);


        menuLayout.addView(view);

        view.setOnClickListener(view1 -> {


            new SimpleTooltip.Builder(this)
                    .anchorView(view1)
                    .gravity(Gravity.BOTTOM)
                    .dismissOnOutsideTouch(true)
                    .dismissOnInsideTouch(false)
                    .showArrow(false)
                    .modal(true)

                    .transparentOverlay(false)
                    .contentView(R.layout.choose_food_menu_layout)
                    .build()
                    .show();


        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 125){
                if (data == null) {
                    return;
                }
                Bundle MarsBuddle = data.getExtras();
                areaBean = (AreaBean) MarsBuddle.getSerializable("AreaBean");
                if (where == 1){ //head
                    edtArea.setText(areaBean.getAreaName());
                }else if (where == 2){ //item;
                    if (itemPosition>=0) {
                        areaBeanList.get(itemPosition-1).setProdId(String.valueOf(areaBean.getId()));
                        areaBeanList.get(itemPosition-1).setProdArea(areaBean.getAreaName());
                        mAdapter.setNewData(areaBeanList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

    }


    /**
     * 点击完成按钮，请求修改菜品详情
     */
    @OnClick(R.id.a_foodinfo_editor_relayout)
    public void requestAddOrder() {
        if (corrInfo ==null)
            return;
        RequestWrap<FoodUpdateAddInfo> foodUpdateAddInfoRequestWrapf = new RequestWrap<>();
        foodUpdateAddInfoRequestWrapf.name = "dsMain";

        FoodUpdateAddInfo foodInfo = new FoodUpdateAddInfo();
        foodInfo.setId(corrInfo.getId());
        foodInfo.setUnitId(corrInfo.getMainUnitId());
        foodInfo.setSalePrice(String.valueOf(corrInfo.getSalePrice()));
        foodInfo.setItemId(corrInfo.getItemId()); //
        foodInfo.setPurUnitId(corrInfo.getPurUnitId()); //
        foodInfo.setSaleUnitId(corrInfo.getSaleUnitId()); //
        foodInfo.setAliasId(aliasId);

        List<FoodUpdateAddInfo> foodInfos = new ArrayList<>();
        foodInfos.add(foodInfo);
        foodUpdateAddInfoRequestWrapf.data = foodInfos;

        RequestWrap<VendorAreaBean> vendorAreaBeanRequestWrap = new RequestWrap<>();
        vendorAreaBeanRequestWrap.name = "dsDet";
        List<VendorAreaBean> vendorAreaBeanList = new ArrayList<>();
        for (VendorAreaBean bean : areaBeanList) {
//            List<LiveSellBatch> liveSellBatches = JSON.parseArray(liveBatchBeans, LiveSellBatch.class);
            vendorAreaBeanList.add(bean);
        }
        vendorAreaBeanRequestWrap.data = vendorAreaBeanList;

        List<RequestWrap> requestWraps = new ArrayList<>();
        requestWraps.add(foodUpdateAddInfoRequestWrapf);
        requestWraps.add(vendorAreaBeanRequestWrap);

        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        simpleRequestWrap.dataset = requestWraps;

        Api.api().saveFoodInfo(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<BaseEntity<FoodInfo>>() {
                    @Override
                    public void onSuccess(BaseEntity<FoodInfo> baseEntity) {

                       if (baseEntity.success == 1){
                           ToastUtil.makeToast("更新菜品信息成功");
                           if (isEdit) {
                               FoodInfo info = baseEntity.getData();
                               info.setItemId(info.getItemId());
                               info.setAlias(corrInfo.getAlias());
                               info.setItemName(corrInfo.getItemName());
                               info.setImgId(corrInfo.getImgId());
//                               startActivity(XZCDGYSActivityAutoBundle.createIntentBuilder(info).build(mActivity));
                               EventMamager.getInstance().postObjEvent(SimpleEventType.ON_JH_ADD_, info);
//                               EventMamager.getInstance().postObjEvent(SimpleEventType.ON_JH_ADD_CD, info);
                           }
                           finish();
                       }
                    }

                    @Override
                    public void onError(ApiError error) {
                        super.onError(error);
                        ToastUtil.makeToast("保存菜品信息失败");
//                        progressHudUtil.dismissProgressHud();
                    }
                    @Override
                    public void onStart() {
                        progressHudUtil.showProgressHud("保存中...", false);
                    }

                    @Override
                    public void onFinish() {
                        progressHudUtil.dismissProgressHud();
                    }
                }
        );

    }



    private void show_Dialog(String food_id) {



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
                    DeleteFood(food_id);
                    dissclose();
                } else {
                    ToastUtil.makeToast("当前网络不可用，请检查网络");
                }


            }
        });

        if (dialoga != null && dialoga.isShowing()) {
            return;
        }

        dialoga = new AlertDialog.Builder(mActivity)
                .create();
        dialoga.show();
        WindowManager windowManager = mActivity.getWindowManager();
        Display  display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialoga.getWindow().getAttributes();
        lp.width = (int) (display.getWidth())*3/4; //设置宽度
//        lp.height = 872;
//                (int) (DensityUtil.dip2px(mActivity, 550));
        dialoga.getWindow().setAttributes(lp);
        dialoga.getWindow().setContentView(view1);

    }

    static AlertDialog dialoga;

    public void dissclose() {
        if (dialoga != null && dialoga.isShowing()) {
            dialoga.dismiss();
        }

    }
    private void DeleteFood(String food_id){
        Api.api().deleteFoodEntry(bindUntilEvent(ActivityEvent.DESTROY),  food_id, new SimpleRequestListener<BaseEntity<String>>() {

            @Override
            public void onSuccess(BaseEntity<String> o) {
                ToastUtil.makeToast(o.msg);
                finish();
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

}
