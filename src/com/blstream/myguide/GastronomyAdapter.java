
package com.blstream.myguide;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blstream.myguide.zoolocations.*;

import java.util.List;

/**
 * Created by Piotrek on 2014-04-07.
 *
 */
public class GastronomyAdapter extends ArrayAdapter<Restaurant> {

	private Activity mContext;
	private List<Restaurant> mRestaurants;
	private int mLayoutResourceId;

	public GastronomyAdapter(Activity context, int layoutResourceId, List<Restaurant> restaurants) {
		super(context, layoutResourceId, restaurants);
		mContext = context;
		mRestaurants = restaurants;
		mLayoutResourceId = layoutResourceId;
	}

	static class ViewHolder {
		public TextView mTxtvRestaurantName;
		public ImageView mImgvRestaurant;
		public TextView mTxtvTimeTo;
		public TextView mTxtvMeter;
		public TextView mTxtvOpenTime;
		public TextView mTxtvMenu;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(mLayoutResourceId, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.mTxtvRestaurantName = (TextView) convertView
					.findViewById(R.id.txtvGastronomyName);
			viewHolder.mImgvRestaurant = (ImageView) convertView.findViewById(R.id.imgvRestaurant);
			viewHolder.mTxtvTimeTo = (TextView) convertView.findViewById(R.id.txtvTimeTo);
			viewHolder.mTxtvMeter = (TextView) convertView
					.findViewById(R.id.txtvGastronomyDistance);
			viewHolder.mTxtvOpenTime = (TextView) convertView.findViewById(R.id.txtvOpen);
			viewHolder.mTxtvMenu = (TextView) convertView.findViewById(R.id.txtvMenu);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Typeface font = Typeface.createFromAsset(mContext.getAssets(),
				BundleConstants.FONT_PATH_BOLD);
		Typeface fontRegular = Typeface.createFromAsset(mContext.getAssets(),
				BundleConstants.FONT_PATH_REGULAR);
		Restaurant restaurant = mRestaurants.get(position);

		viewHolder.mTxtvRestaurantName.setText(restaurant.getName() + "");
		viewHolder.mTxtvRestaurantName.setTypeface(font);
		viewHolder.mTxtvMenu.setText(restaurant.getOpen() + "");
		viewHolder.mTxtvMenu.setTypeface(fontRegular);

		return convertView;
	}
}
