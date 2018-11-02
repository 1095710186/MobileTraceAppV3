package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;

/**
 * Created by zw on 2016/11/16.
 */

public class NetworkReceiver extends BroadcastReceiver {
   private Pay_Data bendi=new Pay_Data();
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();

        if (!mobileInfo.isConnected() && !wifiInfo.isConnected()) {
            ToastUtil.makeToast("当前网络不可用，请检查网络");

        }else {

            if(bendi.getpay_data(context)!=null){//判断本地是否有未提交的数据
                bendi.post_pay(bendi.getpay_data(context),context);//得到本地数据进行提交。断网数据
            }

            if(bendi.getpay_xinxi(context)!=null){//判断本地是否有未提交的数据
                bendi.post_pay_xinxi(bendi.getpay_xinxi(context),context);//得到本地数据进行提交。断电数据
            }

        }

    }


}
