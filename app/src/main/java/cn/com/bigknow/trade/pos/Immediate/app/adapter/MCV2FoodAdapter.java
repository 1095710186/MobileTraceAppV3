package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;

/**
 * Created by hujie on 16/10/11.
 */

public class MCV2FoodAdapter extends BaseQuickAdapter<FoodInfo> {
    public MCV2FoodAdapter() {
        super(R.layout.food_item_layout, null);
    }

    int height;


    private int jsItemWidth() {
        int sWidth = DensityUtil.getScreenW(mContext);
        int ww = (sWidth - DensityUtil.dip2px(mContext, 20)) / 3;
        return ww;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FoodInfo foodInfo) {

        if (height == 0) {
            height = jsItemWidth();
        }


        FrameLayout ff = baseViewHolder.getView(R.id.pLayout);

        ff.getLayoutParams().height = height;
        ff.getLayoutParams().width = height;


        baseViewHolder.setText(R.id.foodinfo_item_tvNmwe, foodInfo.getAlias()); //foodInfo.getItemName()
        UserManager.getInstance().loadFoodHeadImage(mContext,  baseViewHolder.getView(R.id.foodinfo_item_imvhead), foodInfo.getImgId());
        baseViewHolder.setText(R.id.zlTV, foodInfo.getInvQty() + "斤");


    }
}
