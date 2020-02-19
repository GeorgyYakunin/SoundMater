package example.meter.sound.soundmeter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "CREATION_DATE";
    public static final String COL_4 = "DURATION";
    public static final String COL_5 = "MIN_DB";
    public static final String COL_6 = "AVG_DB";
    public static final String COL_7 = "MAX_DB";
    public static final String COL_8 = "ENVIRONMENT";
    public static final String DATABASE_NAME = "recording.db";
    public static final String TABLE_NAME = "recording_table";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE recording_table (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, CREATION_DATE TEXT, DURATION TEXT, MIN_DB REAL, AVG_DB REAL, MAX_DB REAL, ENVIRONMENT INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS recording_table");
        onCreate(sQLiteDatabase);
    }

    public boolean insertData(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str);
        contentValues.put(COL_3, str2);
        contentValues.put(COL_4, str3);
        contentValues.put(COL_5, str4);
        contentValues.put(COL_6, str5);
        contentValues.put(COL_7, str6);
        contentValues.put(COL_8, str7);
        return Integer.parseInt(str7) < 12 && Integer.parseInt(str7) >= 0 && writableDatabase.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor getAllData() {
        return getAllDataOrderByColumn(COL_1, -1);
    }

    public Cursor getAllDataOrderByColumn(String str, int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        String str2 = "SELECT * FROM recording_table ORDER BY ";
        StringBuilder stringBuilder;
        if (i >= 1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(str);
            stringBuilder.append(" ASC");
            return writableDatabase.rawQuery(stringBuilder.toString(), null);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append(str);
        stringBuilder.append(" DESC");
        return writableDatabase.rawQuery(stringBuilder.toString(), null);
    }

    public boolean updateData(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str2);
        if (writableDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{str}) == 0) {
            return false;
        }
        return true;
    }

    public int deleteData(String str) {
        return getWritableDatabase().delete(TABLE_NAME, "ID = ?", new String[]{str});
    }
}
