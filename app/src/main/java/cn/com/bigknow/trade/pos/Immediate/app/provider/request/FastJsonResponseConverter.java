package cn.com.bigknow.trade.pos.Immediate.app.provider.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.apkfuns.logutils.LogUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ResultException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by hujie on 16/9/12.
 */
public class FastJsonResponseConverter<T> implements Converter<ResponseBody, T> {
    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private Type mType;

    private ParserConfig config;
    private int featureValues;
    private Feature[] features;

    FastJsonResponseConverter(Type type, ParserConfig config, int featureValues,
                              Feature... features) {
        mType = type;
        this.config = config;
        this.featureValues = featureValues;
        this.features = features;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {

        try {


            String result = value.string();

            LogUtils.json(result);

            JSONObject jo = JSONObject.parseObject(result);

            if (jo.containsKey("total") && jo.containsKey("rows")) {
                ListResultWrap list = JSON.parseObject(result, mType, config, featureValues, features != null ? features : EMPTY_SERIALIZER_FEATURES);
                return (T) list;
            } else {
                BaseEntity entity = JSON.parseObject(result, mType, config, featureValues, features != null ? features : EMPTY_SERIALIZER_FEATURES);

                if (entity.success == 1) {
                    return (T) entity;
                } else {
                    ApiError apiError = new ApiError();
                    apiError.errorMsg = entity.msg;
                    apiError.errorCode = entity.errorCode;
                    apiError.errorType = ApiError.ERROR_TYPE_API;
                    throw new ResultException(apiError);
                }
            }
        } finally {
            value.close();
        }
    }
}
