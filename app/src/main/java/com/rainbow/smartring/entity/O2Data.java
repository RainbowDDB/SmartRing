package com.rainbow.smartring.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.DaoException;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/9.
 */
@Entity
public class O2Data {

    @Id
    private Long id;

    // 外键，一对多，指定目标实体中指向该实体ID的“外键”属性的名称
    private Long uid;

    @NotNull
    private Integer o2;

    @NotNull
    private Date date;

    @ToOne(joinProperty = "uid")
    private UserProfile userProfile;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 104038617)
    private transient O2DataDao myDao;

    @Generated(hash = 502231841)
    public O2Data(Long id, Long uid, @NotNull Integer o2, @NotNull Date date) {
        this.id = id;
        this.uid = uid;
        this.o2 = o2;
        this.date = date;
    }

    @Generated(hash = 1886698772)
    public O2Data() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getO2() {
        return this.o2;
    }

    public void setO2(Integer o2) {
        this.o2 = o2;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Generated(hash = 1891443441)
    private transient Long userProfile__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1014449986)
    public UserProfile getUserProfile() {
        Long __key = this.uid;
        if (userProfile__resolvedKey == null
                || !userProfile__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserProfileDao targetDao = daoSession.getUserProfileDao();
            UserProfile userProfileNew = targetDao.load(__key);
            synchronized (this) {
                userProfile = userProfileNew;
                userProfile__resolvedKey = __key;
            }
        }
        return userProfile;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1640494744)
    public void setUserProfile(UserProfile userProfile) {
        synchronized (this) {
            this.userProfile = userProfile;
            uid = userProfile == null ? null : userProfile.getId();
            userProfile__resolvedKey = uid;
        }
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
    @Generated(hash = 1061313224)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getO2DataDao() : null;
    }
}
