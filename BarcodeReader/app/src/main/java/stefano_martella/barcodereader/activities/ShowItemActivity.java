package stefano_martella.barcodereader.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import stefano_martella.barcodereader.R;
import stefano_martella.barcodereader.roomdatabase.DBHelper;

public class ShowItemActivity extends BasicActivity {

    private String   CODE, NAME, DESCRIPTION, FLOOR, ROOM,
                     STATE, TYPE;

    private int      NUMBER, ID_OPERATOR, ID_WAREHOUSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_item);

        Intent intent = getIntent();

        TextView code = findViewById(R.id.code);
        TextView name = findViewById(R.id.name);
        TextView description = findViewById(R.id.description);
        TextView floor = findViewById(R.id.floor);
        TextView room = findViewById(R.id.room);
        TextView state = findViewById(R.id.state);
        TextView type = findViewById(R.id.type);
        TextView number = findViewById(R.id.number);
        TextView idOperator = findViewById(R.id.idOperator);
        TextView idWarehouse = findViewById(R.id.idWarehouse);
        Button changeButton = findViewById(R.id.change_item_info);
        Button deleteButton = findViewById(R.id.delete_item);

        changeButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent modifyItemActivity = new Intent(ShowItemActivity.this, ModifyItemActivity.class);
                modifyItemActivity.putExtra("code", CODE);
                modifyItemActivity.putExtra("name", NAME);
                modifyItemActivity.putExtra("description", DESCRIPTION);
                modifyItemActivity.putExtra("floor", FLOOR);
                modifyItemActivity.putExtra("room", ROOM);
                modifyItemActivity.putExtra("state", STATE);
                modifyItemActivity.putExtra("type", TYPE);
                modifyItemActivity.putExtra("number", NUMBER);
                modifyItemActivity.putExtra("idOperator", ID_OPERATOR);
                modifyItemActivity.putExtra("idWarehouse", ID_WAREHOUSE);
                startActivity(modifyItemActivity);
                finish();
            }
        });

        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemActivity.this);
                builder.setTitle("Attenzione");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.get(ShowItemActivity.this).deleteItem(CODE);
                        Toast.makeText(ShowItemActivity.this, "Prodotto eliminato",
                                       Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ShowItemActivity.this, ListItemsActivity.class));
                        finish();
                    }
                });
                builder.setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setMessage("Eliminare prodotto?");
                final AlertDialog delete_item = builder.create();
                delete_item.show();
            }
        });

        CODE = intent.getStringExtra("code");
        NAME = intent.getStringExtra("name");
        DESCRIPTION = intent.getStringExtra("description");
        FLOOR = intent.getStringExtra("floor");
        ROOM = intent.getStringExtra("room");
        STATE = intent.getStringExtra("state");
        TYPE = intent.getStringExtra("type");
        NUMBER = intent.getIntExtra("number", 1);
        ID_OPERATOR = intent.getIntExtra("idOperator", 2);
        ID_WAREHOUSE = intent.getIntExtra("idWarehouse", 3);

        code.setText(Html.fromHtml("<b>Codice:</b><br />" + CODE));
        name.setText(Html.fromHtml("<b>Nome:</b><br />" + NAME));
        description.setText(Html.fromHtml("<b>Descrizione:</b><br />" + DESCRIPTION));
        floor.setText(Html.fromHtml("<b>Piano:</b><br />" + FLOOR));
        room.setText(Html.fromHtml("<b>Stanza:</b><br />" + ROOM));
        state.setText(Html.fromHtml("<b>Stato:</b><br />" + STATE));
        type.setText(Html.fromHtml("<b>Tipo:</b><br />" + TYPE));
        number.setText(Html.fromHtml("<b>Numero:</b><br />" + NUMBER));
        idOperator.setText(Html.fromHtml("<b>ID Operatore:</b><br />" + ID_OPERATOR));
        idWarehouse.setText(Html.fromHtml("<b>ID Magazzino:</b><br />" + ID_WAREHOUSE));
    }

}
