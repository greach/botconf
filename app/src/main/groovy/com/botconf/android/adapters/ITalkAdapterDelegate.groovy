package com.botconf.android.adapters

import groovy.transform.CompileStatic

@CompileStatic
interface ITalkAdapterDelegate {

    void openSlides(String slidesUrl)
    void openVideo(String videoUrl)
    void tappedFavouriteTalk(boolean favourite)
}