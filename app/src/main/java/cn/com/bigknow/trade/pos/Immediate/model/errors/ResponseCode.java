package cn.com.bigknow.trade.pos.Immediate.model.errors;

/**
 * Created by hujie on 16/6/1.
 */
public interface ResponseCode {


    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;


    //errorcode
    int UN_PERMISSION = 1002 ; //未授权
    int GOOGKER_INFO_INCOMPLETE = 1004;//公客资料不完整
}
