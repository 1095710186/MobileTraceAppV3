package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.adapter.BaseRecyclerAdapter;
import cn.com.bigknow.trade.pos.Immediate.base.interface_.OnImageListItemClick;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;

/**
 * Created by
 * 用于
 */

public class JhImageListAdapter extends
        BaseRecyclerAdapter<JhImageListAdapter.ImageListViewHolder,FoodInfo> {
    private OnImageListItemClick onListItemClick; //回掉接口

    public void setOnListItemClick(OnImageListItemClick onListItemClick) {
        this.onListItemClick = onListItemClick;
    }
    public void upDate(List<FoodInfo> data, String type) {
        mDataList = data;
        notifyDataSetChanged();
    }
    public JhImageListAdapter(Context context, List<FoodInfo> data) {
        super(context, data);
    }
    FoodInfo info;
    public  void setFood_(FoodInfo food) {
        this.info=food;
        notifyDataSetChanged();
    }


    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = null;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.food_item_layout, null);
        return new ImageListViewHolder(convertView);
    }
    @Override
    public int getItemCount() {
        return (mDataList.size() + 1);
    }
    public int getCount() {
        return mDataList.size();
    }
    int height;
    private int jsItemWidth() {
        int sWidth = DensityUtil.getScreenW(mContext);
        int ww = (sWidth - DensityUtil.dip2px(mContext, 20)) / 3;
        return ww;
    }
    @Override
    public void onBindViewHolder(final ImageListViewHolder holder, final int position) {
        int number = getCount();
        FoodInfo foodInfo = null;
        if (height == 0) {
            height = jsItemWidth();
        }
        holder.ff.getLayoutParams().height = height;
        holder.ff.getLayoutParams().width = height;


        if (position ==number) {
            holder.layoutBottom.setVisibility(View.GONE);
            holder.imvChced.setVisibility(View.GONE);
            holder.imvHead.setVisibility(View.GONE);
            holder.imgAdd.setVisibility(View.VISIBLE);
            holder.tvName.setVisibility(View.GONE);
        }else {
            foodInfo = getItemData(position);
            holder.imvHead.setVisibility(View.VISIBLE);
            holder.imgAdd.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.VISIBLE);
            if (info!=null){
                if (info.getItemId().equals(foodInfo.getId())) {
                    foodInfo = info;
                }
            }
            if (foodInfo.getArea()!=null){
                holder.layoutBottom.setVisibility(View.VISIBLE);
                holder.imvArea.setImageResource(R.drawable.icon_jh_cd02);
                holder.imvChced.setVisibility(View.VISIBLE);
                holder.tvArea.setText(foodInfo.getArea());
            }else {
                holder.layoutBottom.setVisibility(View.GONE);
                holder.imvChced.setVisibility(View.GONE);
            }

            holder.tvName.setText(foodInfo.getAlias());// foodInfo.getItemName()
            UserManager.getInstance().loadFoodHeadImage(mContext, holder.imvHead , foodInfo.getImgId());
        }




        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClick.OnClickItem(holder.imvHead,position,true);// 增加
            }
        });



    }

    public class ImageListViewHolder extends RecyclerView.ViewHolder{
        FrameLayout ff;
        ImageView imvHead;
        ImageView imgAdd;
        LinearLayout layoutBottom;
        ImageView imvArea;
        TextView tvArea;
        ImageView imvChced;
        TextView tvName;
        LinearLayout layout;
        public ImageListViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            ff = (FrameLayout) itemView.findViewById(R.id.pLayout);
            imvHead = (ImageView) itemView.findViewById(R.id.foodinfo_item_imvhead);
            imgAdd = (ImageView) itemView.findViewById(R.id.imgAdd);

            layoutBottom = (LinearLayout) itemView.findViewById(R.id.foodinfo_item_bottomLayout);
            imvArea = (ImageView) itemView.findViewById(R.id.foodinfo_item_imvArea);
            tvArea = (TextView) itemView.findViewById(R.id.zlTV);
            imvChced = (ImageView) itemView.findViewById(R.id.foodinfo_item_imvChed);
            tvName = (TextView) itemView.findViewById(R.id.foodinfo_item_tvNmwe);//, foodInfo.getAlias())
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
