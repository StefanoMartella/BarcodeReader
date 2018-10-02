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
import stefano_martella.barcodereader.roomdatabase.*;
import stefano_martella.barcodereader.volley.MyVolley;

public class AddItemActivity extends BasicActivity implements android.view.View.OnClickListener{

    private EditText name, description, floor, room, state,
                     type, number, idOperator, idWarehouse;

    private String   CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        Intent intent = getIntent();

        Button addButton = findViewById(R.id.add_item);
        addButton.setOnClickListener(this);

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

        code.setText(getResources().getString(R.string.code_string, CODE));

    }

    @Override
    public void onClick(View view){

        if( name.getText().toString().isEmpty() ) {
            name.setError("This field is required");
            return;
        }
        if( description.getText().toString().isEmpty() ) {
            description.setError("This field is required");
            return;
        }
        if( floor.getText().toString().isEmpty() ) {
            floor.setError("This field is required");
            return;
        }
        if( room.getText().toString().isEmpty() ) {
            room.setError("This field is required");
            return;
        }
        if( state.getText().toString().isEmpty() ) {
            state.setError("This field is required");
            return;
        }
        if( type.getText().toString().isEmpty() ) {
            type.setError("This field is required");
            return;
        }
        if( number.getText().toString().isEmpty() ) {
            number.setError("This field is required");
            return;
        }
        if( idOperator.getText().toString().isEmpty() ) {
            idOperator.setError("This field is required");
            return;
        }
        if( idWarehouse.getText().toString().isEmpty() ) {
            idWarehouse.setError("This field is required");
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


        // Local storage(SQLite)
        DBHelper.get(this).saveItem(item);
        Toast.makeText(AddItemActivity.this,
                       "Prodotto aggiunto",
                       Toast.LENGTH_SHORT).show();



        // Server storage
        String url = "http://www.bitesrl.it/test/course/exam2/script.php";

        StringRequest item_to_store = new StringRequest(Request.Method.POST,
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

        MyVolley.getInstance().addToRequestQueue(item_to_store);

        finish();
    }
}
