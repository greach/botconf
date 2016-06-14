package com.botconf.android.interfaceadapters.persistence.sqlite
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import groovy.transform.CompileStatic

@CompileStatic
class AppPersistenceDatabaseHelper extends SQLiteOpenHelper {

    static final String TAG = AppPersistenceDatabaseHelper.simpleName

    static final String DB_NAME = 'botconf.sqlite'

    static final String TABLE_CONFERENCE = 'conference'
    static final String COL_CONFERENCE_PRIMARY_KEY = 'conference_primarykey'
    static final String COL_CONFERENCE_NAME = 'conference_name'

    static final String TABLE_TALKSPEAKER = 'talk_speaker'
    static final String COL_TALKSPEAKER_TALK_FOREIGN_KEY = 'talkspeaker_talk_primarykey'
    static final String COL_TALKSPEAKER_SPEAKER_FOREIGN_KEY = 'talkspeaker_speaker_primarykey'

    static final String TABLE_SPEAKER = 'speaker'
    static final String COL_SPEAKER_PRIMARY_KEY = 'speaker_primarykey'
    static final String COL_SPEAKER_NAME = 'speaker_name'
    static final String COL_SPEAKER_ABOUT = 'speaker_about'
    static final String COL_SPEAKER_IMAGE_URL = 'speaker_image_url'

    static final String TABLE_TALK = 'talk'
    static final String COL_TALK_PRIMARY_KEY = 'talk_primarykey'
    static final String COL_TALK_FAVOURITE = 'talk_favourite'
    static final String COL_TALK_NAME = 'talk_name'
    static final String COL_TALK_ABOUT = 'talk_about'
    static final String COL_TALK_TRACK = 'talk_track'
    static final String COL_TALK_TAGS = 'talk_tags'
    static final String COL_TALK_START = 'talk_start'
    static final String COL_TALK_END = 'talk_end'
    static final String COL_TALK_SLIDES_URL = 'talk_slides_url'
    static final String COL_TALK_VIDEO_URL = 'talk_video_url'


    static class Patch {
        void apply(SQLiteDatabase db) {}
        void revert(SQLiteDatabase db) {}
    }

    static final List<Patch> PATCHES = new ArrayList<Patch>() {
        {
            add(new Patch() {
                void apply(SQLiteDatabase db) {

                    try {
                        String sql = """
                        CREATE TABLE ${TABLE_CONFERENCE} (
                            ${COL_CONFERENCE_PRIMARY_KEY}  INTEGER primary key,
                            ${COL_CONFERENCE_NAME} TEXT
                        )
                    """
                        db.execSQL(sql)
                    } catch (SQLException exception) {
                        Log.e(TAG, ("Exception while creating ${TABLE_CONFERENCE} table " + exception.getMessage()) as String)
                    }

                    try {
                        String sql = """
                        CREATE TABLE ${TABLE_SPEAKER} (
                            ${COL_SPEAKER_PRIMARY_KEY}  INTEGER primary key,
                            ${COL_SPEAKER_NAME} TEXT,
                            ${COL_SPEAKER_ABOUT} TEXT,
                            ${COL_SPEAKER_IMAGE_URL} TEXT
                        )
                    """
                        db.execSQL(sql)
                    } catch (SQLException exception) {
                        Log.e(TAG, ("Exception while creating ${TABLE_SPEAKER} table " + exception.getMessage()) as String)
                    }

                    try {
                        String sql = """
                        CREATE TABLE ${TABLE_TALK} (
                            ${COL_TALK_PRIMARY_KEY}  INTEGER primary key,
                            ${COL_TALK_FAVOURITE} INTEGER,
                            ${COL_TALK_NAME} TEXT,
                            ${COL_TALK_ABOUT} TEXT,
                            ${COL_TALK_TRACK} TEXT,
                            ${COL_TALK_TAGS} TEXT,
                            ${COL_TALK_START} TEXT,
                            ${COL_TALK_END} TEXT,
                            ${COL_TALK_SLIDES_URL} TEXT,
                            ${COL_TALK_VIDEO_URL} TEXT
                        )
                    """
                        db.execSQL(sql)
                    } catch (SQLException exception) {
                        Log.e(TAG, ("Exception while creating ${TABLE_TALK} table " + exception.getMessage()) as String)
                    }

                    try {
                        String sql = """
                        CREATE TABLE ${TABLE_TALKSPEAKER} (
                            ${COL_TALKSPEAKER_TALK_FOREIGN_KEY}  INTEGER,
                            ${COL_TALKSPEAKER_SPEAKER_FOREIGN_KEY}  INTEGER,
                            FOREIGN KEY (${COL_TALKSPEAKER_TALK_FOREIGN_KEY}) REFERENCES ${TABLE_TALK} (${COL_TALK_PRIMARY_KEY}),
                            FOREIGN KEY (${COL_TALKSPEAKER_SPEAKER_FOREIGN_KEY}) REFERENCES ${TABLE_SPEAKER} (${COL_SPEAKER_PRIMARY_KEY})
                        )
                    """
                        db.execSQL(sql)
                    } catch (SQLException exception) {
                        Log.e(TAG, ("Exception while creating ${TABLE_TALK} table " + exception.getMessage()) as String)
                    }


                }
                void revert(SQLiteDatabase db) {
                    db.execSQL("DROP TABLE " + TABLE_CONFERENCE)
                    db.execSQL("DROP TABLE " + TABLE_SPEAKER)
                    db.execSQL("DROP TABLE " + TABLE_TALK)
                    db.execSQL("DROP TABLE " + TABLE_TALKSPEAKER)
                }
            })
        }
    }


    AppPersistenceDatabaseHelper(Context context) {
        super(context, DB_NAME, null, PATCHES.size())
    }


    @Override
    void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true)
        }
    }

    @Override
    void onOpen(SQLiteDatabase db) {
        super.onOpen(db)
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }


    @Override
    void onCreate(SQLiteDatabase db) {
        for (int i=0; i < PATCHES.size(); i++) {
            PATCHES[i].apply(db)
        }
    }

    @Override
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i=oldVersion; i < newVersion; i++) {
            PATCHES[i].apply(db)
        }
    }

    @Override
    void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i=oldVersion; i > newVersion; i++) {
            PATCHES[i-1].revert(db)
        }
    }

}