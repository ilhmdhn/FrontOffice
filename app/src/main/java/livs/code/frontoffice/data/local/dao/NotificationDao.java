package livs.code.frontoffice.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import livs.code.frontoffice.data.entity.Notification;

@Dao
public interface NotificationDao extends BaseDao<Notification> {
    @Transaction
    @Query("SELECT * FROM notification ORDER BY id DESC")
    LiveData<List<Notification>> getList();

    @Query("UPDATE notification SET is_read=:isRead WHERE is_read = 0")
    void updateReadNotif(boolean isRead);

    @Query("SELECT COUNT(*) FROM notification WHERE is_read=:isRead")
    LiveData<Integer> countUnreadNotif(boolean isRead);
}