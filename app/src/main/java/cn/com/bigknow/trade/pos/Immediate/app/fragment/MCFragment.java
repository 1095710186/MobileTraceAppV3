package cn.com.bigknow.trade.pos.Immediate.app.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle.android.FragmentEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.MCAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.ui.ChooseFoodActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.PayActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.PriceListActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MCLeftTabView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MaterialDialog;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.Pay_Data;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.CommonUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PaymentInformationBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by hujie on 16/10/11.
 */

public class MCFragment extends BaseSupportFragment {


    @BindView(R.id.mcLeftTabView)
    MCLeftTabView mcLeftTabView;


    @BindView(R.id.recyclerView)
    SimpleSwipeRefreshLayout recyclerView;


    @BindView(R.id.noView)
    View noView;

    @BindView(R.id.noPayView)
    View noPayView;

    @BindView(R.id.billIdTV)
    TextView billIdTV;

    @BindView(R.id.yfjeTV)
    TextView yfjeTV;

    @BindView(R.id.retryBtn)
    Button retryBtn;

    @BindView(R.id.stateTV)
    TextView stateTV;


    @BindView(R.id.jzTV)
    View jzView;//结账

    @BindView(R.id.addFoodTV)
    View zjcpView;//增加菜品

    @BindView(R.id.Tv_shouz_time)
    TextView Tv_shouz_time;//时间显示

    @BindView(R.id.dlg_jiaoyan_sz)
    Button sz_qujiaoyan;//去校验

    private int selectIndex = 1;
    public ProgressHudUtil progressHudUtil;

    public static MCFragment newInstance() {
        Bundle args = new Bundle();
        MCFragment fragment = new MCFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }


    private void setLeftTabNum() {
        progressHudUtil = new ProgressHudUtil(getActivity());
        Billbean billbean1 = EntityManager.getBillByType(1);
        Billbean billbean2 = EntityManager.getBillByType(2);
        Billbean billbean3 = EntityManager.getBillByType(3);
        Billbean billbean4 = EntityManager.getBillByType(4);
        Billbean billbean5 = EntityManager.getBillByType(5);
        if (billbean1 != null) {
            if (billbean1.getPayState() == 1) {
                mcLeftTabView.setTab1Num("未支付");
            } else {
                mcLeftTabView.setTab1Num("支付失败");
            }
        } else {
            mcLeftTabView.setTab1Num(EntityManager.getShopCarFoodsCountByType(1) + "");
        }


        if (billbean2 != null) {
            if (billbean2.getPayState() == 1) {
                mcLeftTabView.setTab2Num("未支付");
            } else {
                mcLeftTabView.setTab2Num("支付失败");
            }
        } else {
            mcLeftTabView.setTab2Num(EntityManager.getShopCarFoodsCountByType(2) + "");
        }

        if (billbean3 != null) {
            if (billbean3.getPayState() == 1) {
                mcLeftTabView.setTab3Num("未支付");
            } else {
                mcLeftTabView.setTab3Num("支付失败");
            }
        } else {
            mcLeftTabView.setTab3Num(EntityManager.getShopCarFoodsCountByType(3) + "");
        }

        if (billbean4 != null) {
            if (billbean4.getPayState() == 1) {
                mcLeftTabView.setTab4Num("未支付");
            } else {
                mcLeftTabView.setTab4Num("支付失败");
            }
        } else {
            mcLeftTabView.setTab4Num(EntityManager.getShopCarFoodsCountByType(4) + "");
        }

        if (billbean5 != null) {
            if (billbean5.getPayState() == 1) {
                mcLeftTabView.setTab5Num("未支付");
            } else {
                mcLeftTabView.setTab5Num("支付失败");
            }
        } else {
            mcLeftTabView.setTab5Num(EntityManager.getShopCarFoodsCountByType(5) + "");
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(SimpleEvent event) {
        if (event.type == SimpleEventType.ON_MENUC_SELECT_MC) {
            EventMamager.getInstance().postIntEvent(SimpleEventType.ON_MC_LEFT_TAB_CHANGE, selectIndex);
        }
        if (event.type == SimpleEventType.ON_MC_LEFT_TAB_CHANGE) {
            if (selectIndex != event.intEvent) {
                mcLeftTabView.setSelectTab(event.intEvent);
            }
        }
        if (event.type == SimpleEventType.ON_MENU_DELETE_CLICK) {
            showDeleteDialog();
        }
        if (event.type == SimpleEventType.ON_DELETE_MC_FOOD) {
            refreshData();
        }
        if (event.type == SimpleEventType.ON_EDIT_CONT_MC_FOOD) {
            showDialog((ShopCarFood) event.objectEvent);
        }
    }

    private void showDeleteDialog() {
        int count = EntityManager.getShopCarFoodsCountByType(selectIndex);
        Billbean billbean = EntityManager.getBillByType(selectIndex);
        if (billbean != null) {
            MaterialDialog dialog = new MaterialDialog(getContext());
            dialog.setTitle("提示");
            dialog.setMessage("确定清除手账" + selectIndex + "的内容？");
            dialog.setNegativeButton("取消");
            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = billbean.getId();
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

                            refreshData();

                        }

                        @Override
                        public void onError(ApiError error) {
                            super.onError(error);
                            ToastUtil.makeToast(error.errorMsg);

                        }
                    });
                }
            });
            dialog.show();
        }
        if (count != 0) {
            MaterialDialog dialog = new MaterialDialog(getContext());
            dialog.setTitle("提示");
            dialog.setMessage("确定清除手账" + selectIndex + "的内容？");
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
                    refreshData();
                }
            });
            dialog.show();
        }
    }


    @Override
    public void initLazyView() {


        recyclerView.setRefreshEnable(false);

        mcLeftTabView.setOnTabSelectedListener(new MCLeftTabView.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int selectIndex) {
                MCFragment.this.selectIndex = selectIndex;
                EventMamager.getInstance().postIntEvent(SimpleEventType.ON_MC_LEFT_TAB_CHANGE, selectIndex);
                refreshData();
            }
        });
        mcLeftTabView.setSelectTab(1);

    }

    private void showDialog(ShopCarFood shopCarFood) {
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.weight_num_keyboard_layout, null);
        TextView nameTV = (TextView) view1.findViewById(R.id.nameTV);

        TextView showTV = (TextView) view1.findViewById(R.id.showTV);
        ImageView headIV = (ImageView) view1.findViewById(R.id.headIV);
        NumKeyboardView numkeyboardView = (NumKeyboardView) view1.findViewById(R.id.numkeyboardView);

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
                        if (numkeyboardView.getNumValue() < 1) {
                            ToastUtil.makeToast("重量不能低于1");
                            return;
                        }

                        shopCarFood.setQty(numkeyboardView.getNumValue());
                        EntityManager.saveShopCarFood(shopCarFood);
                        refreshData();
                        AlertDialogUtil.dissMiss();
                    }

                }
            }
        });
    }

    private void refreshData() {
        setData();
        setLeftTabNum();
    }

    Pay_Data get_databendi = new Pay_Data();

    private void setData() {

        //首先看有没有没有支付的
        Billbean billbean = EntityManager.getBillByType(selectIndex);

//        if(Tv_shouz_time.getVisibility()!=View.GONE){ Tv_shouz_time.setVisibility(View.GONE);}
        if (sz_qujiaoyan.getVisibility() != View.GONE) {
            sz_qujiaoyan.setVisibility(View.GONE);
        }

        if (billbean != null) {
            noView.setVisibility(View.GONE);
            noPayView.setVisibility(View.VISIBLE);
            billIdTV.setText(billbean.getBillNo());
            Tv_shouz_time.setText(billbean.getCreateDate());
            yfjeTV.setText(billbean.getPayAmt() + "元");
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
            zjcpView.setVisibility(View.GONE);
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(get_databendi.getorder_data(getContext())!=null) {
//                        if (get_databendi.getorder_data(getContext()).getOridId().toString().equals(billbean.getId())) {
//                            get_databendi.deletorder(getActivity());
//                        }
//                    }
                    startActivity(PayActivityAutoBundle.createIntentBuilder(selectIndex, billbean).build(getActivity()));
                }
            });
            MCAdapter adapter = new MCAdapter();
            recyclerView.setAdapter(adapter);
        } else {
            zjcpView.setVisibility(View.VISIBLE);
            noPayView.setVisibility(View.GONE);
            List<ShopCarFood> shopCarFoods = EntityManager.findShopCarFoodsByType(selectIndex);
            if (shopCarFoods == null || shopCarFoods.size() == 0) {
                noView.setVisibility(View.VISIBLE);
            } else {
                noView.setVisibility(View.GONE);
            }
            MCAdapter adapter = new MCAdapter();
            adapter.setNewData(shopCarFoods);
            recyclerView.setAdapter(adapter);
        }


    }

    @OnClick(R.id.addFoodTV)
    public void onAddFoodClick() {

        //首先看有没有没有支付的
        Billbean billbean = EntityManager.getBillByType(selectIndex);
        if (billbean != null) {
            ToastUtil.makeToast("请先完成订单支付");
            return;
        }

        startActivity(ChooseFoodActivityAutoBundle.createIntentBuilder(selectIndex).build(getActivity()));
        getActivity().overridePendingTransition(0, 0);

    }


    @OnClick(R.id.jzTV)
    public void onJZClick() {

        //首先看有没有没有支付的
        Billbean billbean = EntityManager.getBillByType(selectIndex);
        if (billbean != null) {
//            if(get_databendi.getorder_data(getContext())!=null) {
//                if (get_databendi.getorder_data(getContext()).getOridId().toString().equals(billbean.getId())) {
//                    get_databendi.deletorder(getActivity());
//                }
//            }
            startActivity(PayActivityAutoBundle.createIntentBuilder(selectIndex, billbean).build(getActivity()));
            return;
        }

        if (EntityManager.getShopCarFoodsCountByType(selectIndex) == 0) {
            ToastUtil.makeToast("请先添加菜品");
//            show_jianyan_Dialog();
            return;
        }
        startActivity(PriceListActivityAutoBundle.createIntentBuilder(selectIndex).build(getContext()));
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }


    @Override
    public int layoutId() {
        return R.layout.f_mc_layout;
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

        Billbean billbean = EntityManager.getBillByType(selectIndex);
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

                Billbean billbean = EntityManager.getBillByType(selectIndex);

                if (billbean != null) {

                    if (get_databendi.getorder_data(getContext(), selectIndex) != null) {
                        if (get_databendi.getorder_data(getContext(), selectIndex).getOridId().toString().equals(billbean.getId())) {

                            PaymentInformationBean pay_xinxi = get_databendi.getorder_data(getContext(), selectIndex);

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

                get_databendi.deletorder(getActivity(), selectIndex);
                dissclose();
                Billbean billbean = EntityManager.getBillByType(selectIndex);
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

}
