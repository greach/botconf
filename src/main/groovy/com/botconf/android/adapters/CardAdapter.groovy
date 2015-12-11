package com.botconf.android.adapters
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.botconf.R
import com.botconf.entities.interfaces.IAgendaSession
import com.botconf.entities.interfaces.ITalkCard
import groovy.transform.CompileStatic

@CompileStatic
class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private static String TAG = CardAdapter.class.getSimpleName()

    private static int ROW_TYPE_HEADER = 1
    private static int ROW_TYPE_TALK = 2
    private List<IAgendaSession> agendaSessions
    private Context context
    ICardAdapterDelegate adapterDelegate

    CardAdapter(Context context, ICardAdapterDelegate adapterDelegate) {
        this.context = context
        this.adapterDelegate = adapterDelegate
    }


    void setAgendaSessions(List<IAgendaSession> agendaSessions) {
        this.agendaSessions = agendaSessions
        notifyDataSetChanged()
    }

    @Override
    CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = (viewType == ROW_TYPE_TALK) ? R.layout.card_list_item : R.layout.agenda_session_header_list_item
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false)
        new CardViewHolder(view)
    }

    @Override
    int getItemViewType(int position) {
        def obj = agendaSessions[position]
        (obj in ITalkCard) ? ROW_TYPE_TALK : ROW_TYPE_HEADER
    }

    @Override
    void onBindViewHolder(CardViewHolder holder, int position) {
        if(agendaSessions?.size() > position) {
            holder.bindCard(agendaSessions[position], getItemViewType(position+1))
        }
    }

    @Override
    int getItemCount() {
        agendaSessions?.size() ?: 0
    }

    class CardViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView nameLabel
        TextView timeIntervalLabel
        TextView trackLabel
        TextView tagsTextView
        View bottomBorderView

        IAgendaSession agendaSession

        CardViewHolder(View itemView) {
            super(itemView)
            nameLabel = (TextView) itemView.findViewById(R.id.nameLabel)
            timeIntervalLabel = (TextView) itemView.findViewById(R.id.timeIntervalLabel)
            trackLabel = (TextView) itemView.findViewById(R.id.trackLabel)
            tagsTextView = (TextView) itemView.findViewById(R.id.tagsTextView)
            bottomBorderView = (View) itemView.findViewById(R.id.bottomBorderView)

            itemView.setOnClickListener(this)

        }

        void bindCard(IAgendaSession agendaSession, int nextRowType) {
            this.agendaSession = agendaSession
            nameLabel?.text = agendaSession.name

            if(agendaSession in ITalkCard) {

                ITalkCard card = (ITalkCard)this.agendaSession
                if(card.tags) {
                    tagsTextView.text = card.tags.join(' - ').toUpperCase()
                    tagsTextView.visibility = View.VISIBLE
                } else {
                    tagsTextView.visibility = View.GONE
                }


                timeIntervalLabel?.text = "${card.startDate?.format( 'HH:mm')} - ${card.endDate?.format( 'HH:mm')}"
                trackLabel?.text = card.trackName
            }
            configureBottomBorderVisibility(nextRowType)
        }

        void configureBottomBorderVisibility(int nextRowType) {
            if(bottomBorderView) {
                bottomBorderView.visibility = (nextRowType == ROW_TYPE_HEADER) ? View.GONE  : View.VISIBLE
            }
        }


        @Override
        void onClick(View v) {
            if(agendaSession in ITalkCard) {
                ITalkCard card = (ITalkCard) this.agendaSession
                if (adapterDelegate) {
                    adapterDelegate.cardClicked(card)
                }
            }
        }
    }

}










