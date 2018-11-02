package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 16/6/23.
 */
public interface SimpleEventType {


    int ON_MC_LEFT_TAB_CHANGE = 1; //买菜左边tab切换
    int ON_MENUC_SELECT_MC = 2; //当菜单选中买菜
    int ON_CHANGE_CHEPAI_BACK = 3; //当更改车牌返回
    int ON_JH_BACK = 4;//选择菜品完成返回
    int ON_JH_ADD_BACK = 41;//选择菜品完成返回
    int ON_JH_ADD_ = 42;//
    int ON_CLEANJ_KC_BACK = 35; //清库存后
    int ON_JH_ADD_CD = 423;//

    int ON_JH_CDGYS_EDIT_BACK = 7;//选择菜品完成返回
    int ON_EDIT_FOOD_ZL_CLICK = 5;//当点击修改重量
    int ON_EDIT_FOOD_CDGYS_CLICK = 6;//当点击修改产地供应商
    int ON_JH_FOOD_DELETE_CLICK = 8;//当点击修改产地供应商
    int ON_JH_REFRESH = 9;//当点击修改产地供应商
    int ON_MENU_DELETE_CLICK = 10;//当点击主页上面的删除
    int ON_DELETE_MC_FOOD =11 ;
    int ON_EDIT_CONT_MC_FOOD =12 ;
    int ON_CHOOSE_CYCP_BACK=13 ;
    int ON_CHOOSE_CUSTOMER_BACK=14 ;

    int ON_FOODNAME_CHANGE = 101;// 改名后返回

    int ON_NEW_UPDATE = 111;//有新的版本更新
    int ON_KEY_HOME= 112;//按HOME键回到主页


    int ON_CHOOSE_FOOD_V2_BACK = 300;

    int ON_EDIT_MC_V2_FOOD_ZL = 500;
    int ON_EDIT_MC_V2_FOOD_PRICE = 600;
    int ON_EDIT_MC_V2_FOOD_DELETE = 700;

    int ON_POST_WXPAY = 1245;//推送信息提交

    int ON_CLEAR_WXPAY = 1264;//删除本地订单
    int ON_FOOD_BACK = 1284;//首页卖菜中的返回执行
}
