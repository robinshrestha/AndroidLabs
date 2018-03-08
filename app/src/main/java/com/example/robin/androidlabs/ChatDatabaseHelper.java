package com.example.robin.androidlabs;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Robin on 3/7/2018.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Messages.db";

    public static final int VERSION_NUM = 4;

    public static final String TABLE_NAME = "myTable";

    public static final String KEY_ID = "id";

    public static final String KEY_MESSAGE = "Message";



    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);

    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL( "CREATE TABLE " + TABLE_NAME + "( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_MESSAGE+" text);");

        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    public void onUpgrade (SQLiteDatabase db, int oldVer, int newVer){

        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME+";");

        onCreate(db);

        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
    }
    public void onOpen(SQLiteDatabase db)
    {
        Log.i("Database", "Database opened");
    }
}

