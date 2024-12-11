package com.example.myapplication.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bestRunner";

    private static DataBaseHelper instance = null;

    public static final String TABLE_NAME = "running_records";
    public static final String KEY_ID = "id";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_DAY = "day";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";
    public static final String KEY_PLACE = "place";
    public static final String KEY_TRAININGDURATION = "training_duration";
    public static final String KEY_CALORIES = "calories";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_AVESPEED = "average_speed";
    public static final String KEY_DETAILKMS = "detail_kms";
    public static final String KEY_MAPINFO = "map_info";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TIMESTAMP + " TEXT, " + KEY_DAY + " TEXT, " + KEY_MONTH + " TEXT, " + KEY_YEAR + " TEXT, " + KEY_PLACE + " TEXT, " + KEY_TRAININGDURATION + " TEXT, " + KEY_CALORIES + " TEXT, " + KEY_DISTANCE + " TEXT, " + KEY_AVESPEED + " TEXT, " + KEY_DETAILKMS + " TEXT, " + KEY_MAPINFO + " TEXT);";

    private DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DataBaseHelper getInstance(Context context){
        if (instance == null){
            instance = new DataBaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // add
    public void addRecord(Context context, RunningRecord record){
        DataBaseHelper databaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMESTAMP, record.getTimestamp());
        values.put(KEY_DAY, record.getDay());
        values.put(KEY_MONTH, record.getMonth());
        values.put(KEY_YEAR, record.getYear());
        values.put(KEY_PLACE, record.getPlace());
        values.put(KEY_TRAININGDURATION, record.getTrainingDuration());
        values.put(KEY_CALORIES, record.getCalories());
        values.put(KEY_DISTANCE, record.getDistance());
        values.put(KEY_AVESPEED, record.getAverageSpeed());
        values.put(KEY_DETAILKMS, record.getDetailKms());
        values.put(KEY_MAPINFO, record.getMapInfo());
        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public void addRecord(Context context, String timestamp, String day, String month, String year, String place, String trainingDuration, String calories, String distance, String averageSpeed, String detailKms, String mapInfo){
        DataBaseHelper databaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMESTAMP, timestamp);
        values.put(KEY_DAY, day);
        values.put(KEY_MONTH, month);
        values.put(KEY_YEAR, year);
        values.put(KEY_PLACE, place);
        values.put(KEY_TRAININGDURATION, trainingDuration);
        values.put(KEY_CALORIES, calories);
        values.put(KEY_DISTANCE, distance);
        values.put(KEY_AVESPEED, averageSpeed);
        values.put(KEY_DETAILKMS, detailKms);
        values.put(KEY_MAPINFO, mapInfo);
        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public RunningRecord loadRecordByid(Context context, int id) {
        RunningRecord record = null;
        DataBaseHelper databaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] columns = new String[] {
                KEY_ID, KEY_TIMESTAMP, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_PLACE,
                KEY_TRAININGDURATION, KEY_CALORIES, KEY_DISTANCE, KEY_AVESPEED,
                KEY_DETAILKMS, KEY_MAPINFO
        };
        String where = KEY_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = database.query(TABLE_NAME, columns, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            record = new RunningRecord(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10), cursor.getString(11)
            );
        }
        database.close();
        return record;
    }

    public List<RunningRecord> loadRecordsByTimestamp(Context context, String from, String to) {
        List<RunningRecord> records = new LinkedList<>();
        DataBaseHelper databaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String [] columns = new String [] {KEY_ID,KEY_TIMESTAMP, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_PLACE, KEY_TRAININGDURATION, KEY_CALORIES, KEY_DISTANCE, KEY_AVESPEED, KEY_DETAILKMS, KEY_MAPINFO};
        String where = KEY_TIMESTAMP + " BETWEEN ? AND ?";
        String [] whereArgs = {from, to};
        Cursor cursor = database.query(TABLE_NAME, columns, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()){
            do {
                RunningRecord record = new RunningRecord(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11)
                );
                records.add(record);
            } while (cursor.moveToNext());
        }
        database.close();
        return records;
    }

    public List<RunningRecord> loadRecordsByTimestamp(Context context, String from) {
        String to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return loadRecordsByTimestamp(context, from, to);
    }

    public List<RunningRecord> loadAllRecords(Context context){
        List<RunningRecord> records = new LinkedList<>();
        DataBaseHelper databaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String [] columns = new String [] {KEY_ID, KEY_TIMESTAMP, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_PLACE, KEY_TRAININGDURATION, KEY_CALORIES, KEY_DISTANCE, KEY_AVESPEED, KEY_DETAILKMS, KEY_MAPINFO};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                RunningRecord record = new RunningRecord(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11)
                );
                records.add(record);
            } while (cursor.moveToNext());
        }
        database.close();
        return records;
    }


    public void deleteRecords(Context context){
        DataBaseHelper databaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int numberDeletedRecords = 0;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
