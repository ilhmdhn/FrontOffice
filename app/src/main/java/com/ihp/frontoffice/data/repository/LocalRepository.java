package com.ihp.frontoffice.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.ihp.frontoffice.data.entity.Notification;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.local.FrontOfficeDatabase;
import com.ihp.frontoffice.data.local.dao.ConfigDao;
import com.ihp.frontoffice.data.local.dao.NotificationDao;
import com.ihp.frontoffice.data.local.dao.UserDao;

public class LocalRepository {
    private static LocalRepository localRepository;
    private FrontOfficeDatabase db;
    private ConfigDao configDao;
    private UserDao userDao;
    private NotificationDao notificationDao;

    private LocalRepository(Context application) {
        db = FrontOfficeDatabase.getInstance(application);
        configDao = db.configDao();
        userDao = db.userDao();
        notificationDao = db.notificationDao();
    }

    public static LocalRepository getInstance(Context app) {
        if (localRepository == null) {
            localRepository = new LocalRepository(app);
        }
        return localRepository;
    }

    public LiveData<User> getUser(){
        return userDao.getLiveUser();
    }

    public LiveData<List<Notification>> getAllNotification() {
        LiveData<List<Notification>> notificationMutableLiveData = notificationDao.getList();
        return notificationMutableLiveData;
    }

    public void setUserLogout() {
        userDao.setUserLogout();
    }

    public void insertNotification(Notification notification) {
        new InsertAsyncTask(notificationDao).execute(notification);
    }

    public LiveData<Integer> countUnreadNotification(boolean bol) {
        return notificationDao.countUnreadNotif(bol);
    }

    public void updateNotification() {
        new UpdateAsyncTask(notificationDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Notification, Void, Void> {

        private NotificationDao mAsyncTaskDao;

        InsertAsyncTask(NotificationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Notification... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {
        private NotificationDao mAsyncTaskDao;

        UpdateAsyncTask(NotificationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.updateReadNotif(true);
            return null;
        }
    }

}
