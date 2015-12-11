package com.botconf.interfaceadapters.wpapi

import com.botconf.entities.interfaces.ISpeaker
import grooid.lib.wpapi.WPAPIPost
import groovy.transform.CompileStatic

@CompileStatic
class SpeakerWPAPIPostAdapter implements ISpeaker {

    WPAPIPost post

    SpeakerWPAPIPostAdapter(WPAPIPost post) {
        this.post = post
    }

    @Override
    String getName() {
        post.title
    }

    @Override
    Long getPrimaryKey() {
        Long.valueOf(post.identifier)
    }


    @Override
    String getAbout() {
        post.content
    }

    @Override
    String getImageUrl() {
        post.featuredImage?.source
    }
}