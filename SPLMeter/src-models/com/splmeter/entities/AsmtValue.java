package com.splmeter.entities;

import java.util.List;
import com.splmeter.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.splmeter.dao.AsmtValueDao;
import com.splmeter.dao.SPLValueDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "ASMT_VALUE".
 */
public class AsmtValue {

    private Long id;
    private String imei;
    private String modeltype;
    private String mode;
    private String source;
    private String asmt;
    private Integer gender;
    private Integer age;
    private Float mF;
    private Float mLpa;
    private Float l10;
    private Float l50;
    private Float l90;
    private Float laeq;
    private Float calb;
    private Integer post;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient AsmtValueDao myDao;

    private List<SPLValue> splValueList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public AsmtValue() {
    }

    public AsmtValue(Long id) {
        this.id = id;
    }

    public AsmtValue(Long id, String imei, String modeltype, String mode, String source, String asmt, Integer gender, Integer age, Float mF, Float mLpa, Float l10, Float l50, Float l90, Float laeq, Float calb, Integer post) {
        this.id = id;
        this.imei = imei;
        this.modeltype = modeltype;
        this.mode = mode;
        this.source = source;
        this.asmt = asmt;
        this.gender = gender;
        this.age = age;
        this.mF = mF;
        this.mLpa = mLpa;
        this.l10 = l10;
        this.l50 = l50;
        this.l90 = l90;
        this.laeq = laeq;
        this.calb = calb;
        this.post = post;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAsmtValueDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getModeltype() {
        return modeltype;
    }

    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAsmt() {
        return asmt;
    }

    public void setAsmt(String asmt) {
        this.asmt = asmt;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getMF() {
        return mF;
    }

    public void setMF(Float mF) {
        this.mF = mF;
    }

    public Float getMLpa() {
        return mLpa;
    }

    public void setMLpa(Float mLpa) {
        this.mLpa = mLpa;
    }

    public Float getL10() {
        return l10;
    }

    public void setL10(Float l10) {
        this.l10 = l10;
    }

    public Float getL50() {
        return l50;
    }

    public void setL50(Float l50) {
        this.l50 = l50;
    }

    public Float getL90() {
        return l90;
    }

    public void setL90(Float l90) {
        this.l90 = l90;
    }

    public Float getLaeq() {
        return laeq;
    }

    public void setLaeq(Float laeq) {
        this.laeq = laeq;
    }

    public Float getCalb() {
        return calb;
    }

    public void setCalb(Float calb) {
        this.calb = calb;
    }

    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<SPLValue> getSplValueList() {
        if (splValueList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SPLValueDao targetDao = daoSession.getSPLValueDao();
            List<SPLValue> splValueListNew = targetDao._queryAsmtValue_SplValueList(id);
            synchronized (this) {
                if(splValueList == null) {
                    splValueList = splValueListNew;
                }
            }
        }
        return splValueList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetSplValueList() {
        splValueList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}