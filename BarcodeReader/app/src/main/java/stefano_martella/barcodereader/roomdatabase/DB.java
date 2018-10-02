package stefano_martella.barcodereader.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Item.class}, version = 1)
public abstract class DB extends RoomDatabase {
    public abstract ItemDao ItemDao();
}

