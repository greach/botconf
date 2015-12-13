package com.botconf.android.fragments
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botconf.R
import groovy.transform.CompileStatic
@CompileStatic
class TwitterHashTagFragment extends ListFragment {

    String hashTag() {
        getActivity().getResources().getString(R.string.botconf_twitter_hashtag)
    }

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater.inflate(R.layout.timeline, container, false)
    }
}