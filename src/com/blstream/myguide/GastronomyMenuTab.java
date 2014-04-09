
package com.blstream.myguide;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
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
 *
 */
public class GastronomyMenuTab extends Fragment {

	private ListView mListView;
	private Restaurant mRestaurant;
	private List<Dish> mDishes;
	private DishAdapter mDishAdapter;

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

		mDishes = mRestaurant.getDishes();
		mListView = (ListView) view.findViewById(R.id.lvListItem);
		mDishAdapter = new DishAdapter(getActivity(), R.layout.dish_list_item, mDishes);
		mListView.setAdapter(mDishAdapter);

		return view;
	}

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
			ViewHolder viewHolder = null;

			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(mLayoutResourceId, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.mTxtvDishName = (TextView) convertView.findViewById(R.id.txtvDishName);
				viewHolder.mTxtvDishPrice = (TextView) convertView.findViewById(R.id.txtvDishPrice);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Typeface fontRegular = Typeface.createFromAsset(mContext.getAssets(),
					BundleConstants.FONT_PATH_REGULAR);
			Dish dish = mDishes.get(position);

			viewHolder.mTxtvDishName.setText(dish.getName() + "");
			viewHolder.mTxtvDishName.setTypeface(fontRegular);
			viewHolder.mTxtvDishPrice.setText(dish.getPrice() + "");
			viewHolder.mTxtvDishPrice.setTypeface(fontRegular);

			return convertView;
		}
	}

}
