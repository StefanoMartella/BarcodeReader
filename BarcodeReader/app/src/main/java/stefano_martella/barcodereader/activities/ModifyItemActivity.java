package stefano_martella.barcodereader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
import stefano_martella.barcodereader.R;
import stefano_martella.barcodereader.roomdatabase.DBHelper;
import stefano_martella.barcodereader.roomdatabase.Item;
import stefano_martella.barcodereader.volley.MyVolley;

public class ModifyItemActivity extends BasicActivity implements android.view.View.OnClickListener{

    private EditText name, description, floor, room, state,
                     type, number, idOperator, idWarehouse;

    private String   CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_item);

        Intent intent = getIntent();

        Button modifyButton = findViewById(R.id.modify_item);
        modifyButton.setOnClickListener(this);

        TextView code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        floor = findViewById(R.id.floor);
        room = findViewById(R.id.room);
        state = findViewById(R.id.state);
        type = findViewById(R.id.type);
        number = findViewById(R.id.number);
        idOperator = findViewById(R.id.idOperator);
        idWarehouse = findViewById(R.id.idWarehouse);

        CODE = intent.getStringExtra("code");
        String NAME = intent.getStringExtra("name");
        String DESCRIPTION = intent.getStringExtra("description");
        String FLOOR = intent.getStringExtra("floor");
        String ROOM = intent.getStringExtra("room");
        String STATE = intent.getStringExtra("state");
        String TYPE = intent.getStringExtra("type");
        int NUMBER = intent.getIntExtra("number", 1);
        int ID_OPERATOR = intent.getIntExtra("idOperator", 2);
        int ID_WAREHOUSE = intent.getIntExtra("idWarehouse", 3);

        name.setText(NAME);
        description.setText(DESCRIPTION);
        floor.setText(FLOOR);
        room.setText(ROOM);
        state.setText(STATE);
        type.setText(TYPE);
        number.setText(String.valueOf(NUMBER));
        idOperator.setText(String.valueOf(ID_OPERATOR));
        idWarehouse.setText(String.valueOf(ID_WAREHOUSE));

        code.setText(getResources().getString(R.string.code_string, CODE));
    }

    @Override
    public void onClick(View view){

        if( name.getText().toString().isEmpty() || number.getText().toString().isEmpty() ||
            idOperator.getText().toString().isEmpty() || idOperator.getText().toString().isEmpty() ){
            Toast.makeText(ModifyItemActivity.this,
                           "Il campi asteriscati non possono essere vuoti",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        final Item item = new Item();

        item.setCode(CODE);
        item.setName(name.getText().toString());
        item.setDescription(description.getText().toString());
        item.setFloor(floor.getText().toString());
        item.setRoom(room.getText().toString());
        item.setState(state.getText().toString());
        item.setType(type.getText().toString());
        item.setNumber(Integer.valueOf(number.getText().toString()));
        item.setIdOperator(Integer.valueOf(idOperator.getText().toString()));
        item.setIdWarehouse(Integer.valueOf(idWarehouse.getText().toString()));

        DBHelper.get(this).saveItem(item);
        Toast.makeText(ModifyItemActivity.this,
                       "Prodotto modificato",
                       Toast.LENGTH_SHORT).show();

        // Server storage
        String URL = "http://www.bitesrl.it/test/course/exam2/script.php";

        StringRequest default_operator = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "login");
                params.put("username", "pippo");
                params.put("password", "pluto");
                return params;
            }
        };

        StringRequest item_to_store = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "upload");
                params.put("code", item.getCode());
                params.put("name", item.getName());
                params.put("description", item.getDescription());
                params.put("floor", item.getFloor());
                params.put("room", item.getRoom());
                params.put("state", item.getState());
                params.put("type", item.getType());
                params.put("number", String.valueOf(item.getNumber()));
                params.put("idOperator", String.valueOf(item.getIdOperator()));
                params.put("idWarehouse", String.valueOf(item.getIdWarehouse()));
                return params;
            }
        };

        MyVolley.getInstance().addToRequestQueue(default_operator);
        MyVolley.getInstance().addToRequestQueue(item_to_store);

        finish();

    }

}
