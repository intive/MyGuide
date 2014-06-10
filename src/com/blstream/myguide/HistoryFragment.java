package com.blstream.myguide;

import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blstream.myguide.zoolocations.History;
import com.blstream.myguide.zoolocations.ZooLocationsData;

/**
 * Class designed for displaying History information parsed from data.xml
 * 
 * @author kz
 */

public class HistoryFragment extends Fragment {

	private ListView mListView;
	private ZooLocationsData mZooData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_view, container, false);

		setHasOptionsMenu(true);
		setActionBar();

		mZooData = ((MyGuideApp) this.getActivity().getApplication()).getZooData();
		List<History> history = mZooData.getHistory();

		if (view != null) {
			mListView = (ListView) view.findViewById(R.id.lvListItem);
			mListView.setDivider(null);

			HistoryAdapter adapter = new HistoryAdapter(getActivity(),
					R.layout.history_left, R.layout.history_right, history);
			mListView.setAdapter(adapter);
		}
		
		return view;
	}

	private void setActionBar() {
		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle(getString(R.string.history_list_title));
			getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem itemSearch = menu.findItem(R.id.action_search);
		MenuItem itemFilter = menu.findItem(R.id.action_filter);

		if (itemSearch != null) itemSearch.setVisible(false);
		if (itemFilter != null) itemFilter.setVisible(false);

		super.onCreateOptionsMenu(menu, inflater);
	}

	private class HistoryAdapter extends ArrayAdapter<History> {

		private Activity mContext;
		private List<History> mHistory;
		private int mLayoutLeft;
		private int mLayoutRight;
		private static final int TYPE_LEFT = 0;
		private static final int TYPE_RIGHT = 1;

		public HistoryAdapter(Activity context, int layoutLeft, int layoutRight,
				List<History> history) {
			super(context, layoutLeft, layoutRight, history);
			mContext = context;
			mHistory = history;
			mLayoutLeft = layoutLeft;
			mLayoutRight = layoutRight;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int items = mHistory.size();
			int maxBackgroungAlpha = 235;
			int minBackgroundAlpha = 120;
			int alphaDecreaseRate = (maxBackgroungAlpha - minBackgroundAlpha) / items;

			LayoutInflater inflater;
			ViewHolder viewHolder;
			int type = getItemViewType(position);

			if (convertView == null) {
				viewHolder = new ViewHolder();

				switch (type) {
					case TYPE_LEFT:
						inflater = mContext.getLayoutInflater();
						convertView = inflater.inflate(mLayoutLeft, parent, false);
						break;
					case TYPE_RIGHT:
						inflater = mContext.getLayoutInflater();
						convertView = inflater.inflate(mLayoutRight, parent, false);
						break;
				}

				viewHolder.mDate = (TextView)
						convertView.findViewById(R.id.txtDate);
				viewHolder.mInformation = (TextView)
						convertView.findViewById(R.id.txtInformation);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			History history = mHistory.get(position);

			viewHolder.mInformation.getBackground().setAlpha(
					maxBackgroungAlpha - (mHistory.indexOf(history) + 1) * alphaDecreaseRate);

			viewHolder.mDate.setText(history.getDate());
			viewHolder.mInformation.setText(history.getInformation(Locale.getDefault().getLanguage()));

			return convertView;
		}

		private class ViewHolder {
			public TextView mDate;
			public TextView mInformation;
		}

		public int getItemViewType(int position) {
			return mHistory.indexOf(getItem(position)) % 2 == 0 ? TYPE_LEFT : TYPE_RIGHT;
		}

		public int getViewTypeCount() {
			return 2;
		}

	}

}