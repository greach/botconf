package com.botconf.usecases

import android.content.Context
import com.botconf.R
import com.botconf.entities.interfaces.ITalk
import groovy.transform.CompileStatic

@CompileStatic
class TalkUseCase {
    static final String DATE_HOUR_MINUTES_FORMAT = "HH:mm"

    Context context

    TalkUseCase(Context context) {
        this.context = context
    }

    String getDateFormat() {
        String str = context.getResources().getString(R.string.botconf_compact_date_format)
        if(!str) {
            str = 'yyyy/MM/dd'
        }
        String dateFormat = "EEEE ${str} HH:mm"

        dateFormat
    }

    String formatTalkStartAndEndDates(ITalk talk) {
        "${talk.start.format(getDateFormat())} - ${talk.end.format(DATE_HOUR_MINUTES_FORMAT)}"
    }

    String formatTalkTags(ITalk talk) {
        talk.tags.join(' - ')
    }

    String stripHTMLOffFrom(String about) {

        ['<p>'].each {
            about = about?.replace(it, '')
        }

        ['</p>','<br />','<br/>'].each {
            about = about?.replace(it, '\n')
        }
        about
    }
}