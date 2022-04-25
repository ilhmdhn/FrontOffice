package livs.code.frontoffice.data.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import livs.code.frontoffice.data.entity.Config;
import livs.code.frontoffice.data.entity.User;

@Dao
public interface UserDao extends BaseDao<User> {
    @Transaction
    @Query("SELECT * FROM user ORDER BY user_id ASC")
    List<User> getList();

    @Query("SELECT * FROM user ORDER BY user_id DESC LIMIT 1")
    User getLastUser();

    @Query("UPDATE user SET is_login = 0")
    void setUserLogout();

    @Query("SELECT * FROM user where is_login = 1 ORDER BY user_id DESC LIMIT 1")
    User getUserLogin();
}
