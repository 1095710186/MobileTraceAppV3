package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;
/**
 * 设备模块抽象类，LDAPISample中各模块的父类
 * 
 * @author xiexc
 */
public abstract class AbstractModule {
	protected static String TAG = new Throwable().getStackTrace()[0].getClassName();
	protected Context mcontext;
	
	public AbstractModule(Context context) {
		this.mcontext = context;
	}

	@SuppressLint("ShowToast")
	protected void showNormalMessage(String msg) {
		Toast.makeText(mcontext, msg, Toast.LENGTH_LONG).show();
	}
	
	@SuppressLint("ShowToast")
	protected void showErrorMessage(String msg) {
		Toast.makeText(mcontext, msg, Toast.LENGTH_LONG).show();
	}
	
	public Context getContext() {
		return mcontext;
	}
	
	protected abstract void onDeviceServiceCrash();
	protected abstract void showMessage(String msg);
}
