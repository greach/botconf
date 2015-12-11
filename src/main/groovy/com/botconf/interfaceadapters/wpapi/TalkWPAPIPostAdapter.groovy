package com.botconf.interfaceadapters.wpapi

import com.botconf.entities.Conference
import com.botconf.entities.interfaces.ITalk
import com.botconf.entities.Speaker
import com.botconf.entities.interfaces.ISpeaker
import grooid.lib.wpapi.WPAPIPost
import grooid.lib.wpapi.WPAPIPostBuilder
import groovy.transform.CompileStatic

@CompileStatic
class TalkWPAPIPostAdapter implements ITalk {
    static final String DATE_FORMAT = "dd/MM/yy HH:mm"
    static final String YOUTUBE_URL = 'youtube_url'
    static final String SLIDES_URL = 'slides_url'
    static final String START_TIME = 'start_time'
    static final String END_TIME = 'end_time'
    static final String CONFERENCE = 'conference'
    static final String SPEAKER = 'speaker'


    static Closure customBuilderBlock = {
        if(it instanceof Map ) {
            Map dict = (Map)it
            Map m = [:]
            Object obj = dict['acf']
            if(obj in Map) {
                Map acfDict = (Map) obj
                if(acfDict[YOUTUBE_URL]) {
                    m[YOUTUBE_URL] = acfDict[YOUTUBE_URL]
                }
                if(acfDict[SLIDES_URL]) {
                    m[SLIDES_URL] = acfDict[SLIDES_URL]
                }
                if(acfDict[START_TIME]) {
                    m[START_TIME] = acfDict[START_TIME]
                }
                if(acfDict[END_TIME]) {
                    m[END_TIME] = acfDict[END_TIME]
                }

                if(acfDict[CONFERENCE]) {
                    m[CONFERENCE] = WPAPIPostBuilder.instantiatePostWithMap(acfDict[CONFERENCE])
                }

                if(acfDict[SPEAKER]) {
                    def speakers = [] as List<WPAPIPost>
                    if(acfDict[SPEAKER] in List) {
                        for(Object speakerObj : acfDict[SPEAKER]) {
                            if(speakerObj in Map) {
                                speakers << WPAPIPostBuilder.instantiatePostWithMap(speakerObj)
                            }
                        }
                    } else if(acfDict[SPEAKER] in Map) {
                        speakers << WPAPIPostBuilder.instantiatePostWithMap(acfDict[SPEAKER])
                    }
                    m[SPEAKER] = speakers
                }
            }
            m
        }
    }

    WPAPIPost post

    TalkWPAPIPostAdapter(WPAPIPost post) {
        this.post = post
    }

    Conference conference() {
        if(post.custom[TalkWPAPIPostAdapter.CONFERENCE] in WPAPIPost) {
            WPAPIPost post = (WPAPIPost) post.custom[TalkWPAPIPostAdapter.CONFERENCE]
            return new Conference(name: post.title, primaryKey: Long.valueOf(post.identifier))
        }
        null
    }

    @Override
    List<ISpeaker> getSpeakers() {
        def speakers = [] as List<ISpeaker>
        if(post.custom[TalkWPAPIPostAdapter.SPEAKER] in List) {

            for (Object obj : post.custom[TalkWPAPIPostAdapter.SPEAKER] as List) {
                if(obj in WPAPIPost) {
                    WPAPIPost post = (WPAPIPost) obj
                    speakers << new Speaker(name: post.title, primaryKey: Long.valueOf(post.identifier), about: post.content)
                }

            }
        }
        speakers
    }

    @Override
    Long getPrimaryKey() {
        return Long.valueOf(post.identifier)
    }

    @Override
    String getTrack() {
        post.terms?.category?.name
    }

    @Override
    List<String> getTags() {
        post?.terms?.postTags?.collect { it.name }
    }

    @Override
    String getName() {
        post.title
    }


    @Override
    String getAbout() {
        post.content
    }

    @Override
    Date getStart() {
        (post.custom[START_TIME] in String) ? new Date().parse(DATE_FORMAT, post.custom[START_TIME] as String) : null
    }

    @Override
    Date getEnd() {
        (post.custom[END_TIME] in String) ? new Date().parse(DATE_FORMAT, post.custom[END_TIME] as String) : null
    }

    @Override
    String getSlidesUrl() {
        post.custom[SLIDES_URL]
    }

    @Override
    String getVideoUrl() {
        post.custom[YOUTUBE_URL]
    }

    @Override
    boolean isFavourite() {
        false
    }

    String toString() {
        """
name: ${name}
slides: ${slidesUrl}
video: ${videoUrl}
start: ${start}
end: ${end}
about: ${about}
"""
    }
}

