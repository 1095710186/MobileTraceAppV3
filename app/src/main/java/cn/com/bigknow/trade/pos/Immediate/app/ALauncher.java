package cn.com.bigknow.trade.pos.Immediate.app;

import android.content.Intent;

import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.ui.LoginActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MainActivity;
import cn.com.bigknow.trade.pos.Immediate.app.util.MainUiManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.jpush.android.api.JPushInterface;
import rx.Observable;
import rx.schedulers.Schedulers;

public class ALauncher extends BaseActivity {


    @Override
    public void init() {



        Observable.timer(3, TimeUnit.SECONDS).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribeOn(Schedulers.io()).subscribe(aLong -> {
            if (UserManager.getInstance().getToken() != null && !UserManager.getInstance().getToken().isEmpty()) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public boolean supportActionbar() {
        return false;
    }

    @Override
    public int layoutId() {
        return R.layout.a_launcher_layout;
    }


    @Override
    public void gc() {
        super.gc();
    }

    @Override
    public int getThemeId() {
        return R.style.StartTheme;
    }
}
