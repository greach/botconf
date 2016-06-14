package com.botconf.android.adapters
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.botconf.R
import com.botconf.entities.Sponsor
import groovy.transform.CompileStatic

@CompileStatic
class SponsorsAdapter extends RecyclerView.Adapter {
    private static final String TAG = SponsorsAdapter.simpleName

    private static int ROW_TYPE_HEADER = 1
    private static int ROW_TYPE_SPONSOR = 2

    private Context context

    SponsorsAdapter(Context context) {
        this.context = context
    }

    List<Object> items = []

    void setSponsorsAndCategories(List<String> categories, List<Sponsor> sponsors) {

        for(String category in categories) {
            items << category
            for(Sponsor sponsor in sponsors.findAll { it.category == category }) {
                items << sponsor
            }
        }
        notifyDataSetChanged()
    }

    @Override
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = (viewType == ROW_TYPE_SPONSOR) ? R.layout.sponsor_list_item : R.layout.sponsor_header_list_item
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false)

        (viewType == ROW_TYPE_SPONSOR) ? new SponsorViewHolder(view) : new SponsorCategoryViewHolder(view)
    }

    void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder in SponsorCategoryViewHolder && items[position] in String ) {
            (holder as SponsorCategoryViewHolder).bindCategory((String) items[position])

        } else if(holder in SponsorViewHolder && items[position] in Sponsor ) {
            (holder as SponsorViewHolder).bindSponsor((Sponsor)items[position])
        }
    }

    @Override
    int getItemViewType(int position) {
        def obj = items[position]
        (obj in Sponsor) ? ROW_TYPE_SPONSOR : ROW_TYPE_HEADER
    }


    @Override
    int getItemCount() {
        items?.size()
    }

    class SponsorViewHolder extends RecyclerView.ViewHolder {
        ImageView sponsorImageView


        SponsorViewHolder(View itemView) {
            super(itemView)
            def obj = itemView.findViewById(R.id.sponsorImageView)
            if(obj instanceof ImageView) {
                sponsorImageView = (ImageView)obj
            }

        }

        void bindSponsor(Sponsor sponsor) {
            if(android.os.Build.VERSION.SDK_INT >= 21){
                sponsorImageView?.imageDrawable = context.getResources().getDrawable(sponsor.imageRes, context.getTheme());
            } else {
                sponsorImageView?.imageDrawable = context.getResources().getDrawable(sponsor.imageRes);
            }
        }
    }

    class SponsorCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView sponsorCategoryTextView

        SponsorCategoryViewHolder(View itemView) {
            super(itemView)
            def obj = itemView.findViewById(R.id.sponsorCategoryname)
            if(obj instanceof TextView) {
                sponsorCategoryTextView = (TextView)obj
            }
        }

        void bindCategory(String category) {

            Log.e(TAG, "CATEgory: " + category)
            sponsorCategoryTextView.text = category

        }
    }
}