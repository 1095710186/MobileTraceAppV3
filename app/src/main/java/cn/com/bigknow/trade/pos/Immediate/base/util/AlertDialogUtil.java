package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;

/**
 * Created by hujie on 16/6/20.
 */
public class AlertDialogUtil {
    static AlertDialog dialog;

    public static AlertDialog show(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.argb(255, 0, 0, 0));
        return dialog;
    }

    public static void showNumKeyboardDialog(Activity activity, View view, View.OnClickListener onClickListener) {


        if (dialog != null && dialog.isShowing()) {
            return;
        }

        dialog = new AlertDialog.Builder(activity)
                .create();
        dialog.show();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        lp.height = (int) (display.getHeight() - DensityUtil.dip2px(activity, 60));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
        if (view.findViewById(R.id.cancleBtn) != null) {
            view.findViewById(R.id.cancleBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    onClickListener.onClick(view);
                }
            });
        }
        if (view.findViewById(R.id.sureBtn) != null) {

            view.findViewById(R.id.sureBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onClickListener.onClick(view);
                }
            });
        }


    }


    public static void showProvinceDialog(Activity activity, View view, View.OnClickListener onClickListener) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new AlertDialog.Builder(activity)
                .create();
        dialog.show();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        lp.height = (int) (display.getHeight() - DensityUtil.dip2px(activity, 20));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
        if (view.findViewById(R.id.cancleBtn) != null) {
            view.findViewById(R.id.cancleBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    onClickListener.onClick(view);
                }
            });
        }
        if (view.findViewById(R.id.sureBtn) != null) {

            view.findViewById(R.id.sureBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    onClickListener.onClick(view);
                }
            });
        }

    }

    public static void dissMiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }


}
