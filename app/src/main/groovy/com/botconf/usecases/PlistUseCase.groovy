package com.botconf.usecases

import android.content.Context
import com.botconf.entities.interfaces.IConference
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import com.dd.plist.NSArray
import com.dd.plist.NSDictionary
import com.dd.plist.NSString
import com.dd.plist.PropertyListParser
import groovy.transform.CompileStatic

@CompileStatic
class PlistUseCase {

    void saveSpeakersAsPlist(Context context, List<ISpeaker> speakers, String filename) {
        NSDictionary root = new NSDictionary();

        NSArray arr = new NSArray(speakers.size());

        for(int i = 0; i < speakers.size();i++) {
            ISpeaker speaker = speakers[i]
            NSDictionary dict = nsDictionaryFromSpeaker(speaker)
            arr.setValue(i,dict)
        }
        root.put('speakers', arr)
        File file = new File(filename);
        PropertyListParser.saveAsXML(root, file);
    }

    void saveConferencesAsPlist(Context context, List<IConference> conferences, String filename) {
        NSDictionary root = new NSDictionary();

        NSArray confs = new NSArray(conferences.size());

        for(int i = 0; i < conferences.size();i++) {
            IConference conference = conferences[i]
            NSDictionary dict = new NSDictionary()
            dict.put('primaryKey', conference.primaryKey)
            dict.put('name', conference.name)
            confs.setValue(i,dict)
        }
        root.put('conferences', confs)
        File file = new File(filename)
        PropertyListParser.saveAsXML(root, file)
    }

    void saveTalksAsPlist(Context context, List<ITalk> talks, String filename) {
        NSDictionary root = new NSDictionary();

        NSArray confs = new NSArray(talks.size());

        for(int i = 0; i < talks.size();i++) {
            ITalk talk = talks[i]

            NSDictionary dict = new NSDictionary()
            dict.put('about', talk.about)
            dict.put('slidesUrl', talk.slidesUrl)
            dict.put('videoUrl', talk.videoUrl)
            dict.put('favourite', talk.isFavourite())
            dict.put('favourite', talk.isFavourite())
            dict.put('primaryKey', talk.primaryKey)
            dict.put('track', talk.track)
            dict.put('name', talk.name)
            dict.put('start',talk.start.time)
            dict.put('end',talk.end.time)

            NSArray tags = new NSArray(0)
            if(talk.tags) {
                tags = new NSArray(talk.tags.size());
                for (int x = 0; x < talk.tags.size(); x++) {
                    NSString str = new NSString(talk.tags[x])
                    tags.setValue(x, str)
                }
            }
            dict.put('tags', tags)

            NSArray speakers = new NSArray(0);
            if(talk.speakers) {
                speakers = new NSArray(talks.speakers.size());
                for(int y = 0; y < talk.speakers.size(); y++) {
                    ISpeaker speaker = talk.speakers[y]
                    NSDictionary speakerDict = nsDictionaryFromSpeaker(speaker)
                    speakers.setValue(y, speakerDict)
                }
            }
            dict.put('spakers', speakers)

            confs.setValue(i,dict)
        }
        File file = new File(filename)
        PropertyListParser.saveAsXML(root, file)
    }

    NSDictionary nsDictionaryFromSpeaker(ISpeaker speaker) {
        NSDictionary speakerDict = new NSDictionary()
        speakerDict.put('primaryKey', speaker.primaryKey)
        speakerDict.put('name', speaker.name)
        speakerDict.put('about', speaker.about)
        speakerDict.put('imageUrl',speaker.imageUrl)
        speakerDict
    }

}