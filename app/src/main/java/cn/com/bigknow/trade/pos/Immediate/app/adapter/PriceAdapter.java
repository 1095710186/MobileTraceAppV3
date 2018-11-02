package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by hujie on 16/10/13.
 */

public class PriceAdapter extends BaseQuickAdapter<ShopCarFood> {
    public PriceAdapter() {
        super(R.layout.price_list_item_layout, null);
    }


    @Override
    protected void convert(BaseViewHolder holder, ShopCarFood mcFood) {
        float zxc=124;
        ImageView headIV = holder.getView(R.id.headIV);
        TextView nameTV = holder.getView(R.id.nameTV);
        TextView zldTV = holder.getView(R.id.zldTV);
        TextView zlxTV = holder.getView(R.id.zlxTV);
        TextView jgdTV = holder.getView(R.id.jgdTV);
        TextView jgxTV = holder.getView(R.id.jgxTV);
        TextView totaljgdTV = holder.getView(R.id.totaljgdTV);
        TextView totaljgxTV = holder.getView(R.id.totaljgxTV);

        TextView price_listitem_jianjia = holder.getView(R.id.price_listitem_jianjia);

        TextView price_listitem_jiajia = holder.getView(R.id.price_listitem_jiajia);

        holder.addOnClickListener(R.id.price_listitem_jianjia);
        holder.addOnClickListener(R.id.price_listitem_jiajia);

        UserManager.getInstance().loadFoodHeadImage(mContext, headIV, mcFood.getImgId());

        nameTV.setText(mcFood.getItemName());
        String[] ss = (mcFood.getQty() + "").split("\\.");
        if (ss.length != 0) {
            if (ss.length == 1) {
                zldTV.setText(ss[0]);
            } else {
                zldTV.setText(ss[0]);
                zlxTV.setText("." + ss[1]);
            }
        }

        if (mcFood.getPrice() > 0) {
            String[] jg = (mcFood.getPrice() + "").split("\\.");
            if (jg.length != 0) {
                if (jg.length == 1) {
                    jgdTV.setText(jg[0]);
                } else {
                    jgdTV.setText(jg[0]);
                    jgxTV.setText("." + jg[1]);
                }
            }
        } else {
            jgdTV.setText("");
            jgxTV.setText("");
        }

        if (mcFood.getAmt() > 0) {
            DecimalFormat df = new DecimalFormat("#.##");

            String[] total = (String.valueOf(df.format(mcFood.getAmt())) + "").split("\\.");
            if (total.length != 0) {
                if (total.length == 1) {
                    totaljgdTV.setText(total[0]);
                } else {
                    totaljgdTV.setText(total[0]);
                    totaljgxTV.setText("." + total[1]);
                }
            }
        } else {
            totaljgdTV.setText("");
            totaljgxTV.setText("");
        }


    }
}
