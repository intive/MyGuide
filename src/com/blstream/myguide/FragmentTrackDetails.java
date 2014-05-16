
package com.blstream.myguide;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.zoolocations.*;

/**
 * Created by Piotrek on 2014-04-19. This class show chosen track
 * details(description,image, animal on track and progress)
 */
public class FragmentTrackDetails extends Fragment {

	private String mTitle;
	private Track mTrack;
	private View mRootView;

	public static FragmentTrackDetails newInstance(Track track) {
		FragmentTrackDetails fragmentTrack = new FragmentTrackDetails();

		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleConstants.SELECTED_TRACK, track);
		fragmentTrack.setArguments(bundle);

		return fragmentTrack;
	}

	private void getArgs() {
		Bundle args = getArguments();
		if (args != null) {
			mTrack = (Track) args.getSerializable(BundleConstants.SELECTED_TRACK);
			mTitle = mTrack.getName();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getArgs();
		mRootView = inflater.inflate(R.layout.fragment_track_details, container,
				false);

		setHasOptionsMenu(true);
		setActionBar();
		setUpView();

		return mRootView;
	}

	private void setActionBar() {
		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle(mTitle);
			getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}

	private void setUpView() {
		ImageView imgvTrackDetails = (ImageView) mRootView.findViewById(R.id.imgvTrackDetails);
		TextView txtvTrackProgress = (TextView) mRootView.findViewById(R.id.txtvTrackProgress);
		ProgressBar pbTrackDetails = (ProgressBar) mRootView.findViewById(R.id.pbTrackDetails);
		TextView txtvTrackDescription = (TextView) mRootView
				.findViewById(R.id.txtvTrackDetailsDescription);

		imgvTrackDetails.setImageResource(getResources().getIdentifier(
				mTrack.getImage().substring(4), "drawable", getActivity().getPackageName()));
		txtvTrackProgress.setText(1 + "/" + mTrack.getAnimals().size());
		pbTrackDetails.setProgress(1);
		pbTrackDetails.setMax(mTrack.getAnimals().size());
		txtvTrackDescription.setText(mTrack.getDescription() + "");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem itemSearch = menu.findItem(R.id.action_search);
		MenuItem itemFilter = menu.findItem(R.id.action_filter);
		MenuItem itemStart = menu.add(getString(R.string.start_track));

		if (itemSearch != null) itemSearch.setVisible(false);
		if (itemFilter != null) itemFilter.setVisible(false);

		itemStart.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		// Click START and show Map with animals road
		itemStart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				StartActivity.TRACKING_MODE = true;
				StartActivity.setExploredTrack(mTrack);
				FragmentHelper.swapFragment(R.id.flFragmentHolder,
						SightseeingFragment.newInstance(mTrack),
						getActivity().getSupportFragmentManager(),
						BundleConstants.FRAGMENT_SIGHTSEEING);
				return true;
			}
		});

		super.onCreateOptionsMenu(menu, inflater);
	}
}
