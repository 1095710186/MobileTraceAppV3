package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by hujie on 16/10/26.
 车牌
 */

@Entity
public class ChePai implements Serializable {

    static final long serialVersionUID = 42L;
    @Id(autoincrement = true)
    private Long id;
    private String province;//
    private String city;
    private String number;


    @Generated(hash = 904219646)
    public ChePai(Long id, String province, String city, String number) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.number = number;
    }

    @Generated(hash = 1523006488)
    public ChePai() {
    }


    public String getChePai() {
        if (getProvince()!=null ||getCity()!=null || getNumber()!=null) {
            return getProvince() + getCity() + getNumber();
        }else {
            return null;
        }
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
