package com.botconf.usecases

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import com.botconf.R
import groovy.transform.CompileStatic

@CompileStatic
class VenueUseCase {
    String name() {
        "Teatros Luchana"
    }

    float longitude() {
        -3.698145
    }

    float latitude() {
        40.43188
    }


}