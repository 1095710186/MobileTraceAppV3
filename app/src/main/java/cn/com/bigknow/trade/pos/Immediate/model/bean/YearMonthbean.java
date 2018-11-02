package cn.com.bigknow.trade.pos.Immediate.model.bean;


import java.io.Serializable;

public class YearMonthbean implements Serializable {

  private int year;
    private int month;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
