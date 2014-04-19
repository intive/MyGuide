
package com.blstream.myguide;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blstream.myguide.zoolocations.Language;
import com.blstream.myguide.zoolocations.Ticket;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TicketsFragment extends ListFragment
		implements Parcelable {

	private static final String LOG_TAG = TicketsFragment.class.getSimpleName();

	protected static final class ListRowModel {

		private String mTitle;
		private LinkedList<String> mValues;

		public ListRowModel(String title) {
			mTitle = title;
			mValues = new LinkedList<String>();
		}

		@Override
		public String toString() {
			return mTitle;
		}

		public ListRowModel addValue(String value) {
			mValues.add(value);
			return this;
		}

		public String joinValues(String sep) {
			if (sep == null) sep = "";
			StringBuilder sb = new StringBuilder();

			Iterator<String> it = mValues.iterator();
			while (it.hasNext()) {
				String value = it.next();
				sb.append(value);
				if (it.hasNext()) sb.append(sep);
			}

			return sb.toString();
		}

	}

	public static TicketsFragment newInstance() {
		return new TicketsFragment();
	}

	private void prepareFooter(Map<String, String> information) {
		((TextView) mFooterView.findViewById(R.id.txtvTitle)).setText(getResources().getString(
				R.string.ticket_info));
		((TextView) mFooterView.findViewById(R.id.txtvText)).setText(information.get(Language.PL));
	}

	protected List<ListRowModel> processTickets(Collection<? extends Ticket> c) {
		ListRowModel individual = new ListRowModel(getResources().getString(
				R.string.ticket_individual));
		ListRowModel group = new ListRowModel(getResources().getString(R.string.ticket_group));

		ListRowModel tmp = null;
		for (Ticket ticket : c) {
			switch (ticket.getType()) {
				case INDIVIDUAL:
					tmp = individual;
					break;

				case GROUP:
					tmp = group;
					break;

				default:
					tmp = null;
					break;
			}
			tmp.addValue(String.format("%3d zł   %s",
					ticket.getPrice(),
					ticket.getDescription(Language.DEFAULT)));
		}

		return Arrays.asList(individual, group);
	}

	private View mFooterView;

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View rootView = layoutInflater.inflate(R.layout.fragment_information_tickets, viewGroup,
				false);
		mFooterView = layoutInflater.inflate(R.layout.fragment_information_tickets_row, null);

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();

		MyGuideApp app = (MyGuideApp) getActivity().getApplication();
		List<ListRowModel> tickets = processTickets(app.getZooData().getTickets());

		prepareFooter(app.getZooData().getTicketInformation());
		// prior to KITKAT footer and header must have been set before adapter
		getListView().addFooterView(mFooterView);
		setListAdapter(new ArrayAdapter<ListRowModel>(
				getActivity(),
				R.layout.fragment_information_tickets,
				tickets) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					LayoutInflater inflater = getActivity().getLayoutInflater();
					convertView = inflater.inflate(R.layout.fragment_information_tickets_row, null);
				}

				ListRowModel model = getItem(position);
				TextView titleView = (TextView) convertView.findViewById(R.id.txtvTitle);
				TextView valueView = (TextView) convertView.findViewById(R.id.txtvText);
				titleView.setText(model.toString());
				valueView.setText(model.joinValues("\n"));

				return convertView;
			}

		});
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

	}

}
