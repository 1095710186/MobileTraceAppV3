package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.text.DecimalFormat;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.ui.JhDetaiActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.JhDetail_ActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TXJHDActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TXJHDActivity_ViewBinding;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TXJHD_ActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TXJHD_Activity_ViewBinding;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TXJHD_Y_ActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.ui.Update_JHD_ActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.util.DateUtils;
import cn.com.bigknow.trade.pos.Immediate.base.util.RegularUtil;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ChePai;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;

/**
 * Created by hujie on 16/10/15.
 */

public class JHAdapter extends BaseQuickAdapter<FoodEntryInfo> {
    public JHAdapter() {
        super(R.layout.jh_list_item_layout, null);

    }

    private static final int[] foodTVBGs = {R.drawable.tv1, R.drawable.tv2, R.drawable.tv3, R.drawable.tv4};

    @Override
    protected void convert(BaseViewHolder holder, FoodEntryInfo o) {


        String state = o.getState();

        LinearLayout topBgLL = holder.getView(R.id.topBgLL);

        View wtgView = holder.getView(R.id.wtgView);
        View ytgView = holder.getView(R.id.ytgView);
        TextView ytgTV = holder.getView(R.id.ytgTV);
        TextView timeTV = holder.getView(R.id.timeTV);
        TextView time2TV = holder.getView(R.id.time2TV);
        TextView cpTV = holder.getView(R.id.cpTV);
        TextView kindsTV = holder.getView(R.id.kindsTV);
        TextView itemQtyTV = holder.getView(R.id.itemQtyTV);
        TextView bhTV = holder.getView(R.id.bhTV);
        View bhLL = holder.getView(R.id.bhLL);


        if (state.equals("R")) {
            topBgLL.setBackgroundResource(R.drawable.bg3);
            wtgView.setVisibility(View.VISIBLE);
            ytgView.setVisibility(View.GONE);
            bhLL.setVisibility(View.VISIBLE);
            bhTV.setText("驳回原因:" + o.getOpinionString());
        } else {
            topBgLL.setBackgroundResource(R.drawable.bg33);
            wtgView.setVisibility(View.GONE);
            ytgView.setVisibility(View.VISIBLE);
            if (state.equals("Y")) {
                ytgTV.setText("已通过");
            } else if (state.equals("S")) {
                ytgTV.setText("审核中");
            } else if (state.equals("N")) {
                ytgTV.setText("新建");
            }
            bhLL.setVisibility(View.GONE);
        }
        timeTV.setText(DateUtils.parseDate(o.getBillDate()));
        time2TV.setText(o.getCreateTime());
        cpTV.setText(o.getSearchNo());
        kindsTV.setText(o.getKinds() + "种");

        DecimalFormat df = new DecimalFormat("###.00");
        if (o.getItemQty() != null) {
            itemQtyTV.setText(df.format(Float.parseFloat(o.getItemQty())) + "斤");
        } else

        {
            itemQtyTV.setText("");
        }

        FlexboxLayout flexboxLayout = holder.getView(R.id.flexbox);
        flexboxLayout.removeAllViews();

        if (o.getFoodList() != null) {
            flexboxLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < o.getFoodList().size(); i++) {
                View tvView = LayoutInflater.from(mContext).inflate(R.layout.jh_food_tv, null);
                TextView tv = (TextView) tvView.findViewById(R.id.tv);
                tv.setText(o.getFoodList().get(i));
                tv.setBackgroundResource(foodTVBGs[i % 4]);
                flexboxLayout.addView(tvView);
            }
        } else {
            flexboxLayout.setVisibility(View.GONE);
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TXJHDWrapper wrapper = new TXJHDWrapper();
                wrapper.setFoodInfos(o.getDetList());

                wrapper.setTotal(o.getTotalQty());

                String serachNo = o.getSearchNo();
                if (serachNo != null) {
                    serachNo = serachNo.replaceAll("\\s*", "");
                    if (RegularUtil.isChePai(serachNo)) {
                        ChePai chePai = new ChePai();
                        chePai.setProvince(serachNo.substring(0, 1));
                        chePai.setCity(serachNo.substring(1, 2));
                        chePai.setNumber(serachNo.substring(2));
                        wrapper.setChePai(chePai);
                    }
                }

                wrapper.setId(o.getId());
                wrapper.setUpdateDate(o.getUpdateDate());
                wrapper.setFileId(o.getFileId());
                wrapper.setState(o.getState());


//                long days=5;
//                o.getCreateTime();
//                DateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date curDate = new Date(System.currentTimeMillis());//获取系统当前时间
//                String str_data = time.format(curDate);
//                try {
//                    Date d1 = time.parse(o.getCreateTime());//入场通过时的时间
//                    Date d2 = time.parse(str_data);
//                    long diff = d1.getTime() - d2.getTime();
//                    days = diff / (1000 * 60 * 60);
//                    ToastUtil.makeToast("操作成功"+days);
//                } catch (Exception e) {
//                }
//                if(days>=0&&days<=4){//判断时间是否在4小时内
//                    Intent intent=new Intent(mContext,Update_JHD_Activity.class);
//                    intent.putExtra("wrapper",wrapper);
//                    mContext.startActivity(intent);
//           //       mContext.startActivity(Update_JHD_ActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));
//                }else{

//                    mContext.startActivity(JhDetaiActivityAutoBundle.createIntentBuilder(wrapper).build(mContext));
//                mContext.startActivity(JhDetail_ActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));

//                }

                if (state.equals("Y")) {
                    mContext.startActivity(Update_JHD_ActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));

                }else  if (state.equals("N") ||state.equals("S")) {
                    mContext.startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));

                } else if (state.equals("R")){
                    mContext.startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));

                }


            }
        });
        ytgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TXJHDWrapper wrapper = new TXJHDWrapper();
                wrapper.setFoodInfos(o.getDetList());

                wrapper.setTotal(o.getTotalQty());
                String serachNo = o.getSearchNo();

                if (serachNo != null) {
                    serachNo = serachNo.replaceAll("\\s*", "");

                    if (RegularUtil.isChePai(serachNo)) {
                        ChePai chePai = new ChePai();
                        chePai.setProvince(serachNo.substring(0, 1));
                        chePai.setCity(serachNo.substring(1, 2));
                        chePai.setNumber(serachNo.substring(2));
                        wrapper.setChePai(chePai);
                    }
                }


                wrapper.setId(o.getId());
                wrapper.setUpdateDate(o.getUpdateDate());
                wrapper.setFileId(o.getFileId());
                wrapper.setState(o.getState());
                if (state.equals("N") ||state.equals("S")) {
                    mContext.startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));
//                    mContext.startActivity(TXJHD_ActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));

                }else  if (state.equals("Y")) {

//                    mContext.startActivity(TXJHD_Y_ActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));
                    mContext.startActivity(Update_JHD_ActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));
                }

            }
        });
        wtgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TXJHDWrapper wrapper = new TXJHDWrapper();
                wrapper.setFoodInfos(o.getDetList());

                wrapper.setTotal(o.getTotalQty());

                String serachNo = o.getSearchNo();
                if (serachNo != null) {
                    serachNo = serachNo.replaceAll("\\s*", "");
                    if (RegularUtil.isChePai(serachNo)) {
                        ChePai chePai = new ChePai();
                        chePai.setProvince(serachNo.substring(0, 1));
                        chePai.setCity(serachNo.substring(1, 2));
                        chePai.setNumber(serachNo.substring(2));
                        wrapper.setChePai(chePai);
                    }
                }


                wrapper.setId(o.getId());
                wrapper.setUpdateDate(o.getUpdateDate());
                wrapper.setFileId(o.getFileId());
                wrapper.setState(o.getState());
//                mContext.startActivity(TXJHD_ActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));
                mContext.startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext));


            }
        });


    }
}
