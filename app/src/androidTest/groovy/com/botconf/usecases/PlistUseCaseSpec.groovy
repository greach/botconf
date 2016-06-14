package com.botconf.usecases

import android.util.Log;
import com.andrewreitz.spock.android.AndroidSpecification;
import com.andrewreitz.spock.android.WithContext
import com.botconf.android.usecases.RemoteRepositoryUseCase
import com.botconf.entities.interfaces.IConference
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk;

public class PlistUseCaseSpec extends AndroidSpecification {

    private static final String TAG = PlistUseCaseSpec.simpleName

    @WithContext def context

    void "test dowloadDataAsPlist"() {
        given:
        def plistUseCase = new PlistUseCase()
        def remoteRepositoryUseCase = new RemoteRepositoryUseCase(context)
        def path = '/Users/shoptimix/Downloads/'

        when:
        remoteRepositoryUseCase.loadConferenceData { List<IConference> conferences, List<ISpeaker> speakers, List<ITalk> talks ->
            Log.e(TAG, 'finish loading conference data')
            Log.e(TAG, 'saving plist')
            plistUseCase.saveSpeakersAsPlist(context, speakers, "${path}/greach-speakers.plist")
            plistUseCase.saveConferencesAsPlist(context, conferences, "${path}/greach-conferences.plist")
            plistUseCase.saveTalksAsPlist(context, talks, "${path}/greach-conferences.plist")
        }

        then:
        new File("${path}/greach-speakers.plist").exists()
        new File("${path}/greach-conferences.plist").exists()
        new File("${path}/greach-conferences.plist").exists()

    }
}
