package cn.com.bigknow.trade.pos.Immediate.base.provider;

import retrofit2.Converter;

/**
 * Created by hujie on 16/6/18.
 */
public class RetrofitConfig {

    private String baseUrl;
    private Converter.Factory jsonFactory;


    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setJsonFactory(Converter.Factory factory) {
        this.jsonFactory = factory;
    }

    public Converter.Factory getJsonFactory() {
        return jsonFactory;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
