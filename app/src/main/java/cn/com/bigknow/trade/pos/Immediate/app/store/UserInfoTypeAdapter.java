package cn.com.bigknow.trade.pos.Immediate.app.store;

import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import net.orange_box.storebox.adapters.base.BaseStringTypeAdapter;

import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;

/**
 * Created by hujie on 16/6/19.
 */
public class UserInfoTypeAdapter extends BaseStringTypeAdapter<UserInfo> {
    @Nullable
    @Override
    public String adaptForPreferences(@Nullable UserInfo value) {

        if (value != null) {
            return JSON.toJSONString(value, SerializerFeature.WriteMapNullValue);
        }
        return null;
    }

    @Nullable
    @Override
    public UserInfo adaptFromPreferences(@Nullable String value) {
        if (value != null) {
            return JSON.parseObject(value, UserInfo.class);
        }
        return null;
    }
}
