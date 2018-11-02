package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.app.ui.FoodEntryActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.FoodInfoEdtiorActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.KcActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MyMessageActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MyMsgTypeActivity;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

/**
 * PopWindow工具类
 * Created by Administrator on 2015/12/21.
 */
public class UtilsPopWindow {

    /**
     * 批次选择
     */
    public static void showMenuWindow(View view, final Activity mContext) {
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.choose_food_menu_layout,null);
        SimpleTooltip.Builder bu = new SimpleTooltip.Builder(mContext);
        bu.anchorView(view)
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(false)
                .showArrow(false)
                .modal(true)
                .transparentOverlay(false)
                .contentView(view2, 00);
//                        .build()
//                        .show();
        SimpleTooltip simpleTooltip = bu.build();
        simpleTooltip.show();
        ImageView imv = (ImageView) view2.findViewById(R.id.imvMessage);
        if (BootstrapApp.get().isMessage()){
            imv.setVisibility(View.VISIBLE);
        }else {
            imv.setVisibility(View.GONE);
        }
        view2.findViewById(R.id.menu_kcgl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, KcActivity.class));
                simpleTooltip.dismiss();
            }
        });
        view2.findViewById(R.id.menu_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BootstrapApp.get().setMessage(false);
                mContext.startActivity(new Intent(mContext, MyMsgTypeActivity.class));
                simpleTooltip.dismiss();
            }
        });
        view2.findViewById(R.id.menu_food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, FoodEntryActivity.class));
                simpleTooltip.dismiss();

            }
        });
    }
    //月份选择
    public static View showMonthPopupWindow(View view, final Activity mContext, List<PopupWindow> popupWindowList) {
       /* View view2 = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_m,null);
        RecyclerView cRecyclerView = (RecyclerView)view2.findViewById(R.id.dialog_choose_recycle);
        SimpleTooltip.Builder bu = new SimpleTooltip.Builder(mContext);
        bu.anchorView(view)
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(false)
                .showArrow(false)
                .modal(true)
                .transparentOverlay(false)
                .contentView(view2, 00);
//                        .build()
//                        .show();
        SimpleTooltip simpleTooltip = bu.build();
        simpleTooltip.show();


        simpleTooltips.add(simpleTooltip);
        return cRecyclerView;*/
        // showShare(mContext);
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_choose_m, null);
        RecyclerView cRecyclerView = (RecyclerView)contentView.findViewById(R.id.dialog_choose_recycle);
        final PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);


        // popupWindow自适应大小
        // popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的大小（宽度和高度）
        // 高度和宽度为屏幕的比例
//        popupWindow.setWidth(DensityUtil.px2dip(mContext,175));
//        popupWindow.setHeight(DensityUtil.px2dip(mContext,285));
        // popupWindow.setBackgroundDrawable(getResources().getDrawable( R.drawable.pop_bg));  查看原帖>>
//        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 产生背景变暗效果，设置透明度
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.9f;

        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mContext.getWindow().setAttributes(lp);//这样就设置好了变暗的效果

        //然后再设置退出popupwindow时取消暗背景
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
                lp.alpha = 1f;

                mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mContext.getWindow().setAttributes(lp);
            }
        });


        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                R.color.myTransparent));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
        popupWindowList.add(popupWindow);
        return cRecyclerView;
    }

    //二维码
    public static View showCodePopupWindow(View view, final Activity mContext, List<PopupWindow> popupWindowList) {

        // showShare(mContext);
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_code, null);
        ImageView imvCode = (ImageView) contentView.findViewById(R.id.image_code);
        PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);


        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        // popupWindow自适应大小
//         popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的大小（宽度和高度）
        // 高度和宽度为屏幕的比例
        popupWindow.setWidth((display.getWidth())*3/4);//DensityUtil.px2dip(mContext,175));
        popupWindow.setHeight((display.getWidth())*3/4);//DensityUtil.px2dip(mContext,285));
        // popupWindow.setBackgroundDrawable(getResources().getDrawable( R.drawable.pop_bg));  查看原帖>>
//        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 产生背景变暗效果，设置透明度
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.6f;

        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mContext.getWindow().setAttributes(lp);//这样就设置好了变暗的效果

        //然后再设置退出popupwindow时取消暗背景
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
                lp.alpha = 1f;

                mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mContext.getWindow().setAttributes(lp);
            }
        });


        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                R.color.myTransparent));

        // 设置好参数之后再show
//        popupWindow.showAsDropDown(view);
        popupWindow.showAtLocation(view,Gravity.CENTER,0,0);
        popupWindowList.add(popupWindow);
        return imvCode;
    }
}
