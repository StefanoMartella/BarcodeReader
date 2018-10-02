package stefano_martella.barcodereader.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;
import stefano_martella.barcodereader.R;
import stefano_martella.barcodereader.adapters.ItemAdapter;
import stefano_martella.barcodereader.roomdatabase.DBHelper;
import stefano_martella.barcodereader.roomdatabase.Item;

public class ListItemsActivity extends BasicActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        list = findViewById(R.id.main_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) list.getItemAtPosition(position);
                Intent modifyItemActivity = new Intent(getApplicationContext(), ShowItemActivity.class);
                modifyItemActivity.putExtra("code", item.getCode());
                modifyItemActivity.putExtra("name", item.getName());
                modifyItemActivity.putExtra("description", item.getDescription());
                modifyItemActivity.putExtra("floor", item.getFloor());
                modifyItemActivity.putExtra("room", item.getRoom());
                modifyItemActivity.putExtra("state", item.getState());
                modifyItemActivity.putExtra("type", item.getType());
                modifyItemActivity.putExtra("number", item.getNumber());
                modifyItemActivity.putExtra("idOperator", item.getIdOperator());
                modifyItemActivity.putExtra("idWarehouse", item.getIdWarehouse());
                startActivity(modifyItemActivity);
                finish();
            }
        });
        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void, Void, List<Item>> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(ListItemsActivity.this,
                    "Elaborazione", "Solo un momento...", true, false);
        }

        @Override
        protected List<Item> doInBackground(Void... voids) {

            return DBHelper.get(ListItemsActivity.this).getAll();
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            super.onPostExecute(items);

            dialog.dismiss();

            list.setAdapter(new ItemAdapter(items));
        }
    }

}
