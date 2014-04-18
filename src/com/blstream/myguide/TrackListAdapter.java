
package com.blstream.myguide;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blstream.myguide.zoolocations.Track;

public class TrackListAdapter extends ArrayAdapter<Track> {

	private Context mContext;
	private int mId;

	public TrackListAdapter(Context context, int textViewResourceId, ArrayList<Track> items) {
		super(context, textViewResourceId, items);
		this.mContext = context;
		this.mId = textViewResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(mId, parent, false);

		TextView name = (TextView) rowView.findViewById(R.id.trackName);
		TextView progressText = (TextView) rowView.findViewById(R.id.progressText);
		ProgressBar progress = (ProgressBar) rowView.findViewById(R.id.progressBar);

		// TODO set real progress
		name.setText(getItem(position).getName(mContext.getString(R.string.language)));
		progressText.setText(1 + "/" + getItem(position).getAnimals().size());
		progress.setProgress(1);
		progress.setMax(getItem(position).getAnimals().size());

		return rowView;
	}
}
