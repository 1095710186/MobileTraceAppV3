package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.CDGYSAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.CDGYSAdapter_;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.JHFoodAdapter_;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.JhImageListAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.GlideHelper;
import cn.com.bigknow.trade.pos.Immediate.app.util.ImageUtils;
import cn.com.bigknow.trade.pos.Immediate.app.util.PhotoUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.RecyclerViewItemDecoration;
import cn.com.bigknow.trade.pos.Immediate.app.util.UUIDUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.ProvinceView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.interface_.OnImageListItemClick;
import cn.com.bigknow.trade.pos.Immediate.base.interface_.OntemClickIListener;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.CommonUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.RegularUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ChePai;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryDsMain;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodImgInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.IdResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static cn.com.bigknow.trade.pos.Immediate.R.id.zjcpLL;

/**
 * Created by laixy2017/01/07
 *
 * 填写进货单(改版)
 */

public class TXJHD_Y_Activity extends BaseActivity {
    @BindView(R.id.pTV)
    TextView mPTV;
    @BindView(R.id.cTV)
    TextView mCTV;
    @BindView(R.id.numTV)
    EditText mNumTV;
    @BindView(R.id.zjcpTV1)
    TextView mZjcpTV1;
    @BindView(R.id.zjcpLL1)
    LinearLayout mZjcpLL1;
    @BindView(R.id.zjcpTV2)
    TextView mZjcpTV2;
    @BindView(R.id.zjcpLL2)
    LinearLayout mZjcpLL2;
    @BindView(R.id.zjcpTV3)
    TextView mZjcpTV3;
    @BindView(R.id.zjcpLL3)
    LinearLayout mZjcpLL3;
    @BindView(zjcpLL)
    LinearLayout mZjcpLL;
    @BindView(R.id.totalET)
    TextView mTotalET;
    List<ChePai> chePais;


    @BindView(R.id.imageIV)
    ImageView imageView;


    String selectFilePath;

//    @AutoBundleField(required = false)
    @AutoBundleField
    TXJHDWrapper wrapper;


    @BindView(R.id.sumitTV_)
    TextView onSumitTV;


    @BindView(R.id.kindsTV)
    TextView mKindsTV;
    @BindView(R.id.hjLL)
    LinearLayout mHjLL;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    JhImageListAdapter mAdapter;
    @AutoBundleField(required = false)
    boolean isEdit = false;

    List<FoodInfo> foodInfoList = new ArrayList<>();

    private static final String FILETYPE_entryBill = "entryBill";
    public String IMG_URL = ModelConfig.MVP_URL + "servlet/fileupload?oper_type=img&masterFileType=%1$s&masterId=%2$s&fileId=%3$s";
    private String uploadSession = UUIDUtil.generateFormattedGUID();
    private String userId = UserManager.getInstance().getUserInfo().getUserId();
    private String orgId = UserManager.getInstance().getUserInfo().getOrgId();

    @Override
    protected boolean autoBindBundle() {
        return true;
    }
    @Override
    public boolean autoBindEvent() {
        return true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {
        FoodInfo info;
        if (event.type == SimpleEventType.ON_JH_ADD_CD) {
            dissclose();
            info = (FoodInfo) event.objectEvent;
            getVendorData(info);

        }else if (event.type ==SimpleEventType.ON_JH_ADD_BACK){
            loadData();
        }
    }

    @Override
    public void init() {
        loadChePai();
        setData();
        intRecivew();//菜品列表
        loadData();  //加载菜品列表

    }

    private void intRecivew() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(
                RecyclerViewItemDecoration.MODE_GRID, getResources().getColor(R.color.t), DensityUtil.dip2px(this, 5), 0, 0));

        mAdapter = new JhImageListAdapter(mActivity,foodInfoList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnListItemClick(new OnImageListItemClick() {
            @Override
            public void OnClickItem(View view, int position, boolean isAdd) {
                if (position == foodInfoList.size()) {
                    //跳转
                    //添加菜品
                    Intent intent = new Intent(mActivity, FoodnfoLIistChooseActivity.class);
                    intent.putExtra("type",SimpleEventType.ON_JH_ADD_BACK);
                    startActivity(intent);
                }else {
                    FoodInfo food = mAdapter.getItemData(position); //
                    if (CommonUtil.isNetWorkConnected(mActivity)) {
                        getVendorData(food); //查询对应的产地列表
                    } else {
                        ToastUtil.makeToast("当前网络不可用，请检查网络");
                    }
                }
            }
        });
    /*    recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                FoodInfo food = mAdapter.getItem(i); //基础菜品

                    *//*boolean isV = false;
                    for (FoodInfo info: foodInfoList){
                        if (info.getImgId().equals(food.getImgId())){
//                            isV = true;
                            food = info;
                            break;
                        }
                    }

                    if (isV) {
                        ToastUtil.makeToast("已入库得菜品不能修改产地信息！");
                        return;
                    }else *//*
                    if (CommonUtil.isNetWorkConnected(mActivity)) {
                        getVendorData(food); //查询对应的产地列表
                    } else {
                        ToastUtil.makeToast("当前网络不可用，请检查网络");
                    }

            }
        });*/
    }

    private void getVendorData(FoodInfo item) {
        String id = null;
//        if (item.getArea()!=null){
//           id =  item.getItemId();
//        }else {
            id = item.getId();
//        }
        Api.api().getVendorList(bindUntilEvent(ActivityEvent.DESTROY), id, item.getMerchantId(), new SimpleRequestListener<BaseEntity<List<VendorAreaBean>>>() {
            @Override
            public void onSuccess(BaseEntity<List<VendorAreaBean>> o) {
                if (o != null && o.getData().size() > 0) {
//                    refreshLayout.setRefreshEnable(false);
                    // 弹出对话框
                    showDialog(item,o.getData());

                }else {
                    ToastUtil.makeToast("没有对应得产地信息！");
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

    SimpleSwipeRefreshLayout refreshLayout;

    CDGYSAdapter_ cdgysAdapter;
    Display display;
    static AlertDialog dialog;

    public void dissclose() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
    //弹出产地选择框
    private void showDialog(FoodInfo foodInfo, List<VendorAreaBean> vendorAreaBeanList) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_choose_area, null);

        View cd_back = (View) view1.findViewById(R.id.cd_layout_quxiao);
        View cd_sure = (View) view1.findViewById(R.id.cd_layout_sure);
        ImageView imvAdd = (ImageView) view1.findViewById(R.id.addCd);
        SimpleSwipeRefreshLayout refreshLayout = (SimpleSwipeRefreshLayout) view1.findViewById(R.id.refreshLayout);
        cdgysAdapter = new CDGYSAdapter_();
        refreshLayout.setAdapter(cdgysAdapter);

        cdgysAdapter.setNewData(vendorAreaBeanList);
        if (foodInfo != null && foodInfo.getVendorId() != null) {
            cdgysAdapter.setSelectAreaBeanId(foodInfo.getVendorId(),true);
        }
        refreshLayout.setRefreshEnable(false);
        cdgysAdapter.setListener(new OntemClickIListener() {
            @Override
            public void OnItemClick(View view, int position, VendorAreaBean vendorAreaBean) {
                /*if (isEdit) {  //查看/修改
                    if (foodInfo.getArea() != null) { //改

                    }else{  //新增
                        foodInfo.setItemId(foodInfo.getId());
                    }
                }else{ //新增
                    foodInfo.setItemId(foodInfo.getId());
                }*/
                foodInfo.setVendorId(vendorAreaBean.getId());
                foodInfo.setVendor(vendorAreaBean.getVendor());
                foodInfo.setArea(vendorAreaBean.getProdArea());
            }
        });

        imvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                startActivity(FoodInfoUpdateActivityAutoBundle.createIntentBuilder(foodInfo).build(mActivity));

            }
        });
       /* refreshLayout.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                VendorAreaBean vendorAreaBean = (VendorAreaBean) baseQuickAdapter.getItem(i);
                if (isEdit) {  //查看/修改
                    if (foodInfo.getArea() != null) { //改

                    }else{  //新增
                        foodInfo.setItemId(foodInfo.getId());
                    }
                }else{ //新增
                    foodInfo.setItemId(foodInfo.getId());
                }
                foodInfo.setVendorId(((VendorAreaBean) baseQuickAdapter.getItem(i)).getId());
                foodInfo.setVendor(((VendorAreaBean) baseQuickAdapter.getItem(i)).getVendor());
                foodInfo.setArea(((VendorAreaBean) baseQuickAdapter.getItem(i)).getProdArea());

//                    EventMamager.getInstance().postObjEvent(SimpleEventType.ON_JH_CDGYS_EDIT_BACK, foodInfo);

            }
        });*/
        cd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();
            }
        });
        cd_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isE = false;
                if (foodInfo.getArea()!=null) {
                    foodInfo.setQty(total);


                    if (isEdit) {

                        mAdapter.setFood_(foodInfo);
                        for (int i = 0; i < wrapper.getFoodInfos().size(); i++) {
                            if (foodInfo.getItemId().equals(wrapper.getFoodInfos().get(i).getItemId())) {
                                wrapper.getFoodInfos().set(i, foodInfo);
                                isE = true;
                            }
                        }
                        if (!isE) {
                            wrapper.getFoodInfos().add(foodInfo);
                        }

                    } else {

                        mAdapter.setFood_(foodInfo);
                        if (wrapper != null && wrapper.getFoodInfos().size() > 0) {
                            for (int i = 0; i < wrapper.getFoodInfos().size(); i++) {
                                if (foodInfo.getItemId().equals(wrapper.getFoodInfos().get(i).getItemId())) {
                                    wrapper.getFoodInfos().set(i, foodInfo);
                                    isE = true;
                                }
                            }
                            if (!isE) {
                                wrapper.getFoodInfos().add(foodInfo);
                            }
                        } else {
                            wrapper.getFoodInfos().add(foodInfo);
                        }
                    }
                    if (wrapper.getFoodInfos().size() > 0) {
                        mHjLL.setVisibility(View.VISIBLE);
                        mKindsTV.setText("已选择" + wrapper.getFoodInfos().size() + "种菜品");


                    } else {
                        mHjLL.setVisibility(View.GONE);
                    }
                }else {
                    if (isEdit) {

                        mAdapter.setFood_(foodInfo);
                        for (int i = 0; i < wrapper.getFoodInfos().size(); i++) {
                            if (foodInfo.getItemId().equals(wrapper.getFoodInfos().get(i).getItemId())) {
                                wrapper.getFoodInfos().remove(i);
//                                isE = true;
                            }
                        }
//                        if (!isE) {
//                            wrapper.getFoodInfos().add(foodInfo);
//                        }

                    } else {

                        mAdapter.setFood_(foodInfo);
                        if (wrapper != null && wrapper.getFoodInfos().size() > 0) {
                            for (int i = 0; i < wrapper.getFoodInfos().size(); i++) {
                                if (foodInfo.getItemId().equals(wrapper.getFoodInfos().get(i).getItemId())) {
                                    wrapper.getFoodInfos().remove(i);
//                                    isE = true;
                                }
                            }
//                            if (!isE) {
//                                wrapper.getFoodInfos().add(foodInfo);
//                            }
                        } else {
//                            wrapper.getFoodInfos().add(foodInfo);
                        }
                    }
                    if (wrapper.getFoodInfos().size() > 0) {
                        mHjLL.setVisibility(View.VISIBLE);
                        mKindsTV.setText("已选择" + wrapper.getFoodInfos().size() + "种菜品");


                    } else {
                        mHjLL.setVisibility(View.GONE);
                    }
                }
                dissclose();


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
//        lp.height = (int) (display.getHeight())*1/3;
//                (int) (DensityUtil.dip2px(mActivity, 550));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view1);



    }

    boolean che_fo = true;

    @OnClick(R.id.pai_chepai)
    public void onPaicheClick() {
        Intent intent = new Intent();
        intent.setClass(this, CameraActivity.class);
        startActivityForResult(intent, 11);
    }
    private void loadData() {
        Api.api().getFoodList(bindUntilEvent(ActivityEvent.DESTROY), null, "Y", new SimpleRequestListener<BaseEntity<List<FoodInfo>>>() {
            @Override
            public void onSuccess(BaseEntity<List<FoodInfo>> o) {
                if (o != null && o.getData().size() > 0) {
                    foodInfoList = o.data;
                    if (wrapper!=null){
                        if ( wrapper.getFoodInfos().size() > 0) {
                            for (FoodInfo itemIdF:wrapper.getFoodInfos()){
                                for (FoodInfo idF:foodInfoList) {
                                    if (idF.getId().equals(itemIdF.getItemId())) {
                                        idF.setArea(itemIdF.getArea());
                                        idF.setVendor(itemIdF.getVendor());
                                        idF.setVendorId(itemIdF.getVendorId());
                                    }
                                }
                                itemIdF.setId(itemIdF.getItemId());
                            }
//                            foodInfoList = wrapper.getFoodInfos();
//                            mAdapter.setFood(wrapper.getFoodInfos());
                            mHjLL.setVisibility(View.VISIBLE);
                            mKindsTV.setText("已选择" + wrapper.getFoodInfos().size() + "种菜品");



                        } else {
                            mHjLL.setVisibility(View.GONE);
                        }
                    }else {
//                        foodInfoList = new ArrayList<FoodInfo>();
                    }
                    mAdapter.upDateItems(foodInfoList);
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
    private void uploadImage_che(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("file", file.getName(), photoRequestBody);

            Api.api().uploadChe(bindUntilEvent(ActivityEvent.DESTROY), photo, new SimpleRequestListener<BaseEntity<String>>() {
                @Override
                public void onSuccess(BaseEntity<String> o) {
//                    ToastUtil.makeToast("返回车牌==="+o.data.toString());
                    try {
                        JSONArray jsonArray = new JSONArray(o.data.toString());
                        if (jsonArray.length() > 0) {
                            String jsonObject = jsonArray.get(0).toString();

                            mPTV.setText(jsonObject.substring(0, 1));
                            mCTV.setText(jsonObject.substring(1, 2));
                            mNumTV.setText(jsonObject.substring(2, jsonObject.length()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onStart() {
                    progressHudUtil.showProgressHud();
                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }

                @Override
                public void onError(ApiError error) {
                    super.onError(error);

                }
            });
        }
    }


    @OnClick(R.id.totalET)
    public void onTotalClick() {

        View view = LayoutInflater.from(this).inflate(R.layout.zzl_num_keyboard_layout, null);


        TextView showTV = (TextView) view.findViewById(R.id.showTV);
        TextView CPTV = (TextView) view.findViewById(R.id.cpTV);

        CPTV.setText(mPTV.getText().toString() + mCTV.getText().toString() + mNumTV.getText().toString());
        NumKeyboardView numKeyboardView = (NumKeyboardView) view.findViewById(R.id.numkeyboardView);
        numKeyboardView.setShowTextView(showTV);


        AlertDialogUtil.showNumKeyboardDialog(this, view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.sureBtn) {
                    if (numKeyboardView.getNumValue() == 0) {
                        ToastUtil.makeToast("请输入重量");
                        return;
                    }
                    total = numKeyboardView.getNumValue()*2000;
                    mTotalET.setText(showTV.getText().toString());
                    AlertDialogUtil.dissMiss();
                }

            }
        });


    }


    public void setData() {
        if (wrapper != null) {
            onSumitTV.setText("保存");
            if (wrapper.getChePai() != null) {
                mPTV.setText(wrapper.getChePai().getProvince());
                mCTV.setText(wrapper.getChePai().getCity());
                mNumTV.setText(wrapper.getChePai().getNumber());
            }
            if (wrapper.getTotal()!=null) {
                total = Float.parseFloat(wrapper.getTotal())*2000;
                mTotalET.setText(wrapper.getTotal());
            }
           /* if (wrapper.getImageFilePath() != null) {String filePath = wrapper.getImageFilePath();
                if (filePath != null) {
                    showImage(filePath);
                }
            }*/




        }else{
            wrapper = new TXJHDWrapper();
        }
    }


    @OnClick(R.id.pTV)
    public void onPClick() {


        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.provice_layout, null);
        ProvinceView p = (ProvinceView) view1.findViewById(R.id.provinceView);
        p.setOnItemClickListener(item -> {
            AlertDialogUtil.dissMiss();
            mPTV.setText(item);
        });
        AlertDialogUtil.showProvinceDialog(mActivity, view1, view2 -> {

        });
    }

    private String lastString = "";

    private void loadChePai() {

        mNumTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(lastString)) {
                    String s1 = s.toString().toUpperCase();
                    lastString = s1;
                    mNumTV.setText(s1);
//                    if (s.length()>0) {



//                    }

                }
                mNumTV.setSelection(mNumTV.length());

            }

            @Override
            public void afterTextChanged(Editable s) {
                 chePais = EntityManager.getChePais(s.toString());
                if (chePais != null && chePais.size() > 0) {
                    mZjcpLL.setVisibility(View.GONE);
                    mZjcpLL1.setVisibility(View.GONE);
                    mZjcpLL1.setVisibility(View.GONE);
                    mZjcpLL2.setVisibility(View.GONE);
                    setChepai();
               }

            }
        });


        chePais = EntityManager.getChePais();
        setChepai();

    }

    void setChepai(){
        if (chePais != null && chePais.size() > 0) {
            mZjcpLL.setVisibility(View.VISIBLE);
            if (chePais.size()>3){
                mZjcpLL1.setVisibility(View.VISIBLE);
                mZjcpLL2.setVisibility(View.VISIBLE);
                mZjcpLL3.setVisibility(View.VISIBLE);

                ChePai chePai = chePais.get(0);
                ChePai chePai2 = chePais.get(1);
                ChePai chePai3 = chePais.get(2);

                mZjcpTV1.setText(chePai.getProvince() + chePai.getCity() + "  " + chePai.getNumber());
                mZjcpTV2.setText(chePai2.getProvince() + chePai2.getCity() + "  " + chePai2.getNumber());
                mZjcpTV3.setText(chePai3.getProvince() + chePai3.getCity() + "  " + chePai3.getNumber());

            }else {
                if (chePais.size() == 1) {
                    ChePai chePai = chePais.get(0);
                    mZjcpLL1.setVisibility(View.VISIBLE);
                    mZjcpLL2.setVisibility(View.GONE);
                     mZjcpLL3.setVisibility(View.GONE);
                    mZjcpTV1.setText(chePai.getProvince() + chePai.getCity() + "  " + chePai.getNumber());
                } else if (chePais.size() == 2) {
                    mZjcpLL1.setVisibility(View.VISIBLE);
                    mZjcpLL2.setVisibility(View.VISIBLE);
                    mZjcpLL3.setVisibility(View.GONE);
                    ChePai chePai = chePais.get(0);
                    ChePai chePai2 = chePais.get(1);

                    mZjcpTV1.setText(chePai.getProvince() + chePai.getCity() + "  " + chePai.getNumber());
                    mZjcpTV2.setText(chePai2.getProvince() + chePai2.getCity() + "  " + chePai2.getNumber());

                } else if (chePais.size() == 3) {
                    mZjcpLL1.setVisibility(View.VISIBLE);
                    mZjcpLL2.setVisibility(View.VISIBLE);
                    mZjcpLL3.setVisibility(View.VISIBLE);

                    ChePai chePai = chePais.get(0);
                    ChePai chePai2 = chePais.get(1);
                    ChePai chePai3 = chePais.get(2);

                    mZjcpTV1.setText(chePai.getProvince() + chePai.getCity() + "  " + chePai.getNumber());
                    mZjcpTV2.setText(chePai2.getProvince() + chePai2.getCity() + "  " + chePai2.getNumber());
                    mZjcpTV3.setText(chePai3.getProvince() + chePai3.getCity() + "  " + chePai3.getNumber());

                }
            }


        } else {
            mZjcpLL.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.cTV)
    public void onCClick() {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.provice_letter_layout, null);
        ProvinceView p = (ProvinceView) view1.findViewById(R.id.provinceView);
        p.setOnItemClickListener(item -> {
            AlertDialogUtil.dissMiss();
            mCTV.setText(item);
        });
        AlertDialogUtil.showProvinceDialog(mActivity, view1, view2 -> {

        });
    }

    @OnClick(R.id.sumitTV)
    public void onSumitClick() {
        ChePai chePai = null;
        if (mCTV.getText().length() == 0 || mPTV.getText().length() == 0 || mNumTV.getText().toString().trim().length() == 0) {
            ToastUtil.makeToast("请输入车牌");
            return;
        }else {
            chePai = new ChePai();
            chePai.setProvince(mPTV.getText().toString());
            chePai.setCity(mCTV.getText().toString());
            chePai.setNumber(mNumTV.getText().toString());
            if (!RegularUtil.isChePai(chePai.getChePai())) {
                ToastUtil.makeToast("请输入合法的车牌");
                return;
            }
        }


        if (mTotalET.getText().toString().trim().length() == 0) {
            ToastUtil.makeToast("请输入货物重量");
            return;
        }

//        if (wrapper == null && selectFilePath == null) {
//            ToastUtil.makeToast("请上传过磅单据");
//            return;
//        }
//        if (wrapper != null && wrapper.getImageFilePath() == null) {
//            ToastUtil.makeToast("请上传过磅单据");
//            return;
//        }

        if (wrapper == null) {
            wrapper = new TXJHDWrapper();
            wrapper.setChePai(chePai);
            wrapper.setImageFilePath(selectFilePath);
            wrapper.setFileId(fileId);
            wrapper.setTotal(mTotalET.getText().toString().trim());
        } else {
            wrapper.setChePai(chePai);
            if (selectFilePath != null) {
                wrapper.setImageFilePath(selectFilePath);
            }
            if (fileId != null) {
                wrapper.setFileId(fileId);
            }
            wrapper.setTotal(mTotalET.getText().toString().trim());
        }

        boolean isChepai = false;
        if (chePai!=null) {
            for (ChePai chePai1 : chePais) {
                if (chePai.getProvince().equals(chePai1.getProvince())) {
                    if (chePai.getCity().equals(chePai1.getCity())) {
                        if (chePai.getNumber().equals(chePai1.getNumber())) {
                            isChepai = true;
                            break;
                        }
                    }
                }
            }
            if (!isChepai) {
                EntityManager.saveChePai(chePai);
            }
        }
        if (wrapper.getFoodInfos().size()>0) {
            requstSave();
        }else {
            ToastUtil.makeToast("请添加菜品！");
        }

    }
    float total = 0;
    private void requstSave() {
        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        List<RequestWrap> requestWraps = new ArrayList<>();

        RequestWrap<FoodEntryDsMain> requestWrap = new RequestWrap<>();
        requestWrap.name = "dsMain";
        List<FoodEntryDsMain> list = new ArrayList<>();

        FoodEntryDsMain dsMain = new FoodEntryDsMain();


        float total_ = 0;
        for (FoodInfo foodInfo : wrapper.getFoodInfos()) {
            total_ += total;
        }
//        total_  = wrapper.getFoodInfos().size()*total ;
        if (isEdit) {
            dsMain.id = wrapper.getId();
            dsMain.transType = "C";
            dsMain.state = wrapper.getState();
            if (wrapper.getChePai()!=null) {
                dsMain.searchNo = wrapper.getChePai().getChePai();
            }
            dsMain.kinds = wrapper.getFoodInfos().size() + "";
            dsMain.itemQty = total_+"";//mHjTV.getText().toString();
            dsMain.totalQty = wrapper.getTotal();
            dsMain.fileId = wrapper.getFileId();
            dsMain.updateDate = wrapper.getUpdateDate();
        } else {
            dsMain.transType = "C";
            if (wrapper.getChePai()!=null) {
                dsMain.searchNo = wrapper.getChePai().getChePai();
            }
            dsMain.kinds = wrapper.getFoodInfos().size() + "";
            dsMain.itemQty = total_+"";//mHjTV.getText().toString();
            dsMain.totalQty = wrapper.getTotal();
            dsMain.fileId = wrapper.getFileId();

        }

        list.add(dsMain);

        requestWrap.data = list;
        requestWraps.add(requestWrap);

        RequestWrap<FoodEntryInfo> requestWrap1 = new RequestWrap<FoodEntryInfo>();
        requestWrap1.name = "dsDet";

        List list1 = new ArrayList<>();

        for (FoodInfo foodInfo : wrapper.getFoodInfos()) {
            if (foodInfo.getUnitId() == null) {
                foodInfo.setUnitId(foodInfo.getPurUnitId());
            }
//                foodInfo.setItemId(foodInfo.getId());
        }
        list1.addAll(wrapper.getFoodInfos());
        requestWrap1.data = list1;
        requestWraps.add(requestWrap1);

        simpleRequestWrap.dataset = requestWraps;

        Api.api().reFoodEntry(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<IdResultWrap>() {
            @Override
            public void onStart() {
                super.onStart();
                progressHudUtil.showProgressHud("正在提交数据，请稍后", false);
            }

            @Override
            public void onSuccess(IdResultWrap o) {
                ToastUtil.makeToast("操作成功");
                EventMamager.getInstance().postEvent(SimpleEventType.ON_JH_REFRESH);
                finish();
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }
        });
    }
    boolean isCHoose = false;
    @OnClick(value = {R.id.zjcpLL1, R.id.zjcpLL2, R.id.zjcpLL3})
    public void onZJCPCLICK(View v) {

        v.setSelected(true);
        ChePai chePai = null;
        if (chePais.size()>=3) {
            if (v == mZjcpLL1) {
                chePai = chePais.get(0);
                mZjcpLL2.setSelected(false);
                mZjcpLL3.setSelected(false);
            }
            if (v == mZjcpLL2) {
                chePai = chePais.get(1);
                mZjcpLL1.setSelected(false);
                mZjcpLL3.setSelected(false);
            }
            if (v == mZjcpLL3) {
                chePai = chePais.get(2);
                mZjcpLL1.setSelected(false);
                mZjcpLL2.setSelected(false);
            }
        }else if (chePais.size() ==2){
             if (v == mZjcpLL1) {
                 chePai = chePais.get(0);
                 mZjcpLL2.setSelected(false);
                 mZjcpLL3.setSelected(false);
             }
             if (v == mZjcpLL2) {
                 chePai = chePais.get(1);
                 mZjcpLL1.setSelected(false);
                 mZjcpLL3.setSelected(false);
             }
        }  else if (chePais.size() == 1){
            if (v == mZjcpLL1) {
                  chePai = chePais.get(0);
                mZjcpLL2.setSelected(false);
                mZjcpLL3.setSelected(false);
            }

        }

        mPTV.setText(chePai.getProvince());
        mCTV.setText(chePai.getCity());
        isCHoose= true;
        mNumTV.setText(chePai.getNumber());
    }

String url_gp_path=null;
    @OnClick(R.id.imageLL)
    public void onClick() {
        che_fo = false;
        url_gp_path=null;
//        PhotoUtil.open(this, true, 1);
        url_gp_path=  PhotoUtil.takePhoto(TXJHD_Y_Activity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            if (che_fo) {
//                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
//                if (pathList != null && pathList.size() > 0) {
//                    String selectFilePath = pathList.get(0);
//                    uploadImage_che(selectFilePath);
//                }
//            } else {
//                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
//                if (pathList != null && pathList.size() > 0) {
//                    String selectFilePath = pathList.get(0);
//                    uploadImage(selectFilePath);
//                }
//            }
//        }
        if (data != null && resultCode == 11) {
            String path = data.getStringExtra("path_url");
            uploadImage_che(path);
        }

        if (requestCode == PhotoUtil.REQUESTCODE_TAKE_PHOTO) {
          uploadImage(url_gp_path);
        }

    }

    String fileId;

    private void uploadImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {

            byte[] fileByte = null;
            Bitmap bitmap = ImageUtils.getScaleBitmap(filePath);
            if (bitmap == null) {
                ToastUtil.makeToast("图片不存在");
                return;
            }

            progressHudUtil.showProgressHud();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100表示不压缩，将不压缩的数据存放到baos中
            int per = 100;
            int size = baos.toByteArray().length / 1024;

            while (size > 400 ) { // 循环判断如果压缩后图片是否大于400kb,大于继续压缩
                if (per > 10) {
                    per -= 10;// 每次都减少10
                } else {
                    break;
                }
                baos.reset();// 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, per, baos);// 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
                size = baos.toByteArray().length / 1024;
            }
            try {
                baos.flush();
                baos.close();
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                    System.gc();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileByte = baos.toByteArray();

            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), fileByte);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("file", file.getName(), photoRequestBody);

            Api.api().uploadFile(bindUntilEvent(ActivityEvent.DESTROY), photo, uploadSession, null, FILETYPE_entryBill, userId, orgId, new SimpleRequestListener() {
                @Override
                public void onSuccess(Object o) {
                    selectFilePath = filePath;
                    fileId = ((BaseEntity) o).fileid;
                    showImage(filePath);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }

                @Override
                public void onError(ApiError error) {
                    super.onError(error);

                }
            });
        }
    }


    private void getImagesData(String id) {
        Api.api().getFoodImg(bindUntilEvent(ActivityEvent.DESTROY), id, FILETYPE_entryBill, new SimpleRequestListener<ListResultWrap<List<FoodImgInfo>>>() {
                    @Override
                    public void onSuccess(ListResultWrap<List<FoodImgInfo>> o) {
                        if (o != null && o.rows != null && o.rows.size() > 0) {
                            String url = String.format(IMG_URL, id, FILETYPE_entryBill, o.rows.get(0).getId());
                            wrapper.setFileId(o.rows.get(0).getId());
                            wrapper.setImageFilePath(url);
                            showImage(url);
                        }
                    }

                    @Override
                    public void onError(ApiError error) {
                        super.onError(error);
                        progressHudUtil.dismissProgressHud();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressHudUtil.dismissProgressHud();
                    }
                }
        );
    }


    private void showImage(String filePath) {

        GlideHelper.load(this, filePath, true, true, imageView);

    }


    @Override
    public int layoutId() {
        return R.layout.jhd_layout;
    }


}
