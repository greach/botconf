package com.botconf.android.interfaceadapters.persistence.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import groovy.transform.CompileStatic

@CompileStatic
abstract class AbstractDataSource {

    abstract SQLiteOpenHelper getSqliteHelper()

    void initialize() {
        SQLiteDatabase db = getSqliteHelper().getReadableDatabase()
        db.close()
    }

    SQLiteDatabase open() {
        getSqliteHelper().getWritableDatabase()
    }

    SQLiteDatabase openReadable() {
        getSqliteHelper().getReadableDatabase()
    }

    protected void close() {
        getSqliteHelper().close()
    }

    static String getStringFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName)
        if (!cursor.isNull(columnIndex)) {
            cursor.getString(columnIndex)
        }
    }

    static Long getLongFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName)
        if (!cursor.isNull(columnIndex)) {
            cursor.getLong(columnIndex)
        }
    }

    static Boolean getBooleanFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName)
        if (cursor.isNull(columnIndex)) {
            return false
        }
        cursor.getInt(columnIndex) != 0
    }

    static Double getDoubleFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName)
        if (!cursor.isNull(columnIndex)) {
            return cursor.getDouble(columnIndex)
        }
    }

    int countByRawQueryWithDB(RawQueryParams queryParams, SQLiteDatabase db) {
        Cursor c = db.rawQuery(queryParams.sql, queryParams.selectionArgs)
        int count = 0
        if(c.moveToFirst()) {
            count = c.getInt(0)
        }
        c.close()
        count
    }


    /**
     * Only used for testing purposes
     */
    SQLiteDatabase getWritableDatabase() {
        open()
    }

    /**
     * Only used for testing purposes
     */
    SQLiteDatabase getReadableDatabase() {
        openReadable()
    }

}