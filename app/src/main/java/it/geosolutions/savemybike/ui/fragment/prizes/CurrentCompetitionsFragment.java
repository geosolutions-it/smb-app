package it.geosolutions.savemybike.ui.fragment.prizes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.data.server.RetrofitClient;
import it.geosolutions.savemybike.data.server.SMBRemoteServices;
import it.geosolutions.savemybike.model.PaginatedResult;
import it.geosolutions.savemybike.model.competition.Competition;
import it.geosolutions.savemybike.model.competition.CompetitionParticipationInfo;
import it.geosolutions.savemybike.ui.activity.SaveMyBikeActivity;
import it.geosolutions.savemybike.ui.adapters.competition.BaseCompetitionAdapter;
import it.geosolutions.savemybike.ui.adapters.competition.CurrentCompetitionAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Lorenzo Natali, GeoSolutions S.a.s.
 * Fragment for current competitions list
 */

public class CurrentCompetitionsFragment extends BaseCompetitionsFragment<CompetitionParticipationInfo>
{
	@Override
	protected int getEmptyTextResourceId()
	{
		return R.string.no_competition_currently_active_title;
	}

	@Override
	protected int getEmptyDescriptionResourceId()
	{
		return R.string.no_competition_currently_active_description;
	}

	protected BaseCompetitionAdapter<CompetitionParticipationInfo> createAdapter()
	{
		return new CurrentCompetitionAdapter(getActivity(),R.layout.item_competition,new ArrayList<CompetitionParticipationInfo>());
	}

	protected int getHeaderTextResourceId()
	{
		return R.string.up_to_grab;
	}

	@Override
	protected void fetchItems()
	{
        RetrofitClient client = RetrofitClient.getInstance(this.getContext());
        SMBRemoteServices portalServices = client.getPortalServices();

        showProgress(true);

        portalServices.getMyCompetitionsCurrent().enqueue(new Callback<PaginatedResult<CompetitionParticipationInfo>>()
        {
            @Override
            public void onResponse(Call<PaginatedResult<CompetitionParticipationInfo>> call, Response<PaginatedResult<CompetitionParticipationInfo>> response)
            {
                showProgress(false);
                PaginatedResult<CompetitionParticipationInfo> result = response.body();
                if(result != null && result.getResults() != null)
                {
                    adapter.clear();
                    adapter.addAll(response.body().getResults());
                    showEmpty(response.body().getResults().size() == 0, false);
                } else {
                    adapter.clear();
                    adapter.addAll(new ArrayList<>());
                    showEmpty(true, false);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PaginatedResult<CompetitionParticipationInfo>> call, Throwable t)
            {
                showProgress(false);
                showEmpty(true, true);
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


}
