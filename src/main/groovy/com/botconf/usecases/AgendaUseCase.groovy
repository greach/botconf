package com.botconf.usecases
import android.content.Context
import com.botconf.R
import com.botconf.entities.AgendaSessionTimeHeader
import com.botconf.entities.interfaces.IAgendaSession
import com.botconf.entities.interfaces.ITalkCard
import groovy.time.TimeCategory
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic
class AgendaUseCase {

    Context context
    String dateFormat = 'MM/dd/yyyy/'

    AgendaUseCase(Context context) {
        this.context = context
        if(context) {
            dateFormat = context.getResources().getString(R.string.botconf_compact_date_format)
        }
    }

    List<IAgendaSession> buildAgendaWithAnchorDate(List<ITalkCard> talks, Date anchorDate) {
        def agenda = []

        talks.sort { a, b ->
            boolean chronologicalOrder = a.start.after(anchorDate) && b.start.after(anchorDate)
            int compareTo = (chronologicalOrder) ? a.start <=> b.start : b.start <=> a.start
            (compareTo == 0) ? a.name <=> b.name : compareTo
        }

        for(ITalkCard talk in talks) {

            def timeHeader = new AgendaSessionTimeHeader(sessionDate: talk.start,dateFormat: dateFormat)
            boolean shouldShowTalk = shouldShowTalk(talk)
            if(shouldShowTalk && !agenda.any { it == timeHeader }) {
                agenda << timeHeader
            }
            if(shouldShowTalk || (!shouldShowTalk && talk.start.after(anchorDate))) {
                agenda << talk
            }

        }

        agenda
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    Date agendaAnchorDate() {
        def currentDate = new Date()
        use( TimeCategory ) {
            currentDate = currentDate - 9.hours
        }
        currentDate
    }

    List<IAgendaSession> buildAgenda(List<ITalkCard> talks) {
        buildAgendaWithAnchorDate(talks, agendaAnchorDate())
    }

    static boolean shouldShowTalk(ITalkCard talk) {


        if(!talk.tags && !talk.track) {
            return false
        }


        true
    }
}