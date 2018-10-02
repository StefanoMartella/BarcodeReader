package stefano_martella.barcodereader.roomdatabase;

import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
//import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Item item);

    /*@Update
    int update(Item item);*/

    /*@Delete
    int delete(Item item);*/

    @Query("SELECT * FROM items WHERE code = :code LIMIT 1")
    Item get(String code);

    @Query("SELECT * FROM items ORDER BY name")
    List<Item> getItems();

    @Query("DELETE FROM items")
    void deleteAll();

    @Query("DELETE FROM items WHERE code = :code")
    void deleteItem(String code);
}
