package cn.com.bigknow.trade.pos.Immediate.app.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.trello.rxlifecycle.android.FragmentEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.MCV2Adapter;
import cn.com.bigknow.trade.pos.Immediate.app.ui.LoginActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MCV2ChooseFoodActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.OrderDetailActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.PayActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MCNumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MCRightTabView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MaterialDialog;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MoneyView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MyListView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.Pay_Data;
import cn.com.bigknow.trade.pos.Immediate.app.widget.SimpleOnKeyBoardClick;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.CommonUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PaymentInformationBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PriceSort;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFoodPay;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static cn.com.bigknow.trade.pos.Immediate.R.id.zjeTV;
import static cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager.getAllPriceSort;
import static java.lang.Float.parseFloat;

/**
 * Created by hujie on 16/12/26.
 */

public class MCFragmentV2 extends BaseSupportFragment {


    @BindView(R.id.leftInputPartView)
    View leftInputView;  //输入菜品面板

    @BindView(R.id.ssfoodView)
    View ssFoodView;//选中菜品的区域
    @BindView(R.id.ssfoodImgIV)
    ImageView ssFoodIV;//选中菜品的头图
    @BindView(R.id.ssfoodNameTV)
    TextView ssFoodNameTV; //选择菜品的名字


    @BindView(R.id.priceCursorView)
    View priceCursorView;

    @BindView(R.id.zlCursorView)
    View zlCursorView;

    @BindView(R.id.inputPriceView)
    View inputPriceView;//输入价格区域
    @BindView(R.id.inputPriceET)
    TextView inputPriceET;//输入价格文本框v

    @BindView(R.id.inputZLView)
    View inputZLView; //输入重量区域
    @BindView(R.id.inputZLET)
    TextView inputZLET;//输入重量文本框


    @BindView(R.id.leftPayListPartView)
    View leftPayView; //支付列表面板

    @BindView(R.id.noView)
    View noView;


    @BindView(R.id.leftUnPayPartView)
    View leftUnPayView; //未支付面板

    @BindView(R.id.bottomInputPartView)
    View bottomInputView; //输入时底部按钮区域

    @BindView(R.id.bottomUnPayPartView)
    View bottomUnPayView; //未支付时底部按钮

    @BindView(R.id.bottomPayListPartView)
    View bottomPayView; //支付时底部按钮

    @BindView(R.id.rightMenuPartView)
    View rightMenuView; //右边菜单面板

    @BindView(R.id.rightFoodPartView)
    LinearLayout rightFoodView;//右边菜品列表
    @BindView(R.id.moreFoodView)
    View moreFoodView;
    @BindView(R.id.foodsLL)
    LinearLayout foodsLL;


    @BindView(R.id.Tv_shouz_time)
    TextView Tv_shouz_time;//时间显示

    @BindView(R.id.dlg_jiaoyan_sz)
    Button sz_qujiaoyan;//去校验
    @BindView(R.id.billIdTV)
    TextView billIdTV;

    @BindView(R.id.yfjeTV)
    TextView yfjeTV;

    @BindView(R.id.retryBtn)
    Button retryBtn;

    @BindView(R.id.stateTV)
    TextView stateTV;


    @BindView(R.id.exchangeView)
    View exchangeView;

    @BindView(R.id.mcRightTabView)
    MCRightTabView mcRightTabView;

    @BindView(R.id.numkeyboardView)
    MCNumKeyboardView numkeyboardView;


    private static final int INPUT_MODE = 1;
    private static final int PAY_MODE = 3;

    private int currentMode; //

    private FoodInfo selectFood;
    private List<FoodInfo> foodInfoList;
    public ProgressHudUtil progressHudUtil;


    @BindView(R.id.refreshLayout)
    SimpleSwipeRefreshLayout refreshLayout;

    @BindView(zjeTV)
    MoneyView zjeView;

    @BindView(R.id.mc_mlvList)
    MyListView mc_mlvList;   //未支付  菜品列表

    @BindView(R.id.mcx_tvSum)
    TextView mcx_tvSum;//未支付  货品总额

    @BindView(R.id.mcx_kindsTV)
    TextView mcx_kindsTV;

    @BindView(R.id.mcx_mxlayout)
    LinearLayout mcx_mxlayout;

    @BindView(R.id.mcx_checkbox_ordermx)
    CheckBox mcx_checkbox_ordermx;  //订单详情  展开与隐藏

    @BindView(R.id.mcx_checkbox_text)
    TextView mcx_checkbox_text;

    @BindView(R.id.xjTV)
    TextView xjTV;


    DecimalFormat df = new java.text.DecimalFormat("#.##");

    @Override
    public void initLazyView() {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        setInitMode();
        initView();
        setViewMode(currentMode);
    }


    private void setInitMode() {
        if (isUnPayMode() || hasPayFoodCount()) {
            currentMode = PAY_MODE;
        } else {
            currentMode = PAY_MODE;//INPUT_MODE;
        }
    }

    /**
     * 是否是未支付模式
     * (未支付或者支付失败)
     * @return
     */
    private boolean isUnPayMode() {
        int i = mcRightTabView.getSelectIndex();
        Billbean billbean1 = EntityManager.getBillByType(i);
        if (billbean1 != null) {
            return true;
        }
        return false;
    }

    /**
     * 是否有待支付的菜品
     * @return
     */
    private boolean hasPayFoodCount() {
        int i = mcRightTabView.getSelectIndex();
        return EntityManager.getShopCarFoodsCountByType(i) != 0;
    }


    private void initView() {
        mcRightTabView.setOnTabSelectedListener(selectIndex ->
        {
            EventMamager.getInstance().postIntEvent(SimpleEventType.ON_MC_LEFT_TAB_CHANGE, selectIndex);
            onTabExchange();
        });
        mcRightTabView.setSelectTab(1);

        numkeyboardView.setSimpleOnKeyBoardClick(new SimpleOnKeyBoardClick() {
            @Override
            public void onClickTab() {
                exchangeInputView(!inputPriceView.isSelected());
            }
        });
        exchangeInputView(false);

        progressHudUtil = new ProgressHudUtil(getActivity());
        refreshLayout.setRefreshEnable(false);

        inputPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                jsxj();
                sortFood();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputZLET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                jsxj();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void sortFood() {
        if (inputPriceET.getText().toString().length() == 0) {
            return;
        }
        float price;
        try {
            price = parseFloat(inputPriceET.getText().toString());
        } catch (Exception e) {
            return;
        }
        if (price > 0) {
            if (foodInfoList != null && foodInfoList.size() > 0) {
                List<PriceSort> sorts = getAllPriceSort();
                if (sorts != null && sorts.size() > 0) {
                    for (PriceSort priceSorts : sorts) {
                        for (FoodInfo foodInfo : foodInfoList) {
                            if (foodInfo.getId().endsWith(priceSorts.getId())) {
                                foodInfo.setSortPrice(priceSorts.getPrice());
                            }
                        }
                    }
                    Collections.sort(foodInfoList, new Comparator<FoodInfo>() {
                        @Override
                        public int compare(FoodInfo o1, FoodInfo o2) {
                            float a1 = Math.abs(o1.getSortPrice() - price);
                            float a2 = Math.abs(o2.getSortPrice() - price);
                            if (a1 < a2) {
                                return -1;
                            } else if (a1 == a2) {
                                return 0;
                            }
                            return 1;
                        }
                    });

                    handleFoodsData(foodInfoList);

                } else {
                    return;
                }


            }

        }

    }


    private void jsxj() {

        if (getNumValue(inputZLET) == 0) {
            xjTV.setText("0");
            return;
        }
        if (getNumValue(inputPriceET) == 0) {
            xjTV.setText("0");
            return;
        }
        xjTV.setText(df.format(getNumValue(inputPriceET) * getNumValue(inputZLET)));
    }


    private void showEditPriceDialog(ShopCarFood shopCarFood) {

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.price_num_keyboard_layout, null);
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
                    BigDecimal numQ = new BigDecimal(shopCarFood.getQty());
                    BigDecimal numP = new BigDecimal(numkeyboardView.getNumValue());
                    numQ.multiply(numP);
                    zjTV.setText(df.format(numQ.multiply(numP)) + "");
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

        UserManager.getInstance().loadFoodHeadImage(getActivity(), headIV, shopCarFood.getImgId());

        AlertDialogUtil.showNumKeyboardDialog(getActivity(), view1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sureBtn) {
                    if (numkeyboardView.getNumValue() == 0) {
                        ToastUtil.makeToast("请输入价格");
                    } else {
                        shopCarFood.setPrice(numkeyboardView.getNumValue());

                        EntityManager.saveShopCarFood(shopCarFood);
                        EntityManager.savePriceSort(shopCarFood.getItemId(), numkeyboardView.getNumValue());

                        AlertDialogUtil.dissMiss();

                        refreshData();
                    }

                }
            }
        });
    }

    private void showEditZLDialog(ShopCarFood shopCarFood) {
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.weight_num_keyboard_layout, null);
        TextView nameTV = (TextView) view1.findViewById(R.id.nameTV);

        TextView showTV = (TextView) view1.findViewById(R.id.showTV);
        ImageView headIV = (ImageView) view1.findViewById(R.id.headIV);
        TextView showJ = (TextView) view1.findViewById(R.id.showJ);
        TextView showT = (TextView) view1.findViewById(R.id.showT);
        NumKeyboardView numkeyboardView = (NumKeyboardView) view1.findViewById(R.id.numkeyboardView);

        showJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showT.setVisibility(View.VISIBLE);
                showJ.setVisibility(View.GONE);
                if (numkeyboardView.getNumValue() > 0) {
//                    showTV.setText("");
                    numkeyboardView.onKeyBoardClick.onCleanAll();
                }
            }
        });
        showT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJ.setVisibility(View.VISIBLE);
                showT.setVisibility(View.GONE);
                if (numkeyboardView.getNumValue() > 0) {
//                    showTV.setText("");
                    numkeyboardView.onKeyBoardClick.onCleanAll();//onClickClear();

                }
            }
        });

        numkeyboardView.setShowTextView(showTV);

        nameTV.setText(shopCarFood.getItemName());

        UserManager.getInstance().loadFoodHeadImage(getActivity(), headIV, shopCarFood.getImgId());


        AlertDialogUtil.showNumKeyboardDialog(getActivity(), view1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sureBtn) {
                    if (numkeyboardView.getNumValue() == 0) {
                        ToastUtil.makeToast("请输入重量");
                    } else {
                        if (showJ.getVisibility() == View.VISIBLE) {
                            shopCarFood.setQty(numkeyboardView.getNumValue());
                        } else {
                            shopCarFood.setQty(numkeyboardView.getNumValue() * 2000);
                        }
                        EntityManager.saveShopCarFood(shopCarFood);
                        refreshData();
                        AlertDialogUtil.dissMiss();
                    }
                }
            }
        });
    }

    private void showDeleteDialog(ShopCarFood shopCarFood) {
        EntityManager.deleteShopCarFood(shopCarFood);
        refreshData();
    }


    Pay_Data get_databendi = new Pay_Data();

    /**
     * 当切换右侧的tab时
     */
    private void onTabExchange() {

        setViewMode(currentMode);

        int selectIndex = mcRightTabView.getSelectIndex();
        //首先看有没有没有支付的
        Billbean billbean = EntityManager.getBillByType(selectIndex);
        sz_qujiaoyan.setVisibility(View.GONE);
        if (billbean != null) {
            noView.setVisibility(View.GONE);
            billIdTV.setText(billbean.getBillNo());
            Tv_shouz_time.setText(billbean.getCreateDate());
            DecimalFormat df = new DecimalFormat("#.##");
            yfjeTV.setText(df.format(billbean.getPayAmt()) + "元");

            mcx_kindsTV.setText("共" + billbean.getKinds() + "种/" + df.format(billbean.getSumQty()) + "斤");
            mcx_tvSum.setText("￥" + df.format(billbean.getSumAmt()));
            mcx_checkbox_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mcx_checkbox_ordermx.isChecked()) {
                        mcx_checkbox_ordermx.setChecked(false);
                        mcx_checkbox_text.setText("订单详细");
                        mcx_mxlayout.setVisibility(View.GONE);
                    } else {
                        mcx_checkbox_ordermx.setChecked(true);
                        mcx_checkbox_text.setText("收起");
                        mcx_mxlayout.setVisibility(View.VISIBLE);
                    }
                }
            });
            mcx_checkbox_ordermx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mcx_checkbox_ordermx.isChecked()) {
                        mcx_checkbox_ordermx.setText("收起");
                        mcx_mxlayout.setVisibility(View.VISIBLE);
                    } else {
                        mcx_checkbox_ordermx.setText("订单详细");
                        mcx_mxlayout.setVisibility(View.GONE);
                    }
                }
            });

            mcmxadapter = new MC_weipayAdapter(billbean.getShopCarFoods());
            mc_mlvList.setAdapter(mcmxadapter);
            if (billbean.getPayState() == 1) {
                stateTV.setText("该订单未支付");
                retryBtn.setText("去支付");
                if (get_databendi.getorder_data(getContext(), selectIndex) != null) {
                    if (get_databendi.getorder_data(getContext(), selectIndex).getOridId().toString().equals(billbean.getId())) {
                        Tv_shouz_time.setVisibility(View.VISIBLE);//时间显示
                        sz_qujiaoyan.setVisibility(View.VISIBLE);//去校验
                        sz_qujiaoyan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                show_jianyan_Dialog();
                            }
                        });
                    }
                }
            } else {
                retryBtn.setText("重试");
                stateTV.setText("该订单支付失败");
            }
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(PayActivityAutoBundle.createIntentBuilder(selectIndex, billbean).build(getActivity()));
                }
            });
        } else {

            if (!hasPayFoodCount()) {
                noView.setVisibility(View.VISIBLE);
            } else {
                noView.setVisibility(View.GONE);
                setPayListData();
            }

        }
    }

    private void setPayListData() {
        MCV2Adapter adapter = new MCV2Adapter();
        List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(mcRightTabView.getSelectIndex());
        adapter.setNewData(shopCarFoods);
        refreshLayout.setAdapter(adapter);
        String zje = jsZJE_(shopCarFoods);
        zjeView.setMoneyText(zje + "");
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
        DecimalFormat df = new DecimalFormat("#.##");
        if (zje != 0) {
            zje = Float.valueOf(df.format(zje));
        }
        return zje;
    }
    private String jsZJE_(List<ShopCarFood> shopCarFoods) {
        float zje = 0;
        for (ShopCarFood shopCarFood : shopCarFoods) {
            zje += shopCarFood.getAmt();
        }
        DecimalFormat df = new DecimalFormat("#.##");

        return df.format(zje);
    }


    private List<ShopCarFood> getCombinList() {
        List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(mcRightTabView.getSelectIndex());
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


    @Override
    public boolean autoBindEvent() {
        return true;
    }

    Pay_Data bendi_da = new Pay_Data();

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        if (bendi_da.getpay_data(getActivity()) != null) {
            bendi_da.post_pay(bendi_da.getpay_data(getActivity()), getActivity());//得到本地数据进行提交。断网数据
        }
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadFoodsData();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause
        } else {
            // 相当于Fragment的onResume
            currentMode = PAY_MODE;
            setViewMode(currentMode);
        }
    }

    /**
     * 加载菜品
     */
    private void loadFoodsData() {
       /* if (foodInfoList != null) {
            if (foodsLL.getChildCount() == 0) {
                handleFoodsData(foodInfoList);
            }
            return;
        }*/

        Api.api().getFoodList(bindUntilEvent(FragmentEvent.DESTROY), "Y", "Y", new SimpleRequestListener<BaseEntity<List<FoodInfo>>>() {
            @Override
            public void onSuccess(BaseEntity<List<FoodInfo>> o) {
                if (o != null && o.getData().size() > 0) {
                    moreFoodView.setVisibility(View.VISIBLE);
                    handleFoodsData(sortFoodinfo(o.data));
                }
                else {
                    moreFoodView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFinish() {
//                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onStart() {
//                progressHudUtil.showProgressHud();
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
                moreFoodView.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 处理菜品数据
     * @param foodInfos
     */
    private void handleFoodsData(List<FoodInfo> foodInfos) {
        this.foodInfoList = foodInfos;

        int height = rightFoodView.getHeight();
        if (height == 0) {
            return;
        }
        height = height - 0;//DensityUtil.dip2px(getContext(), 30);
        int itemHeight = DensityUtil.dip2px(getContext(), 80);
        int count = height / itemHeight;
        foodsLL.removeAllViews();

        foodsLL.getLayoutParams().height = itemHeight * count;
        rightFoodView.updateViewLayout(foodsLL, foodsLL.getLayoutParams());

        /*if (count < foodInfos.size()) {
            int moreHeight = height - itemHeight * count;
            moreFoodView.getLayoutParams().height = moreHeight + DensityUtil.dip2px(getContext(), 30);
            moreFoodView.setVisibility(View.VISIBLE);
            rightFoodView.updateViewLayout(moreFoodView, moreFoodView.getLayoutParams());
        } else {

            moreFoodView.setVisibility(View.INVISIBLE);
        }*/

        for (int i = 0; i < (count < foodInfos.size() ? count : foodInfos.size()); i++) {
            View foodItemView = LayoutInflater.from(getContext()).inflate(R.layout.mc_v2_food_item_layout, null);


            ImageView imgIV = (ImageView) foodItemView.findViewById(R.id.imgIV);
            TextView nameTV = (TextView) foodItemView.findViewById(R.id.nameTV);
            TextView zlTv = (TextView) foodItemView.findViewById(R.id.zlTV);

            zlTv.setText(foodInfos.get(i).getInvQty() + "斤");
            nameTV.setText(foodInfos.get(i).getAlias());
            UserManager.getInstance().loadFoodHeadImage(getActivity(), imgIV, foodInfos.get(i).getImgId());

            foodsLL.addView(foodItemView);
            int finalI = i;
            foodItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectFood(foodInfos.get(finalI));
                }
            });

        }
        moreFoodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList aa = new ArrayList();
                if (foodInfoList !=null && foodInfoList.size()>0) {
                    aa.addAll(foodInfoList);
                    startActivity(MCV2ChooseFoodActivityAutoBundle.createIntentBuilder(mcRightTabView.getSelectIndex(), aa).build(getActivity()));
                    getActivity().overridePendingTransition(0, 0);
                }
            }
        });
    }

    /**
     * 当选中菜品
     * @param foodInfo
     */
    private void onSelectFood(FoodInfo foodInfo) {

        EntityManager.saveFoodSortCount(foodInfo.getId());
//        if (findShopCarFoodById(foodInfo.getId()) != null) {
//            inputPriceET.setText(findShopCarFoodById(foodInfo.getId()).getPrice() + "");
//            if (inputPriceView.isSelected()) {
//                numkeyboardView.setShowTextView(inputPriceET);
//                numkeyboardView.setShowText(inputPriceET.getText().toString());
//            }
//        }
        List<PriceSort> list_price = getAllPriceSort();
        for (int i = 0; i < list_price.size(); i++) {
            if (list_price.get(i).getId().equals(foodInfo.getId()) && inputPriceET.getText().toString().length() == 0) {
                inputPriceET.setText(list_price.get(i).getPrice() + "");
                exchangeInputView(true);
            }
        }

        this.selectFood = foodInfo;
        ssFoodNameTV.setText(foodInfo.getAlias());
        UserManager.getInstance().loadFoodHeadImage(getActivity(), ssFoodIV, foodInfo.getImgId());
        ssFoodView.setVisibility(View.VISIBLE);
    }


    /**
     * 对菜品排序
     * @param foodInfos
     * @return
     */
    private List<FoodInfo> sortFoodinfo(List<FoodInfo> foodInfos) {


        for (FoodInfo foodInfo : foodInfos) {
            EntityManager.setFoodSortCount(foodInfo);
        }
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


    /**
     * 刷新数据
     */
    public void refreshData() {
        setRightTabViewNum();
        onTabExchange();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(SimpleEvent event) {
        if (event.type == SimpleEventType.ON_MENUC_SELECT_MC) {
            EventMamager.getInstance().postIntEvent(SimpleEventType.ON_MC_LEFT_TAB_CHANGE, mcRightTabView.getSelectIndex());
        }
        if (event.type == SimpleEventType.ON_MC_LEFT_TAB_CHANGE) {
            if (mcRightTabView.getSelectIndex() != event.intEvent) {
                mcRightTabView.setSelectTab(event.intEvent);
            }
        }
        if (event.type == SimpleEventType.ON_CHOOSE_FOOD_V2_BACK) {
            onSelectFood((FoodInfo) event.objectEvent);
        }


        if (event.type == SimpleEventType.ON_EDIT_MC_V2_FOOD_PRICE) {
            showEditPriceDialog((ShopCarFood) event.objectEvent);
        }
        if (event.type == SimpleEventType.ON_EDIT_MC_V2_FOOD_ZL) {
            showEditZLDialog((ShopCarFood) event.objectEvent);
        }
        if (event.type == SimpleEventType.ON_EDIT_MC_V2_FOOD_DELETE) {
            showDeleteDialog((ShopCarFood) event.objectEvent);
        }
        if (event.type == SimpleEventType.ON_MENU_DELETE_CLICK) {
            showClearDialog();
        }
        if (event.type == SimpleEventType.ON_CLEAR_WXPAY) {
            tuisongpost(Integer.parseInt(event.strEvent));

        }if (event.type == SimpleEventType.ON_FOOD_BACK) {
            int selectIndex = mcRightTabView.getSelectIndex();
            //首先看有没有没有支付的
            Billbean billbean = EntityManager.getBillByType(selectIndex);
            if (billbean == null) {
                OnClickQiehuan();
            }
        }

        if (event.type == SimpleEventType.ON_CLEANJ_KC_BACK){ // 清库存后
            loadFoodsData();
        }


    }

    public void OnClickQiehuan(){
        if (currentMode == PAY_MODE) {
            currentMode = INPUT_MODE;
        } else {
            currentMode = PAY_MODE;
        }
        setViewMode(currentMode);

    }

    public void tuisongpost(int aaa){

        EntityManager.deleteBill(EntityManager.getBillByType(aaa));
        setInitMode();
        setViewMode(currentMode);
        refreshData();


    }



    private void showClearDialog() {
        int selectIndex = mcRightTabView.getSelectIndex();
        int count = EntityManager.getShopCarFoodsCountByType(selectIndex);
        Billbean billbean = EntityManager.getBillByType(selectIndex);
        clear();
        if (billbean != null) {
            MaterialDialog dialog = new MaterialDialog(getContext());
            dialog.setTitle("提示");
            dialog.setMessage("确定清除" + selectIndex + "篮的内容？");
            dialog.setNegativeButton("取消");
            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String>  ids = new ArrayList() {
                        {
                            add(billbean.getId());
                        }
                    };
                    Api.api().deleteBill(bindUntilEvent(FragmentEvent.DESTROY), ids, new SimpleRequestListener() {
                        @Override
                        public void onSuccess(Object o) {

                            if (get_databendi.getorder_data(getContext(), selectIndex) != null) {
                                if (get_databendi.getorder_data(getContext(), selectIndex).getOridId().toString().equals(billbean.getId())) {
                                    get_databendi.deletorder(getActivity(), selectIndex);
                                }
                            }
                            EntityManager.deleteBill(billbean);

                            setInitMode();
                            setViewMode(currentMode);

                            refreshData();
                        }

                        @Override
                        public void onError(ApiError error) {
                            super.onError(error);
                            ToastUtil.makeToast(error.errorMsg);
                            if (error.errorCode.equals("common.data.ood.err_code")){
                                if (get_databendi.getorder_data(getContext(), selectIndex) != null) {
                                    if (get_databendi.getorder_data(getContext(), selectIndex).getOridId().toString().equals(billbean.getId())) {
                                        get_databendi.deletorder(getActivity(), selectIndex);
                                    }
                                }
                                EntityManager.deleteBill(billbean);

                                setInitMode();
                                setViewMode(currentMode);

                                refreshData();
                            }

                        }
                    });
                }
            });
            dialog.show();
        }
        if (count != 0) {
            MaterialDialog dialog = new MaterialDialog(getContext());
            dialog.setTitle("提示");
            dialog.setMessage("确定清除" + selectIndex + "篮的内容？");
            dialog.setNegativeButton("取消");
            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (get_databendi.getorder_data(getContext(), selectIndex) != null) {
                        if (get_databendi.getorder_data(getContext(), selectIndex).getOridId().toString().equals(billbean.getId())) {
                            get_databendi.deletorder(getActivity(), selectIndex);
                        }
                    }

                    EntityManager.deleteShopCarFoodsByType(selectIndex);

                    setInitMode();
                    setViewMode(currentMode);
                    refreshData();
                }
            });
            dialog.show();
        }
    }


    /**
     * 设置右边tab的数量
     */
    private void setRightTabViewNum() {
        Billbean billbean1 = EntityManager.getBillByType(1);
        Billbean billbean2 = EntityManager.getBillByType(2);
        Billbean billbean3 = EntityManager.getBillByType(3);
        Billbean billbean4 = EntityManager.getBillByType(4);
        Billbean billbean5 = EntityManager.getBillByType(5);
        if (billbean1 != null) {
            if (billbean1.getPayState() == 1) {
                mcRightTabView.setTab1Num("未支付");
            } else {
                mcRightTabView.setTab1Num("支付失败");
            }
        } else {
            mcRightTabView.setTab1Num(EntityManager.getShopCarFoodsCountByType(1) + "");
        }
        if (billbean2 != null) {
            if (billbean2.getPayState() == 1) {
                mcRightTabView.setTab2Num("未支付");
            } else {
                mcRightTabView.setTab2Num("支付失败");
            }
        } else {
            mcRightTabView.setTab2Num(EntityManager.getShopCarFoodsCountByType(2) + "");
        }

        if (billbean3 != null) {
            if (billbean3.getPayState() == 1) {
                mcRightTabView.setTab3Num("未支付");
            } else {
                mcRightTabView.setTab3Num("支付失败");
            }
        } else {
            mcRightTabView.setTab3Num(EntityManager.getShopCarFoodsCountByType(3) + "");
        }

        if (billbean4 != null) {
            if (billbean4.getPayState() == 1) {
                mcRightTabView.setTab4Num("未支付");
            } else {
                mcRightTabView.setTab4Num("支付失败");
            }
        } else {
            mcRightTabView.setTab4Num(EntityManager.getShopCarFoodsCountByType(4) + "");
        }

        if (billbean5 != null) {
            if (billbean5.getPayState() == 1) {
                mcRightTabView.setTab5Num("未支付");
            } else {
                mcRightTabView.setTab5Num("支付失败");
            }
        } else {
            mcRightTabView.setTab5Num(EntityManager.getShopCarFoodsCountByType(5) + "");
        }
    }


    /**
     * 设置显示隐藏view
     * @param mode
     */
    private void setViewMode(int mode) {
        EventMamager.getInstance().postIntEvent(SimpleEventType.ON_MC_LEFT_TAB_CHANGE, mcRightTabView.getSelectIndex());
        if (mode == INPUT_MODE) {
            leftInputView.setVisibility(View.VISIBLE);
            leftPayView.setVisibility(View.INVISIBLE);
            leftUnPayView.setVisibility(View.INVISIBLE);
            rightFoodView.setVisibility(View.VISIBLE);
            if (rightFoodView.getVisibility() == View.VISIBLE){
                if (foodInfoList!=null && foodInfoList.size()>0){
                    moreFoodView.setVisibility(View.VISIBLE);
                    handleFoodsData(sortFoodinfo(foodInfoList));
                }else {
                    loadFoodsData();
                }
            }
            rightMenuView.setVisibility(View.INVISIBLE);
            bottomInputView.setVisibility(View.VISIBLE);
            bottomPayView.setVisibility(View.INVISIBLE);
            bottomUnPayView.setVisibility(View.INVISIBLE);
//            exchangeView.setVisibility(View.VISIBLE);

        } else if (mode == PAY_MODE) {

            if (!isUnPayMode()) {
                leftInputView.setVisibility(View.INVISIBLE);
                leftPayView.setVisibility(View.VISIBLE);
                leftUnPayView.setVisibility(View.INVISIBLE);
                rightFoodView.setVisibility(View.INVISIBLE);
                rightMenuView.setVisibility(View.VISIBLE);
                bottomInputView.setVisibility(View.INVISIBLE);
                bottomPayView.setVisibility(View.VISIBLE);
                bottomUnPayView.setVisibility(View.INVISIBLE);
//                exchangeView.setVisibility(View.VISIBLE);
            } else {
                leftInputView.setVisibility(View.INVISIBLE);
                leftPayView.setVisibility(View.INVISIBLE);
                leftUnPayView.setVisibility(View.VISIBLE);
                rightFoodView.setVisibility(View.INVISIBLE);
                rightMenuView.setVisibility(View.VISIBLE);
                bottomInputView.setVisibility(View.INVISIBLE);
                bottomPayView.setVisibility(View.INVISIBLE);
                bottomUnPayView.setVisibility(View.INVISIBLE);
//                exchangeView.setVisibility(View.INVISIBLE);
            }

        }

    }


    @OnClick(value = {R.id.inputPriceView, R.id.inputZLView})
    public void onInputClick(View v) {
        if (v.getId() == R.id.inputPriceView) {
//            inputPriceET.setText("");
            exchangeInputView(true);
        } else {
            exchangeInputView(false);
        }
    }

    /**
     * 切换重量跟价格输入
     * @param isInputPrice
     */
    public void exchangeInputView(boolean isInputPrice) {
        if (isInputPrice) {
            inputPriceView.setSelected(true);
            inputZLView.setSelected(false);
            inputPriceET.setSelected(true);
            inputZLET.setSelected(false);
//            setCursorView(priceCursorView);
//            priceCursorView.setVisibility(View.VISIBLE);
//            zlCursorView.setVisibility(View.INVISIBLE);

            numkeyboardView.setShowTextView(inputPriceET);
            numkeyboardView.setShowText("");//inputPriceET.getText().toString());


        } else {
            inputPriceView.setSelected(false);
            inputZLView.setSelected(true);
            inputPriceET.setSelected(false);
            inputZLET.setSelected(true);

//            setCursorView(zlCursorView);
//            priceCursorView.setVisibility(View.INVISIBLE);
//            zlCursorView.setVisibility(View.VISIBLE);

            numkeyboardView.setShowTextView(inputZLET);
            numkeyboardView.setShowText("");//inputZLET.getText().toString());


        }
    }

    Subscription cursorSubscription;

    @Override
    public void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    private void unSubscribe() {
        if (cursorSubscription != null && !cursorSubscription.isUnsubscribed()) {
            cursorSubscription.unsubscribe();
            cursorSubscription = null;
        }
    }


    private void setCursorView(View view) {

        unSubscribe();
        cursorSubscription = rx.
                Observable.interval(0, 500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                view.setVisibility(view.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }


    @OnClick(R.id.exchangeView)
    public void onExchangeViewClick() {
        if (currentMode == PAY_MODE) {
            currentMode = INPUT_MODE;
        } else {
            currentMode = PAY_MODE;
        }
        setViewMode(currentMode);

    }

    @OnClick(value = {R.id.addFoodView, R.id.zhifuView, R.id.mxView, R.id.rlView, R.id.inputZhifuView})
    public void onBottomBtnClick(View v) {
        switch (v.getId()) {
            case R.id.addFoodView:   //添加
//                onExchangeViewClick();
                OnClickQiehuan();
                break;
            case R.id.mxView:    //明细
//                onExchangeViewClick();
                OnClickQiehuan();
                break;
            case R.id.rlView:

                onClickRL();   //入篮

                break;

            case R.id.zhifuView:  //直接支付（明细）


                onZhifu();
                break;
            case R.id.inputZhifuView:  //直接支付（甜菜）
                onInputZhifu();
                break;

        }

    }


    boolean isRequestAddOrder = false;

    private synchronized void onZhifu() {
        if (isRequestAddOrder) {
            return;
        }
        isRequestAddOrder = true;
        List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(mcRightTabView.getSelectIndex());
        if (shopCarFoods == null || shopCarFoods.size() == 0) {
            isRequestAddOrder = false;
            return;
        }

        requestAddOrder(shopCarFoods);
    }

    private synchronized void onInputZhifu() {
        if (isRequestAddOrder) {
            return;
        }
        isRequestAddOrder = true;
        List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(mcRightTabView.getSelectIndex());

        ShopCarFood ss = hasComplete();

        if (shopCarFoods == null || shopCarFoods.size() == 0) {
            if (ss != null) {
                EntityManager.saveShopCarFood(ss);
                EntityManager.savePriceSort(ss.getItemId(), getNumValue(inputPriceET));
                shopCarFoods = new ArrayList<>();
                shopCarFoods.add(ss);
            } else {
                isRequestAddOrder = false;
                return;
            }

        } else if (ss != null) {
            EntityManager.saveShopCarFood(ss);
            EntityManager.savePriceSort(ss.getItemId(), getNumValue(inputPriceET));
            shopCarFoods.add(ss);
        }

        if(selectFood!=null){
            if (selectFood.getId()!=null) {
                EntityManager.savePriceSort(selectFood.getId(), getNumValue(inputPriceET));
            }
        }
        requestAddOrder(shopCarFoods);
    }

    private ShopCarFood hasComplete() {
        if (getNumValue(inputZLET) == 0) {

            return null;
        }

        if (getNumValue(inputPriceET) == 0) {

            return null;
        }
        if (selectFood == null) {

            return null;
        }
        ShopCarFood food1 = new ShopCarFood();
        food1.setImgId(selectFood.getImgId());
        food1.setItemId(selectFood.getId());
        food1.setQty(food1.getQty() + getNumValue(inputZLET));
        food1.setMainUnitId(selectFood.getMainUnitId());
        food1.setUnitId(selectFood.getUnitId());
        food1.setItemName(selectFood.getAlias());//food.getItemName()
        food1.setShopCarType(mcRightTabView.getSelectIndex());
        food1.setPrice(getNumValue(inputPriceET));
//        saveShoopCarFood();


        return food1;

    }


    private float totalzl(List<ShopCarFood> shopCarFoods) {
        float total = 0;
        for (ShopCarFood shopCarFood : shopCarFoods) {
            total += shopCarFood.getQty();
        }
        return total;

    }

    public void requestAddOrder(List<ShopCarFood> shopCarFoods) {


        for (ShopCarFood shopCarFood : shopCarFoods) {
            if (shopCarFood.getUnitId() == null) {
                shopCarFood.setUnitId(shopCarFood.getMainUnitId());
            }
        }

        RequestWrap<ShopCarFoodPay> shopCarFoodPayRequestWrap = new RequestWrap<>();
        shopCarFoodPayRequestWrap.name = "dsMain";


        List<ShopCarFoodPay> shopCarFoodPays = new ArrayList<>();


        ShopCarFoodPay pay = new ShopCarFoodPay();

        pay.kinds = shopCarFoods.size();//EntityManager.getShopCarFoodsCountByType(mcRightTabView.getSelectIndex());
        pay.sumAmt = jsZJE(shopCarFoods);
        pay.payAmt = pay.sumAmt;
        pay.sumQty = totalzl(shopCarFoods);


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


        Api.api().addNewOrder(bindUntilEvent(FragmentEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<BaseEntity<Billbean>>() {
            @Override
            public void onSuccess(BaseEntity<Billbean> o) {

                if (currentMode == INPUT_MODE && hasComplete() != null) {
                    clear();
                }

                ToastUtil.makeToast("生成订单成功");
                o.data.setSelectIndex(mcRightTabView.getSelectIndex());
                o.data.setBillDate(o.data.getCreateDate());
                o.data.setShopCarFoods(shopCarFoods);
                EntityManager.saveBill(o.data);

                Billbean index_baen = EntityManager.getBillByType(mcRightTabView.getSelectIndex());
                if (index_baen.getKinds() != index_baen.getShopCarFoods().size() && index_baen.getShopCarFoods().size() > 0) {
                    if (index_baen.getShopCarFoods().size() == index_baen.getKinds() + 1) {
                        index_baen.setKinds(index_baen.getShopCarFoods().size());
                        EntityManager.saveBill(index_baen);
                    }
                }

//                EntityManager.deleteShopCarFoodsByType(mcRightTabView.getSelectIndex());
//                startActivity(PayActivityAutoBundle.createIntentBuilder(mcRightTabView.getSelectIndex(), o.data).build(getActivity()));
                startActivity(OrderDetailActivityAutoBundle.createIntentBuilder(mcRightTabView.getSelectIndex(), o.data).build(getActivity()));
                setInitMode();


            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud();
            }

            @Override
            public void onFinish() {
                isRequestAddOrder = false;
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onError(ApiError error) {
                if (error.errorCode.equals("mvp.inv.short.inv.err_code")) {
                    MaterialDialog dialog = new MaterialDialog(getActivity());
                    dialog.setTitle("提示！");
                    dialog.setMessage(error.errorMsg.toString());
                    dialog.setNegativeButton("确定");
                    dialog.show();
                } else {
                    if (error.isApiError() && ("common.access.err_code".equals(error.errorCode)
                            ||("common.connect.err_code").equals(error.errorCode))
                            ||("common.conncect.err_code").equals(error.errorCode)) {
                        toLogin(BootstrapApp.get());
                    }
                    ToastUtil.makeToast(error.toString());
                }

            }

        });

    }
    // 重新登录
    private static void toLogin(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("reLogin", true);
        context.startActivity(intent);
    }

    public float getNumValue(TextView textView) {
        if (textView.length() == 0) {
            return 0;
        } else {
            if (textView.getText().toString().startsWith(".")) {
                return Float.parseFloat("0" + textView.getText().toString());
            } else {
                return Float.parseFloat(textView.getText().toString());
            }
        }
    }

    /**
     * 点击入蓝
     */
    private void onClickRL() {
        if (getNumValue(inputZLET) == 0) {
            ToastUtil.makeToast("请输入重量");
            return;
        }

        if (getNumValue(inputPriceET) == 0) {
            ToastUtil.makeToast("请输入价格");
            return;
        }
        if (selectFood == null) {
            ToastUtil.makeToast("请选择菜品");
            return;
        }
        saveShoopCarFood();
        clear();
        refreshSelectTab();

//        onExchangeViewClick();
        OnClickQiehuan();
    }


    /**
     * 刷新选中的tab
     */
    private void refreshSelectTab() {
        EventMamager.getInstance().postIntEvent(SimpleEventType.ON_MC_LEFT_TAB_CHANGE, mcRightTabView.getSelectIndex());
        refreshData();
    }


    private ShopCarFood findShopCarFoodById(String id) {
        List<ShopCarFood> ss = EntityManager.findShopCarFoodsByType(mcRightTabView.getSelectIndex());
        //判断本地是否已经存在一个同样的菜品
        if (ss != null) {
            for (ShopCarFood sc : ss) {
                if (id.equals(sc.getItemId())) {
                    return sc;
                }
            }
        }
        return null;
    }

    /**
     * 保存到购物车
     */
    private void saveShoopCarFood() {


        ShopCarFood food1 = new ShopCarFood();

        food1.setImgId(selectFood.getImgId());
        food1.setItemId(selectFood.getId());
        food1.setQty(food1.getQty() + getNumValue(inputZLET));
        food1.setMainUnitId(selectFood.getMainUnitId());
        food1.setUnitId(selectFood.getUnitId());
        food1.setItemName(selectFood.getAlias());//food.getItemName()
        food1.setShopCarType(mcRightTabView.getSelectIndex());
        food1.setPrice(getNumValue(inputPriceET));
        EntityManager.saveShopCarFood(food1);
        EntityManager.savePriceSort(food1.getItemId(), getNumValue(inputPriceET));
    }

    private void clear() {
        inputPriceET.setText("");
        inputZLET.setText("");
        selectFood = null;
        ssFoodView.setVisibility(View.GONE);
        exchangeInputView(false);
    }


    public static MCFragmentV2 newInstance() {
        Bundle args = new Bundle();
        MCFragmentV2 fragment = new MCFragmentV2();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int layoutId() {
        return R.layout.f_mc_v2;
    }


    TextView text_jy_sb;
    TextView text_jy_sbyuanyin;
    EditText jiansuohao;

    private void show_jianyan_Dialog() {

        if (dialog != null && dialog.isShowing()) {
            return;
        }

        dialog = new AlertDialog.Builder(getActivity()).create();

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dailog_order_pay, null);

        TextView jy_kahao = (TextView) view1.findViewById(R.id.dlg_jy_kahao);
        TextView jy_yingfu = (TextView) view1.findViewById(R.id.dlg_jy_yingfu);
        TextView jy_time = (TextView) view1.findViewById(R.id.dlg_jy_time);

        jiansuohao = (EditText) view1.findViewById(R.id.jiaoyan_jiansuohao);
        jiansuohao.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        text_jy_sb = (TextView) view1.findViewById(R.id.jiaoy_shibai);
        text_jy_sbyuanyin = (TextView) view1.findViewById(R.id.jiaoy_shibai_yuany);

        Billbean billbean = EntityManager.getBillByType(mcRightTabView.getSelectIndex());
        if (billbean != null) {
            jy_kahao.setText(billbean.getBillNo());
            jy_yingfu.setText(billbean.getPayAmt() + "元");
            jy_time.setText(billbean.getBillDate());
        }
        View janyan_back = (View) view1.findViewById(R.id.dlg_jy_back);
        View janyan_jinru = (View) view1.findViewById(R.id.dlg_jy_jiaoyan);
        janyan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();
            }
        });
        janyan_jinru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isjshNum(jiansuohao.getText().toString())) {
                    if (CommonUtil.isNetWorkConnected(getActivity())) {
                        Intent intent = new Intent("ICBCScript");
                        Bundle bundle = new Bundle();
                        bundle.putString("CallStr", "11032|037" + jiansuohao.getText().toString());//70000644
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 123);
                    } else {
                        ToastUtil.makeToast("当前网络不可用，请检查网络");
                    }
                } else {
                    ToastUtil.makeToast("请输入8位数字的检索号！");
                }

            }
        });

        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new AlertDialog.Builder(getActivity()).create();

        dialog.setView(view1, 0, 0, 0, 0);
        /** 3.自动弹出软键盘 **/
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(jiansuohao, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        dialog.show();

    }

    public final static Pattern jshPattern = Pattern.compile("^[0-9]{8}$");

    public boolean isjshNum(String jsh) {
        return jshPattern.matcher(jsh).matches();
    }

    static AlertDialog dialog;

    public void dissclose() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //获取返回字符串，格式及内容说明见“脚本交易返回字符串说明”
        if (requestCode == 123 && data != null) {

            String retStr = data.getExtras().getString("ReturnStr");
            if (get_databendi.parsing_data(retStr) != null) {
                PaymentInformationBean javabean = get_databendi.parsing_data(retStr);

                Billbean billbean = EntityManager.getBillByType(mcRightTabView.getSelectIndex());

                if (billbean != null) {

                    if (get_databendi.getorder_data(getContext(), mcRightTabView.getSelectIndex()) != null) {
                        if (get_databendi.getorder_data(getContext(), mcRightTabView.getSelectIndex()).getOridId().toString().equals(billbean.getId())) {

                            PaymentInformationBean pay_xinxi = get_databendi.getorder_data(getContext(), mcRightTabView.getSelectIndex());

                            pay_xinxi.setState(javabean.getState());

                            pay_xinxi.setPayDate(javabean.getPayDate());

                            pay_xinxi.setBankId(javabean.getBankId());

                            pay_xinxi.setAccountNo(javabean.getAccountNo());

                            pay_xinxi.setResponseTime(javabean.getResponseTime());

                            pay_xinxi.setResponseId(javabean.getResponseId());

                            pay_xinxi.setPayDate(javabean.getPayDate());

                            float money = Float.parseFloat(pay_xinxi.getAmt());
                            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                            String zfp = decimalFormat.format(money);//format 返回的是字符串
                            String regEx = "[^0-9]";
                            Pattern p = Pattern.compile(regEx);
                            Matcher m = p.matcher(zfp);
                            String zifu = m.replaceAll("").trim();
                            int strLen = zifu.length();
                            StringBuffer sb = null;
                            while (strLen < 12) {
                                sb = new StringBuffer();
                                sb.append("0").append(zifu);// 左(前)补0
                                // sb.append(str).append("0");//右(后)补0
                                zifu = sb.toString();
                                strLen = zifu.length();
                            }

                            //判断订单是否一致
                            if (zifu.equals(javabean.getAmt())) {//判断订单金额与支付金额是否相等

                                panduan_time(billbean.getCreateDate(), javabean.getPayDate(), pay_xinxi);//判断订单时间与支付时间的大小

                            } else {
                                text_jy_sb.setText("校验失败！");
                                text_jy_sbyuanyin.setText("此检索号对应的支付金额与此订单对应的金额不相同！");

                            }

                        }
                    }
                }

            } else {
//                ToastUtil.makeToast("校验失败！"+retStr);
                text_jy_sb.setText("校验失败！");
                text_jy_sbyuanyin.setText("(没有此检索号)或是(此检索号对应的订单没有付款)");

            }
            Log.d("6666666666666666", retStr);
//            ToastUtil.makeToast("检验返回数据==="+retStr);

        }

    }


    //提交本地数据的请求
    public void post_pay(PaymentInformationBean pay_bean, Context context) {

        List<PaymentInformationBean> pay_bean_list = new ArrayList<>();


        pay_bean_list.add(pay_bean);

        RequestWrap<PaymentInformationBean> foodBeanRequestWrap = new RequestWrap<>();

        foodBeanRequestWrap.name = "dsMain";

        foodBeanRequestWrap.data = pay_bean_list;

        List<RequestWrap> requestWraps = new ArrayList<>();

        requestWraps.add(foodBeanRequestWrap);

        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        simpleRequestWrap.dataset = requestWraps;

        ToastUtil.makeToast("同步数据");
        Api.api().PaymentSubmitgb(simpleRequestWrap, new SimpleRequestListener<String>() {
            @Override
            public void onSuccess(String o) {
                text_jy_sb.setText("校验成功！");
                ToastUtil.makeToast("校验成功！");

                get_databendi.deletorder(getActivity(), mcRightTabView.getSelectIndex());
                dissclose();
                Billbean billbean = EntityManager.getBillByType(mcRightTabView.getSelectIndex());
                if (billbean != null) {
                    EntityManager.deleteBill(billbean);
                    refreshData();
                }
                get_databendi.delet_xinxi(context);
                dissclose();
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
                ToastUtil.makeToast(error.toString());
                text_jy_sb.setText("校验失败！");
                text_jy_sbyuanyin.setText(error.errorMsg);
                text_jy_sb.getResources().getColor(R.color.red);
                text_jy_sbyuanyin.getResources().getColor(R.color.red);
                if (!TextUtils.isEmpty(error.errorCode)) {
                    if (error.errorCode.equals("common.data.ood.err_code")) {//||error.errorCode.equals("mvp.pay.used.response.err_code")
                        get_databendi.delet_xinxi(context);
                    }
                }

            }
        });


    }


    public void panduan_time(String order_time, String pay_time, PaymentInformationBean pay_xinxi) {//判断订单时间与支付时间的大小

        String s1 = order_time;
        String s2 = pay_time;
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
        } catch (java.text.ParseException e) {
            System.err.println("时间格式格式不正确！");
        }
        int result = c1.compareTo(c2);

        if (result == 0) {

            System.out.println("order_time相等pay_time");
            text_jy_sb.setText("校验失败！");
            text_jy_sb.setText("订单时间与支付时间相同！");

        } else if (result < 0) {


            System.out.println("order_time小于pay_time");
            text_jy_sb.setText("");
            text_jy_sbyuanyin.setText("");

            get_databendi.savepay_xinxi(getActivity(), pay_xinxi);

            post_pay(pay_xinxi, getActivity());


        } else {


            text_jy_sb.setText("校验失败！");
            System.out.println("order_time大于pay_time");
            text_jy_sbyuanyin.setText("订单时间与支付时间的先后不对应！");


        }
    }


    private MC_weipayAdapter mcmxadapter;

    public class MC_weipayAdapter extends BaseAdapter {
        private List<ShopCarFood> mvpOrderDet;
        DecimalFormat df = new java.text.DecimalFormat("#.##");

        public MC_weipayAdapter(List<ShopCarFood> mvpOrderDet) {
            this.mvpOrderDet = mvpOrderDet;
        }

        public void Update(List<ShopCarFood> mvpOrderDet) {
            this.mvpOrderDet = mvpOrderDet;
            notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return mvpOrderDet.size();
        }

        @Override
        public Object getItem(int position) {
            return mvpOrderDet.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MC_weipayAdapter.Holder holder;
            if (null == convertView) {
                holder = new MC_weipayAdapter.Holder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.w_tj_dzdetail_item, null); //mContext指的是调用的Activtty
                holder.tvFoodName = (TextView) convertView.findViewById(R.id.tvName);
                holder.tvPayPrice = (TextView) convertView.findViewById(R.id.tvPayPrice);
                holder.tvPayNum = (TextView) convertView.findViewById(R.id.tvPayNum);

                convertView.setTag(holder);
            } else {
                holder = (MC_weipayAdapter.Holder) convertView.getTag();
            }
            ShopCarFood billDetBean = mvpOrderDet.get(position);
            holder.tvFoodName.setText(billDetBean.getItemName()); //billDetBean.getItemName()
            holder.tvPayPrice.setText("￥" + df.format(billDetBean.getPrice()) + "x" + df.format(billDetBean.getQty()) + "斤");
            holder.tvPayNum.setText("小计：￥" + df.format(billDetBean.getAmt()));
            return convertView;
        }

        class Holder {
            public TextView tvFoodName, tvPayNum, tvPayPrice;

        }
    }


}
