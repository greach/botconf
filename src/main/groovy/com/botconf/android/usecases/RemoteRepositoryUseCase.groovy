package com.botconf.android.usecases
import android.content.Context
import com.botconf.R
import com.botconf.interfaceadapters.wpapi.SpeakerWPAPIPostAdapter
import com.botconf.interfaceadapters.wpapi.TalkWPAPIPostAdapter
import grooid.lib.wpapi.WPAPIPost
import grooid.lib.wpapi.WPAPIUseCase
import groovy.transform.CompileStatic

@CompileStatic
class RemoteRepositoryUseCase {

    static final String SPEAKER_TYPE = 'speaker'
    static final String TALK_TYPE = 'talk'

    Context ctx

    WPAPIUseCase useCase

    RemoteRepositoryUseCase(Context ctx) {
        useCase = new WPAPIUseCase(ctx)
        this.ctx = ctx
    }

    String getBaseUrlStr() {
        ctx?.getResources().getString(R.string.botconf_wordpress_url)
    }

    void loadConferenceData(Closure closure) {

        useCase.fetchAllPosts(baseUrlStr, SPEAKER_TYPE,null) {  List<WPAPIPost> posts ->
            def speakers = posts.collect { new SpeakerWPAPIPostAdapter(it)}

            useCase.fetchAllPosts(baseUrlStr, TALK_TYPE,TalkWPAPIPostAdapter.customBuilderBlock) { talkposts ->
                def talks = talkposts.collect { WPAPIPost talkpost -> new TalkWPAPIPostAdapter(talkpost)}

                def conferences = talks.collect { TalkWPAPIPostAdapter talk -> talk.conference() }
                conferences = conferences.unique()
                closure(conferences,speakers, talks)
            }
        }

    }
}