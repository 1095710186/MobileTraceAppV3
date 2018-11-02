package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.PriceAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MaterialDialog;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFoodPay;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by hujie on 16/10/13.
 */

public class PriceListActivity extends BaseActivity {
    @BindView(R.id.actionbar)
    Toolbar mToolbar;
    @AutoBundleField
    int selectIndex = 1;


    @BindView(R.id.toolbarTitleTV)
    TextView titleView;


    @BindView(R.id.refreshLayout)
    SimpleSwipeRefreshLayout refreshLayout;

    @BindView(R.id.zjeTV)
    TextView zjeTV;//总金额

    float test=1;

    @Override
    public void init() {
        setToolbar();
        setToolbarColor(colors[selectIndex - 1]);
        titleView.setText("手账" + selectIndex);
        refreshLayout.setRefreshEnable(false);

        refreshLayout.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                showInputPriceDialog((ShopCarFood) baseQuickAdapter.getItem(i));
            }
        });
        setData();

        refreshLayout.getRecyclerView().addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {




                ShopCarFood  item_data=  (ShopCarFood) baseQuickAdapter.getItem(i);
                if(view.getId()==R.id.price_listitem_jiajia){//加价
                      Float xx = 0.1f;
                    BigDecimal b1 = new BigDecimal(Float.toString(xx));
                    BigDecimal b2 = new BigDecimal(Float.toString(item_data.getPrice()));

                    float qwe=b1.add(b2).floatValue();
                   item_data.setPrice(qwe);
                    List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(selectIndex);

                    for (ShopCarFood ss : shopCarFoods) {
                        if (ss.getItemId().equals(item_data.getItemId())) {
                            ss.setPrice(qwe);
                            EntityManager.saveShopCarFood(ss);
                        }

                    }
                    EntityManager.savePriceSort(item_data.getItemId(), qwe);

                    setData();

                }else{
                    if(view.getId()==R.id.price_listitem_jianjia){//减价
                        Float xx = 0.1f;
                        BigDecimal b1 = new BigDecimal(Float.toString(xx));
                        BigDecimal b2 = new BigDecimal(Float.toString(item_data.getPrice()));

                        float qwe=b2.subtract(b1).floatValue();
                        if(qwe>0){
                        item_data.setPrice(qwe);
                        List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(selectIndex);

                        for (ShopCarFood ss : shopCarFoods) {
                            if (ss.getItemId().equals(item_data.getItemId())) {
                                ss.setPrice(qwe);
                                EntityManager.saveShopCarFood(ss);
                            }

                        }
                            EntityManager.savePriceSort(item_data.getItemId(), qwe);

                            setData();
                        }
                    }
                }
            }
        });
    }

    private void showInputPriceDialog(ShopCarFood shopCarFood) {


        View view1 = LayoutInflater.from(this).inflate(R.layout.price_num_keyboard_layout, null);
        TextView nameTV = (TextView) view1.findViewById(R.id.nameTV);

        TextView showTV = (TextView) view1.findViewById(R.id.showTV);
        TextView zlTV = (TextView) view1.findViewById(R.id.zlTV);
        TextView zjTV = (TextView) view1.findViewById(R.id.zjTV);
        ImageView headIV = (ImageView) view1.findViewById(R.id.headIV);
        NumKeyboardView numkeyboardView = (NumKeyboardView) view1.findViewById(R.id.numkeyboardView);

        numkeyboardView.setShowTextView(showTV);
        numkeyboardView.setOpenRing(false);

        showTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numkeyboardView.getNumValue() != 0) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    zjTV.setText(df.format(shopCarFood.getQty() * numkeyboardView.getNumValue()) + "");
                } else {
                    zjTV.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        zlTV.setText(shopCarFood.getQty() + "斤");
        nameTV.setText(shopCarFood.getItemName());

        UserManager.getInstance().loadFoodHeadImage(this, headIV, shopCarFood.getImgId());

        AlertDialogUtil.showNumKeyboardDialog(this, view1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sureBtn) {
                    if (numkeyboardView.getNumValue() == 0) {
                        ToastUtil.makeToast("请输入价格");
                    } else {
                        shopCarFood.setPrice(numkeyboardView.getNumValue());
                        List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(selectIndex);

                        for (ShopCarFood ss : shopCarFoods) {
                            if (ss.getItemId().equals(shopCarFood.getItemId())) {
                                ss.setPrice(numkeyboardView.getNumValue());
                                EntityManager.saveShopCarFood(ss);
                            }
                        }
                        EntityManager.savePriceSort(shopCarFood.getItemId(),numkeyboardView.getNumValue());
                        setData();
                        AlertDialogUtil.dissMiss();
                    }

                }
            }
        });
    }


    private void setData() {

        PriceAdapter adapter = new PriceAdapter();

       for(int i=0;i<getCombinList().size();i++){

           if(getCombinList().get(i).getPrice()<=0){
            for(int k=0;k<EntityManager.getAllPriceSort().size();k++){
                if(getCombinList().get(i).getItemId().toString().equals(EntityManager.getAllPriceSort().get(k).getId().toString())){
                    getCombinList().get(i).setPrice(EntityManager.getAllPriceSort().get(k).getPrice());
                }
            }
           }

       }

        adapter.setNewData(getCombinList());
        refreshLayout.setAdapter(adapter);
        float zje = jsZJE(getCombinList());
        if (zje > 0) {
            DecimalFormat df = new DecimalFormat("#.##");
            zjeTV.setText(df.format(zje) + "");
        } else {
            zjeTV.setText("");
        }
    }


    /**
     * 计算总金额
     * @param shopCarFoods
     * @return
     */
    private float jsZJE(List<ShopCarFood> shopCarFoods) {
        float zje = 0;
        for (ShopCarFood shopCarFood : shopCarFoods) {
            zje += shopCarFood.getAmt();
        }
        return zje;
    }

    private boolean checkAllPrice() {
        List<ShopCarFood> shopCarFoods = getCombinList();
        for (ShopCarFood shopCarFood : shopCarFoods) {
            if (shopCarFood.getAmt() <= 0) {
                return false;
            }
        }
        return true;
    }

    private float totalzl() {
        List<ShopCarFood> shopCarFoods = getCombinList();
        float total = 0;
        for (ShopCarFood shopCarFood : shopCarFoods) {
            total += shopCarFood.getQty();
        }
        return total;

    }

    @OnClick(R.id.fh)
    public void onfh() {
        finish();
    }

    @OnClick(R.id.scddTV)
    public void onSCDD() {
        if (zjeTV.getText().length() == 0) {
            ToastUtil.makeToast("请先输入价格");
            return;
        } else if (!checkAllPrice()) {
            ToastUtil.makeToast("请输入每个菜品的价格");
            return;
        }
        requestAddOrder();

    }

    private List<ShopCarFood> getCombinList() {
        List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(selectIndex);
        Map<String, List<ShopCarFood>> map = new HashMap<>();
        for (ShopCarFood shopCarFood : shopCarFoods) {
            if (map.containsKey(shopCarFood.getItemId())) {
                map.get(shopCarFood.getItemId()).add(shopCarFood);
            } else {
                List<ShopCarFood> shopCarFoods1 = new ArrayList<>();
                shopCarFoods1.add(shopCarFood);
                map.put(shopCarFood.getItemId(), shopCarFoods1);
            }
        }

        Set<Map.Entry<String, List<ShopCarFood>>> set = map.entrySet();
        List<ShopCarFood> shopCarFoodList = new ArrayList<>();
        for (Map.Entry<String, List<ShopCarFood>> entry : set) {
            List<ShopCarFood> s = entry.getValue();
            if (s.size() > 1) {
                ShopCarFood shopCarFood = new ShopCarFood();

                for (ShopCarFood s1 : s) {
                    shopCarFood.setItemId(s1.getItemId());
                    shopCarFood.setShopCarType(s1.getShopCarType());
                    shopCarFood.setUnitId(s1.getUnitId());
                    shopCarFood.setPrice(s1.getPrice());
                    shopCarFood.setQty(shopCarFood.getQty() + s1.getQty());
                    shopCarFood.setItemName(s1.getItemName());
                    shopCarFood.setMainUnitId(s1.getMainUnitId());
                }
                shopCarFoodList.add(shopCarFood);
            } else {
                shopCarFoodList.addAll(s);
            }
        }
        return shopCarFoodList;
    }


    public void requestAddOrder() {

        List<ShopCarFood> shopCarFoods = getCombinList();
        for (ShopCarFood shopCarFood : shopCarFoods) {
            if (shopCarFood.getUnitId() == null) {
                shopCarFood.setUnitId(shopCarFood.getMainUnitId());
            }
        }

        RequestWrap<ShopCarFoodPay> shopCarFoodPayRequestWrap = new RequestWrap<>();
        shopCarFoodPayRequestWrap.name = "dsMain";


        List<ShopCarFoodPay> shopCarFoodPays = new ArrayList<>();


        ShopCarFoodPay pay = new ShopCarFoodPay();

        pay.kinds = EntityManager.getShopCarFoodsCountByType(selectIndex);
        pay.sumAmt = Float.parseFloat(zjeTV.getText().toString());
        pay.payAmt = pay.sumAmt;
        pay.sumQty = totalzl();


        shopCarFoodPays.add(pay);

        shopCarFoodPayRequestWrap.data = shopCarFoodPays;


        RequestWrap<ShopCarFood> foodBeanRequestWrap = new RequestWrap<>();
        foodBeanRequestWrap.name = "dsDet";

        foodBeanRequestWrap.data = shopCarFoods;

        List<RequestWrap> requestWraps = new ArrayList<>();
        requestWraps.add(shopCarFoodPayRequestWrap);
        requestWraps.add(foodBeanRequestWrap);

        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        simpleRequestWrap.dataset = requestWraps;


        Api.api().addNewOrder(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<BaseEntity<Billbean>>() {
            @Override
            public void onSuccess(BaseEntity<Billbean> o) {

                ToastUtil.makeToast("生成订单成功");
                o.data.setSelectIndex(selectIndex);
                o.data.setBillDate(o.data.getCreateDate());
                EntityManager.saveBill(o.data);
                EntityManager.deleteShopCarFoodsByType(selectIndex);
                startActivity(PayActivityAutoBundle.createIntentBuilder(selectIndex, o.data).build(mActivity));
                finish();
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
                if(error.errorCode.equals("mvp.inv.short.inv.err_code")){
                    MaterialDialog dialog = new MaterialDialog(mActivity);
                    dialog.setTitle("提示！");
                    dialog.setMessage(error.errorMsg.toString());
                    dialog.setNegativeButton("确定");
                    dialog.show();
                }else{
                    ToastUtil.makeToast(error.toString());
                }

            }


        });

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
        return R.layout.a_privce_list_layout;
    }
}
