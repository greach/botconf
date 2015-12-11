package com.botconf.android.adapters
import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.botconf.R
import com.botconf.android.interfaceadapters.network.volley.VolleySingleton
import com.botconf.entities.interfaces.ISpeaker
import com.botconf.entities.interfaces.ITalk
import com.botconf.usecases.TalkUseCase
import groovy.transform.CompileStatic

@CompileStatic
class TalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = TalkAdapter.class.getSimpleName()

    private static int ROW_TYPE_SPEAKER = 1
    private static int ROW_TYPE_TALK = 2
    Context context
    ITalk talk
    TalkUseCase talkUseCase
    ITalkAdapterDelegate delegate

    TalkAdapter(Context context, ITalkAdapterDelegate delegate, TalkUseCase talkUseCase) {
        this.context = context
        this.delegate = delegate
        this.talkUseCase = talkUseCase
    }


    void setTalk(ITalk talk) {
        this.talk = talk
        notifyDataSetChanged()
    }

    @Override
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == ROW_TYPE_TALK) {
            int resource = R.layout.talk_list_item
            View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false)
            return new TalkViewHolder(view)
        } else if(viewType == ROW_TYPE_SPEAKER) {
            int resource = R.layout.speaker_list_item
            View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false)
            return new SpeakerViewHolder(view)
        }
    }

    @Override
    int getItemViewType(int position) {
        if(position == 0) {
            return ROW_TYPE_TALK
        }
        ROW_TYPE_SPEAKER
    }

    @Override
    void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder in TalkViewHolder) {
            (holder as TalkViewHolder).bindTalk(delegate, talk)

        } else if(holder in SpeakerViewHolder) {
            ISpeaker speaker = talk.speakers[position - 1]
            (holder as SpeakerViewHolder).bindSpeaker(speaker)
        }
    }

    @Override
    int getItemCount() {
        1 + talk?.speakers?.size()
    }

    class SpeakerViewHolder extends RecyclerView.ViewHolder {
        TextView speakerNameTextView
        ImageView speakerImageView
        TextView speakerBioTextView

        SpeakerViewHolder(View itemView) {
            super(itemView)

            speakerNameTextView = (TextView) itemView.findViewById(R.id.speakerNameTextView)
            speakerImageView = (ImageView) itemView.findViewById(R.id.speakerImageView)
            speakerBioTextView = (TextView) itemView.findViewById(R.id.speakerBioTextView)
        }

        void bindSpeaker(ISpeaker speaker) {

            if(speaker) {

                speakerNameTextView?.text = speaker.name
                speakerBioTextView?.text = talkUseCase.stripHTMLOffFrom(speaker.about)

                if(speaker.imageUrl) {

                    loadImage(speaker.imageUrl)

                } else {
                    speakerImageView.visibility = View.GONE
                }


            }

        }

        void loadImage(String imageUrl) {
        // Retrieves an image specified by the URL, displays it in the UI.
            ImageRequest request = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        void onResponse(Bitmap bitmap) {
                            speakerImageView.setImageBitmap(bitmap)
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.message)
                            speakerImageView.visibility = View.GONE
                        }
                    })
            // Access the RequestQueue through your singleton class.
            VolleySingleton.getInstance(context).addToRequestQueue(request)
        }
    }

    class TalkViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView
        TextView tagsTextView
        TextView descriptionTextView
        TextView trackTextView
        TextView fullDateTextView

        Button videoButton
        Button slidesButton
        Button favouriteButton

        ITalk talk


        TalkViewHolder(View itemView) {
            super(itemView)
            titleTextView = (TextView)itemView.findViewById(R.id.titleTextView)
            tagsTextView = (TextView)itemView.findViewById(R.id.tagsTextView)
            descriptionTextView = (TextView)itemView.findViewById(R.id.descriptionTextView)
            trackTextView = (TextView)itemView.findViewById(R.id.trackTextView)
            fullDateTextView = (TextView)itemView.findViewById(R.id.fullDateTextView)
            videoButton = (Button)itemView.findViewById(R.id.videoButton)
            slidesButton = (Button)itemView.findViewById(R.id.slidesButton)
            favouriteButton = (Button)itemView.findViewById(R.id.favouriteButton)
        }

        void bindTalk(ITalkAdapterDelegate delegate, ITalk talk ) {
            this.talk = talk

            titleTextView.text = talk.name
            tagsTextView.text = talkUseCase.formatTalkTags(talk)
            descriptionTextView.text = talkUseCase.stripHTMLOffFrom(talk.about)
            trackTextView.text = talk.track
            fullDateTextView.text = talkUseCase.formatTalkStartAndEndDates(talk)

            if(videoButton) {
                videoButton.visibility = talk.videoUrl ? View.VISIBLE : View.GONE
                if(talk.videoUrl) {
                    videoButton.onClickListener = new VideoButtonClickListener(delegate, talk.videoUrl)
                }
            }

            if(slidesButton) {
                slidesButton.visibility = talk.slidesUrl ? View.VISIBLE : View.GONE
                if(talk.slidesUrl) {
                    slidesButton.onClickListener = new SlidesButtonClickListener(delegate, talk.slidesUrl)
                }
            }

            if(favouriteButton) {
                favouriteButton?.text = context.getResources().getString((talk.favourite ? R.string.remove_from_favourites : R.string.add_to_favourites))
                favouriteButton?.onClickListener = new FavouriteButtonClickListener(delegate, !talk.favourite)
            }

        }

    }
}

@CompileStatic
class FavouriteButtonClickListener implements View.OnClickListener {

    ITalkAdapterDelegate delegate
    boolean favourite

    FavouriteButtonClickListener(ITalkAdapterDelegate delegate, boolean favourite) {
        this.delegate = delegate
        this.favourite = favourite
    }

    @Override
    void onClick(View v) {
        delegate.tappedFavouriteTalk(favourite)
    }
}

@CompileStatic
class SlidesButtonClickListener implements View.OnClickListener {

    ITalkAdapterDelegate delegate
    String slidesUrl

    SlidesButtonClickListener(ITalkAdapterDelegate delegate, String slidesUrl) {
        this.delegate = delegate
        this.slidesUrl = slidesUrl
    }

    @Override
    void onClick(View v) {
        delegate.openSlides(slidesUrl)
    }
}

@CompileStatic
class VideoButtonClickListener implements View.OnClickListener {

    ITalkAdapterDelegate delegate
    String videoUrl

    VideoButtonClickListener(ITalkAdapterDelegate delegate, String videoUrl) {
        this.delegate = delegate
        this.videoUrl = videoUrl
    }

    @Override
    void onClick(View v) {
        delegate.openVideo(videoUrl)
    }
}