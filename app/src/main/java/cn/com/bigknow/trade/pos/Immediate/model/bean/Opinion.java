package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by hujie on 16/10/26.
 *
 * 进货管理 驳回实体
 */

public class Opinion implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
