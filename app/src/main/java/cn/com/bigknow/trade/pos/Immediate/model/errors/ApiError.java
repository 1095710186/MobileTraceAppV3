package cn.com.bigknow.trade.pos.Immediate.model.errors;

import java.io.Serializable;

/**
 * Created by hujie on 16/6/18.
 */
public class ApiError implements Serializable {


    public static final int ERROR_TYPE_NETWORK = 1;
    public static final int ERROR_TYPE_PARSE = 2;
    public static final int ERROR_TYPE_SERVER = 3;
    public static final int ERROR_TYPE_UNKNOW = 4;
    public static final int ERROR_TYPE_API = 5;


    //未知错误
    public static final int UNKNOWN_CODE = 607;
    //解析异常
    public static final int PARSE_ERROR_CODE = 608;
    //网络异常
    public static final int NETWORK_ERROR_CODE = 606;


    //出错提示
    public static final String SERVER_ERROR_MSG = "系统出故障";
    public static final String NETWORK_ERROR_MSG = "网络异常,请检查网络状态";
    public static final String PARSE_ERROR_MSG = "数据解析出错";
    public static final String UNKNOW_MSG = "未知错误";
    public static final String PERMISSION_ERROR_MSG = "无权访问";


    public int errorType;//错误类型
    public String errorCode;
    public String errorMsg;

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ApiError() {

    }


    public boolean isApiError() {
        return errorType == ERROR_TYPE_API;

    }

    @Override
    public String toString() {
        if (errorCode == null) {
            return errorMsg;
        }
        return errorMsg + "(" + errorCode + ")";
    }

    public ApiError(int errorType, String code, String msg) {
        this.errorType = errorType;
        this.errorCode = code;
        this.errorMsg = msg;
    }
}
