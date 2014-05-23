
package com.blstream.myguide.database;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.blstream.myguide.zoolocations.*;

/**
 * Created by Piotrek on 2014-05-19. Use of DbDataManager private DbDataManager
 * mDbManager = DbDataManager.getInstance(mContext);
 */
public class DbDataManager {

	public interface OnCheckVisitAnimalListener {
		void onCheckLoaded(boolean isVisited);
	}

	public interface OnAnimalInsertListener {
		void onAnimalInsert(long animalId);
	}

	private static DbDataManager sDataManager;

	private DbHelper mDbHelper;
	private Executor executor;

	private DbDataManager(Context context) {
		mDbHelper = new DbHelper(context);
		executor = Executors.newFixedThreadPool(5);
	}

	public static DbDataManager getInstance(Context context) {
		if (sDataManager == null) {
			sDataManager = new DbDataManager(context);
		}
		return sDataManager;
	}

	public void insertAnimalsListToDb(final List<Animal> animals) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				mDbHelper.insertAnimals(animals);
			}
		};
		executor.execute(runnable);
	}

	public void insertAnimalToDb(final OnAnimalInsertListener listener, final Animal animal) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				if (listener != null) {
					listener.onAnimalInsert(mDbHelper.insertAnimal(animal));
				}
			}
		};
		executor.execute(runnable);
	}

	public void updateAnimalInDb(final int animalId, final boolean isVisited) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				mDbHelper.updateVisitedAnimal(animalId, isVisited);
			}
		};
		executor.execute(runnable);
	}

	public void resetAllVistedAnimals() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				mDbHelper.resetAllVisitedAnimal();
			}
		};
		executor.execute(runnable);
	}

	public void checkVisitAnimal(final OnCheckVisitAnimalListener listener, final int animalId) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				if (listener != null) {
					listener.onCheckLoaded(mDbHelper.getVisitAnimal(animalId));
				}
			}
		};
		executor.execute(runnable);
	}
}
