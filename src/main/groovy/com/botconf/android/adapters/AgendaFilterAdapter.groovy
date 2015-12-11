package com.botconf.android.adapters
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.botconf.R
import com.botconf.entities.agendafilters.AgendaFilter
import com.botconf.entities.agendafilters.IAgendaFilter
import groovy.transform.CompileStatic

@CompileStatic
class AgendaFilterAdapter  extends RecyclerView.Adapter<AgendaFilterAdapter.AgendaViewHolder> {
    private static int ROW_TYPE_HEADER = 1
    private static int ROW_TYPE_AGENDA_FILTER = 2
    private List<IAgendaFilter> agendaFilters
    private Context context
    IAgendaFilterAdapterDelegate adapterDelegate

    AgendaFilterAdapter(Context context,IAgendaFilterAdapterDelegate adapterDelegate) {
        this.context = context
        this.adapterDelegate = adapterDelegate
    }

    void setAgendaFilters(List<IAgendaFilter> agendaFilters) {
        this.agendaFilters = agendaFilters
        notifyDataSetChanged()
    }

    @Override
    AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = (viewType == ROW_TYPE_AGENDA_FILTER) ? R.layout.agenda_filter_list_item : R.layout.agenda_filter_header_list_item
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false)
        new AgendaViewHolder(view)
    }

    @Override
    int getItemViewType(int position) {
        def obj = agendaFilters[position]
        (obj in AgendaFilter) ? ROW_TYPE_AGENDA_FILTER : ROW_TYPE_HEADER
    }

    @Override
    void onBindViewHolder(AgendaViewHolder holder, int position) {
        if(agendaFilters?.size() > position) {
            holder.bindCard(agendaFilters[position], getItemViewType(position+1))
        }
    }

    @Override
    int getItemCount() {
        agendaFilters?.size() ?: 0
    }

    class AgendaViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView nameLabel
        View bottomBorderView

        IAgendaFilter agendaFilter


        AgendaViewHolder(View itemView) {
            super(itemView)
            nameLabel = (TextView) itemView.findViewById(R.id.nameLabel)
            bottomBorderView = (View) itemView.findViewById(R.id.bottomBorderView)

            itemView.setOnClickListener(this)
        }

        void bindCard(IAgendaFilter agendaFilter, int nextRowType) {
            this.agendaFilter = agendaFilter
            nameLabel?.text = agendaFilter.name
            configureBottomBorderVisibility(nextRowType)
        }

        void configureBottomBorderVisibility(int nextRowType) {
            if(bottomBorderView) {
                bottomBorderView.visibility = (nextRowType == ROW_TYPE_HEADER) ? View.GONE  : View.VISIBLE
            }
        }

        @Override
        void onClick(View v) {
            if(agendaFilter in AgendaFilter) {
                AgendaFilter agendaFilter = (AgendaFilter) this.agendaFilter
                if (adapterDelegate != null) {
                    adapterDelegate
                }
            }
        }
    }
}