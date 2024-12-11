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
    private static final int DATABASE_VERSION = 3;
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

    // Training Plans Table
    public static final String TRAINING_PLANS_TABLE_NAME = "training_plans";
    public static final String TRAINING_KEY_ID = "training_id";
    public static final String TRAINING_KEY_DATE = "training_date";
    public static final String TRAINING_KEY_DISTANCE = "training_distance";
    public static final String TRAINING_KEY_PACE = "training_pace";
    public static final String TRAINING_KEY_TYPE = "training_type";
    public static final String TRAINING_KEY_DURATION = "training_estimated_duration";
    public static final String TRAINING_KEY_NOTES = "training_notes";

    private static final String CREATE_TRAINING_PLANS_TABLE_SQL = "CREATE TABLE " + TRAINING_PLANS_TABLE_NAME +
            " (" + TRAINING_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TRAINING_KEY_DATE + " TEXT, "
            + TRAINING_KEY_DISTANCE + " TEXT, "
            + TRAINING_KEY_PACE + " TEXT, "
            + TRAINING_KEY_TYPE + " TEXT, "
            + TRAINING_KEY_DURATION + " TEXT, "
            + TRAINING_KEY_NOTES + " TEXT);";

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

    // Method to add a new training plan
    public void addTrainingPlan(Context context, TrainingPlan plan) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRAINING_KEY_DATE, plan.getTrainingDate());
        values.put(TRAINING_KEY_DISTANCE, plan.getDistance());
        values.put(TRAINING_KEY_PACE, plan.getPace());
        values.put(TRAINING_KEY_TYPE, plan.getTrainingType());
        values.put(TRAINING_KEY_DURATION, plan.getEstimatedDuration());
        values.put(TRAINING_KEY_NOTES, plan.getNotes());
        database.insert(TRAINING_PLANS_TABLE_NAME, null, values);
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

    // Method to load all training plans
    public List<TrainingPlan> loadAllTrainingPlans(Context context) {
        List<TrainingPlan> plans = new LinkedList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{TRAINING_KEY_ID, TRAINING_KEY_DATE, TRAINING_KEY_DISTANCE,
                TRAINING_KEY_PACE, TRAINING_KEY_TYPE, TRAINING_KEY_DURATION, TRAINING_KEY_NOTES};
        Cursor cursor = database.query(TRAINING_PLANS_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                TrainingPlan plan = new TrainingPlan(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6));
                plans.add(plan);
            } while (cursor.moveToNext());
        }
        database.close();
        return plans;
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

    public void addMockTrainingPlans(Context context) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Mock Training Plan 1
        values.put(TRAINING_KEY_DATE, "2024-11-28");
        values.put(TRAINING_KEY_DISTANCE, "5");
        values.put(TRAINING_KEY_PACE, "6:00");
        values.put(TRAINING_KEY_TYPE, "Easy Run");
        values.put(TRAINING_KEY_DURATION, "30 mins");
        values.put(TRAINING_KEY_NOTES, "Keep a steady, comfortable pace.");
        database.insert(TRAINING_PLANS_TABLE_NAME, null, values);

        // Mock Training Plan 2
        values.clear();
        values.put(TRAINING_KEY_DATE, "2024-11-29");
        values.put(TRAINING_KEY_DISTANCE, "10");
        values.put(TRAINING_KEY_PACE, "5:30");
        values.put(TRAINING_KEY_TYPE, "Long Run");
        values.put(TRAINING_KEY_DURATION, "1 hour");
        values.put(TRAINING_KEY_NOTES, "Focus on endurance, keep a consistent pace.");
        database.insert(TRAINING_PLANS_TABLE_NAME, null, values);

        database.close();
    }



    public void deleteRecords(Context context){
        DataBaseHelper databaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int numberDeletedRecords = 0;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
        sqLiteDatabase.execSQL(CREATE_TRAINING_PLANS_TABLE_SQL);
        Log.d("DatabaseHelper", "Tables created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRAINING_PLANS_TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
