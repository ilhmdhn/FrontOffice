package com.ihp.frontoffice.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.local.FrontOfficeDatabase;
import com.ihp.frontoffice.data.local.dao.ConfigDao;
import com.ihp.frontoffice.data.local.dao.UserDao;

public class LocalRepository {
    private static LocalRepository localRepository;
    private FrontOfficeDatabase db;
    private ConfigDao configDao;
    private UserDao userDao;

    private LocalRepository(Context application) {
        db = FrontOfficeDatabase.getInstance(application);
        configDao = db.configDao();
        userDao = db.userDao();
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

    public void setUserLogout() {
        userDao.setUserLogout();
    }
}
