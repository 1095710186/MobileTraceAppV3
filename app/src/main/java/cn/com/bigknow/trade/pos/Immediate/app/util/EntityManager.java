package cn.com.bigknow.trade.pos.Immediate.app.util;

import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.app.di.DBManager;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BillbeanDao;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ChePai;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ChePaiDao;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodSortCount;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodSortCountDao;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PriceSort;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PriceSortDao;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFoodDao;

/**
 * Created by hujie on 16/10/26.
 */

public class EntityManager {

    public static void saveChePai(ChePai chePai) {
        ChePaiDao chePaiDao = (ChePaiDao) DBManager.getInstance(BootstrapApp.get()).getDao(ChePai.class);
        chePaiDao.save(chePai);
    }

    public static List<ChePai> getChePais() {
        ChePaiDao chePaiDao = (ChePaiDao) DBManager.getInstance(BootstrapApp.get()).getDao(ChePai.class);
        return chePaiDao.queryBuilder().limit(3).orderDesc(ChePaiDao.Properties.Id).list();
    }

    public static List<ChePai> getChePais(String value) {
        ChePaiDao chePaiDao = (ChePaiDao) DBManager.getInstance(BootstrapApp.get()).getDao(ChePai.class);
//        return chePaiDao.queryBuilder().where(ChePaiDao.Properties.Number.like("%" + value + "%")).list();//where(ChePaiDao.Properties.Number.like("%" + value + "%")).list();//limit(3).orderDesc(ChePaiDao.Properties.Id).list();
        return chePaiDao.queryBuilder().limit(3).where(ChePaiDao.Properties.Number.like("%" + value + "%")).list();

    }


    //保存或者更新
    public static void saveShopCarFood(ShopCarFood shopCarFood) {
        ShopCarFoodDao dao = (ShopCarFoodDao) DBManager.getInstance(BootstrapApp.get()).getDao(ShopCarFood.class);
        dao.insertOrReplace(shopCarFood);
    }

    public static long shopCarFoodCount() {
        ShopCarFoodDao dao = (ShopCarFoodDao) DBManager.getInstance(BootstrapApp.get()).getDao(ShopCarFood.class);
        return dao.count();
    }

    public static List<ShopCarFood> findShopCarFoodsByType(int type) {
        ShopCarFoodDao dao = (ShopCarFoodDao) DBManager.getInstance(BootstrapApp.get()).getDao(ShopCarFood.class);
        return dao.queryBuilder().orderDesc(ShopCarFoodDao.Properties.ShopCarId).where(ShopCarFoodDao.Properties.ShopCarType.eq(type)).list();
    }

    public static void deleteShopCarFoodsByType(int type) {
        ShopCarFoodDao dao = (ShopCarFoodDao) DBManager.getInstance(BootstrapApp.get()).getDao(ShopCarFood.class);
        dao.deleteInTx(findShopCarFoodsByType(type));
    }


    public static void deleteShopCarFood(ShopCarFood shopCarFood) {
        ShopCarFoodDao dao = (ShopCarFoodDao) DBManager.getInstance(BootstrapApp.get()).getDao(ShopCarFood.class);
        dao.delete(shopCarFood);
    }

    public static int getShopCarFoodsCountByType(int type) {
//        List<ShopCarFood> list = findShopCarFoodsByType(type);
//        int count = 0;
//        if (list != null && list.size() > 0) {
//            List<String> ids = new ArrayList<>();
//            for (ShopCarFood food : list) {
//                if (!ids.contains(food.getItemId())) {
//                    ids.add(food.getItemId());
//                }
//            }
//            count = ids.size();
//        }

//
//        return count;
        return getShopCarFoodsCountNoCombinByType(type);
    }


    public static int getShopCarFoodsCountNoCombinByType(int type) {
        List<ShopCarFood> list = findShopCarFoodsByType(type);

        return list != null ? list.size() : 0;
    }

    public static void saveBill(Billbean billbean) {
        BillbeanDao dao = (BillbeanDao) DBManager.getInstance(BootstrapApp.get()).getDao(Billbean.class);
        dao.insertOrReplace(billbean);
    }

    public static void deleteBill(Billbean billbean) {
        BillbeanDao dao = (BillbeanDao) DBManager.getInstance(BootstrapApp.get()).getDao(Billbean.class);
        dao.delete(billbean);
    }

    public static Billbean getBillByType(int selectIndex) {
        BillbeanDao dao = (BillbeanDao) DBManager.getInstance(BootstrapApp.get()).getDao(Billbean.class);
        List<Billbean> billbeen = dao.queryBuilder().limit(1).where(BillbeanDao.Properties.SelectIndex.eq(selectIndex)).list();
        if (billbeen != null && billbeen.size() >= 1) {
            return billbeen.get(0);
        }
        return null;
    }


    public static void saveFoodSortCount(String itemId) {
        FoodSortCountDao dao = (FoodSortCountDao) DBManager.getInstance(BootstrapApp.get()).getDao(FoodSortCount.class);
        FoodSortCount sortCount = dao.load(itemId);
        if (sortCount != null) {
            sortCount.setCount(sortCount.getCount() + 1);
        } else {
            sortCount = new FoodSortCount();
            sortCount.setItemId(itemId);
            sortCount.setCount(1);
        }
        dao.insertOrReplace(sortCount);
    }

    public static FoodInfo setFoodSortCount(FoodInfo foodInfo) {
        FoodSortCountDao dao = (FoodSortCountDao) DBManager.getInstance(BootstrapApp.get()).getDao(FoodSortCount.class);
        FoodSortCount sortCount = dao.load(foodInfo.getId());
        if (sortCount != null) {
            foodInfo.setSortCount(sortCount.getCount());
        }
        return foodInfo;
    }


    public static void savePriceSort(String id, float price) {
        PriceSortDao dao = (PriceSortDao) DBManager.getInstance(BootstrapApp.get()).getDao(PriceSort.class);
        dao.insertOrReplace(new PriceSort(id, price));
    }


    public static List<PriceSort> getAllPriceSort() {
        PriceSortDao dao = (PriceSortDao) DBManager.getInstance(BootstrapApp.get()).getDao(PriceSort.class);
        return dao.loadAll();
    }


}
