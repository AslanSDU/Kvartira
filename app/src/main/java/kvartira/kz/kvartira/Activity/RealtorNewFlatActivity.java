package kvartira.kz.kvartira.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONObject;

import kvartira.kz.kvartira.Basic.DatabaseHelper;
import kvartira.kz.kvartira.Basic.JSONParser;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 06.02.2017.
 */
public class RealtorNewFlatActivity extends BaseActivity implements OnClickListener {

    private Toolbar toolbar;
    private GridLayout gridLayout;

    private EditText address;
    private EditText price;
    private EditText roomNumber;
    private EditText floor;
    private EditText floors;
    private EditText type;
    private EditText description;

    private String addressText;
    private String priceText;
    private String roomNumberText;
    private String floorText;
    private String floorsText;
    private String typeText;
    private String descriptionText;

    private ProgressDialog dialog;
    private JSONParser jsonParser;
    private SharedPreferences sp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtor_new_flat);

        jsonParser = new JSONParser();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        databaseHelper = new DatabaseHelper(this);

        toolbar = (Toolbar) findViewById(R.id.activity_realtor_new_flat_toolbar);
        toolbar.setTitle("Квартира");
        setSupportActionBar(toolbar);

        gridLayout = (GridLayout) findViewById(R.id.activity_realtor_new_flat_photos);
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.photo);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
        image.setLayoutParams(layoutParams);
        gridLayout.addView(image);

        address = (EditText) findViewById(R.id.activity_realtor_new_flat_address);
        price = (EditText) findViewById(R.id.activity_realtor_new_flat_price);
        roomNumber = (EditText) findViewById(R.id.activity_realtor_new_flat_room_number);
        floor = (EditText) findViewById(R.id.activity_realtor_new_flat_floor);
        floors = (EditText) findViewById(R.id.activity_realtor_new_flat_floors);
        type = (EditText) findViewById(R.id.activity_realtor_new_flat_type);
        description = (EditText) findViewById(R.id.activity_realtor_new_flat_description);
        Button publish = (Button) findViewById(R.id.activity_realtor_new_flat_button);
        publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_realtor_new_flat_button) {

        }
    }

    private class PublishFlat extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }
}