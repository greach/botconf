package com.botconf.android.interfaceadapters.persistence.sqlite
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.botconf.R
import com.botconf.entities.*
import com.botconf.entities.interfaces.*
import groovy.transform.CompileStatic

import static com.botconf.android.interfaceadapters.persistence.sqlite.AppPersistenceDatabaseHelper.*

@CompileStatic
class AppPersistenceDataSource extends AbstractDataSource implements IAppPersistenceDataSource {

    static final String JOIN_SEPARATOR = '@'

    AppPersistenceDatabaseHelper sqliteHelper

    Context context

    String getDateFormat() {
        context.getResources().getString(R.string.botconf_date_format)
    }

    AppPersistenceDataSource(Context context) {
        this.context = context
        sqliteHelper = new AppPersistenceDatabaseHelper(context)
        initialize()
    }


    @Override
    void addToFavorites(ITalk talk) {

        changeTalkFavouriteFlag(talk,true)

    }

    void changeTalkFavouriteFlag(ITalk talk, boolean favourite) {
        SQLiteDatabase db = open();
        db.beginTransaction();

        if(existsTalkByPrimaryKeyWithDB(talk.primaryKey, db)) {
            ContentValues cv = new ContentValues();
            cv.put(COL_TALK_FAVOURITE, favourite ? 1 : 0);
            db.update(TABLE_TALK, cv, COL_TALK_PRIMARY_KEY + " = ? ", ["${talk.primaryKey}"] as String[])
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    @Override
    void removeFromFavorites(ITalk talk) {
        changeTalkFavouriteFlag(talk,false)
    }


    @Override
    ITalk findTalkByPrimaryKey(Long primaryKey) {
        SQLiteDatabase db = getReadableDatabase()

        String sqlString = """
        SELECT
            ${COL_TALK_PRIMARY_KEY},
            ${COL_TALK_FAVOURITE},
            ${COL_TALK_NAME},
            ${COL_TALK_ABOUT},
            ${COL_TALK_TAGS},
            ${COL_TALK_TRACK},
            ${COL_TALK_SLIDES_URL},
            ${COL_TALK_VIDEO_URL},
            ${COL_TALK_START},
            ${COL_TALK_END},
            ${COL_SPEAKER_PRIMARY_KEY},
            ${COL_SPEAKER_NAME},
            ${COL_SPEAKER_IMAGE_URL},
            ${COL_SPEAKER_ABOUT}
        FROM ${TABLE_TALK}
        LEFT OUTER JOIN ${TABLE_TALKSPEAKER} ON ${TABLE_TALKSPEAKER}.${COL_TALKSPEAKER_TALK_FOREIGN_KEY} = ${TABLE_TALK}.${COL_TALK_PRIMARY_KEY}
        LEFT OUTER JOIN ${TABLE_SPEAKER} ON ${TABLE_SPEAKER}.${COL_SPEAKER_PRIMARY_KEY} = ${TABLE_TALKSPEAKER}.${COL_TALKSPEAKER_SPEAKER_FOREIGN_KEY}
        WHERE ${COL_TALK_PRIMARY_KEY} = ?
"""
        String[] args = [String.valueOf(primaryKey)] as String[]
        Cursor c = db.rawQuery(sqlString,args)

        ITalk talk
        def speakers = [] as Set<ISpeaker>
        if (c.moveToFirst()) {
            for (; ;) {
                if(!talk) {
                    talk = createTalkFromCursor(c)
                }
                ISpeaker speaker = createSpeakerFromCursor(c)
                if(speaker) {
                    speakers << speaker
                }

                if (!c.moveToNext()) {
                    break
                }
            }
        }

        (talk as Talk)?.speakers = speakers as List
        c.close()
        db.close()

        talk
    }

    @Override
    long insertTalk(ITalk talk) {
        SQLiteDatabase db = open()
        db.beginTransaction()

        long primaryKey = insertTalkWithDB(talk, db)

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()

        primaryKey
    }

    long insertTalkWithDB(ITalk talk, SQLiteDatabase db) {

        ContentValues cv = contentValuesFromTalk(talk)


        Long primaryKey = talk.primaryKey

        if(existsTalkByPrimaryKeyWithDB(primaryKey,db)) {
            db.update(TABLE_TALK, cv, "${COL_TALK_PRIMARY_KEY} = ? " as String, [String.valueOf(primaryKey)] as String[])

        } else {

            // Only update favourite if inserting not while updating to avoid overriding user preferences
            cv.put(COL_TALK_FAVOURITE, talk.favourite ? 1 : 0)

            primaryKey = db.insert(TABLE_TALK, null, cv)
        }

        removeTalkSpeakersRelationsWithDB(primaryKey,db)
        for(ISpeaker speaker : talk.speakers) {
            db.insert(TABLE_TALKSPEAKER, null, contentValuesFromTalkAndSpeaker(primaryKey, speaker))
        }

        primaryKey
    }

    ContentValues contentValuesFromTalkAndSpeaker(Long talkPrimaryKey, ISpeaker speaker) {
        ContentValues cv = new ContentValues()
        cv.with {
            put(COL_TALKSPEAKER_TALK_FOREIGN_KEY, talkPrimaryKey)
            put(COL_TALKSPEAKER_SPEAKER_FOREIGN_KEY, speaker.primaryKey)
        }
        cv
    }

    void removeTalkSpeakersRelationsWithDB(Long talkPrimaryKey, SQLiteDatabase db) {
        String whereClause = "${COL_TALKSPEAKER_TALK_FOREIGN_KEY}=?"
        db.delete(TABLE_TALKSPEAKER, whereClause,[talkPrimaryKey] as String[])
    }

    boolean existsConferenceByPrimaryKeyWithDB(Long primaryKey, SQLiteDatabase db) {
        RawQueryParams rawQueryParams = countRawQueryParamsForFindConferenceByPrimaryKey(primaryKey)
        countByRawQueryWithDB(rawQueryParams, db) > 0 ? true : false
    }

    boolean existsTalkByPrimaryKeyWithDB(Long primaryKey, SQLiteDatabase db) {
        RawQueryParams rawQueryParams = countRawQueryParamsForFindTalkByPrimaryKey(primaryKey)
        countByRawQueryWithDB(rawQueryParams, db) > 0 ? true : false
    }

    boolean existsSpeakerByPrimaryKeyWithDB(Long primaryKey, SQLiteDatabase db) {
        RawQueryParams rawQueryParams = countRawQueryParamsForFindSpeakerByPrimaryKey(primaryKey)
        countByRawQueryWithDB(rawQueryParams, db) > 0 ? true : false
    }

    RawQueryParams countRawQueryParamsForFindSpeakerByPrimaryKey(Long primaryKey) {
        countRawQueryParamsByPrimaryKey(primaryKey,TABLE_SPEAKER,COL_SPEAKER_PRIMARY_KEY)
    }

    RawQueryParams countRawQueryParamsForFindConferenceByPrimaryKey(Long primaryKey) {
        countRawQueryParamsByPrimaryKey(primaryKey,TABLE_CONFERENCE,COL_CONFERENCE_PRIMARY_KEY)
    }

    RawQueryParams countRawQueryParamsForFindTalkByPrimaryKey(Long primaryKey) {
        countRawQueryParamsByPrimaryKey(primaryKey,TABLE_TALK,COL_TALK_PRIMARY_KEY)
    }

    @Override
    int countTalks() {
        SQLiteDatabase db = openReadable()
        RawQueryParams rawQueryParams = countRawQueryParams(TABLE_TALK)
        countByRawQueryWithDB(rawQueryParams, db)
    }

    RawQueryParams countRawQueryParams(String tableName) {
        String sql = "SELECT COUNT(*) FROM ${tableName}"
        String[] args = [] as String[]
        new RawQueryParams(sql:sql,selectionArgs: args)
    }

    RawQueryParams countRawQueryParamsByPrimaryKey(Long primaryKey, String tableName, String primaryKeyColumnName) {
        String sql = "SELECT COUNT(*) FROM ${tableName} WHERE ${primaryKeyColumnName} = ? "
        String[] args = [String.valueOf(primaryKey)] as String[]
        new RawQueryParams(sql:sql,selectionArgs: args)
    }

    @Override
    long insertConference(IConference conference) {

        SQLiteDatabase db = open();
        db.beginTransaction();
        long rowId = insertConferenceWithDB(conference, db)
        db.setTransactionSuccessful()
        db.endTransaction()
        close()
        rowId
    }

    @Override
    long insertSpeaker(ISpeaker speaker) {

        SQLiteDatabase db = open();
        db.beginTransaction();
        long rowId = insertSpeakerWithDB(speaker, db)
        db.setTransactionSuccessful()
        db.endTransaction()
        close()
        rowId
    }

    long insertConferenceWithDB(IConference conference, SQLiteDatabase db) {
        ContentValues cv = contentValuesFromConference(conference)


        Long primaryKey = conference.primaryKey

        if(existsConferenceByPrimaryKeyWithDB(primaryKey,db)) {
            db.update(TABLE_CONFERENCE, cv, "${COL_CONFERENCE_PRIMARY_KEY} = ? " as String, [String.valueOf(primaryKey)] as String[])

        } else {
            primaryKey = db.insert(TABLE_CONFERENCE, null, cv)
        }

        primaryKey
    }

    IConference createConferenceFromCursor(Cursor c) {
        String name = getStringFromColumnName(c, COL_CONFERENCE_NAME);
        Long primaryKey = getLongFromColumnName(c, COL_CONFERENCE_PRIMARY_KEY)
        new Conference(name: name, primaryKey: primaryKey)
    }



    @Override
    List<ITalkCard> findAllTalkCards() {
        findAllTalkCardsWithWherClause()
    }

    @Override
    List<ITalkCard> findAllFavouriteTalkCards() {
        findAllTalkCardsWithWherClause(" WHERE ${COL_TALK_FAVOURITE} = 1  ")
    }

    String speakerTalkPrimaryKeyQuery() {
        """
            SELECT
                ${COL_TALK_PRIMARY_KEY},
                ${COL_SPEAKER_NAME}
            FROM ${TABLE_TALK}
            INNER JOIN ${TABLE_TALKSPEAKER} ON ${TABLE_TALKSPEAKER}.${COL_TALKSPEAKER_TALK_FOREIGN_KEY} = ${TABLE_TALK}.${COL_TALK_PRIMARY_KEY}
            INNER JOIN ${TABLE_SPEAKER} ON ${TABLE_SPEAKER}.${COL_SPEAKER_PRIMARY_KEY} = ${TABLE_TALKSPEAKER}.${COL_TALKSPEAKER_SPEAKER_FOREIGN_KEY}
            WHERE ${COL_TALK_PRIMARY_KEY} = ?
        """
    }

    String speakerNamesForTalkWithDB(Long talkPrimaryKey, SQLiteDatabase db) {
        Cursor c = db.rawQuery(speakerTalkPrimaryKeyQuery(), [talkPrimaryKey] as String[]);
        def speakerNames = [] as Set

        if (c.moveToFirst()) {
            for (; ;) {
                speakerNames << getStringFromColumnName(c, COL_SPEAKER_NAME)
                if (!c.moveToNext()) {
                    break
                }
            }
        }

        speakerNames.join(', ')
    }

    List<ITalkCard> findAllTalkCardsWithWherClause(String whereClause = null) {
        SQLiteDatabase db = getReadableDatabase()

        String sqlString = """
        SELECT
            ${COL_TALK_PRIMARY_KEY},
            ${COL_TALK_NAME},
            ${COL_TALK_TRACK},
            ${COL_TALK_START},
            ${COL_TALK_END},
            ${COL_TALK_TAGS}
        FROM ${TABLE_TALK}
        ${whereClause ?: ''}
"""
        Cursor c = db.rawQuery(sqlString, null);
        List<ITalkCard> talks = []

        if (c.moveToFirst()) {
            for (; ;) {
                ITalkCard card = createTalkCardFromCursor(c)
                if(card in TalkCard) {
                    ((TalkCard)card).speakerName = speakerNamesForTalkWithDB(card.primaryKey, db)
                }
                talks << card

                if (!c.moveToNext()) {
                    break
                }
            }
        }

        c.close()
        db.close()
        talks
    }

    long insertSpeakerWithDB(ISpeaker speaker, SQLiteDatabase db) {
        ContentValues cv = contentValuesFromSpeaker(speaker)
        Long primaryKey = speaker.primaryKey

        if(existsSpeakerByPrimaryKeyWithDB(primaryKey,db)) {
            db.update(TABLE_SPEAKER, cv, "${COL_SPEAKER_PRIMARY_KEY} = ? " as String, [String.valueOf(primaryKey)] as String[])

        } else {
            primaryKey = db.insert(TABLE_SPEAKER, null, cv)
        }

        primaryKey
    }


    ContentValues contentValuesFromSpeaker(ISpeaker speaker) {

        ContentValues cv = new ContentValues()

        if(speaker.primaryKey) {
            cv.put(COL_SPEAKER_PRIMARY_KEY, speaker.primaryKey)
        }

        if(!speaker.name) {
            cv.putNull(COL_SPEAKER_NAME)
        } else {
            cv.put(COL_SPEAKER_NAME, speaker.name)
        }

        if(!speaker.about) {
            cv.putNull(COL_SPEAKER_ABOUT)
        } else {
            cv.put(COL_SPEAKER_ABOUT, speaker.about)
        }

        if(!speaker.imageUrl) {
            cv.putNull(COL_SPEAKER_IMAGE_URL)
        } else {
            cv.put(COL_SPEAKER_IMAGE_URL, speaker.imageUrl)
        }
        cv
    }

    ContentValues contentValuesFromTalk(ITalk talk) {

        ContentValues cv = new ContentValues()

        if(talk.primaryKey) {
            cv.put(COL_TALK_PRIMARY_KEY, talk.primaryKey)
        } else {
            cv.putNull(COL_TALK_PRIMARY_KEY)
        }

        if(talk.name) {
            cv.put(COL_TALK_NAME, talk.name)
        } else {
            cv.putNull(COL_TALK_NAME)
        }

        if(talk.about) {
            cv.put(COL_TALK_ABOUT, talk.about)
        } else {
            cv.putNull(COL_TALK_ABOUT)
        }

        if(talk.tags) {
            cv.put(COL_TALK_TAGS, talk.tags.join(JOIN_SEPARATOR))
        } else {
            cv.putNull(COL_TALK_TAGS)
        }

        if(talk.track) {
            cv.put(COL_TALK_TRACK, talk.track)
        } else {
            cv.putNull(COL_TALK_TRACK)
        }


        if(talk.start) {
            cv.put(COL_TALK_START, talk.start.format(getDateFormat()))
        } else {
            cv.putNull(COL_TALK_START)
        }

        if(talk.end) {
            cv.put(COL_TALK_END, talk.end.format(getDateFormat()))
        } else {
            cv.putNull(COL_TALK_END)
        }

        if(talk.slidesUrl) {
            cv.put(COL_TALK_SLIDES_URL, talk.slidesUrl)
        } else {
            cv.putNull(COL_TALK_SLIDES_URL)
        }

        if(talk.videoUrl) {
            cv.put(COL_TALK_VIDEO_URL, talk.videoUrl)
        } else {
            cv.putNull(COL_TALK_VIDEO_URL)

        }

        cv
    }

    ISpeaker createSpeakerFromCursor(Cursor c) {

        Speaker speaker = new Speaker()

        speaker.with {
            primaryKey = getLongFromColumnName(c, COL_SPEAKER_PRIMARY_KEY)
            name = getStringFromColumnName(c, COL_SPEAKER_NAME)
            imageUrl = getStringFromColumnName(c, COL_SPEAKER_IMAGE_URL)
            about = getStringFromColumnName(c, COL_SPEAKER_ABOUT)
        }
        return (speaker.primaryKey) ? speaker : null
    }

    void populateTalk(Cursor c, AbstractTalk talk) {
        String startStr = getStringFromColumnName(c, COL_TALK_START)
        String endStr = getStringFromColumnName(c, COL_TALK_END)
        talk.with {
            primaryKey = getLongFromColumnName(c, COL_TALK_PRIMARY_KEY)
            start = new Date().parse(getDateFormat(), startStr)
            end = new Date().parse(getDateFormat(), endStr)
            name = getStringFromColumnName(c, COL_TALK_NAME)
            track = getStringFromColumnName(c, COL_TALK_TRACK)
        }
        talk.tags = tagsFromCursor(c)
    }

    ITalk createTalkFromCursor(Cursor c) {

        Talk talk = new Talk()
        populateTalk(c,talk)
        talk.with {
            about = getStringFromColumnName(c, COL_TALK_ABOUT)
            slidesUrl = getStringFromColumnName(c, COL_TALK_SLIDES_URL)
            videoUrl = getStringFromColumnName(c, COL_TALK_VIDEO_URL)
            favourite = getBooleanFromColumnName(c,COL_TALK_FAVOURITE)
        }
        return (talk.primaryKey) ? talk : null
    }

    List<String> tagsFromCursor(Cursor c) {
        String tags = getStringFromColumnName(c, COL_TALK_TAGS)
        (tags) ? (tags.tokenize(JOIN_SEPARATOR) as List<String>) : [] as List<String>
    }

    ITalkCard createTalkCardFromCursor(Cursor c) {

        TalkCard talk = new TalkCard()
        populateTalk(c,talk)
        return (talk.primaryKey) ? talk : null
    }

    ContentValues contentValuesFromConference(IConference conference) {

        ContentValues cv = new ContentValues()

        if(conference.getPrimaryKey()) {
            cv.put(COL_CONFERENCE_PRIMARY_KEY, conference.getPrimaryKey())
        }

        if(conference.getName()) {
            cv.put(COL_CONFERENCE_NAME, conference.getName())
        } else {
            cv.putNull(COL_CONFERENCE_NAME)
        }

        cv
    }
}