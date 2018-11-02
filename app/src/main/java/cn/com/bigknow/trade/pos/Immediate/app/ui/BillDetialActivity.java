package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MyListView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BillDetBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * 订单详情
 * 2016/8/23 17:49
 */
public class BillDetialActivity extends BaseActivity {
    @BindView(R.id.tvBillNO)
    TextView tvBillNO;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.tvTotalprice)
    TextView tvTotalprice;
    @BindView(R.id.tvSaleP)
    TextView tvSaleP;
    @BindView(R.id.tvSalePrice)
    TextView tvSalePrice;
    @BindView(R.id.tvMolingP)
    TextView tvMolingP;
    @BindView(R.id.tvMolingPrice)
    TextView tvMolingPrice;
    @BindView(R.id.tvPingTaiP)
    TextView tvPingTaiP;
    @BindView(R.id.tvPingTaiPrice)
    TextView tvPingTaiPrice;
    @BindView(R.id.tvAllTotal)
    TextView tvAllTotal;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvBankNO)
    TextView tvBankNO;
    @BindView(R.id.tvPayName)
    TextView tvPayName;
    @BindView(R.id.tvPayDate)
    TextView tvPayDate;
    @BindView(R.id.rlButton)
    View rlButton;
    @BindView(R.id.llBank)
    View llBank;
    @BindView(R.id.ivAvator)
    RoundedImageView ivAvator;
    @BindView(R.id.mlvList)
    MyListView mlvList;
    @BindView(R.id.btnDetel)
    ImageView btnDetel;
    public static final String IMG_URL = ModelConfig.MVP_URL + "mvpMerchantItem/getHead.action";
    public String IMG_URL_avator = ModelConfig.BIGKNOW_FRAMEWORK_URL + "servlet/fileupload?oper_type=img&masterFileType=psnPhoto&masterId=";
    Billbean billbean;
    private ProgressHudUtil progressHudUtil;

    @Override
    public void init() {
        progressHudUtil = new ProgressHudUtil(this);
        billbean = (Billbean) getIntent().getSerializableExtra("billbean");
       /* if (billbean!=null) {
            tvBillNO.setText("订单号：" + billbean.getBillNo());
            if (billbean.getBalState().equals("Y")) {//已付款
                tvStatus.setText("已付款");
                tvStatus.setBackgroundResource(R.drawable.w_shape_billlist_item_text_bg2);
                rlButton.setVisibility(View.GONE);
            } else if (billbean.getBalState().equals("N")) {//待付款
                if (billbean.getState().equals("C")) {
                    tvStatus.setText("已撤销");
                    tvStatus.setBackgroundResource(R.drawable.w_shape_billlist_item_text_bg);
                    rlButton.setVisibility(View.GONE);
                    btnDetel.setVisibility(View.VISIBLE);
                } else {
                    tvStatus.setText("待付款");
                    tvStatus.setBackgroundResource(R.drawable.w_shape_billlist_item_text_bg1);
                    rlButton.setVisibility(View.VISIBLE);
                }
                llBank.setVisibility(View.GONE);
            } else {

            }

            String corrInfo = billbean.getCorrInfo();
            String[] strArray = null;
            if (corrInfo != null || !"".equals(corrInfo)) {
                strArray = corrInfo.split(","); //拆分字符为"," ,然后把结果交给数组strArray
                if (strArray.length > 0 && strArray.length == 4) {
                    //加载logo  touxiang
                    Glide.with(this).load(IMG_URL_avator + strArray[0]).placeholder(R.drawable.w_pos_icon_tx).crossFade().into(ivAvator);

                    tvName.setText(strArray[1] + "");
                    tvShopName.setText(strArray[2] + " " + strArray[3]);
                } else {
                    tvName.setText(billbean.getCorrName() + "");
                    tvShopName.setText(billbean.getCorrInfo() != null ? billbean.getCorrInfo() : "--");
                }
            } else {
                tvName.setText(billbean.getCorrName() + "");
                tvShopName.setText(billbean.getCorrInfo() != null ? billbean.getCorrInfo() : "--");
            }
//        tvName.setText(billbean.getCreator());
//        tvShopName.setText("字段不明确");
            tvDate.setText(billbean.getBillDate());
            float numAl = getTotalAll(billbean.getMvpOrderDet());
            tvCount.setText("共" + numAl + billbean.getMvpOrderDet().get(0).getUnitName());
            tvTotalprice.setText(billbean.getSumAmt() + "");
            tvSaleP.setText((billbean.getDiscRate()*100) + "%");
            float salePrice = (float)(Math.round(billbean.getSumAmt()*billbean.getDiscRate()*100))/100;//billbean.getDiscAmt(); //折扣后
            tvSalePrice.setText(salePrice + "");
            tvMolingP.setText("-" + billbean.getIgnoreAmt());
            float molingPrece = salePrice -billbean.getIgnoreAmt();// 抹零后；
            tvMolingPrice.setText(molingPrece + "");
            tvPingTaiP.setText("-" + billbean.getSubsidyAmt());

            tvPingTaiPrice.setText(billbean.getSubsidyAmt() + "");
//        tvAllTotal.setText(billbean.getBalAmt() + "");
            float pay = molingPrece - billbean.getSubsidyAmt();
            tvAllTotal.setText(billbean.getPayAmt() == pay ? billbean.getPayAmt() + "" : pay + "");
            if (billbean.getMvpPayinfo().size() > 0 && (billbean.getMvpPayinfo().get(0).getPayChanner() != null && billbean.getMvpPayinfo().get(0).getPayChanner().equals("U"))) {
                PayInfoBean payInfoBean = billbean.getMvpPayinfo().get(0);
                tvPayDate.setText(payInfoBean.getPayDate());
                tvBankName.setText(payInfoBean.getBankName());
                tvBankNO.setText(payInfoBean.getBankId());
                llBank.setVisibility(View.VISIBLE);
            }
            llBank.setVisibility(View.GONE);


            mlvList.setAdapter(new BillDetialAdapter(billbean.getMvpOrderDet()));
        }*/

    }

    private static float getTotalAll(List<BillDetBean> mvpOrderDet) {
        float numAll = 0;
        for (int i = 0; i <mvpOrderDet.size() ; i++) {
            BillDetBean model = mvpOrderDet.get(i);
            numAll += model.getQty();
        }
      return numAll;
    }

    @Override
    public int layoutId() {
        return R.layout.a_billdetial_layout;
    }

    @OnClick(R.id.btnCancel)
    public void onBtnCancel() {//撤销订单
        doAction(false);
    }

    @OnClick(R.id.btnPay)
    public void onBtnPay() {//支付
       /* Intent intent = new Intent();
        intent.setClass(BillDetialActivity.this, BalanceActivity.class);
        intent.putExtra("billean",billbean);
        startActivity(intent);
        finish();*/

    }

    @OnClick(R.id.btnDetel)
    public void onDetel() {//删除订单
        doAction(true);
    }


    public void doAction(boolean isDetel) {
        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        List<RequestWrap<Billbean>> requestWraps = new ArrayList<>();
        RequestWrap<Billbean> requestWrap = new RequestWrap<>();
        requestWrap.name = "dsMain";
        List<Billbean> list = new ArrayList<>();
        list.add(billbean);
        requestWrap.data = list;
        requestWraps.add(requestWrap);
        simpleRequestWrap.dataset = requestWraps;
        Api.api().deleteOrCancelBill(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener() {
            @Override
            public void onStart() {
                super.onStart();
                progressHudUtil.showProgressHud("正在提交", false);
            }

            @Override
            public void onSuccess(Object o) {
                progressHudUtil.dismissProgressHud();
                ToastUtil.makeToast(((BaseEntity) o).msg);
                finishAC();
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
                progressHudUtil.dismissProgressHud();
                ToastUtil.makeToast(error.errorMsg);
            }
        }, isDetel);
    }

    public void finishAC() {
        String where = getIntent().getStringExtra("WHERE");
        if (where != null && where.equals("PUSH")) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            this.setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String where = getIntent().getStringExtra("WHERE");
        if (where != null && where.equals("PUSH")) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else if (where != null && where.equals("PAYSUCCESS")){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        String where_ = getIntent().getStringExtra("WHERE");
        if (where_ != null && where_.equals("PUSH")) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }else if (where_ != null && where_.equals("PAYSUCCESS")){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class BillDetialAdapter extends BaseAdapter {
        private List<BillDetBean> mvpOrderDet;

        public BillDetialAdapter(List<BillDetBean> mvpOrderDet) {
            this.mvpOrderDet = mvpOrderDet;
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
            Holder holder;
            if (null == convertView) {
                holder = new Holder();
               /* convertView = LayoutInflater.from(BillDetialActivity.this).inflate(R.layout.w_billdetial_list_item, null); //mContext指的是调用的Activtty
                holder.tvFoodName = (TextView) convertView.findViewById(R.id.tvFoodName);
                holder.tvCreatePlace = (TextView) convertView.findViewById(R.id.tvCreatePlace);
                holder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
                holder.ivFoodImage = (ImageView) convertView.findViewById(R.id.ivFoodImage);*/
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            BillDetBean billDetBean = mvpOrderDet.get(position);
//            Glide.with(BillDetialActivity.this).load(IMG_URL + "?id=" + mvpOrderDet.get(position).getItemId()).placeholder(R.drawable.p_default_list_pic).crossFade().into(holder.ivFoodImage);
            holder.tvFoodName.setText(billDetBean.getItemName());
            holder.tvNum.setText(billDetBean.getQty() + "x" + billDetBean.getPrice());
            holder.tvPrice.setText("￥" + billDetBean.getAmt());
            return convertView;
        }

        class Holder {
            public ImageView ivFoodImage;
            public TextView tvFoodName, tvCreatePlace, tvNum, tvPrice;

        }
    }
}
