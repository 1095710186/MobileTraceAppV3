package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import com.trello.rxlifecycle.android.ActivityEvent;

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
 * x新增，编辑菜品信息界面（基本信息）
 */


public class FoodInfoEdtiorActivity extends BaseActivity {
    @BindView(R.id.a_foodinfo_editor_actionbar)
    Toolbar mToolbar;

    @BindView(R.id.a_foodinfo_editor_imvhead)
    ImageView imvHead;
    @BindView(R.id.a_foodinfo_editor_tvname)
    TextView tvName;
    @BindView(R.id.a_foodinfo_editor_tvChangename)
    TextView tvChangename;
    @BindView(R.id.a_foodinfo_editor_recyclerView)
    RecyclerView mRecyclerView;





    public AlertDialog.Builder builder;
    public AlertDialog dialog;

/*
    private String uploadSession = UUIDUtil.generateFormattedGUID();
    private String userId = UserManager.getInstance().getUserInfo().getUserId();
    private String orgId = UserManager.getInstance().getUserInfo().getOrgId();*/

    private FoodInfoAreaAdapter mAdapter;
    private FoodTypeDetailModel corrInfo;
    private int where = 1;  //1head;2 item

    private String aliasId = null;


    @OnClick(R.id.a_foodinfo_editor_tvChangename)
    public void onChangeNameOnClick() {
        Intent intent = new Intent(this,FoodNameChangeActivity.class);
        intent.putExtra("foodtypedetail",corrInfo);
        startActivity(intent);
//        finish();
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {

        if (event.type == SimpleEventType.ON_FOODNAME_CHANGE) {
            aliasId = event.idEvent;
            tvName.setText(event.strEvent);
        }
    }
    @Override
    public void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//把整个Layout顶上去露出获得焦点的EditText,不压缩多余空间.
        areaBeanList = new ArrayList<>();
        corrInfo = (FoodTypeDetailModel) getIntent().getSerializableExtra("foodtypedetail");
        if (corrInfo!=null) {
            UserManager.getInstance().loadFoodHeadImage(this, imvHead, corrInfo.getImgId());
            tvName.setText(corrInfo.getName());
        }
        setToolbar();
//        addRightMenu();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

//        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(
//                RecyclerViewItemDecoration.MODE_GRID, getResources().getColor(R.color.t), DensityUtil.dip2px(this, 5), 0, 0));


        mAdapter = new FoodInfoAreaAdapter();

        mRecyclerView.setAdapter(mAdapter);

        addHeaderView();
       /* mAdapter.addData(new Object());
        mAdapter.addData(new Object());
        mAdapter.addData(new Object());
        mAdapter.addData(new Object());
        mAdapter.addData(new Object());*/

        mAdapter.notifyDataSetChanged();
        mAdapter.setItemClick(new FoodInfoAreaAdapter.OnListItemChildClick() {
            @Override
            public void OnClick(int position, int type, String vendor) {  //  type :0选择地区 ;1 change;2，dele
                if (type == 0){
//                    ToastUtil.makeToast("");
                    where =2;
                    itemPosition =  position;
                    Intent intent = new Intent(FoodInfoEdtiorActivity.this,FoodAreaLIistChooseActivity.class);
                    startActivityForResult(intent,125);
                }else if (type ==1){
//                    ToastUtil.makeToast( "修改");
                    itemPosition =  position;
                    areaBeanList.get( itemPosition-1 ).setVendor(vendor);
                    mAdapter.setNewData(areaBeanList);
                    mAdapter.notifyDataSetChanged();
//                    baseViewHolder
//                            .setText(R.id.food_area_item_tvEitor,"编辑");
//                    baseViewHolder.setVisible(R.id.food_area_item_imvChooseArea,false)
//                            .setVisible(R.id.food_area_item_imvArea,true);
//                    baseViewHolder.getView(R.id.food_area_item_edtGys).setEnabled(false);


                }else if (type ==2){
                    itemPosition =  position;
                    areaBeanList.remove( itemPosition-1 );
                    mAdapter.setNewData(areaBeanList);
                    mAdapter.notifyDataSetChanged();
                }else if (type == 3){
                    itemPosition =  position;
                    areaBeanList.get( itemPosition-1 ).setVendor(vendor);
//                    mAdapter.setNewData(areaBeanList);
                }
            }
        });
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
                    ToastUtil.makeToast("产地信息不能为空！");
                }
            }
        });
        imvChooseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                where = 1;
                Intent intent = new Intent(FoodInfoEdtiorActivity.this,FoodAreaLIistChooseActivity.class);
                startActivityForResult(intent,125);
            }
        });

        mAdapter.addHeaderView(view);


    }
    public void createDialog(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要退出登录？");
        builder.setPositiveButton("确定", (dialog, id) -> {
            Api.api().exitLogin(bindUntilEvent(ActivityEvent.DESTROY), new SimpleRequestListener() {
                @Override
                public void onSuccess(Object o) {
                    BaseEntity entity = (BaseEntity) o;
                    LogUtil.v("exitLogin", o.toString());
                    if (entity.getSuccess() == 1) {

                        Toast.makeText(FoodInfoEdtiorActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FoodInfoEdtiorActivity.this, LoginActivity.class);
//                        intent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                }
            });
        });
        builder.setNegativeButton("取消", (dialog, id) -> {
            dialog.dismiss();
        });
        dialog = builder.create();
    }

    @Override
    public int layoutId() {
        return R.layout.a_foodinfo_add_layout;
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
                VendorAreaBean vendorAreaBean = new VendorAreaBean();
                Bundle MarsBuddle = data.getExtras();
                areaBean = (AreaBean) MarsBuddle.getSerializable("AreaBean");
                if (where == 1){ //head
                    edtArea.setText(areaBean.getAreaName());
                }else if (where == 2){ //item;
                    if (itemPosition>0) {
                        areaBeanList.get(itemPosition-1).setProdId(String.valueOf(areaBean.getId()));
                        areaBeanList.get(itemPosition-1).setProdArea(areaBean.getAreaName());
                        mAdapter.setNewData(areaBeanList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
//                areaBeanList.add(areaBean);
            }
        }

    }

    /**
     * 上传图片接口
     *
     * @param fileImgWrappers
     */
    public void uploadHead(FileImgWrapper fileImgWrappers) {

        File file = new File(fileImgWrappers.filePath);
        /*if (file.exists()) {
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("file", file.getName(), photoRequestBody);
            Api.api().uploadHead(bindUntilEvent(ActivityEvent.DESTROY), ModelConfig.UPLOAD, photo, uploadSession, fileImgWrappers.getMasterId(), fileImgWrappers.masterType, userId, orgId, new SimpleRequestListener() {
                @Override
                public void onSuccess(Object o) {

                    fileImgWrappers.setFileId(((BaseEntity) o).fileid);
                    if (((BaseEntity) o).getSuccess() == 1) {
                        ToastUtil.makeToast("头像" + "上传成功");
                        ivAvator.setImageBitmap(null);
                        UserManager.getInstance().loadHeadImage(mActivity, ivAvator);
                    } else {
                        ToastUtil.makeToast("头像" + "上传失败");
                    }

                }

                @Override
                public void onError(ApiError error) {
                    super.onError(error);
                    ToastUtil.makeToast("头像" + "上传失败");

                }
            });
        }*/

    }

    /**
     * 点击完成按钮，请求修改菜品详情
     */
    @OnClick(R.id.a_foodinfo_editor_relayout)
    public void requestAddOrder() {
        if (corrInfo ==null)
            return;
        if (areaBeanList==null||areaBeanList.size()==0){
            ToastUtil.makeToast("请添加产地供应商信息");
            return;
        }
        RequestWrap<FoodUpdateAddInfo> foodUpdateAddInfoRequestWrapf = new RequestWrap<>();
        foodUpdateAddInfoRequestWrapf.name = "dsMain";

        FoodUpdateAddInfo foodInfo = new FoodUpdateAddInfo();
        foodInfo.setId("");
        foodInfo.setUnitId(corrInfo.getUnitId());
        foodInfo.setSalePrice(corrInfo.getPrice());
        foodInfo.setItemId(corrInfo.getId()); //
        foodInfo.setPurUnitId(corrInfo.getPurUntiId()); //
        foodInfo.setSaleUnitId(corrInfo.getPurUntiId()); //
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
        LogUtil.v("simpleRequestWrap",simpleRequestWrap.toString());
        Api.api().saveFoodInfo(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<BaseEntity<FoodInfo>>() {
                    @Override
                    public void onSuccess(BaseEntity<FoodInfo> baseEntity) {

                       if (baseEntity.success == 1){
                           ToastUtil.makeToast("新增菜品信息成功");
                           if (getIntent().getIntExtra("type",0)>0) {
                               FoodInfo info = baseEntity.getData();
                               info.setItemId(info.getId());
                                info.setAlias(corrInfo.getName());
                               info.setItemName(corrInfo.getName());
                               info.setImgId(corrInfo.getImgId());
                               info.setMainUnitId(corrInfo.getPurUntiId());
                               startActivity(XZCDGYSActivityAutoBundle.createIntentBuilder(info).build(mActivity));
//                               EventMamager.getInstance().postEvent(SimpleEventType.ON_JH_ADD_BACK);

                           }
                           finish();
                       }
                    }

                    @Override
                    public void onError(ApiError error) {
                        super.onError(error);
                        ToastUtil.makeToast("新增菜品信息失败");
                        //progressHudUtil.dismissProgressHud();
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



}
