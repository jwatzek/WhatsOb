package com.juliawatzek.whatsob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class NotebookDbAdapter {

    private static final String DATABASE_NAME = "whatsob.db";
    private static final int DATABASE_VERSION = 3;

    public static final String NOTE_TABLE = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_OBSERVER = "observer";
    public static final String COLUMN_ESTROUS = "estrous";
    public static final String COLUMN_COMMENTS = "comments";
    public static final String COLUMN_WAS_FED = "wasFed";
    public static final String COLUMN_HAD_FOOD_IN_ENCLOSURE = "hadFoodInEnclosure";

    private String[] allColumns = { COLUMN_ID, COLUMN_TITLE, COLUMN_MESSAGE, COLUMN_CATEGORY,
            COLUMN_OBSERVER, COLUMN_ESTROUS, COLUMN_COMMENTS, COLUMN_WAS_FED, COLUMN_HAD_FOOD_IN_ENCLOSURE};

    // SQL snippet
    public static final String CREATE_TABLE_NOTE = "create table " + NOTE_TABLE + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_MESSAGE + " text not null, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_OBSERVER + " text not null, "
            + COLUMN_ESTROUS + " text not null, "
            + COLUMN_COMMENTS + " text not null, "
            + COLUMN_WAS_FED + " integer not null, "
            + COLUMN_HAD_FOOD_IN_ENCLOSURE + " integer not null "
            + " );";

    private SQLiteDatabase sqlDB;
    private Context context;

    private NotebookDBHelper notebookDBHelper;

    public NotebookDbAdapter(Context ctx) {
        context = ctx;
    }

    public NotebookDbAdapter open() throws android.database.SQLException {
        notebookDBHelper = new NotebookDBHelper(context);
        sqlDB = notebookDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        notebookDBHelper.close();
    }

    public Note createNote(String title, String message, Note.Category category, String observer,
                           String estrous, String comments, boolean wasFed, boolean hadFoodInEnclosure) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_CATEGORY, category.name());
        values.put(COLUMN_OBSERVER, observer);
        values.put(COLUMN_ESTROUS, estrous);
        values.put(COLUMN_COMMENTS, comments);
        values.put(COLUMN_WAS_FED, wasFed);
        values.put(COLUMN_HAD_FOOD_IN_ENCLOSURE, hadFoodInEnclosure);

        // insert the note we just created
        long insertId = sqlDB.insert(NOTE_TABLE, null, values);

        // to also *return* the note we just created
        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns,
                COLUMN_ID + " = " + insertId, null, null, null, null);

        // move to first row *of those that were returned* (only one returned here)
        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
    }

    public long deleteNote(long idToDelete) {
        // delete the note; return number of deleted/affected rows (should be 1 in our case)
        return sqlDB.delete(NOTE_TABLE, COLUMN_ID + " = " + idToDelete, null);
    }

    public long updateNote(long idToUpdate, String newTitle, String newMessage, Note.Category newCategory,
                           String observer, String estrous, String comments, boolean wasFed, boolean hadFoodInEnclosure) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        values.put(COLUMN_MESSAGE, newMessage);
        values.put(COLUMN_CATEGORY, newCategory.name());
        values.put(COLUMN_OBSERVER, observer);
        values.put(COLUMN_ESTROUS, estrous);
        values.put(COLUMN_COMMENTS, comments);
        values.put(COLUMN_WAS_FED, wasFed? 1 : 0);
        values.put(COLUMN_HAD_FOOD_IN_ENCLOSURE, hadFoodInEnclosure? 1 : 0);

        // look in note table where col id is same as id to update
        // when you find it, update with the values I gave you
        // then return how many rows this affected (should only ever be one in our case)
        return sqlDB.update(NOTE_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        // grab all of the info in our database for the notes in it
        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns, null, null, null, null, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            Note note = cursorToNote(cursor);
            notes.add(note);
        }

        cursor.close();
        return notes;
    }

    private Note cursorToNote (Cursor cursor) {
         Note newNote = new Note(cursor.getString(1), cursor.getString(2),
                Note.Category.valueOf(cursor.getString(3)), cursor.getLong(0), cursor.getString(4),
                 cursor.getString(5), cursor.getString(6), cursor.getInt(7) == 1, cursor.getInt(8) == 1);

        return newNote;
    }


    private static class NotebookDBHelper extends SQLiteOpenHelper {

        NotebookDBHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create note table
            db.execSQL(CREATE_TABLE_NOTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(NotebookDBHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to " + newVersion +
                    ", which will destroy all old data");

            // destroy data
            db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
            onCreate(db);
        }
    }

}
