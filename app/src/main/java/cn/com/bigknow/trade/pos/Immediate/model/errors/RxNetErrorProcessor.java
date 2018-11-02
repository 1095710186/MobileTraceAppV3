/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.com.bigknow.trade.pos.Immediate.model.errors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.apkfuns.logutils.LogUtils;

import java.io.IOException;

import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * Created by Piasy{github.com/Piasy} on 15/7/24.
 *
 * used for Rx network error handling
 * Usage: Observable.subscribe(onNext, RxNetErrorProcessor.NetErrorProcessor)
 * run in the observeOn() thread
 * onErrorReturn run in subscribeOn thread (retrofit run in background thread, not good for
 * error handling)
 *
 * Note: if you handle onError for the net request, than you should call it manually:
 * RxNetErrorProcessor.NetErrorProcessor.call(throwable);
 * Otherwise this method won't be invoked
 */
public class RxNetErrorProcessor implements Action1<Throwable> {


    /**
     * Final path of network error processing, submit it to server at production build.
     */
    @Override
    public void call(final Throwable throwable) {
        LogUtils.e("RxNetErrorProcessor", throwable);
    }

    public boolean tryWithApiError(final Throwable throwable, final Action1<ApiError> handler) {
        ApiError apiError = null;
        LogUtils.e("requestException---->" + throwable);
        if (throwable instanceof HttpException) {
            final HttpException exception = (HttpException) throwable;
            final String errorBody;
            try {
                errorBody = exception.response().errorBody().string();
                BaseEntity<String> re = JSON.parseObject(errorBody, BaseEntity.class);
                apiError = new ApiError(ApiError.ERROR_TYPE_SERVER,re.errorCode, re.msg);

            } catch (Exception e) {
            }
            switch (exception.code()) {
                case ResponseCode.UNAUTHORIZED:
                case ResponseCode.FORBIDDEN:
                    if (apiError == null) {
                        apiError = new ApiError(ApiError.ERROR_TYPE_SERVER,exception.code()+"", ApiError.PERMISSION_ERROR_MSG);
                    }
                    //权限错误，需要实现
                    break;
                case ResponseCode.NOT_FOUND:
                case ResponseCode.REQUEST_TIMEOUT:
                case ResponseCode.GATEWAY_TIMEOUT:
                case ResponseCode.INTERNAL_SERVER_ERROR:
                case ResponseCode.BAD_GATEWAY:
                case ResponseCode.SERVICE_UNAVAILABLE:
                default:
                    if (apiError == null) {
                        apiError = new ApiError(ApiError.ERROR_TYPE_SERVER,exception.code()+"", ApiError.SERVER_ERROR_MSG);
                    }
                    break;
            }

        } else if (throwable instanceof ResultException) {    //服务器返回的错误
            apiError = ((ResultException) throwable).getApiError();

        } else if (throwable instanceof JSONException || throwable instanceof org.json.JSONException) {
            apiError = new ApiError(ApiError.ERROR_TYPE_PARSE, ApiError.PARSE_ERROR_CODE+"", ApiError.PARSE_ERROR_MSG);
        } else if (throwable instanceof IOException) {
            apiError = new ApiError(ApiError.ERROR_TYPE_NETWORK, ApiError.NETWORK_ERROR_CODE+"", ApiError.NETWORK_ERROR_MSG);
        }

        if (apiError != null) {
            LogUtils.e("requestException---->" + apiError);
            handler.call(apiError);
            return true;
        } else {
            apiError = new ApiError(ApiError.ERROR_TYPE_UNKNOW, ApiError.UNKNOWN_CODE+"", ApiError.UNKNOW_MSG);
            handler.call(apiError);
            call(throwable);
            return false;
        }

    }
}
