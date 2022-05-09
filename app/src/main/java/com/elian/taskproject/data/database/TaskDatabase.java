package com.elian.taskproject.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.elian.taskproject.data.dao.TaskDAO;
import com.elian.taskproject.data.model.Task;

// 1. Define the configuration of the database.

@Database(version = 1, entities = { Task.class })
public abstract class TaskDatabase extends RoomDatabase {

    // 2. Create the methods to get the DAO.
    public abstract TaskDAO getDao();

    private static volatile TaskDatabase INSTANCE;


    // Google version method
    public static TaskDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_database")
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
            synchronized (TaskDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "inventory")
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

    public static TaskDatabase getDatabase() {
        return INSTANCE;
    }

    /*
     * Function when on create insert the data we need.
     */
//    private static void prepopulate(Context context) {
//
//        UserDao userDao = INSTANCE.userDao();
//        User user = new User("moronlu18@gmail.com", "Lourdes18?");
//        getDatabase().runInTransaction(() -> {
//            userDao.insert(user);
//        });
//    }
}