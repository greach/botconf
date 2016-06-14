import com.andrewreitz.spock.android.AndroidSpecification
import com.andrewreitz.spock.android.WithContext
import com.botconf.entities.AgendaSessionTimeHeader
import com.botconf.entities.TalkCard
import com.botconf.entities.interfaces.IAgendaSession
import com.botconf.entities.interfaces.ITalkCard
import com.botconf.usecases.AgendaUseCase
import spock.lang.Shared

class AgendaUseCaseSpec extends AndroidSpecification {

    @Shared
    def formatString = 'yyyy/MM/dd HH:mm'

    @WithContext def context

    void "Given a talks set an agenda is built correctly"() {
        given:

        def talks = unsortedGreach2015Sessions()
        talks += unsortedGreach2016Sessions()
        AgendaUseCase useCase = new AgendaUseCase(context)

        def expected = sortedGreach2016Agenda()
        expected += sortedGreach2015Agenda()

        when:
        def agendaSessions = useCase.buildAgendaWithAnchorDate(talks, new Date().parse(formatString, '2015/12/12 09:00'))

        then:
        agendaSessions
        agendaSessions.size() == expected.size()
        for(int i = 0; i < agendaSessions.size();i++) {
            assert agendaSessions[i] == expected[i]
        }

    }

    List<ITalkCard> unsortedGreach2016Sessions() {
        def talks = []
        talks << new TalkCard(track: 'Track 1', name: 'Second day Keynote', start: new Date().parse(formatString, '2016/04/09 09:00'), end: new Date().parse(formatString, '2016/04/09 10:45'))
        talks << new TalkCard(track: 'Track 1', name: 'First day Keynote', start: new Date().parse(formatString, '2016/04/08 09:00'), end: new Date().parse(formatString, '2016/04/08 10:45'))
        talks
    }

    List<ITalkCard> unsortedGreach2015Sessions() {
        def talks = []
        talks << new TalkCard(track: 'Track 1', name: 'Gpars dataflow', start: new Date().parse(formatString, '2015/04/11 14:00'), end: new Date().parse(formatString, '2015/04/11 15:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Stateless Authentication for microservices',start:new Date().parse(formatString,'2015/04/11 14:00'), end: new Date().parse(formatString, '2015/04/11 14:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Is Groovy better for Testing than Java?',start:new Date().parse(formatString, '2015/04/11 14:00'), end: new Date().parse(formatString,'2015/04/11 14:45'))
        talks << new TalkCard(track: 'Track 1', name: 'No-Nonsense NOSQL',start:new Date().parse(formatString, '2015/04/11 12:15'), end: new Date().parse(formatString, '2015/04/11 13:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Groovy Environment Manager (2015)',start:new Date().parse(formatString, '2015/04/11 12:15'), end: new Date().parse(formatString, '2015/04/11 13:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Intrastructure automation with gradle and puppet',start:new Date().parse(formatString, '2015/04/11 11:15'), end: new Date().parse(formatString, '2015/04/11 13:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Securing Ratpack', start:new Date().parse(formatString, '2015/04/11 11:15'), end: new Date().parse(formatString, '2015/04/11 12:00'))
        talks << new TalkCard(track: 'Track 1', name: 'DSL&#8217;ING YOUR GROOVY',start:new Date().parse(formatString, '2015/04/11 11:15'), end: new Date().parse(formatString, '2015/04/11 12:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Groovy options for reactive programming',start:new Date().parse(formatString, '2015/04/11 10:00'), end: new Date().parse(formatString, '2015/04/11 10:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Documentation Brought to Life: ASCIIDOCTOR',start:new Date().parse(formatString, '2015/04/11 10:00'), end: new Date().parse(formatString, '2015/04/11 10:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Hands on Ratpack',start:new Date().parse(formatString, '2015/04/11 09:00'), end: new Date().parse(formatString, '2015/04/11 10:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Cut your grails application to pieces & build feature plugins',start:  new Date().parse(formatString, '2015/04/11 09:00'), end: new Date().parse(formatString, '2015/04/11 09:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Grooscript in Action',start:new Date().parse(formatString, '2015/04/11 09:00'), end: new Date().parse(formatString,'2015/04/11 09:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Grails and Cassandra',start:new Date().parse(formatString, '2015/04/10 17:15'), end: new Date().parse(formatString,'2015/04/10 18:00'))
        talks << new TalkCard(track: 'Track 1', name: 'The Groovy Ecosystem',start:new Date().parse(formatString, '2015/04/10 17:15'), end: new Date().parse(formatString,'2015/04/10 18:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Advanced Microservices Concerns',start:new Date().parse(formatString,'2015/04/10 16:00'), end: new Date().parse(formatString,'2015/04/10 16:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Decathlon Sportmeeting &#8211; Sports, a new Grails Discipline',start:new Date().parse(formatString,'2015/04/10 16:00'), end: new Date().parse(formatString,'2015/04/10 16:45'))
        talks << new TalkCard(track: 'Track 1', name: 'GR8Workshops: A Guided discussion about Teaching and Diversity in the Groovy Community',start:  new Date().parse(formatString,'2015/04/10 15:00'), end: new Date().parse(formatString,'2015/04/10 15:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Getting Started with Groovy on Android',start:new Date().parse(formatString,'2015/04/10 14:00'), end: new Date().parse(formatString,'2015/04/10 15:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Hacking the Grails Spring Security 2.0 Plugin',start:new Date().parse(formatString,'2015/04/10 14:00'), end: new Date().parse(formatString,'2015/04/10 14:45'))
        talks << new TalkCard(track: 'Track 1', name: 'GPars Remoting',start:new Date().parse(formatString,'2015/04/10 12:15'), end: new Date().parse(formatString,'2015/04/10 13:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Grails Goodness',start:new Date().parse(formatString,'2015/04/10 12:15'), end: new Date().parse(formatString,'2015/04/10 13:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Building android apps with Gradle',start:new Date().parse(formatString,'2015/04/10 11:15'), end: new Date().parse(formatString,'2015/04/10 12:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Introducing Workflow Architectures using grails',start:new Date().parse(formatString,'2015/04/10 10:00'), end: new Date().parse(formatString,'2015/04/10 10:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Groovy and Scala: Friends or Foes',start:new Date().parse(formatString, '2015/04/10 10:00'), end: new Date().parse(formatString, '2015/04/10 10:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Groovy Past and Future',start:new Date().parse(formatString,'2015/04/10 09:00'), end: new Date().parse(formatString,'2015/04/10 09:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Beyond Gradle 2.0',start:new Date().parse(formatString,'2015/04/11 15:00'), end: new Date().parse(formatString, '2015/04/11 15:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Little did he know &#8230;',start:new Date().parse(formatString,'2015/04/11 15:00'), end: new Date().parse(formatString,'2015/04/11 15:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Use Groovy &#038; Grails in your Spring Boot Projects Don&#8217;t be afraid!',start:new Date().parse(formatString,'2015/04/11 16:00'), end: new Date().parse(formatString, '2015/04/11 16:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Idiomatic Gradle Plugin Writing',start:new Date().parse(formatString,'2015/04/11 16:45'), end: new Date().parse(formatString,'2015/04/11 16:45'))
        talks << new TalkCard(track: 'Track 1', name: 'Spock: Testing (In the) Enterprise',start:new Date().parse(formatString,'2015/04/11 16:00'), end: new Date().parse(formatString, '2015/04/11 18:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Debugging your Legacy',start:new Date().parse(formatString,'2015/04/11 17:15'), end: new Date().parse(formatString,'2015/04/11 18:00'))
        talks << new TalkCard(track: 'Track 1', name: 'Groovy on the shell',start:new Date().parse(formatString,'2015/04/11 17:15'), end: new Date().parse(formatString,'2015/04/11 18:00'))
        talks << new TalkCard(track: 'Track 1', name: 'AST &#8211; Groovy Transformers: More than meets the Eye!',start:new Date().parse(formatString,'2015/04/11 15:00'), end: new Date().parse(formatString, '2015/05/10 12:00'))
        talks
    }

    List<IAgendaSession> sortedGreach2016Agenda() {
        def expected = []

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2016/04/08 09:00'))
        expected << new TalkCard(track: 'Track1', name: 'First day Keynote', start: new Date().parse(formatString, '2016/04/08 09:00'), end: new Date().parse(formatString, '2016/04/08 10:45'))
        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2016/04/09 09:00'))
        expected << new TalkCard(track: 'Track1', name: 'Second day Keynote', start: new Date().parse(formatString, '2016/04/09 09:00'), end: new Date().parse(formatString, '2016/04/09 10:45'))

        expected
    }

    List<IAgendaSession> sortedGreach2015Agenda() {
        def expected = []


        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/11 17:15'))
        expected << new TalkCard(track: 'Track1', name: 'Debugging your Legacy',start:new Date().parse(formatString,'2015/04/11 17:15'), end: new Date().parse(formatString,'2015/04/11 18:00'))
        expected << new TalkCard(track: 'Track1', name: 'Groovy on the shell',start:new Date().parse(formatString,'2015/04/11 17:15'), end: new Date().parse(formatString,'2015/04/11 18:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/11 16:45'))
        expected << new TalkCard(track: 'Track1', name: 'Idiomatic Gradle Plugin Writing',start:new Date().parse(formatString,'2015/04/11 16:45'), end: new Date().parse(formatString,'2015/04/11 16:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/11 16:00'))
        expected << new TalkCard(track: 'Track1', name: 'Spock: Testing (In the) Enterprise',start:new Date().parse(formatString,'2015/04/11 16:00'), end: new Date().parse(formatString, '2015/04/11 18:00'))
        expected << new TalkCard(track: 'Track1', name: 'Use Groovy &#038; Grails in your Spring Boot Projects Don&#8217;t be afraid!',start:new Date().parse(formatString,'2015/04/11 16:00'), end: new Date().parse(formatString, '2015/04/11 16:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/11 15:00'))
        expected << new TalkCard(track: 'Track1', name: 'AST &#8211; Groovy Transformers: More than meets the Eye!',start:new Date().parse(formatString,'2015/04/11 15:00'), end: new Date().parse(formatString, '2015/05/10 12:00'))
        expected << new TalkCard(track: 'Track1', name: 'Beyond Gradle 2.0',start:new Date().parse(formatString,'2015/04/11 15:00'), end: new Date().parse(formatString, '2015/04/11 15:45'))
        expected << new TalkCard(track: 'Track1', name: 'Little did he know &#8230;',start:new Date().parse(formatString,'2015/04/11 15:00'), end: new Date().parse(formatString,'2015/04/11 15:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 14:00'))
        expected << new TalkCard(track: 'Track1', name: 'Gpars dataflow', start: new Date().parse(formatString, '2015/04/11 14:00'), end: new Date().parse(formatString, '2015/04/11 15:45'))
        expected << new TalkCard(track: 'Track1', name: 'Is Groovy better for Testing than Java?',start:new Date().parse(formatString, '2015/04/11 14:00'), end: new Date().parse(formatString,'2015/04/11 14:45'))
        expected << new TalkCard(track: 'Track1', name: 'Stateless Authentication for microservices',start:new Date().parse(formatString,'2015/04/11 14:00'), end: new Date().parse(formatString, '2015/04/11 14:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 12:15'))
        expected << new TalkCard(track: 'Track1', name: 'Groovy Environment Manager (2015)',start:new Date().parse(formatString, '2015/04/11 12:15'), end: new Date().parse(formatString, '2015/04/11 13:00'))
        expected << new TalkCard(track: 'Track1', name: 'No-Nonsense NOSQL',start:new Date().parse(formatString, '2015/04/11 12:15'), end: new Date().parse(formatString, '2015/04/11 13:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 11:15'))
        expected << new TalkCard(track: 'Track1', name: 'DSL&#8217;ING YOUR GROOVY',start:new Date().parse(formatString, '2015/04/11 11:15'), end: new Date().parse(formatString, '2015/04/11 12:00'))
        expected << new TalkCard(track: 'Track1', name: 'Intrastructure automation with gradle and puppet',start:new Date().parse(formatString, '2015/04/11 11:15'), end: new Date().parse(formatString, '2015/04/11 13:00'))
        expected << new TalkCard(track: 'Track1', name: 'Securing Ratpack', start:new Date().parse(formatString, '2015/04/11 11:15'), end: new Date().parse(formatString, '2015/04/11 12:00'))


        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 10:00'))
        expected << new TalkCard(track: 'Track1', name: 'Documentation Brought to Life: ASCIIDOCTOR',start:new Date().parse(formatString, '2015/04/11 10:00'), end: new Date().parse(formatString, '2015/04/11 10:45'))
        expected << new TalkCard(track: 'Track1', name: 'Groovy options for reactive programming',start:new Date().parse(formatString, '2015/04/11 10:00'), end: new Date().parse(formatString, '2015/04/11 10:45'))


        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/11 09:00'))
        expected << new TalkCard(track: 'Track1', name: 'Cut your grails application to pieces & build feature plugins',start:  new Date().parse(formatString, '2015/04/11 09:00'), end: new Date().parse(formatString, '2015/04/11 09:45'))
        expected << new TalkCard(track: 'Track1', name: 'Grooscript in Action',start:new Date().parse(formatString, '2015/04/11 09:00'), end: new Date().parse(formatString,'2015/04/11 09:45'))
        expected << new TalkCard(track: 'Track1', name: 'Hands on Ratpack',start:new Date().parse(formatString, '2015/04/11 09:00'), end: new Date().parse(formatString, '2015/04/11 10:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/10 17:15'))
        expected << new TalkCard(track: 'Track1', name: 'Grails and Cassandra',start:new Date().parse(formatString, '2015/04/10 17:15'), end: new Date().parse(formatString,'2015/04/10 18:00'))
        expected << new TalkCard(track: 'Track1', name: 'The Groovy Ecosystem',start:new Date().parse(formatString, '2015/04/10 17:15'), end: new Date().parse(formatString,'2015/04/10 18:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 16:00'))
        expected << new TalkCard(track: 'Track1', name: 'Advanced Microservices Concerns',start:new Date().parse(formatString,'2015/04/10 16:00'), end: new Date().parse(formatString,'2015/04/10 16:45'))
        expected << new TalkCard(track: 'Track1', name: 'Decathlon Sportmeeting &#8211; Sports, a new Grails Discipline',start:new Date().parse(formatString,'2015/04/10 16:00'), end: new Date().parse(formatString,'2015/04/10 16:45'))


        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 15:00'))
        expected << new TalkCard(track: 'Track1', name: 'GR8Workshops: A Guided discussion about Teaching and Diversity in the Groovy Community',start:  new Date().parse(formatString,'2015/04/10 15:00'), end: new Date().parse(formatString,'2015/04/10 15:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 14:00'))
        expected << new TalkCard(track: 'Track1', name: 'Getting Started with Groovy on Android',start:new Date().parse(formatString,'2015/04/10 14:00'), end: new Date().parse(formatString,'2015/04/10 15:45'))
        expected << new TalkCard(track: 'Track1', name: 'Hacking the Grails Spring Security 2.0 Plugin',start:new Date().parse(formatString,'2015/04/10 14:00'), end: new Date().parse(formatString,'2015/04/10 14:45'))

        expected << new AgendaSessionTimeHeader(sessionDate: new Date().parse(formatString,'2015/04/10 12:15'))
        expected << new TalkCard(track: 'Track1', name: 'GPars Remoting',start:new Date().parse(formatString,'2015/04/10 12:15'), end: new Date().parse(formatString,'2015/04/10 13:00'))
        expected << new TalkCard(track: 'Track1', name: 'Grails Goodness',start:new Date().parse(formatString,'2015/04/10 12:15'), end: new Date().parse(formatString,'2015/04/10 13:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 11:15'))
        expected << new TalkCard(track: 'Track1', name: 'Building android apps with Gradle',start:new Date().parse(formatString,'2015/04/10 11:15'), end: new Date().parse(formatString,'2015/04/10 12:00'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString, '2015/04/10 10:00'))
        expected << new TalkCard(track: 'Track1', name: 'Groovy and Scala: Friends or Foes',start:new Date().parse(formatString, '2015/04/10 10:00'), end: new Date().parse(formatString, '2015/04/10 10:45'))
        expected << new TalkCard(track: 'Track1', name: 'Introducing Workflow Architectures using grails',start:new Date().parse(formatString,'2015/04/10 10:00'), end: new Date().parse(formatString,'2015/04/10 10:45'))

        expected << new AgendaSessionTimeHeader(sessionDate:new Date().parse(formatString,'2015/04/10 09:00'))
        expected << new TalkCard(track: 'Track1', name: 'Groovy Past and Future',start:new Date().parse(formatString,'2015/04/10 09:00'), end: new Date().parse(formatString,'2015/04/10 09:45'))

        expected
    }
}

