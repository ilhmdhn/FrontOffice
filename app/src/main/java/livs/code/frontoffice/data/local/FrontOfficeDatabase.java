package livs.code.frontoffice.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import livs.code.frontoffice.data.entity.Config;
import livs.code.frontoffice.data.entity.Notification;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.local.dao.ConfigDao;
import livs.code.frontoffice.data.local.dao.NotificationDao;
import livs.code.frontoffice.data.local.dao.UserDao;

@Database(entities = {
        User.class,
        Config.class,
        Notification.class
}, version = 3, exportSchema = false)
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

    public abstract NotificationDao notificationDao();
}