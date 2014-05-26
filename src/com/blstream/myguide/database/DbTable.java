
package com.blstream.myguide.database;

/**
 * Created by Piotrek on 2014-05-19.
 */
public class DbTable {
	public final class AnimalTable {
		public static final String TABLE_NAME = "animal";

		public final class Column {
			public static final String ID = "_id";
			public static final String ANIMAL_ID = "animal_id";
			public static final String VISITED = "visited";
		}

		public final class ColumnID {
			public static final int ID = 0;
			public static final int ANIMAL_ID = 1;
			public static final int VISITED = 2;
		}
	}
}
