package com.botconf
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.botconf.android.adapters.ITalkAdapterDelegate
import com.botconf.android.adapters.TalkAdapter
import com.botconf.entities.interfaces.ITalk
import com.botconf.usecases.ContactUseCase
import com.botconf.usecases.LocalRepositoryUseCase
import com.botconf.usecases.TalkUseCase
import groovy.transform.CompileStatic

@CompileStatic
class TalkActivity extends Activity implements  ITalkAdapterDelegate {
    static final String TAG = TalkActivity.simpleName
    static final String EXTRA_PRIMARY_KEY = "talk_primary_key"

    Long talkPrimaryKey

    LocalRepositoryUseCase localRepositoryUseCase
    TalkUseCase talkUseCase

    RecyclerView recyclerView

    TalkAdapter adapter

    ITalk talk

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.talk)

        recyclerView = (RecyclerView)findViewById(R.id.reyclerView)

        talkPrimaryKey = getIntent().getLongExtra(EXTRA_PRIMARY_KEY, -1l)

        localRepositoryUseCase = new LocalRepositoryUseCase(getApplicationContext())
        talkUseCase = new TalkUseCase(this)

        adapter = new TalkAdapter(getApplicationContext(), this, talkUseCase,)
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext())
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        recyclerView.setAdapter(adapter)

        refreshUi()
    }

    void refreshUi() {
        talk = localRepositoryUseCase.findTalkByPrimaryKey(talkPrimaryKey)
        adapter.talk = talk
    }


    void tappedFavouriteTalk(boolean favourite) {

        if(favourite) {
            localRepositoryUseCase.addtoFavourites(talk)
        } else {
            localRepositoryUseCase.removeFromFavourites(talk)
        }
        refreshUi()
    }


    void openSlides(String slidesUrl) {
        ContactUseCase.startBrowserIntent(this, slidesUrl)
    }

    void openVideo(String videoUrl) {
        ContactUseCase.startBrowserIntent(this, videoUrl)
    }
}