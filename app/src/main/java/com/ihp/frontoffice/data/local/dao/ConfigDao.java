package com.ihp.frontoffice.data.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import com.ihp.frontoffice.data.entity.Config;
@Dao
public interface ConfigDao extends BaseDao<Config> {
    @Transaction

    @Query("SELECT * FROM config ORDER BY id DESC LIMIT 1")
    Config getLastConfig();
}
