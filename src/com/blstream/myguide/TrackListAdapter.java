
package com.blstream.myguide;

import java.util.ArrayList;
import java.util.Locale;

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
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(mId, parent, false);
		}
		TextView name = (TextView) convertView.findViewById(R.id.txtvTrackName);
		TextView progressText = (TextView) convertView.findViewById(R.id.txtvProgressText);
		ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.pbProgressBar);

		// TODO set real progress
		name.setText(getItem(position).getName(Locale.getDefault().getLanguage()));
		progressText.setText(1 + "/" + getItem(position).getAnimals().size());
		progress.setProgress(1);
		progress.setMax(getItem(position).getAnimals().size());

		return convertView;
	}
}
