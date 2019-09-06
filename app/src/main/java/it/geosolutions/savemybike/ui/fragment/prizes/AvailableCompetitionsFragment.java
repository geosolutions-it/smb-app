package it.geosolutions.savemybike.ui.fragment.prizes;

import java.util.ArrayList;

import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.data.server.RetrofitClient;
import it.geosolutions.savemybike.data.server.SMBRemoteServices;
import it.geosolutions.savemybike.model.PaginatedResult;
import it.geosolutions.savemybike.model.competition.CompetitionBaseData;
import it.geosolutions.savemybike.model.competition.CompetitionParticipationInfo;
import it.geosolutions.savemybike.ui.adapters.competition.AvailableCompetitionAdapter;
import it.geosolutions.savemybike.ui.adapters.competition.BaseCompetitionAdapter;
import it.geosolutions.savemybike.ui.adapters.competition.CurrentCompetitionAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableCompetitionsFragment extends BaseCompetitionsFragment<CompetitionBaseData>
{
	@Override
	protected BaseCompetitionAdapter<CompetitionBaseData> createAdapter()
	{
		return new AvailableCompetitionAdapter(getActivity(), R.layout.item_competition,new ArrayList<CompetitionBaseData>());
	}

	@Override
	protected int getHeaderTextResourceId()
	{
		return 0;
	}

	@Override
	protected int getEmptyTextResourceId()
	{
		return 0;
	}

	@Override
	protected int getEmptyDescriptionResourceId()
	{
		return 0;
	}

	@Override
	protected void fetchItems()
	{
		RetrofitClient client = RetrofitClient.getInstance(this.getContext());
		SMBRemoteServices portalServices = client.getPortalServices();

		showProgress(true);

		portalServices.getMyCompetitionsAvailable().enqueue(new Callback<PaginatedResult<CompetitionBaseData>>()
		{
			@Override
			public void onResponse(Call<PaginatedResult<CompetitionBaseData>> call, Response<PaginatedResult<CompetitionBaseData>> response)
			{
				showProgress(false);
				PaginatedResult<CompetitionBaseData> result = response.body();
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
			public void onFailure(Call<PaginatedResult<CompetitionBaseData>> call, Throwable t)
			{
				showProgress(false);
				showEmpty(true, true);
				adapter.clear();
				adapter.notifyDataSetChanged();
			}
		});
	}

}
