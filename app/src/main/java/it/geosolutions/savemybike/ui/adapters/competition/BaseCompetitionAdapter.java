package it.geosolutions.savemybike.ui.adapters.competition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.model.competition.Competition;
import it.geosolutions.savemybike.model.competition.CompetitionParticipationInfo;

/**
 * Base adapter for competitions
 */
public abstract class BaseCompetitionAdapter<ResultClass> extends ArrayAdapter<ResultClass> {

    protected int resource;

    static class ViewHolder
    {
        @BindView(R.id.item_competition) View view;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.subtitle) TextView subtitle;
        @BindView(R.id.competition_image) ImageView icon;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    public BaseCompetitionAdapter(final Context context, int resourceId, List<ResultClass> results)
    {
		super(context, resourceId, results);

        resource = resourceId;
    }

    public abstract Competition getCompetitionData(ResultClass rc);

	public void onCompetitionSelected(ResultClass bd)
	{
	}

                                                           @NonNull
                                                           @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
		if(position < 0)
			return null;
		if(position >= getCount())
			return null;

		ResultClass rc = getItem(position);

		final Competition competition = (rc != null) ? getCompetitionData(rc) : null;

        ViewHolder holder;

        if (view != null)
        {
            holder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        // setup view

	    final BaseCompetitionAdapter<ResultClass> that = this;

	    view.setOnClickListener(new View.OnClickListener()
	    {
		    @Override
		    public void onClick(View view)
		    {
		    	if(rc != null)
				    that.onCompetitionSelected(rc);
		    }
	    });

	    if(competition != null)
        {
            holder.title.setText(competition.name);
            holder.description.setText(competition.description);

            if(rc instanceof CompetitionParticipationInfo)
            {
            	CompetitionParticipationInfo pi = (CompetitionParticipationInfo)rc;

	            if(pi.registrationStatus.contains("pending"))
		            holder.icon.setImageResource(R.drawable.ic_competition_waiting);
	            else
		            holder.icon.setImageResource(R.drawable.ic_competition_participating);
	        } else {
				holder.icon.setImageResource(R.drawable.ic_competition);
	        }
        }

        return view;
    }


}