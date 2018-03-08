package com.botconf.usecases

import android.content.Context
import com.botconf.R
import com.botconf.android.usecases.ConferenceAdapter
import com.botconf.entities.Sponsor
import com.botconf.entities.interfaces.IConference
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import com.dd.plist.NSArray
import com.dd.plist.NSDictionary
import com.dd.plist.PropertyListParser
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic
class SponsorsUseCase {
    Context ctx

    SponsorsUseCase(Context ctx) {
        this.ctx = ctx
    }

    static int sponsorInt(String kind) {
        if ( kind.compareToIgnoreCase('Diamond') == 0 ) {
            return 6

        } else if ( kind.compareToIgnoreCase('Platinum') == 0 ) {
            return 5

        } else if ( kind.compareToIgnoreCase('Gold') == 0 ) {
            return 4

        } else if ( kind.compareToIgnoreCase('Silver') == 0 ) {
            return 3

        } else if ( kind.compareToIgnoreCase('Bronze') == 0 ) {
            return 2

        } else if ( kind.compareToIgnoreCase('Collaborators') == 0 ) {
            return 1
        }
        0
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    String getBaseUrlStr() {
        ctx?.getResources()?.getString(R.string.sponsors_plist_url)
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    InputStream sponsorsInputStream() {
        return ctx.getResources().openRawResource(R.raw.sponsors);
    }

    List<Sponsor> sponsors() {
        def sponsors = [] as List<Sponsor>
        InputStream is = sponsorsInputStream()
        NSArray rootArr = (NSArray) PropertyListParser.parse(is)
        for (int i = 0; i < rootArr.count(); i++) {
            NSDictionary sponsorDict = (NSDictionary) rootArr.objectAtIndex(i)
            String kind = sponsorDict.get('kind');
            String url = sponsorDict.get('url');
            String imageUrl = sponsorDict.get('image_url');
            sponsors << new Sponsor(category: kind, url: url, imageUrl: imageUrl)
        }
        sponsors
    }
}