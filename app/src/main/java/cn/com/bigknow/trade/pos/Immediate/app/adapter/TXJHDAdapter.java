package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by hujie on 16/10/20.
 */

public class TXJHDAdapter extends BaseQuickAdapter<FoodInfo> {
    public TXJHDAdapter() {
        super(R.layout.txjhd_item_layout, null);
    }
    boolean test=true;
    public void setTest(boolean stest){
        this.test=stest;
    }


    @Override
    protected void convert(BaseViewHolder holder, FoodInfo bean) {

        ImageView headIV = holder.getView(R.id.headIV);
        TextView nameTV = holder.getView(R.id.nameTV);
        TextView cdTV = holder.getView(R.id.cdTV);
        TextView gysTV = holder.getView(R.id.gysTV);
        TextView zlTV = holder.getView(R.id.zlTV);
        View delete = holder.getView(R.id.btDelete);
        View cdgysView = holder.getView(R.id.cdgysView);
        View zlView = holder.getView(R.id.zlView);


        if(test){
            delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }

        UserManager.getInstance().loadFoodHeadImage(mContext, headIV, bean.getImgId());
        nameTV.setText(bean.getAlias());//nameTV.setText(bean.getItemName());

        cdTV.setText(bean.getArea());
        gysTV.setText(bean.getVendor());

        DecimalFormat df = new  DecimalFormat("#.##");
        BigDecimal num = new BigDecimal(bean.getQty());
        String res = df.format(num);
        zlTV.setText(res + "斤");
        delete.setOnClickListener(v -> {
            EventMamager.getInstance().postObjEvent(SimpleEventType.ON_JH_FOOD_DELETE_CLICK,holder.getLayoutPosition());
//            remove(holder.getLayoutPosition());
        });
        zlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_EDIT_FOOD_ZL_CLICK, bean);
            }
        });

        cdgysView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_EDIT_FOOD_CDGYS_CLICK, bean);
            }
        });
        gysTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_EDIT_FOOD_CDGYS_CLICK, bean);
            }
        });


    }
}
