
package com.blstream.myguide.database;

import android.test.AndroidTestCase;

import com.blstream.myguide.zoolocations.Animal;

/**
 * Created by Piotrek on 2014-05-19.
 */
public class DbInsertAnimalTest extends AndroidTestCase {

	public void testInsertAnimal() {
		DbDataManager dbManager = DbDataManager.getInstance(mContext);
		Animal animal = new Animal();

		animal.setId(1000);
		dbManager.insertAnimalToDb(new DbDataManager.OnAnimalInsertListener() {
			@Override
			public void onAnimalInsert(long animalId) {
				assertTrue(animalId >= 0);
			}
		}, animal);
	}
}
