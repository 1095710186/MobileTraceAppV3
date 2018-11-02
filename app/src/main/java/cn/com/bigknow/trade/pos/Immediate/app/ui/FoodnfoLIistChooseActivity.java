package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.di.DBManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.RecyclerViewItemDecoration;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeDetailModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeModel;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodTypeModelDao;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by laixiaoyang on 2016/10/12..
 */


public class FoodnfoLIistChooseActivity extends BaseActivity {


    @BindView(R.id.foodtype_RV)
    RecyclerView foodtype_RV;

   /* @BindView(R.id.foodtype_RV2)
    RecyclerView foodtype_RV2;*/

    @BindView(R.id.foodtype_detail_RV)
    RecyclerView foodtype_detail_RV;

    @BindView(R.id.foodtype_searCh)
    RecyclerView foodtype_searCh;
    @BindView(R.id.layout_foodtype)
    LinearLayout layoutFoodtype;

    private List<FoodTypeModel> mfoodTypeModels;
//    private List<FoodTypeModel> mfoodTypeModels2;

    private FoodTypeAdapter foodTypeAdapter;
//    private FoodTypeAdapter2 foodTypeAdapter2;
    private FoodTypeDetailAdapter foodTypeDetailAdapter;

    private FoodTypeDetailAdapter foodTypeSearchAdapter;

    public static final int CHOSE_FOOD_RESULT = 0x1100;


    @BindView(R.id.food_chose_ET)
    EditText edtSearch;
    @BindView(R.id.area_searchYuyin)
    ImageView imvYuyin;
    @BindView(R.id.area_searchCancel)
    ImageView imvCancel;
    private String param;// 搜索
    @BindView(R.id.food_search)
    Button btnSearch;
    @OnClick(R.id.food_search)
    public void gotoSearch(){
        closeInputMethod();
        //调用搜索接口
        param = edtSearch.getText().toString().trim();
        if (param!=null&& !param.equals("")) {
            getFoodList(null,param);
//            ToastUtil.makeToast(edtSearch.getText().toString().trim());
        }else {
            ToastUtil.makeToast("请输入菜名");
        }
    }
    @OnClick(R.id.area_searchCancel)
    public void cancelSearch(){
        edtSearch.setText("");
        edtSearch.clearFocus();
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {
         if (event.type ==SimpleEventType.ON_JH_ADD_BACK){
            finish();
        }
    }
    @Override
    public void init() {
        setYuying(edtSearch,imvYuyin);
        setTitle("新增菜品");
        foodtype_RV.setLayoutManager(new LinearLayoutManager(this));
//        foodtype_RV2.setLayoutManager(new LinearLayoutManager(this));
        foodtype_detail_RV.setLayoutManager(new LinearLayoutManager(this));
        foodtype_searCh.setLayoutManager(new LinearLayoutManager(this));
//        foodtype_RV2.addItemDecoration(new RecyclerViewItemDecoration(RecyclerView.HORIZONTAL, this, R.drawable.p_line));
        foodtype_detail_RV.addItemDecoration(new RecyclerViewItemDecoration(RecyclerView.HORIZONTAL, this, R.drawable.p_line));
        mfoodTypeModels = getFoodTypeList();

        foodTypeAdapter = new FoodTypeAdapter();
//        foodTypeAdapter2 = new FoodTypeAdapter2();
        foodTypeDetailAdapter = new FoodTypeDetailAdapter();
        foodTypeSearchAdapter = new FoodTypeDetailAdapter();

        foodtype_RV.setAdapter(foodTypeAdapter);
//        foodtype_RV2.setAdapter(foodTypeAdapter2);
        foodtype_detail_RV.setAdapter(foodTypeDetailAdapter);
        foodtype_searCh.setAdapter(foodTypeSearchAdapter);
        foodtype_RV.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                closeInputMethod();
                clickFoodType(view, i);
            }
        });
        /*foodtype_RV2.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                clickFoodType2(view, i);
            }
        });*/
        foodtype_detail_RV.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                FoodTypeDetailModel foodTypeDetailModel  = foodTypeDetailAdapter.getItem(i);
                if (foodTypeDetailModel!=null && foodTypeDetailModel.getId()!=null) {
                    clickFoodDetailType(view, i,0);
                }else {
                    getFoodList(foodTypeDetailModel.getTypeId(),null);
                }
            }
        });
        foodtype_searCh.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                clickFoodDetailType(view, i,1);
            }
        });
//        foodTypeAdapter.setOnRecyclerViewItemClickListener((view, i) -> clickFoodType(view, i));
//        foodTypeDetailAdapter.setOnRecyclerViewItemClickListener((view, i) -> clickFoodDetailType(view, i));
      /*  if (mfoodTypeModels != null) {
            foodTypeAdapter.setNewData(mfoodTypeModels);
        }*/

        getFoodType1(null);


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0) {
                    imvYuyin.setVisibility(View.GONE);
                    imvCancel.setVisibility(View.VISIBLE);
                    foodtype_searCh.setVisibility(View.VISIBLE);
                    layoutFoodtype.setVisibility(View.GONE);
                    param = edtSearch.getText().toString().trim();
                    getFoodList(null,param);
//                    ToastUtil.makeToast(edtSearch.getText().toString().trim());
                }else {
                    imvYuyin.setVisibility(View.VISIBLE);
                    imvCancel.setVisibility(View.GONE);
                    foodtype_searCh.setVisibility(View.GONE);
                    layoutFoodtype.setVisibility(View.VISIBLE);
                }
            }
        });

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

    private void closeInputMethod() {
        edtSearch.clearFocus();
        // 关闭输入法
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public int layoutId() {
        return R.layout.a_food_infolist_choose_layout;
    }

    public int selectPositoin = -1;


    void  getFoodType1(String parentId){

        //获取菜品类型列表
        Api.api().getFoodType(bindUntilEvent(ActivityEvent.DESTROY), null, null, null,parentId, new SimpleRequestListener<ListResultWrap<List<FoodTypeModel>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<FoodTypeModel>> listListResultWrap) {
                if (listListResultWrap.total>0) {
                    mfoodTypeModels = listListResultWrap.rows;
                    foodTypeAdapter.setNewData(mfoodTypeModels);
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

    void  getFoodType2(String parentId){

        //获取菜品类型列表
   /*     Api.api().getFoodType(bindUntilEvent(ActivityEvent.DESTROY), null, null, null,parentId, new SimpleRequestListener<ListResultWrap<List<FoodTypeModel>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<FoodTypeModel>> listListResultWrap) {
                foodtype_RV2.setVisibility(View.VISIBLE);
                if (listListResultWrap.total>0) {

                    foodtype_detail_RV.setVisibility(View.GONE);
                    mfoodTypeModels2 = listListResultWrap.rows;
                    foodTypeAdapter2.setNewData(mfoodTypeModels2);
                }else {
                    mfoodTypeModels2 = new ArrayList<FoodTypeModel>();
                    foodTypeAdapter2.setNewData(mfoodTypeModels2);
                    foodtype_detail_RV.setVisibility(View.GONE);
                }

            }
        });
*/
    }

    /**
     * 处理点击左侧菜品类型逻辑
     * @param view
     * @param i
     */
    public void clickFoodType(View view, int i) {
        edtSearch.clearFocus();
        FoodTypeModel foodTypeModel = mfoodTypeModels.get(i);
//        List<FoodTypeDetailModel> foodTypeDetailModels = foodTypeModel.getFoodTypeDetailModels();
        view.setBackgroundResource(R.drawable.p_chosefood_select_bg);
        selectPositoin = i;
        foodTypeAdapter.notifyDataSetChanged();
        /*if (foodTypeDetailModels != null) {
            foodTypeDetailAdapter.setNewData(foodTypeDetailModels);
            foodTypeDetailAdapter.notifyDataSetChanged();
        }*/

//        getFoodType2(foodTypeModel.getId());//获取二级类别
        //获取菜品类型详情列表

        getFoodList(foodTypeModel.getId(),null);
    }
    public void clickFoodType2(View view, int i) {
//        FoodTypeModel foodTypeModel = mfoodTypeModels2.get(i);
        //获取菜品类型详情列表

//        getFoodList(foodTypeModel.getId(),null);
    }


    /**
     * 处理点击右侧详细菜品逻辑
     * @param view
     * @param i
     */
    public void clickFoodDetailType(View view, int i,int type) {
        FoodTypeDetailModel foodTypeDetailModel = null;
        if (type == 0){
            foodTypeDetailModel = foodTypeDetailAdapter.getItem(i);
        }else if (type == 1){
            foodTypeDetailModel = foodTypeSearchAdapter.getItem(i);
        }
        Intent intent = new Intent(this,FoodInfoEdtiorActivity.class);
//        intent.setClass(this,FoodInfoEdtiorActivity.class);
        intent.putExtra("foodtypedetail", foodTypeDetailModel);
        if (getIntent().getIntExtra("type",0)>0) {
            intent.putExtra("type", SimpleEventType.ON_JH_ADD_BACK);
        }
        startActivity(intent);
//        setResult(CHOSE_FOOD_RESULT, intent);
        finish();
    }

    public void getFoodList(String typeId,String name){
        Api.api().getFoodTypeDetail(bindUntilEvent(ActivityEvent.DESTROY), typeId, name, new SimpleRequestListener<ListResultWrap<List<FoodTypeDetailModel>>>() {
            @Override
            public void onSuccess(ListResultWrap<List<FoodTypeDetailModel>> listListResultWrap) {
                List<FoodTypeDetailModel> foodTypeDetailModels;
                if (listListResultWrap.rows.size() > 0) {
//                    foodtype_RV2.setVisibility(View.GONE);
                    foodtype_detail_RV.setVisibility(View.VISIBLE);
                    foodTypeDetailModels = listListResultWrap.rows;


                }else {
                    foodTypeDetailModels = new ArrayList<FoodTypeDetailModel>();
//                    foodtype_RV2.setVisibility(View.VISIBLE);
//                    foodtype_detail_RV.setVisibility(View.GONE);
                }
                if (foodtype_searCh.getVisibility() ==View.GONE) {
                    foodTypeDetailAdapter.setNewData(foodTypeDetailModels);
                    foodTypeDetailAdapter.notifyDataSetChanged();
                }else {
                    foodTypeSearchAdapter.setNewData(foodTypeDetailModels);
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
    public class FoodTypeAdapter extends BaseQuickAdapter<FoodTypeModel> {


        public FoodTypeAdapter() {
            super(R.layout.food_choose_item_layout, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, FoodTypeModel foodTypeModel) {
            baseViewHolder
                    .setVisible(R.id.choosefood_img,false)
                    .setVisible(R.id.choose_xzy,false)
                    .setText(R.id.choosefood_TV, foodTypeModel.getName());
            if (baseViewHolder.getAdapterPosition() == selectPositoin) {
                baseViewHolder.getConvertView().setBackgroundResource(R.drawable.p_chosefood_select_bg);
            } else {
                baseViewHolder.getConvertView().setBackgroundDrawable(null);
            }
        }
    }
    public class FoodTypeAdapter2 extends BaseQuickAdapter<FoodTypeModel> {


        public FoodTypeAdapter2() {
            super(R.layout.food_choose_item_layout, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, FoodTypeModel foodTypeModel) {
            baseViewHolder
                    .setVisible(R.id.choosefood_img,false)
                    .setText(R.id.choosefood_TV, foodTypeModel.getName());

        }
    }

    public class FoodTypeDetailAdapter extends BaseQuickAdapter<FoodTypeDetailModel> {


        public FoodTypeDetailAdapter() {
            super(R.layout.food_choose_item_layout, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, FoodTypeDetailModel foodTypeDetailModel) {
            baseViewHolder
                    .setText(R.id.choosefood_TV, foodTypeDetailModel.getName());
            ImageView imvHead = (ImageView) baseViewHolder.getView(R.id.choosefood_img);
            if (foodTypeDetailModel.getId()!=null) {
                imvHead.setVisibility(View.VISIBLE);
                UserManager.getInstance().loadFoodHeadImage(FoodnfoLIistChooseActivity.this, imvHead, foodTypeDetailModel.getImgId());
            }else {
                imvHead.setVisibility(View.GONE);
            }

        }
    }


    public List<FoodTypeModel> getFoodTypeList() {
        FoodTypeModelDao foodTypeModelDao = (FoodTypeModelDao) DBManager.getInstance(this).getDao(FoodTypeModel.class);
        List<FoodTypeModel> foodTypeModels = foodTypeModelDao.loadAll();
        return foodTypeModels;
    }
}
