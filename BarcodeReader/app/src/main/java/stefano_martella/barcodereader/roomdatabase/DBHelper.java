package stefano_martella.barcodereader.roomdatabase;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;


public class DBHelper {
    private static final String DATABASE = "roomDatabase.db";
    private DB database;

    /* Singleton */
    private static DBHelper instance = null;


    public static DBHelper get(Context context) {
        return instance == null ? instance = new DBHelper(context) : instance;
    }

    /* Builder */
    private DBHelper(Context context) {
        database = Room.databaseBuilder(context, DB.class, DATABASE).build();
    }

    /* Public methods */


    public Item getItem(final String code){
        return database.ItemDao().get(code);
    }

    public List<Item> getAll(){ return database.ItemDao().getItems(); }

    public void deleteAll(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.ItemDao().deleteAll();
            }
        }).start();
    }

    public void saveItem(final Item item){
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.ItemDao().save(item);
            }
        }).start();
    }

    public void deleteItem(final String code){
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.ItemDao().deleteItem(code);
            }
        }).start();
    }


}
