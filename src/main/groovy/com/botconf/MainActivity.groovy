package com.botconf
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnUIThread
import com.botconf.android.TraitAppInfo
import com.botconf.android.TraitGoogleAnalytics
import com.botconf.android.fragments.AllTalksFragment
import com.botconf.android.fragments.FavouritesTalksFragment
import com.botconf.android.fragments.IUpdatableFragment
import com.botconf.android.fragments.TwitterHashTagFragment
import com.botconf.android.fragments.UpcomingTalksFragment
import com.botconf.android.usecases.IRemoteRepository
import com.botconf.android.usecases.RemoteRepositoryUseCase
import com.botconf.entities.interfaces.IConference
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import com.botconf.usecases.LocalRepositoryUseCase
import com.dd.plist.NSArray
import com.dd.plist.NSDictionary
import com.dd.plist.NSString
import com.dd.plist.PropertyListParser


import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends AppCompatActivity implements TraitGoogleAnalytics, TraitAppInfo {

    @Override
    String screenName() {
        'All Talks'
    }

    static final String TAG = MainActivity.simpleName

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = ""
    private static final String TWITTER_SECRET = ""

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter sectionsPagerAdapter

    IRemoteRepository remoteRepositoryUseCase
    LocalRepositoryUseCase localRepositoryUseCase

    FloatingActionButton fab

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    private ViewPager viewPager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logScreen()

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar)
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        logScreen('All Talks')

                    case 1:
                        logScreen('Favorites')

                    case 2:
                        logScreen('Twitter')
                }
            }

            @Override
            void onPageScrollStateChanged(int state) {}
        })

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.onClickListener = {
            showEmailComposer()
        }
        localRepositoryUseCase = new LocalRepositoryUseCase(this)

        int total = localRepositoryUseCase.countTalkCardsFromLocalRepository()
        if(!total) {

            refresh()
        }
    }

    static final String recipientEmail = "me@sergiodelamo.com"

    void showEmailComposer() {
        String htmlBody = appAndDeviceHtml()
        Intent i = new Intent(Intent.ACTION_SENDTO)
        i.with {
            setData(Uri.parse("mailto:" + recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject)
            putExtra(Intent.EXTRA_TEXT, Html.fromHtml(htmlBody))
        }
        try {
            startActivity(i);
        } catch (ActivityNotFoundException ex) {
            showSnackBar(fab,R.string.snackbar_no_email_client_available, Snackbar.LENGTH_LONG,R.color.snackbar_red)
        }
    }

    @Override
    public void onResume() {
        super.onResume()
        refreshViewPager()
    }

/**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Context context

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context
        }


        @Override
        int getCount() {
            4
        }

        @Override
        Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new UpcomingTalksFragment()
                case 1:
                    return new FavouritesTalksFragment()
                case 2:
                    return new TwitterHashTagFragment()
                case 0:
                    return new AllTalksFragment()
            }
        }



        @Override
        int getItemPosition(Object object) {
            if(object in IUpdatableFragment) {
                IUpdatableFragment f = (IUpdatableFragment ) object
                if (f) {
                    f.refreshUi()
                }
            }
            return super.getItemPosition(object);
        }

        @Override
        CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                   return context.getResources().getString(R.string.tab_talks)

                case 1:
                    return context.getResources().getString(R.string.tab_favourites)

                case 2:
                    return context.getResources().getString(R.string.botconf_twitter_hashtag)

                case 4:
                    return context.getResources().getString(R.string.tab_talks_all)
            }
        }
    }

    @OnUIThread
    void refreshViewPager() {
        sectionsPagerAdapter.notifyDataSetChanged()
    }

    @OnUIThread
    void showSnackBar(View v, int stringResource, int length, int colorResource) {
        String message = getResources().getString(stringResource)
        Snackbar snackbar = Snackbar.make(v,message,length)
        snackbar.getView().setBackgroundColor(colorResource)
        snackbar.show()
    }

    void showConferenceDataLoadedFeedback() {
        showSnackBar(fab,R.string.snackbar_loading_agenda, Snackbar.LENGTH_LONG,R.color.snackbar_green)
    }

    void showConferenceDataLoadingFeedback() {
        showSnackBar(fab,R.string.snackbar_loading_agenda, Snackbar.LENGTH_INDEFINITE,R.color.snackbar_blue)

    }

    @OnBackground
    void refresh() {
        showConferenceDataLoadingFeedback()
        remoteRepositoryUseCase = new RemoteRepositoryUseCase(this)
        remoteRepositoryUseCase.loadConferenceData { List<IConference> conferences, List<ISpeaker> speakers, List<ITalk> talks ->
            Log.e(TAG, 'finish loading conference data')
            Log.e(TAG, 'saving plist')
            saveSpeakersAsPlist(speakers,'greach-speakers.plist')
            saveConferencesAsPlist(conferences,'greach-conferences.plist')
            saveTalksAsPlist(talks,'greach-conferences.plist')

            showConferenceDataLoadedFeedback()

            Log.e(TAG, 'updating local repository')
            localRepositoryUseCase.updateLocalRepositoryWithSpeakers(speakers)

            localRepositoryUseCase.updateLocalRepositoryWithConference(conferences)

            localRepositoryUseCase.updateLocalRepositoryWithTalks(talks)

            refreshViewPager()
        }
    }

    void saveSpeakersAsPlist(List<ISpeaker> speakers, String filename) {
        NSDictionary root = new NSDictionary();

        NSArray arr = new NSArray(speakers.size());

        for(int i = 0; i < speakers.size();i++) {
            ISpeaker speaker = speakers[i]
            NSDictionary dict = nsDictionaryFromSpeaker(speaker)
            arr.setValue(i,dict)
        }
        root.put('speakers', arr)
        PropertyListParser.saveAsXML(root, new File(filename));
    }

    void saveConferencesAsPlist(List<IConference> conferences, String filename) {
        NSDictionary root = new NSDictionary();

        NSArray confs = new NSArray(conferences.size());

        for(int i = 0; i < conferences.size();i++) {
            IConference conference = conferences[i]
            NSDictionary dict = new NSDictionary()
            dict.put('primaryKey', conference.primaryKey)
            dict.put('name', conference.name)
            confs.setValue(i,dict)
        }
        root.put('conferences', confs)
        PropertyListParser.saveAsXML(root, new File(filename));
    }

    void saveTalksAsPlist(List<ITalk> talks, String filename) {
        NSDictionary root = new NSDictionary();

        NSArray confs = new NSArray(talks.size());

        for(int i = 0; i < talks.size();i++) {
            ITalk talk = talks[i]

            NSDictionary dict = new NSDictionary()
            dict.put('about', talk.about)
            dict.put('slidesUrl', talk.slidesUrl)
            dict.put('videoUrl', talk.videoUrl)
            dict.put('favourite', talk.isFavourite())
            dict.put('favourite', talk.isFavourite())
            dict.put('primaryKey', talk.primaryKey)
            dict.put('track', talk.track)
            dict.put('name', talk.name)
            dict.put('start',talk.start.time)
            dict.put('end',talk.end.time)

            NSArray tags = new NSArray(talk.tags.size());
            for(int x = 0; x < talk.tags.size(); x++) {
                NSString str = new NSString(talk.tags[x])
                tags.setValue(x, str)
            }
            dict.put('tags', tags)

            NSArray speakers = new NSArray(talks.speakers.size());
            for(int y = 0; y < talk.speakers.size(); y++) {
                ISpeaker speaker = talk.speakers[y]
                NSDictionary speakerDict = nsDictionaryFromSpeaker(speaker)
                speakers.setValue(y, speakerDict)
            }
            dict.put('spakers', speakers)

            confs.setValue(i,dict)
        }
        PropertyListParser.saveAsXML(root, new File(filename));
    }

    NSDictionary nsDictionaryFromSpeaker(ISpeaker speaker) {
        NSDictionary speakerDict = new NSDictionary()
        speakerDict.put('primaryKey', speaker.primaryKey)
        speakerDict.put('name', speaker.name)
        speakerDict.put('about', speaker.about)
        speakerDict.put('imageUrl',speaker.imageUrl)
        speakerDict
    }

    @Override
    boolean onCreateOptionsMenu(Menu menu) {
        menuInflater.inflate(R.menu.menu_main, menu)
        true
    }

        @Override
    boolean onOptionsItemSelected(MenuItem item) {
        int id = item.itemId

        if(id == R.id.action_refresh) {
            refresh()
        }

        super.onOptionsItemSelected(item)
    }

}
