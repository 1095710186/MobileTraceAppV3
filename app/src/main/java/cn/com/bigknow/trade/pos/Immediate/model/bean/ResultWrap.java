package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by laixiaoyang
 */
public class ResultWrap<T> implements Serializable {

    public String msg ;
    public  T data;
    public int success;
    public String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
