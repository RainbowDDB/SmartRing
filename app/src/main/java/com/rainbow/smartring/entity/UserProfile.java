package com.rainbow.smartring.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Unique;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/9.
 */
@Entity
public class UserProfile {

    @Id
    private Long id;

    @NotNull
    @Unique
    private String sid;

    @ToMany(referencedJoinProperty = "uid")
    @OrderBy("date ASC")
    private List<TData> tDataList;

    @ToMany(referencedJoinProperty = "uid")
    @OrderBy("date ASC")
    private List<HrData> hrDataList;

    @ToMany(referencedJoinProperty = "uid")
    @OrderBy("date ASC")
    private List<O2Data> o2DataList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 336345787)
    private transient UserProfileDao myDao;

    @Generated(hash = 1410574147)
    public UserProfile(Long id, @NotNull String sid) {
        this.id = id;
        this.sid = sid;
    }

    @Generated(hash = 968487393)
    public UserProfile() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1231553789)
    public List<TData> getTDataList() {
        if (tDataList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TDataDao targetDao = daoSession.getTDataDao();
            List<TData> tDataListNew = targetDao._queryUserProfile_TDataList(id);
            synchronized (this) {
                if (tDataList == null) {
                    tDataList = tDataListNew;
                }
            }
        }
        return tDataList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 572489066)
    public synchronized void resetTDataList() {
        tDataList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 422148292)
    public List<HrData> getHrDataList() {
        if (hrDataList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HrDataDao targetDao = daoSession.getHrDataDao();
            List<HrData> hrDataListNew = targetDao._queryUserProfile_HrDataList(id);
            synchronized (this) {
                if (hrDataList == null) {
                    hrDataList = hrDataListNew;
                }
            }
        }
        return hrDataList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 205954810)
    public synchronized void resetHrDataList() {
        hrDataList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1897360064)
    public List<O2Data> getO2DataList() {
        if (o2DataList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            O2DataDao targetDao = daoSession.getO2DataDao();
            List<O2Data> o2DataListNew = targetDao._queryUserProfile_O2DataList(id);
            synchronized (this) {
                if (o2DataList == null) {
                    o2DataList = o2DataListNew;
                }
            }
        }
        return o2DataList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 377006252)
    public synchronized void resetO2DataList() {
        o2DataList = null;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 431775681)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserProfileDao() : null;
    }
}