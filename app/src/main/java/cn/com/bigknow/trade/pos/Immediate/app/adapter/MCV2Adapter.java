package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MoneyView;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import me.grantland.widget.AutofitTextView;

/**
 * Created by hujie on 16/12/28.
 */

public class MCV2Adapter extends BaseQuickAdapter<ShopCarFood> {

    DecimalFormat df = new  DecimalFormat("#.##");
    public MCV2Adapter() {
        super(R.layout.mc_left_pay_list_item, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ShopCarFood shopCarFood) {
        MCV2ViewHolder holder = (MCV2ViewHolder) baseViewHolder;
        UserManager.getInstance().loadFoodHeadImage(mContext, holder.mHeadIV, shopCarFood.getImgId());
        holder.mNameTV.setText(shopCarFood.getItemName());
        BigDecimal numZ = new BigDecimal(shopCarFood.getAmt());
        holder.mXjTV.setMoneyText(df.format(shopCarFood.getAmt()) + "");

        holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_EDIT_MC_V2_FOOD_DELETE, shopCarFood);
            }
        });

        holder.mPriceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_EDIT_MC_V2_FOOD_PRICE, shopCarFood);
            }
        });
        holder.mZlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_EDIT_MC_V2_FOOD_ZL, shopCarFood);
            }
        });
        DecimalFormat df = new  DecimalFormat("#.##");
        BigDecimal numPr = new BigDecimal(shopCarFood.getPrice());
        String res = df.format(numPr);
        holder.mPriceTV.setMoneyText(df.format(numPr) + "");
        BigDecimal numZl = new BigDecimal(shopCarFood.getQty());
        holder.mZlTV.setMoneyText(df.format(numZl) + "");
    }

    @Override
    protected MCV2ViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return new MCV2ViewHolder(getItemView(layoutResId, parent));
    }

    class MCV2ViewHolder extends BaseViewHolder {

        @BindView(R.id.headIV)
        ImageView mHeadIV;
        @BindView(R.id.nameTV)
        AutofitTextView mNameTV;
        @BindView(R.id.zlTV)
        MoneyView mZlTV;
        @BindView(R.id.zlView)
        LinearLayout mZlView;
        @BindView(R.id.priceTV)
        MoneyView mPriceTV;
        @BindView(R.id.priceView)
        LinearLayout mPriceView;
        @BindView(R.id.xjTV)
        MoneyView mXjTV;
        @BindView(R.id.btDelete)
        LinearLayout mBtDelete;

        protected MCV2ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
