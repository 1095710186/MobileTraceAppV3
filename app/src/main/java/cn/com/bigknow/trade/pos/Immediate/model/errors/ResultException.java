package cn.com.bigknow.trade.pos.Immediate.model.errors;

/**
 * Created by hujie on 16/5/31.
 */
public class ResultException extends RuntimeException {

    private ApiError apiError;

    public ApiError getApiError() {
        return apiError;
    }

    public ResultException(ApiError error) {
        this.apiError = error;
    }

}
