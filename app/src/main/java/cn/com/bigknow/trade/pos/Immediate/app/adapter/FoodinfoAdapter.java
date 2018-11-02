package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;

/**
 * Created by laixiaoyang
 */

public class FoodinfoAdapter extends BaseQuickAdapter<FoodInfo> {
    Activity activity;
    public FoodinfoAdapter(Activity mActivity) {
        super(R.layout.foodinfo_item_layout, null);
        activity = mActivity;

    }

    boolean sc_boolean;
    public  boolean setxianshi(boolean sc_boolean) {
        this.sc_boolean=sc_boolean;
        return sc_boolean;
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

        int position = baseViewHolder.getLayoutPosition();

        FrameLayout ff = baseViewHolder.getView(R.id.pLayout);

        ff.getLayoutParams().height = height;
        ff.getLayoutParams().width = height;

        baseViewHolder.setText(R.id.foodinfo_item_tvNmwe,foodInfo.getAlias());
        UserManager.getInstance().loadFoodHeadImage(activity, (ImageView) baseViewHolder.getView(R.id.foodinfo_item_imvhead), foodInfo.getImgId());


        if(sc_boolean) {
            baseViewHolder.getView(R.id.foodinNmwe).setVisibility(View.VISIBLE);
        }else{
            baseViewHolder.getView(R.id.foodinNmwe).setVisibility(View.GONE);
        }
        baseViewHolder.addOnClickListener(R.id.foodinNmwe);

    }
}
