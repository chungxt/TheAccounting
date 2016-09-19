package com.example.hsin_tingchung.htc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Hello";
    private static final int DATABASE_VERSION = 1;

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Now (N_UID " +
                "integer primary key autoincrement, " +
                "time text no null, H real no null, FH real no null, T real no null, total real no null, Done real no null, save real" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Budget (B_UID " +
                "integer primary key autoincrement, " +
                "name text no null, period real no null, amount real no null" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Spent (UID " +
                "integer primary key autoincrement, " +
                "Time text no null, Money integer no null, Memo text" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Income (UID " +
                "integer primary key autoincrement, " +
                "Time text no null, Money integer no null, Memo text" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Category (C_UID " +
                "integer primary key autoincrement, " +
                "C_Name text no null" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Tag (T_UID " +
                "integer primary key autoincrement, " +
                "C_UID integer, T_name text no null," +
                "FOREIGN KEY(C_UID) REFERENCES Category(C_UID)" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS S_tag (ST_UID " +
                "integer primary key autoincrement, " +
                "S_UID integer no null," +
                "T_UID integer no null," +
                "FOREIGN KEY(S_UID) REFERENCES Spent(S_UID)," +
                "FOREIGN KEY(T_UID) REFERENCES Tag(T_UID)" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS I_tag (IT_UID " +
                "integer primary key autoincrement, " +
                "I_UID integer no null," +
                "T_UID integer no null," +
                "FOREIGN KEY(I_UID) REFERENCES Spent(I_UID)," +
                "FOREIGN KEY(T_UID) REFERENCES Tag(T_UID)" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS B_tag (BT_UID " +
                "integer primary key autoincrement, " +
                "B_UID integer no null," +
                "T_UID integer no null," +
                "FOREIGN KEY(B_UID) REFERENCES Income(B_UID)," +
                "FOREIGN KEY(T_UID) REFERENCES Tag(T_UID)" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Now");
        db.execSQL("DROP TABLE IF EXISTS S_tag");
        db.execSQL("DROP TABLE IF EXISTS I_tag");
        db.execSQL("DROP TABLE IF EXISTS Tag");
        db.execSQL("DROP TABLE IF EXISTS Category");
        db.execSQL("DROP TABLE IF EXISTS Spent");
        db.execSQL("DROP TABLE IF EXISTS Income");
        onCreate(db);
    }
}
