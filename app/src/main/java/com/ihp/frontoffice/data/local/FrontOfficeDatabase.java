package com.ihp.frontoffice.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ihp.frontoffice.data.entity.Config;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.local.dao.ConfigDao;
import com.ihp.frontoffice.data.local.dao.UserDao;

@Database(entities = {
        User.class,
        Config.class
}, version = 5, exportSchema = false)
public abstract class FrontOfficeDatabase extends RoomDatabase {

    private static FrontOfficeDatabase frontOfficeDatabase;


    public static FrontOfficeDatabase getInstance(Context context) {

        if (frontOfficeDatabase == null) {
            frontOfficeDatabase = buildDatabaseInstance(context);
        }

        return frontOfficeDatabase;
    }

    private static FrontOfficeDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context, FrontOfficeDatabase.class, "fo.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

    }

    public abstract UserDao userDao();

    public abstract ConfigDao configDao();
}