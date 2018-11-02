package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;

/**
 * Created by hujie on 16/10/11.
 */

public class JHFoodAdapter_ extends BaseQuickAdapter<FoodInfo> {
    public JHFoodAdapter_() {
        super(R.layout.food_item_layout, null);

    }
    List<FoodInfo> Infos = new ArrayList<>();
    FoodInfo info;
    public  void setFood(List<FoodInfo> foodInfos) {
        this.Infos=foodInfos;
        notifyDataSetChanged();
    }
    public  void setFood_(FoodInfo food) {
        this.info=food;
        notifyDataSetChanged();
    }
    int height;


    private int jsItemWidth() {
        int sWidth = DensityUtil.getScreenW(mContext);
        int ww = (sWidth - DensityUtil.dip2px(mContext, 20)) / 3;
        return ww;
    }


   /* @Override
    public int getItemCount() {
        return getData().size()+1;
    }*/



    public int getCount() {
        return getData().size();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FoodInfo foodInfo) {
//        int number = getCount();
        if (height == 0) {
            height = jsItemWidth();
        }
        int position = baseViewHolder.getAdapterPosition();

        FrameLayout ff = baseViewHolder.getView(R.id.pLayout);

        ff.getLayoutParams().height = height;
        ff.getLayoutParams().width = height;

        ImageView imvHead = baseViewHolder.getView(R.id.foodinfo_item_imvhead);

        LinearLayout layoutBottom = baseViewHolder.getView(R.id.foodinfo_item_bottomLayout);
        ImageView imvArea = baseViewHolder.getView(R.id.foodinfo_item_imvArea);
        TextView tvArea = baseViewHolder.getView(R.id.zlTV);
        ImageView imvChced = baseViewHolder.getView(R.id.foodinfo_item_imvChed);
        TextView tvName = baseViewHolder.getView(R.id.foodinfo_item_tvNmwe);//, foodInfo.getAlias());
        if (Infos.size()>0) {
            for (FoodInfo foodI: Infos) {
                if (foodI.getItemId().equals(foodInfo.getId())) {
                    foodInfo = foodI;
                }
            }
        }else {

        }
        if (info!=null){
            if (info.getItemId().equals(foodInfo.getId())) {
                foodInfo = info;
            }
        }
        if (foodInfo.getArea()!=null){
            layoutBottom.setVisibility(View.VISIBLE);
            imvChced.setVisibility(View.VISIBLE);
            tvArea.setText(foodInfo.getArea());
        }else {
            layoutBottom.setVisibility(View.GONE);
            imvChced.setVisibility(View.GONE);
        }

        imvArea.setImageResource(R.drawable.icon_jh_cd02);
        tvName.setText( foodInfo.getAlias());// foodInfo.getItemName()

//        if (position ==number) {
//            layoutBottom.setVisibility(View.GONE);
//            imvChced.setVisibility(View.GONE);
//            imvHead.setImageResource(R.drawable.icon_xz03);
//            tvName.setVisibility(View.GONE);
//        }else {
            UserManager.getInstance().loadFoodHeadImage(mContext, imvHead , foodInfo.getImgId());
//        }







    }
}
