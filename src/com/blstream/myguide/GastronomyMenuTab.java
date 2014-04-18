
package com.blstream.myguide;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blstream.myguide.zoolocations.Dish;
import com.blstream.myguide.zoolocations.Restaurant;

import java.util.List;

/**
 * Created by Piotrek on 2014-04-09.
 * This fragment show Restaurant Menu.
 */
public class GastronomyMenuTab extends Fragment implements Parcelable {

	private Restaurant mRestaurant;

	public static GastronomyMenuTab newInstance(Restaurant restaurant) {
		GastronomyMenuTab fragmentTab = new GastronomyMenuTab();

		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleConstants.SELECTED_RESTAURANT, restaurant);
		fragmentTab.setArguments(bundle);

		return fragmentTab;
	}

	private void getArgs() {
		Bundle args = getArguments();
		if (args != null) {
			mRestaurant = (Restaurant) args.getSerializable(BundleConstants.SELECTED_RESTAURANT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getArgs();
		View view = inflater.inflate(R.layout.fragment_list_view, container,
				false);

		ListView lvDishes;
		List<Dish> dishes = mRestaurant.getDishes();

		if (view != null) {
			lvDishes = (ListView) view.findViewById(R.id.lvListItem);
			DishAdapter dishAdapter = new DishAdapter(getActivity(), R.layout.dish_list_item,
					dishes);
			lvDishes.setAdapter(dishAdapter);
		}

		return view;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

	}

	/**
	 * This Adapter is used to show Restaurant Menu. Every label has dish name
	 * and price.
	 */
	private class DishAdapter extends ArrayAdapter<Dish> {

		private Activity mContext;
		private List<Dish> mDishes;
		private int mLayoutResourceId;

		public DishAdapter(Activity context, int layoutResourceId, List<Dish> restaurants) {
			super(context, layoutResourceId, restaurants);
			mContext = context;
			mDishes = restaurants;
			mLayoutResourceId = layoutResourceId;
		}

		class ViewHolder {
			public TextView mTxtvDishName;
			public TextView mTxtvDishPrice;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;

			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(mLayoutResourceId, parent, false);

				viewHolder = new ViewHolder();
				if (convertView != null) {
					viewHolder.mTxtvDishName = (TextView) convertView
							.findViewById(R.id.txtvDishName);
					viewHolder.mTxtvDishPrice = (TextView) convertView
							.findViewById(R.id.txtvDishPrice);
					convertView.setTag(viewHolder);
				}
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Dish dish = mDishes.get(position);

			viewHolder.mTxtvDishName.setText(dish.getName() + "");
			viewHolder.mTxtvDishPrice.setText(dish.getPrice() + "");

			return convertView;
		}
	}

}
