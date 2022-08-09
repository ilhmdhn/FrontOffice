package com.ihp.frontoffice.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import com.ihp.frontoffice.data.entity.User;

@Dao
public interface UserDao extends BaseDao<User> {
    @Transaction

    @Query("DELETE FROM user")
    void setUserLogout();

    @Query("SELECT * FROM user where is_login = 1 ORDER BY user_id DESC LIMIT 1")
    User getUserLogin();

    @Query("SELECT * FROM user WHERE is_login = 1")
    LiveData<User> getLiveUser();
}
