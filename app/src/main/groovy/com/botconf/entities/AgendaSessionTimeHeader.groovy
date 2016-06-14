package com.botconf.entities
import com.botconf.entities.interfaces.IAgendaSession
import groovy.time.TimeCategory
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeCheckingMode

@ToString
@EqualsAndHashCode(excludes = ['dateFormat'])
@CompileStatic
class AgendaSessionTimeHeader implements IAgendaSession {

    Date sessionDate

    String dateFormat = "yyyy/MM/dd"

    String getName() {

        String format = "EEEE HH:mm (${dateFormat})"

        if( sessionDate.after(nineHoursAgo()) && sessionDate.before(aWeekFromNow())) {
            format = "EEEE HH:mm"
        }
        sessionDate.format(format)
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    Date nineHoursAgo() {
        def currentDate = new Date()
        use( TimeCategory ) {
            currentDate = currentDate - 9.hours
        }
        currentDate
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    Date aWeekFromNow() {
        def currentDate = new Date()
        use( TimeCategory ) {
            currentDate = currentDate + 1.week
        }
        currentDate
    }

}