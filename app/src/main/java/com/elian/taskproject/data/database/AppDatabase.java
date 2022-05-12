package com.elian.taskproject.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.elian.taskproject.data.dao.TaskDAO;
import com.elian.taskproject.data.dao.UserDAO;
import com.elian.taskproject.data.model.Task;
import com.elian.taskproject.data.model.User;

// 1. Define the configuration of the database.

@Database(version = 3, entities = { Task.class, User.class }, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String databaseName = "app_database";

    // 2. Create the methods to get the DAO.
    public abstract TaskDAO getTaskDAO();
    public abstract UserDAO getUserDAO();

    private static volatile AppDatabase INSTANCE;


    // Google version method
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, databaseName)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public static void create(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, databaseName)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    //databaseWriteExecutor.execute(() -> prepopulate(context));
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
    }

    public static AppDatabase getDatabase() {
        return INSTANCE;
    }
}