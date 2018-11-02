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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.RecyclerViewItemDecoration;
import cn.com.bigknow.trade.pos.Immediate.app.widget.Yuying;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.AreaBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;

/**
 * Created by laixiaoyang on 2016/10/17.
 */


public class FoodAreaLIistChooseActivity extends BaseActivity {

    @BindView(R.id.area_search)
    Button btnSearch;
    @BindView(R.id.area_chose_ET)
    EditText edtSearch;

    @BindView(R.id.areatype_RV)
    RecyclerView areatype_RV;

    @BindView(R.id.areatype_detail_RV)
    RecyclerView areatype_detail_RV;

    @BindView(R.id.areatype_searCh)
    RecyclerView areatype_searCh;
    @BindView(R.id.layout_areatype)
    LinearLayout layoutAreatype;
    @BindView(R.id.area_searchCancel)
    ImageView imvCancel;
    @BindView(R.id.area_searchYuyin)
    ImageView imvYuyin;

    AreaBean areaBean;
    private AreaTypeAdapter areaTypeAdapter;
    private AreaTypeDetailAdapter areaTypeDetailAdapter;

    private AreaTypeDetailAdapter areaTypeSearchAdapter;

    public static final int CHOSE_FOOD_RESULT = 0x1100;

    private String param;// 搜索
    @OnClick(R.id.area_search)
    public void gotoSearch(){
        closeInputMethod();
        //调用搜索接口
        param = edtSearch.getText().toString().trim();
        if (param!=null&& !param.equals("")) {
            getOutList(3, areaBean);
//            ToastUtil.makeToast(edtSearch.getText().toString().trim());
        }else {
            ToastUtil.makeToast("请输入菜名");
        }
    }
    @OnClick(R.id.area_searchCancel)
    public void cancelSearch(){
        edtSearch.setText("");
        edtSearch.clearFocus();
//        closeInputMethod();
    }
    @Override
    public void init() {
        setYuying(edtSearch,imvYuyin);
        setTitle("选择产地");
        areatype_RV.setLayoutManager(new LinearLayoutManager(this));
        areatype_detail_RV.setLayoutManager(new LinearLayoutManager(this));
        areatype_searCh.setLayoutManager(new LinearLayoutManager(this));
        areatype_detail_RV.addItemDecoration(new RecyclerViewItemDecoration(RecyclerView.HORIZONTAL, this, R.drawable.p_line));

        areaTypeAdapter = new AreaTypeAdapter();
        areaTypeDetailAdapter = new AreaTypeDetailAdapter();
        areaTypeSearchAdapter = new AreaTypeDetailAdapter();

        areatype_RV.setAdapter(areaTypeAdapter);
        areatype_detail_RV.setAdapter(areaTypeDetailAdapter);
        areatype_searCh.setAdapter(areaTypeSearchAdapter);
        areatype_RV.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                closeInputMethod();
                clickFoodType(view, i);
            }
        });
        areatype_detail_RV.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                clickFoodDetailType(view, i,0);
            }
        });
        areatype_searCh.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                clickFoodDetailType(view, i,1);
            }
        });


//        areaTypeAdapter.setOnRecyclerViewItemClickListener((view, i) -> clickFoodType(view, i));
//        foodTypeDetailAdapter.setOnRecyclerViewItemClickListener((view, i) -> clickFoodDetailType(view, i));
        getOutList(0,areaBean);

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
                    areatype_searCh.setVisibility(View.VISIBLE);
                    layoutAreatype.setVisibility(View.GONE);
                    param = edtSearch.getText().toString().trim();
                    getOutList(3,areaBean);
//                    ToastUtil.makeToast(edtSearch.getText().toString().trim());
                }else {
                    imvYuyin.setVisibility(View.VISIBLE);
                    imvCancel.setVisibility(View.GONE);
                    areatype_searCh.setVisibility(View.GONE);
                    layoutAreatype.setVisibility(View.VISIBLE);
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
        return R.layout.a_food_areaist_choose_layout;
    }

    public int selectPositoin = -1;


    /**
     * 处理点击左侧菜品类型逻辑
     * @param view
     * @param i
     */
    public void clickFoodType(View view, int i) {

        AreaBean areaBean = areaTypeAdapter.getItem(i);
        view.setBackgroundResource(R.drawable.p_chosefood_select_bg);
        view.findViewById(R.id.choose_xzy).setVisibility(View.VISIBLE);
        selectPositoin = i;
        areaTypeAdapter.notifyDataSetChanged();

        getOutList(1,areaBean);

    }


    /**
     * 处理点击右侧详细菜品逻辑
     * @param view
     * @param i
     */
    public void clickFoodDetailType(View view, int i,int type) {
        AreaBean areaBean = null;
        if (type == 0){
            areaBean  = areaTypeDetailAdapter.getItem(i);
        }else if (type ==1){
            areaBean  = areaTypeSearchAdapter.getItem(i);
        }
        Intent in = new Intent();
        in.putExtra("AreaBean", (Serializable) areaBean);
        setResult(RESULT_OK, in);
        finish();
    }

    public void getOutList(int type,AreaBean areaBean) {

        String parentId = null; ;//上级编码，省份上级编码为0
        String areaName ="";  //区域名，模糊查询
        int level = 0 ;  //区域级别，1-省，2-市，3-区县
        boolean supportPaging = false;
        int curPage = 1;
        int rows = 20;
        if (type == 0){ //省
            parentId = "0";
            level = 1;
        }else if (type == 1){ // 查询市区
           level = 2;
            parentId = String.valueOf(areaBean.getId());
        }else if(type == 3){  //搜索
            parentId = null;
            areaName = param;
            level = 2;
        }

        Api.api().getAreaDetialList(bindUntilEvent(ActivityEvent.DESTROY),
                parentId, areaName,level,
                supportPaging,curPage,rows, new SimpleRequestListener<BaseEntity<List<AreaBean>>>() {

                    @Override
                    public void onSuccess(BaseEntity<List<AreaBean>> listBaseEntity) {
                        if (listBaseEntity!=null && listBaseEntity.getData().size()>0){
                            if (type == 0){ //省
                                areaTypeAdapter.setNewData(convertData(listBaseEntity.getData()));
                                areaTypeAdapter.notifyDataSetChanged();
                            }else if (type == 1){ // 查询市区
                                areaTypeDetailAdapter.setNewData(convertData(listBaseEntity.getData()));
                                areaTypeDetailAdapter.notifyDataSetChanged();
                            }else if(type == 3){  //搜索
//                                areaName = param;
                                areaTypeSearchAdapter.setNewData(convertData(listBaseEntity.getData()));
                            }
                        }else {
                            if (type == 0){ //省
                                areaTypeAdapter.setNewData(new ArrayList<>());
                            }else if (type == 1){ // 查询市区
                                areaTypeDetailAdapter.setNewData(new ArrayList<>());
                            }else if(type == 3){  //搜索
//                                areaName = param;
                                areaTypeSearchAdapter.setNewData(new ArrayList<>());
                            }
                        }
                    }
                    @Override
                    public void onFinish() {
                        if (type == 0) {
                            progressHudUtil.dismissProgressHud();
                        }
                    }

                    @Override
                    public void onStart() {
                        if (type == 0) {
                            progressHudUtil.showProgressHud(false);
                        }
                    }
                });
    }

    private List<AreaBean> convertData(List list) {

        try {
            AreaBean task = (AreaBean) list.get(0);
            return list;
        } catch (ClassCastException e) {
            List<AreaBean> tasks = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof JSONObject) {
                    tasks.add(JSON.parseObject(((JSONObject) list.get(i)).toJSONString(), AreaBean.class));
                } else if (list.get(i) instanceof AreaBean) {
                    tasks.add((AreaBean) list.get(i));
                }
            }
            return tasks;
        }


    }
    public class AreaTypeAdapter extends BaseQuickAdapter<AreaBean> {


        public AreaTypeAdapter() {
            super(R.layout.food_choose_item_layout, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, AreaBean areaBean) {
            baseViewHolder
                    .setVisible(R.id.choosefood_img,false)
                    .setVisible(R.id.choose_xzy,false)
                    .setText(R.id.choosefood_TV, areaBean.getAreaName());
            if (baseViewHolder.getAdapterPosition() == selectPositoin) {
                baseViewHolder.getConvertView().setBackgroundResource(R.drawable.p_chosefood_select_bg);
                baseViewHolder
                        .setVisible(R.id.choose_xzy,true);
            } else {
                baseViewHolder.getConvertView().setBackgroundDrawable(null);

            }
        }
    }

    public class AreaTypeDetailAdapter extends BaseQuickAdapter<AreaBean> {


        public AreaTypeDetailAdapter() {
            super(R.layout.food_choose_item_layout, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, AreaBean areaBean) {
            baseViewHolder
                    .setVisible(R.id.choosefood_img,false)
                    .setVisible(R.id.choose_xzy,false)
                    .setText(R.id.choosefood_TV, areaBean.getAreaName());

        }
    }



}
