
package com.blstream.myguide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.blstream.myguide.zoolocations.*;
import android.util.Log;

import java.util.List;

/**
 * Created by Piotrek on 2014-05-19.
 */
public class DbHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "myguide_db";

	private static final String LOG_TAG = "MyGuideDb";

	private SQLiteDatabase mDatabase;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

		mDatabase = getWritableDatabase();
		if (mDatabase == null) {
			Log.d(LOG_TAG, "Database null");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		// table create method
		createAnimalTable(sqLiteDatabase);

		Log.d(LOG_TAG, "Database created.");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbTable.AnimalTable.TABLE_NAME);

		Log.d(LOG_TAG, "Database upgrade.");
		onCreate(sqLiteDatabase);
	}

	private void createAnimalTable(SQLiteDatabase db) {
		String createAnimalTable = String.format("CREATE TABLE %s (" +
				"\"%s\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
				"\"%s\" INTEGER UNIQUE NOT NULL," +
				"\"%s\" TEXT NOT NULL)",
				DbTable.AnimalTable.TABLE_NAME,
				DbTable.AnimalTable.Column.ID,
				DbTable.AnimalTable.Column.ANIMAL_ID,
				DbTable.AnimalTable.Column.VISITED);

		db.execSQL(createAnimalTable);

		Log.d(LOG_TAG, "Category table created.");
	}

	public void insertAnimals(final List<Animal> animals) {
		for (Animal animal : animals) {
			insertAnimal(animal);
		}
		Log.d(LOG_TAG, "All animals inserted to database. " + animals.size());
	}

	public long insertAnimal(final Animal animal) {
		mDatabase.beginTransaction();

		ContentValues values = new ContentValues();
		values.put(DbTable.AnimalTable.Column.ANIMAL_ID, animal.getId());
		values.put(DbTable.AnimalTable.Column.VISITED, "false");

		long result = mDatabase.insertWithOnConflict(DbTable.AnimalTable.TABLE_NAME,
				null, values, SQLiteDatabase.CONFLICT_REPLACE);

		Log.d(LOG_TAG, "inserted animal  " + animal.getId() + ": " + animal.getName());

		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();

		Log.d(LOG_TAG, "Animals inserted to database.");
		return result;
	}

	public void updateVisitedAnimal(int animalId, boolean isVisited) {
		mDatabase.beginTransaction();

		ContentValues values = new ContentValues();

		values.put(DbTable.AnimalTable.Column.VISITED, String.valueOf(isVisited));

		mDatabase.updateWithOnConflict(DbTable.AnimalTable.TABLE_NAME, values,
				DbTable.AnimalTable.Column.ANIMAL_ID + " = " + Integer.toString(animalId), null,
				SQLiteDatabase.CONFLICT_REPLACE);

		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();

		Log.d(LOG_TAG, "Update animal id: " + animalId + " visited: " + isVisited);
	}

	public void resetAllVisitedAnimal() {
		mDatabase.beginTransaction();

		ContentValues values = new ContentValues();

		values.put(DbTable.AnimalTable.Column.VISITED, String.valueOf(false));

		mDatabase.updateWithOnConflict(DbTable.AnimalTable.TABLE_NAME, values, null, null,
				SQLiteDatabase.CONFLICT_REPLACE);

		mDatabase.setTransactionSuccessful();
		mDatabase.endTransaction();

		Log.d(LOG_TAG, "Update all animal to not visited");
	}

	public boolean getIsAnimalVisited(int animalId) {
		boolean isVisited = false;

		Cursor cursor = mDatabase.query(DbTable.AnimalTable.TABLE_NAME, null,
				DbTable.AnimalTable.Column.ANIMAL_ID + " = " + Integer.toString(animalId), null,
				null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					isVisited = Boolean.parseBoolean(cursor
							.getString(DbTable.AnimalTable.ColumnID.VISITED));
				} while (cursor.moveToNext());
			}
		}

		Log.d(LOG_TAG, "Animal is visited: " + isVisited + " " + animalId);

		return isVisited;
	}

}
