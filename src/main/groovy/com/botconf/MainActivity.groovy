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
import android.telephony.TelephonyManager
import android.text.Html
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
import com.botconf.android.usecases.RemoteRepositoryUseCase
import com.botconf.entities.interfaces.IConference
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import com.botconf.usecases.LocalRepositoryUseCase
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

    RemoteRepositoryUseCase remoteRepositoryUseCase
    LocalRepositoryUseCase localRepositoryUseCase

    FloatingActionButton fab

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    private ViewPager viewPager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        logScreen()

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        String htmlBody = appAndDeviceHtml(getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager, getPackageManager(), getPackageName())
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
            3
        }

        @Override
        Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AllTalksFragment()
                case 1:
                    return new FavouritesTalksFragment()
                case 2:
                    return new TwitterHashTagFragment()
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

            showConferenceDataLoadedFeedback()

            localRepositoryUseCase.updateLocalRepositoryWithSpeakers(speakers)

            localRepositoryUseCase.updateLocalRepositoryWithConference(conferences)

            localRepositoryUseCase.updateLocalRepositoryWithTalks(talks)

            refreshViewPager()
        }
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
