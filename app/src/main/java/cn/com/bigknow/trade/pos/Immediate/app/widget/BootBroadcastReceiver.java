package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.com.bigknow.trade.pos.Immediate.app.ALauncher;

/**
 * Created by zw on 2017/2/6.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(ACTION)){
            Intent aintent=new Intent(context,ALauncher.class);
            aintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(aintent);
        }
    }
}
