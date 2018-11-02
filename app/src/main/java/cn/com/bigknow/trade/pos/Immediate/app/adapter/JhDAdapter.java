package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by lixy
 */

public class JhDAdapter extends BaseQuickAdapter<FoodInfo> {
    DecimalFormat df=new   java.text.DecimalFormat("#.##");
    public JhDAdapter() {
        super(R.layout.txjhd_item_layout, null);
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

        UserManager.getInstance().loadFoodHeadImage(mContext, headIV, bean.getImgId());
        nameTV.setText(bean.getAlias()); //bean.getItemName()

        cdTV.setText(bean.getArea());
        gysTV.setText(bean.getVendor());

        zlTV.setText(df.format(bean.getQty()) + "斤");
        delete.setVisibility(View.GONE);

    }
}
