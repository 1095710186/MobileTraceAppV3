package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by hujie on 16/12/12.
 */

@Entity
public class PriceSort {



    @Id
    public String id;

    public float price;

    @Generated(hash = 208009436)
    public PriceSort(String id, float price) {
        this.id = id;
        this.price = price;
    }

    @Generated(hash = 164730331)
    public PriceSort() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
