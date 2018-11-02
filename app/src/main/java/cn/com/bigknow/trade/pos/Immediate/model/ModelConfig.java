package cn.com.bigknow.trade.pos.Immediate.model;

import cn.com.bigknow.trade.pos.Immediate.BuildConfig;

/**
 * Created by hujie on 16/6/18.
 */
public class ModelConfig {

    public static final String BASE_URL = BuildConfig.API_URL;  // "http://10.1.8.56:8080/";//BuildConfig.API_URL;

    public static final String BIGKNOW_FRAMEWORK_URL = BASE_URL+"mvp-framework/";

    public static final String MVP_URL = BASE_URL+"mvp-app/";

    public static final String LOGON = BIGKNOW_FRAMEWORK_URL + "SSOServer/logon";
    public static final String EXIT_LOGON = BIGKNOW_FRAMEWORK_URL + "SSOServer/logoff";

    public static final String UPLOAD = BIGKNOW_FRAMEWORK_URL + "SSOServer/setUserHead";

    public static final String PUSHID = BIGKNOW_FRAMEWORK_URL + "SSOServer/savePushId";

    public static final String GET_SMS_CODE = BIGKNOW_FRAMEWORK_URL + "SSOServer/verifyCode";

    public static final String RESET_PASSWORD = BIGKNOW_FRAMEWORK_URL + "SSOServer/pwdReset";

    public static final String CHANGE_PASSWORD = BIGKNOW_FRAMEWORK_URL + "SSOServer/pwdModify";

    public static final String CHANGE_PHONE = BIGKNOW_FRAMEWORK_URL + "SSOServer/mobileModify";

    public static final String CHECK_TOKEN = BIGKNOW_FRAMEWORK_URL + "SSOServer/tokencert";

    public static final String DATABASE_NAME = "trade_cache";

    public static final String MERCHANT_INFO = BIGKNOW_FRAMEWORK_URL + "SSOServer/queryMerchantInfo";

    public static final String USERINFO = BIGKNOW_FRAMEWORK_URL + "SSOServer/queryUserInfo";

    public static final String TRADEPWD= BIGKNOW_FRAMEWORK_URL + "SSOServer/tradePwd";



    /**
     * 扫码支付相关
     */
    public static final String BASE_URL_ = "http://121.42.53.93:80/";
    public static final String PAY_CODE_PAY = BASE_URL+"weixin/pay/applyQrCode";
    public static final String PAY_CODE_ = BASE_URL+"weixin/pay/micro";
    public static final String GET_FROM_ORDERNO = BASE_URL+"weixin/pay/query"; //根据订单号查询



}
