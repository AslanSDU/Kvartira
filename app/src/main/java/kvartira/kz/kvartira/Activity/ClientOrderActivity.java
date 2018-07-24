package kvartira.kz.kvartira.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.*;
import android.widget.RadioButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kvartira.kz.kvartira.Basic.DatabaseHelper;
import kvartira.kz.kvartira.Basic.JSONParser;
import kvartira.kz.kvartira.Basic.URL;
import kvartira.kz.kvartira.Model.Order;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 23.03.2016.
 */
public class ClientOrderActivity extends BaseActivity implements OnClickListener {

    private Toolbar toolbar;

    private EditText address;
    private EditText rooms;
    private EditText price;
    private EditText notation;

    private int residence;
    private String addressText;
    private String roomsText;
    private String priceText;
    private String notationText;

    private ProgressDialog dialog;
    private JSONParser jsonParser;
    private SharedPreferences sp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_order);

        jsonParser = new JSONParser();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        databaseHelper = new DatabaseHelper(this);
        residence = 0;

        toolbar = (Toolbar) findViewById(R.id.activity_client_order_toolbar);
        toolbar.setTitle("Подать заявку");
        setSupportActionBar(toolbar);

        address = (EditText) findViewById(R.id.activity_client_order_area);
        rooms = (EditText) findViewById(R.id.activity_client_order_room_number);
        price = (EditText) findViewById(R.id.activity_client_order_price);
        notation = (EditText) findViewById(R.id.activity_client_order_note);
        RadioButton daily = (RadioButton) findViewById(R.id.activity_client_order_daily);
        daily.setOnClickListener(this);
        RadioButton longterm = (RadioButton) findViewById(R.id.activity_client_order_longterm);
        longterm.setOnClickListener(this);
        Button order = (Button) findViewById(R.id.activity_client_order_button);
        order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_client_order_daily) {
            residence = 1;
        } else if (v.getId() == R.id.activity_client_order_longterm) {
            residence = 2;
        } else if (v.getId() == R.id.activity_client_order_button) {
            new SendOrder().execute();
        }
    }

    private class SendOrder extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ClientOrderActivity.this);
            dialog.setMessage("Отправляем ваш заказ");
            dialog.setCancelable(false);
            dialog.show();

            addressText = address.getText().toString();
            roomsText = rooms.getText().toString();
            priceText = price.getText().toString();
            notationText = notation.getText().toString();
        }

        @Override
        protected JSONObject doInBackground(Void... args) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", sp.getInt("own_id", 0) + ""));
            params.add(new BasicNameValuePair("area", addressText));
            params.add(new BasicNameValuePair("room_number", roomsText));
            params.add(new BasicNameValuePair("price", priceText));
            params.add(new BasicNameValuePair("note", notationText));
            params.add(new BasicNameValuePair("residence", residence + ""));
            params.add(new BasicNameValuePair("city_id", sp.getInt("city_id", 0) + ""));

            JSONObject json = jsonParser.makeHttpRequest(URL.send_order, "POST", params);
            Log.d("SEND ORDER", json.toString());
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            dialog.dismiss();
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:SS");
                    String date = dateFormat.format(Calendar.getInstance().getTime());
                    String time = timeFormat.format(Calendar.getInstance().getTime());
                    Order order = new Order(json.getInt("id"), json.getString("user_name"), json.getString("user_surname"),
                            "", json.getString("city_name"), json.getString("user_phone"),
                            json.getString("address"), json.getInt("rooms"), json.getInt("price"), json.getString("notation"),
                            json.getInt("residence"), date, time);
                    databaseHelper.addMyOrder(order);
                }
            } catch (Exception e) {
            }
            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}
