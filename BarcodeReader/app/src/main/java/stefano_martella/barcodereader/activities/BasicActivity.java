package stefano_martella.barcodereader.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import stefano_martella.barcodereader.R;
import stefano_martella.barcodereader.roomdatabase.DBHelper;
import stefano_martella.barcodereader.volley.MyVolley;

public class BasicActivity extends AppCompatActivity{

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Method to manage selected item on menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            // List stored items
            case R.id.list_items:

                startActivity(new Intent(getApplicationContext(), ListItemsActivity.class));
                break;

            case R.id.delete_items:

                AlertDialog.Builder builder = new AlertDialog.Builder(BasicActivity.this);
                builder.setTitle("Attenzione");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.get(BasicActivity.this).deleteAll();
                        Toast.makeText(BasicActivity.this, "Prodotti eliminati",
                                       Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                        if( ! (BasicActivity.this instanceof MainActivity) )
                        finish();
                    }
                });
                builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setMessage("Eliminare tutti i prodotti?");
                final AlertDialog delete_items = builder.create();
                delete_items.show();
                break;

            case R.id.delete_items_from_server:

                final String url = "http://www.bitesrl.it/test/course/exam2/script.php?action=clean";

                AlertDialog.Builder warning = new AlertDialog.Builder(BasicActivity.this);
                warning.setTitle("Attenzione");
                warning.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    StringRequest delete_from_server = new StringRequest(Request.Method.GET,
                            url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Response", error.toString());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }
                    };

                    MyVolley.getInstance().addToRequestQueue(delete_from_server);

                    startActivity(getIntent());
                    if( ! (BasicActivity.this instanceof MainActivity) )
                        finish();
                    }
                });
                warning.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                warning.setMessage(Html.fromHtml("Eliminare tutti i prodotti dal server?<br /><br />" +
                                                 "<b>Questa operazione non potrà essere annullata!</b>"));
                final AlertDialog delete_from_server_warning = warning.create();
                delete_from_server_warning.show();

                break;

            // Close app
            case R.id.exit_button:

                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

}
