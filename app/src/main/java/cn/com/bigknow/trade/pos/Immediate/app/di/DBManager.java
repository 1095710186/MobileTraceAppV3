package cn.com.bigknow.trade.pos.Immediate.app.di;

import android.content.Context;

import org.greenrobot.greendao.AbstractDao;

import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.bean.DaoMaster;
import cn.com.bigknow.trade.pos.Immediate.model.bean.DaoSession;

/**
 * Created by
 * 数据库相关
 */


public class DBManager {

    private static DBManager mInstance;
    private static DaoMaster.DevOpenHelper openHelper;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;


    public DBManager(Context context) {
        try{
            if (UserManager.getInstance().getUserInfo() != null) {
                openHelper = new DaoMaster.DevOpenHelper(context, ModelConfig.DATABASE_NAME + "-" + UserManager.getInstance().getUserInfo().getUserId() + ".db", null);
            } else {
                openHelper = new DaoMaster.DevOpenHelper(context, ModelConfig.DATABASE_NAME + ".db", null);
            }
        }catch (Exception e){
            LogUtil.d("Exception_DB",e.toString());
        }

    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                    // 数据库对象
                    daoSession = getDaoSession();
                }
            }
        }
        return mInstance;
    }

    /**
     * 取得DaoMaster
     *
     * @return
     */
    private static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @return
     */
    private static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    public static void destory() {
        mInstance = null;
        daoSession = null;
        daoMaster = null;
        openHelper = null;

    }

    public AbstractDao<?, ?> getDao(Class<?> entityClass) {
        AbstractDao<?, ?> abstractDao = daoSession.getDao(entityClass);

        return abstractDao;
    }
}
