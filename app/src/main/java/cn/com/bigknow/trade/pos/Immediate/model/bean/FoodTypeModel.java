package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * Created by hujie on 2016/8/19..
 */

@Entity
public class FoodTypeModel {
    @Unique
    public String id;
    public String parentName;
    @Unique
    @NotNull
    public String code;
    public String innerCode;
    public String treeLevel;
    public String name;
    public String enabledFlag;
    public String endFlag;
    public String remark;
    public String parentId;

    @ToMany(joinProperties = {
            @JoinProperty(name = "code", referencedName = "code")
    })
    public List<FoodTypeDetailModel> foodTypeDetailModels;
    /** Used for active entity operations. */
    @Generated(hash = 1933588193)
    private transient FoodTypeModelDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    @Generated(hash = 1008427532)
    public FoodTypeModel(String id, String parentName, @NotNull String code,
                         String innerCode, String treeLevel, String name, String enabledFlag,
                         String endFlag, String remark, String parentId) {
        this.id = id;
        this.parentName = parentName;
        this.code = code;
        this.innerCode = innerCode;
        this.treeLevel = treeLevel;
        this.name = name;
        this.enabledFlag = enabledFlag;
        this.endFlag = endFlag;
        this.remark = remark;
        this.parentId = parentId;
    }

    @Generated(hash = 1937208598)
    public FoodTypeModel() {
    }

   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }

    public String getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(String treeLevel) {
        this.treeLevel = treeLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 645815425)
    public synchronized void resetFoodTypeDetailModels() {
        foodTypeDetailModels = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 255544628)
    public List<FoodTypeDetailModel> getFoodTypeDetailModels() {
        if (foodTypeDetailModels == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FoodTypeDetailModelDao targetDao = daoSession.getFoodTypeDetailModelDao();
            List<FoodTypeDetailModel> foodTypeDetailModelsNew = targetDao._queryFoodTypeModel_FoodTypeDetailModels(code);
            synchronized (this) {
                if(foodTypeDetailModels == null) {
                    foodTypeDetailModels = foodTypeDetailModelsNew;
                }
            }
        }
        return foodTypeDetailModels;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 929324215)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFoodTypeModelDao() : null;
    }


   
}
