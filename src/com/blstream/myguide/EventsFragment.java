
package com.blstream.myguide;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blstream.myguide.zoolocations.Event;
import com.blstream.myguide.zoolocations.ZooLocationsData;

/**
 * Class designed for displaying Events parsed from data.xml
 * 
 * @author kz
 */

public class EventsFragment extends Fragment {

	private ListView mListView;
	private ZooLocationsData mZooData;
	private List<Event> mEvents;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_view, container, false);

		setHasOptionsMenu(true);
		setActionBar();

		mZooData = ((MyGuideApp) this.getActivity().getApplication()).getZooData();
		mEvents = mZooData.getEvent();

		if (view != null) {
			mListView = (ListView) view.findViewById(R.id.lvListItem);

			// sorting events by starting time
			Collections.sort(mEvents, new Comparator<Event>() {
				public int compare(Event e1, Event e2) {
					String[] e1split = e1.getTime().split(":");
					String[] e2split = e2.getTime().split(":");
					int e1hour = Integer.parseInt(e1split[0]);
					int e2hour = Integer.parseInt(e2split[0]);

					if (e1hour == e2hour) {
						return e1split[1].compareTo(e2split[1]);
					} else

					return ((Integer) e1hour).compareTo(e2hour);
				}
			});

			EventsAdapter adapter = new EventsAdapter();
			mListView.setAdapter(adapter);
		}

		return view;
	}

	private void setActionBar() {
		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle(getString(R.string.events_list_title));
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

	private class EventsAdapter extends BaseAdapter {

		private static final int TYPE_LEFT = 0;
		private static final int TYPE_RIGHT = 1;
		private LayoutInflater mInflater;

		public EventsAdapter() {
			mInflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
			int type = getItemViewType(position);

			if (convertView == null) {
				viewHolder = new ViewHolder();

				switch (type) {
					case TYPE_LEFT:
						convertView = mInflater.inflate(R.layout.event_list_item_left, null);
						break;
					case TYPE_RIGHT:
						convertView = mInflater.inflate(R.layout.event_list_item_right, null);
						break;
				}

				viewHolder.mEventName = (TextView) convertView.findViewById(R.id.txtEventName);
				viewHolder.mTime = (TextView) convertView.findViewById(R.id.txtTime);
				viewHolder.mWeekendsLabel = (TextView) convertView
						.findViewById(R.id.txtWeekendsLabel);
				viewHolder.mWeekends = (TextView) convertView.findViewById(R.id.txtWeekends);
				viewHolder.mHolidaysLabel = (TextView) convertView
						.findViewById(R.id.txtHolidaysLabel);
				viewHolder.mHolidays = (TextView) convertView.findViewById(R.id.txtHolidays);
				viewHolder.mDateLabel = (TextView) convertView.findViewById(R.id.txtDateLabel);
				viewHolder.mDate = (TextView) convertView.findViewById(R.id.txtDate);
				viewHolder.mImageEvent = (ImageView) convertView.findViewById(R.id.imgEvent);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Event event = mEvents.get(position);

			viewHolder.mEventName.setText(event.getName(Locale.getDefault().getLanguage()));
			viewHolder.mTime.setText(event.getTime().replaceAll(";", " "));
			String[] name = event.getImagePath().substring(4).split("\\.");
			int id = getResources().getIdentifier(name[0], "drawable",
					getActivity().getPackageName());
			viewHolder.mImageEvent.setImageResource(id);

			if (event.getTimeWeekends() != null) {
				viewHolder.mWeekends.setText(event.getTimeWeekends().replaceAll(";", " "));
				viewHolder.mWeekends.setVisibility(View.VISIBLE);
				viewHolder.mWeekendsLabel.setVisibility(View.VISIBLE);
			} else {
				viewHolder.mWeekends.setVisibility(View.GONE);
				viewHolder.mWeekendsLabel.setVisibility(View.GONE);
			}

			if (event.getTimeHolidays() != null) {
				viewHolder.mHolidays.setText(event.getTimeHolidays().replaceAll(";", " "));
				viewHolder.mHolidays.setVisibility(View.VISIBLE);
				viewHolder.mHolidaysLabel.setVisibility(View.VISIBLE);
			} else {
				viewHolder.mHolidays.setVisibility(View.GONE);
				viewHolder.mHolidaysLabel.setVisibility(View.GONE);
			}

			if (event.getStartDate() != null) {
				viewHolder.mDate.setText(event.getStartDate().replaceAll(";", " "));
				viewHolder.mDate.setVisibility(View.VISIBLE);
				viewHolder.mDateLabel.setVisibility(View.VISIBLE);
			} else {
				viewHolder.mDate.setVisibility(View.GONE);
				viewHolder.mDateLabel.setVisibility(View.GONE);
			}

			return convertView;
		}

		private class ViewHolder {
			public TextView mEventName;
			public TextView mTime;
			public TextView mHolidays;
			public TextView mHolidaysLabel;
			public TextView mWeekendsLabel;
			public TextView mWeekends;
			public TextView mDateLabel;
			public TextView mDate;
			public ImageView mImageEvent;
		}

		@Override
		public int getCount() {
			return mEvents.size();
		}

		@Override
		public Event getItem(int position) {
			return mEvents.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return mEvents.indexOf(getItem(position)) % 2 == 0 ? TYPE_LEFT : TYPE_RIGHT;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

	}

}
