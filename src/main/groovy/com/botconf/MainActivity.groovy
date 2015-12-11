package com.botconf
import android.content.Context
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
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnUIThread
import com.botconf.android.fragments.AllTalksFragment
import com.botconf.android.fragments.FavouritesTalksFragment
import com.botconf.android.fragments.IUpdatableFragment
import com.botconf.android.usecases.RemoteRepositoryUseCase
import com.botconf.entities.interfaces.IConference
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import com.botconf.entities.interfaces.ITalkCard
import com.botconf.usecases.LocalRepositoryUseCase
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends AppCompatActivity {
    static final String TAG = MainActivity.simpleName



    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter

    RemoteRepositoryUseCase remoteRepositoryUseCase
    LocalRepositoryUseCase localRepositoryUseCase

    FloatingActionButton fab

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        localRepositoryUseCase = new LocalRepositoryUseCase(this)

        int total = localRepositoryUseCase.countTalkCardsFromLocalRepository()
        if(!total) {

            refresh()
        }
    }

    @Override
    public void onResume() {
        super.onResume()
        Log.e(TAG, "On resume")
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
        Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AllTalksFragment()
                case 1:
                    return new FavouritesTalksFragment()
            }
        }

        @Override
        public int getItemPosition(Object object) {
            Log.e(TAG, "inside getItemPosition")
            if(object in IUpdatableFragment) {
                IUpdatableFragment f = (IUpdatableFragment ) object
                if (f != null) {
                    f.refreshUi()
                }
            }
            return super.getItemPosition(object);
        }

        @Override
        int getCount() {
            2
        }

        @Override
        CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                   return context.getResources().getString(R.string.tab_talks)


                case 1:
                    return context.getResources().getString(R.string.tab_favourites)
            }
        }
    }

    @OnUIThread
    void refreshViewPager() {
        mSectionsPagerAdapter.notifyDataSetChanged()
    }

    @OnUIThread
    void showConferenceDataLoadedFeedback() {
        Snackbar snackbar = Snackbar.make(fab, getResources().getString(R.string.snackbar_loading_agenda), Snackbar.LENGTH_LONG)
        snackbar.with {
            view.setBackgroundColor(R.color.snackbar_green)
            show()
        }
    }

    @OnUIThread
    void showConferenceDataLoadingFeedback() {
        Snackbar snackbar = Snackbar.make(fab, getResources().getString(R.string.snackbar_loading_agenda), Snackbar.LENGTH_INDEFINITE)
        snackbar.with {
            view.setBackgroundColor(R.color.snackbar_blue)
            show()
        }

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

            List<ITalkCard> talkCards = localRepositoryUseCase.fetchTalkCardsFromLocalRepository()

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
