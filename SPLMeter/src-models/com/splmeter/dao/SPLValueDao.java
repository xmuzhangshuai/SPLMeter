package com.splmeter.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.splmeter.entities.AsmtValue;

import com.splmeter.entities.SPLValue;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SPLVALUE".
*/
public class SPLValueDao extends AbstractDao<SPLValue, Long> {

    public static final String TABLENAME = "SPLVALUE";

    /**
     * Properties of entity SPLValue.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Time = new Property(1, java.util.Date.class, "time", false, "TIME");
        public final static Property Earphone = new Property(2, Integer.class, "earphone", false, "EARPHONE");
        public final static Property Lng = new Property(3, Float.class, "lng", false, "LNG");
        public final static Property Lat = new Property(4, Float.class, "lat", false, "LAT");
        public final static Property Alt = new Property(5, Float.class, "alt", false, "ALT");
        public final static Property Acc = new Property(6, Float.class, "acc", false, "ACC");
        public final static Property Spl = new Property(7, Float.class, "spl", false, "SPL");
        public final static Property MainF = new Property(8, Float.class, "mainF", false, "MAIN_F");
        public final static Property MaxLpa = new Property(9, Float.class, "maxLpa", false, "MAX_LPA");
        public final static Property Asmt_id = new Property(10, long.class, "asmt_id", false, "ASMT_ID");
    };

    private DaoSession daoSession;

    private Query<SPLValue> asmtValue_SplValueListQuery;

    public SPLValueDao(DaoConfig config) {
        super(config);
    }
    
    public SPLValueDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SPLVALUE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TIME\" INTEGER," + // 1: time
                "\"EARPHONE\" INTEGER," + // 2: earphone
                "\"LNG\" REAL," + // 3: lng
                "\"LAT\" REAL," + // 4: lat
                "\"ALT\" REAL," + // 5: alt
                "\"ACC\" REAL," + // 6: acc
                "\"SPL\" REAL," + // 7: spl
                "\"MAIN_F\" REAL," + // 8: mainF
                "\"MAX_LPA\" REAL," + // 9: maxLpa
                "\"ASMT_ID\" INTEGER NOT NULL );"); // 10: asmt_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SPLVALUE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SPLValue entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(2, time.getTime());
        }
 
        Integer earphone = entity.getEarphone();
        if (earphone != null) {
            stmt.bindLong(3, earphone);
        }
 
        Float lng = entity.getLng();
        if (lng != null) {
            stmt.bindDouble(4, lng);
        }
 
        Float lat = entity.getLat();
        if (lat != null) {
            stmt.bindDouble(5, lat);
        }
 
        Float alt = entity.getAlt();
        if (alt != null) {
            stmt.bindDouble(6, alt);
        }
 
        Float acc = entity.getAcc();
        if (acc != null) {
            stmt.bindDouble(7, acc);
        }
 
        Float spl = entity.getSpl();
        if (spl != null) {
            stmt.bindDouble(8, spl);
        }
 
        Float mainF = entity.getMainF();
        if (mainF != null) {
            stmt.bindDouble(9, mainF);
        }
 
        Float maxLpa = entity.getMaxLpa();
        if (maxLpa != null) {
            stmt.bindDouble(10, maxLpa);
        }
        stmt.bindLong(11, entity.getAsmt_id());
    }

    @Override
    protected void attachEntity(SPLValue entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SPLValue readEntity(Cursor cursor, int offset) {
        SPLValue entity = new SPLValue( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // time
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // earphone
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // lng
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // lat
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // alt
            cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6), // acc
            cursor.isNull(offset + 7) ? null : cursor.getFloat(offset + 7), // spl
            cursor.isNull(offset + 8) ? null : cursor.getFloat(offset + 8), // mainF
            cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9), // maxLpa
            cursor.getLong(offset + 10) // asmt_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SPLValue entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTime(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setEarphone(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setLng(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setLat(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setAlt(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setAcc(cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6));
        entity.setSpl(cursor.isNull(offset + 7) ? null : cursor.getFloat(offset + 7));
        entity.setMainF(cursor.isNull(offset + 8) ? null : cursor.getFloat(offset + 8));
        entity.setMaxLpa(cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9));
        entity.setAsmt_id(cursor.getLong(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SPLValue entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SPLValue entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "splValueList" to-many relationship of AsmtValue. */
    public List<SPLValue> _queryAsmtValue_SplValueList(long asmt_id) {
        synchronized (this) {
            if (asmtValue_SplValueListQuery == null) {
                QueryBuilder<SPLValue> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Asmt_id.eq(null));
                asmtValue_SplValueListQuery = queryBuilder.build();
            }
        }
        Query<SPLValue> query = asmtValue_SplValueListQuery.forCurrentThread();
        query.setParameter(0, asmt_id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getAsmtValueDao().getAllColumns());
            builder.append(" FROM SPLVALUE T");
            builder.append(" LEFT JOIN ASMT_VALUE T0 ON T.\"ASMT_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected SPLValue loadCurrentDeep(Cursor cursor, boolean lock) {
        SPLValue entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        AsmtValue asmtValue = loadCurrentOther(daoSession.getAsmtValueDao(), cursor, offset);
         if(asmtValue != null) {
            entity.setAsmtValue(asmtValue);
        }

        return entity;    
    }

    public SPLValue loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<SPLValue> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<SPLValue> list = new ArrayList<SPLValue>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<SPLValue> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<SPLValue> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
