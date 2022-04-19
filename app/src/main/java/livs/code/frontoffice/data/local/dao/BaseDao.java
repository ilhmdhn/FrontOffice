package livs.code.frontoffice.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(T item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<T> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(T... items);

    @Update
    void update(T item);

    @Delete
    void delete(T item);
}
