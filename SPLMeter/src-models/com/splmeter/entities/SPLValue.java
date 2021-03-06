package com.splmeter.entities;

import com.splmeter.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.splmeter.dao.AsmtValueDao;
import com.splmeter.dao.SPLValueDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "SPLVALUE".
 */
public class SPLValue {

    private Long id;
    private java.util.Date time;
    private Integer earphone;
    private Float lng;
    private Float lat;
    private Float alt;
    private Float acc;
    private Float spl;
    private Float mainF;
    private Float maxLpa;
    private long asmt_id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient SPLValueDao myDao;

    private AsmtValue asmtValue;
    private Long asmtValue__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public SPLValue() {
    }

    public SPLValue(Long id) {
        this.id = id;
    }

    public SPLValue(Long id, java.util.Date time, Integer earphone, Float lng, Float lat, Float alt, Float acc, Float spl, Float mainF, Float maxLpa, long asmt_id) {
        this.id = id;
        this.time = time;
        this.earphone = earphone;
        this.lng = lng;
        this.lat = lat;
        this.alt = alt;
        this.acc = acc;
        this.spl = spl;
        this.mainF = mainF;
        this.maxLpa = maxLpa;
        this.asmt_id = asmt_id;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSPLValueDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getTime() {
        return time;
    }

    public void setTime(java.util.Date time) {
        this.time = time;
    }

    public Integer getEarphone() {
        return earphone;
    }

    public void setEarphone(Integer earphone) {
        this.earphone = earphone;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getAlt() {
        return alt;
    }

    public void setAlt(Float alt) {
        this.alt = alt;
    }

    public Float getAcc() {
        return acc;
    }

    public void setAcc(Float acc) {
        this.acc = acc;
    }

    public Float getSpl() {
        return spl;
    }

    public void setSpl(Float spl) {
        this.spl = spl;
    }

    public Float getMainF() {
        return mainF;
    }

    public void setMainF(Float mainF) {
        this.mainF = mainF;
    }

    public Float getMaxLpa() {
        return maxLpa;
    }

    public void setMaxLpa(Float maxLpa) {
        this.maxLpa = maxLpa;
    }

    public long getAsmt_id() {
        return asmt_id;
    }

    public void setAsmt_id(long asmt_id) {
        this.asmt_id = asmt_id;
    }

    /** To-one relationship, resolved on first access. */
    public AsmtValue getAsmtValue() {
        long __key = this.asmt_id;
        if (asmtValue__resolvedKey == null || !asmtValue__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AsmtValueDao targetDao = daoSession.getAsmtValueDao();
            AsmtValue asmtValueNew = targetDao.load(__key);
            synchronized (this) {
                asmtValue = asmtValueNew;
            	asmtValue__resolvedKey = __key;
            }
        }
        return asmtValue;
    }

    public void setAsmtValue(AsmtValue asmtValue) {
        if (asmtValue == null) {
            throw new DaoException("To-one property 'asmt_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.asmtValue = asmtValue;
            asmt_id = asmtValue.getId();
            asmtValue__resolvedKey = asmt_id;
        }
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
