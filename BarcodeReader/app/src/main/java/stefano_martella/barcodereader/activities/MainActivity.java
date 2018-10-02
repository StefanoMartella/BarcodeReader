package stefano_martella.barcodereader.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import stefano_martella.barcodereader.roomdatabase.*;
import stefano_martella.barcodereader.volley.MyVolley;

public class MainActivity extends BasicActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                       Manifest.permission.CAMERA,
                                                       Manifest.permission.INTERNET},
                                          1);

        // LOGIN AS DEFAULT OPERATOR PIPPO

        String url = "http://www.bitesrl.it/test/course/exam2/script.php";

        StringRequest default_operator = new StringRequest(Request.Method.POST,
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
        })
        {
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

        MyVolley.getInstance().addToRequestQueue(default_operator);

        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);
        setContentView(mScannerView);
    }

    // Method to manage permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        // If request is cancelled, the result arrays are empty.
        if ( ! (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) ) {

            finish();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mScannerView == null) {
            mScannerView = new ZXingScannerView(this);
            setContentView(mScannerView);
        }
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handleResult(final Result rawResult) {

        final String CODE = rawResult.getText();

        new MyTask().execute(CODE);

    }

    private class MyTask extends AsyncTask<String, Void, Item> {

        private ProgressDialog dialog;
        private String CODE;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(MainActivity.this,
                     "Elaborazione", "Solo un attimo...",
                     true, false);
        }

        @Override
        protected Item doInBackground(String... strings) {

            // LOCAL CHECK

            CODE = strings[0];

            Item local_item = DBHelper.get(MainActivity.this).getItem(CODE);

            if( local_item != null ){
                return local_item;
            }



            //SERVER CHECK

            Item server_item = null;
            final String SERVER_URL = "http://www.bitesrl.it/test/course/exam2/script.php?action=items";
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                System.out.println(response.toString());
                JSONArray items = new JSONArray(response.toString());
                for ( int i = 0; i < items.length(); i++ ) {
                    try {
                        JSONObject current_item = items.getJSONObject(i);
                        if( current_item.getString("code").equals(CODE) ){
                            server_item = new Item();
                            server_item.setCode(current_item.getString("code"));
                            server_item.setName(current_item.getString("name"));
                            server_item.setDescription(current_item.getString("description"));
                            server_item.setFloor(current_item.getString("floor"));
                            server_item.setRoom(current_item.getString("room"));
                            server_item.setState(current_item.getString("state"));
                            server_item.setType(current_item.getString("type"));
                            server_item.setNumber(Integer.valueOf(current_item.getString("number")));
                            server_item.setIdOperator(Integer.valueOf(current_item.getString("idOperator")));
                            server_item.setIdWarehouse(Integer.valueOf(current_item.getString("idWarehouse")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return server_item;

        }

        @Override
        protected void onPostExecute(Item item) {
            super.onPostExecute(item);

            dialog.dismiss();

            if ( item == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Nuovo prodotto:");
                builder.setPositiveButton("Nuova scansione", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScannerView.resumeCameraPreview(MainActivity.this);
                    }
                });
                builder.setNeutralButton("Aggiungi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent showScanResult = new Intent(MainActivity.this, AddItemActivity.class);
                        showScanResult.putExtra("code", CODE);
                        startActivity(showScanResult);
                    }
                });
                builder.setMessage(Html.fromHtml("<b>Codice:</b><br />" + CODE));
                final AlertDialog new_product_alert = builder.create();
                new_product_alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        new_product_alert.dismiss();
                        mScannerView.resumeCameraPreview(MainActivity.this);
                    }
                });
                new_product_alert.show();
            }
            else {
                Intent showItemActivity = new Intent(MainActivity.this, ShowItemActivity.class);
                showItemActivity.putExtra("code", item.getCode());
                showItemActivity.putExtra("name", item.getName());
                showItemActivity.putExtra("description", item.getDescription());
                showItemActivity.putExtra("floor", item.getFloor());
                showItemActivity.putExtra("room", item.getRoom());
                showItemActivity.putExtra("state", item.getState());
                showItemActivity.putExtra("type", item.getType());
                showItemActivity.putExtra("number", item.getNumber());
                showItemActivity.putExtra("idOperator", item.getIdOperator());
                showItemActivity.putExtra("idWarehouse", item.getIdWarehouse());
                startActivity(showItemActivity);
            }
        }
    }

}