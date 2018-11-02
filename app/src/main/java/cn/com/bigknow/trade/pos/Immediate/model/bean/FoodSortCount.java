package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hujie on 16/11/16.
 */

@Entity
public class FoodSortCount  {

    @Id
    private String itemId;
    private int count;

    @Generated(hash = 695408116)
    public FoodSortCount(String itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    @Generated(hash = 742422331)
    public FoodSortCount() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
