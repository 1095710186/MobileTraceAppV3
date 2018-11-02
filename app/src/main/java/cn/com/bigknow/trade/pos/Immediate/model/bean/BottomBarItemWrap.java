package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 16/6/17.
 */
public class BottomBarItemWrap {

    public String title;
    public int activeIcon;
    public int inActiveIcon;


    public BottomBarItemWrap(String title, int activeIcon, int inActiveIcon) {
        this.title = title;
        this.activeIcon = activeIcon;
        this.inActiveIcon = inActiveIcon;
    }

    public BottomBarItemWrap(String title, int activeIcon) {
        this.title = title;
        this.activeIcon = activeIcon;
    }

    public String getTitle() {
        return title;
    }

    public int getActiveIcon() {
        return activeIcon;
    }

    public int getInActiveIcon() {
        return inActiveIcon;
    }
}
