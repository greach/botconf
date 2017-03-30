package com.botconf.android.usecases

import android.content.Context
import com.botconf.R
import com.botconf.entities.interfaces.IConference
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import com.dd.plist.*
import groovy.time.TimeCategory
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic
class RemoteRepositoryUseCase implements IRemoteRepository {

    static final String SPEAKER_TYPE = 'speaker'
    static final String TALK_TYPE = 'talk'

    Context ctx

    RemoteRepositoryUseCase(Context ctx) {
        this.ctx = ctx
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    String getBaseUrlStr() {
        ctx?.getResources()?.getString(R.string.botconf_plist_url)
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    InputStream botconfInputStream() {
        return ctx.getResources().openRawResource(R.raw.botconf);
    }

    InputStream onlinePlistInputStream() {
        URL url = new URL(getBaseUrlStr())
        url.openStream()
    }

    void loadConferenceData(Closure closure) {
        try {
            byte[] bytes = getBaseUrlStr().toURL().bytes
            NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(bytes)

            List<IConference> conferences = new ArrayList<>()
            conferences << new ConferenceAdapter(name: 'Greach 2017', primaryKey: 2017000l)
            List<ISpeaker> speakers = new ArrayList<>()
            List<ITalk> talks = new ArrayList<>()

            def tracks = [] as List<String>
            NSArray tracksArray = (NSArray) rootDict.get('tracks')
            for ( int i = 0; i < tracksArray.count(); i++ ) {
                String track = tracksArray.objectAtIndex(i)
                tracks << track
            }

            NSDictionary speakersDict = (NSDictionary) rootDict.get('people')
            for ( String key : speakersDict.keySet() ) {
                NSDictionary speakerDict = (NSDictionary) speakersDict.get(key)
                speakers << new SpeakerAdapter(name: "${speakerDict.get('first').toString()} ${speakerDict.get('last').toString()}",
                        primaryKey: ((NSNumber) speakerDict.get('primaryKey')).longValue(),
                        about: speakerDict.get('bio').toString(),
                        imageUrl: speakerDict.get('imageUrl').toString(),
                        twitter: speakerDict.get('twitter').toString()
                )
            }

            NSDictionary sessionsDict = (NSDictionary) rootDict.get('sessions')
            for ( String key : sessionsDict.keySet() ) {
                NSDictionary sessionDict = (NSDictionary) sessionsDict.get(key)
                //boolean  active = (boolean) sessionsDict.get('active')
                NSDate startDate = (NSDate) sessionDict.get('date')
                NSNumber duration = (NSNumber) sessionDict.get('duration')
                NSNumber trackId = (NSNumber) sessionDict.get('trackId')
                String track = tracks[trackId.intValue()]
                NSNumber column = (NSNumber) sessionDict.get('column')
                //NSNumber roomId = (NSNumber) sessionDict.get('sessionNumber')
                NSString title = (NSString) sessionDict.get('title')
                NSString sessionDescription = (NSString) sessionDict.get('sessionDescription')
                NSArray presenters = (NSArray) sessionDict.get('presenters')
                NSNumber roomId = (NSNumber) sessionDict.get('roomId')
                NSNumber primaryKey =  (NSNumber) sessionDict.get('primaryKey');

                def sessionSpeakers = [] as List<ISpeaker>
                for ( int i = 0; i < presenters.count(); i++ ) {
                    String twitter = presenters.objectAtIndex(i) as String
                    def sessionSpeaker = speakers.find { ISpeaker speaker -> (speaker as SpeakerAdapter).twitter == twitter }
                    if ( sessionSpeaker ) {
                        sessionSpeakers << sessionSpeaker
                    }
                }

                def endDate = endDate(startDate, duration.intValue())

                talks << new TalkAdapter(
                        primaryKey: primaryKey.longValue(),
                        tags: [],
                        track: track.toString(),
                        name: title.toString(),
                        start: startDate.date,
                        end: endDate,
                        about: sessionDescription.toString(),
                        speakers: sessionSpeakers)
            }

            closure(conferences, speakers, talks)
        } catch(Exception ex) {
            ex.printStackTrace()
        }
    }
    @CompileStatic(TypeCheckingMode.SKIP)
    Date endDate(NSDate startDate, int duration) {
        Date endDate = startDate.date
        use(TimeCategory) {
            endDate += duration.minute
        }
        endDate
    }
}