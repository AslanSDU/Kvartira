package kvartira.kz.kvartira.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.*;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kvartira.kz.kvartira.Adapter.CityListAdapter;
import kvartira.kz.kvartira.Basic.DatabaseHelper;
import kvartira.kz.kvartira.Basic.JSONParser;
import kvartira.kz.kvartira.Basic.URL;
import kvartira.kz.kvartira.Model.City;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 04.10.2016.
 */
public class ProfileActivity extends BaseActivity implements OnClickListener {

    private Toolbar toolbar;
    private Uri imageURI;

    private ImageView photo;
    private EditText name;
    private EditText surname;
    private EditText date_of_birth;
    private EditText city;

    private JSONParser jsonParser;
    private ProgressDialog dialog;

    private String photoText;
    private String nameText;
    private String surnameText;
    private String birthText;
    private String genderText;
    private String phoneText;
    private int whoText;
    private City choosenCity;

    private SharedPreferences sp;

    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormat;

    private AlertDialog cityDialog;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        jsonParser = new JSONParser();
        dialog = new ProgressDialog(this);
        dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
        databaseHelper = new DatabaseHelper(this);

        toolbar = (Toolbar) findViewById(R.id.activity_profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.profile_activity);
        ImageButton save = (ImageButton) toolbar.findViewById(R.id.toolbar_with_button_save);
        save.setOnClickListener(this);

        photo = (ImageView) findViewById(R.id.activity_profile_photo);
        String photoInMemory = sp.getString("photo", "");
        byte[] b = Base64.decode(photoInMemory, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        photo.setImageBitmap(bitmap);
        BitmapDrawable drawable = (BitmapDrawable) photo.getDrawable();
        bitmap = drawable.getBitmap();
        photo.setImageBitmap(getCircleBitmap(bitmap));
        photo.setOnClickListener(this);

        name = (EditText) findViewById(R.id.activity_profile_name);
        name.setText(sp.getString("name", ""));
        surname = (EditText) findViewById(R.id.activity_profile_surname);
        surname.setText(sp.getString("surname", ""));
        date_of_birth = (EditText) findViewById(R.id.activity_profile_dof);
        date_of_birth.setText(sp.getString("date_of_birth", ""));
        date_of_birth.setInputType(InputType.TYPE_NULL);
        createDatePicker();
        date_of_birth.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker.show();
                }
            }
        });

        RadioButton male = (RadioButton) findViewById(R.id.activity_profile_male);
        male.setOnClickListener(this);
        RadioButton female = (RadioButton) findViewById(R.id.activity_profile_female);
        female.setOnClickListener(this);
        String myGender = sp.getString("gender", "");
        if (myGender.equals("Мужчина")) {
            male.setChecked(true);
        } else if (myGender.equals("Женщина")) {
            female.setChecked(true);
        }

        city = (EditText) findViewById(R.id.activity_profile_city);
        city.setInputType(InputType.TYPE_NULL);
        choosenCity = new City(sp.getInt("city_id", 0), databaseHelper.getCity(sp.getInt("city_id", 0)));
        city.setText(databaseHelper.getCity(sp.getInt("city_id", 0)));
        createDialog();
        city.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cityDialog.show();
                }
            }
        });
        city.setOnClickListener(this);

        whoText = getIntent().getIntExtra("who", 0);
    }

    private void createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DatePickerTheme);
        builder.setTitle("Выберите город");
        ListView cityList = new ListView(this);
        final ArrayList<City> cities = databaseHelper.getCities();
        CityListAdapter adapter = new CityListAdapter(this, R.layout.city_list_item, cities);
        cityList.setAdapter(adapter);
        cityList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choosenCity = new City(cities.get(position).getId(), cities.get(position).getName());
                city.setText(cities.get(position).getName());
                cityDialog.dismiss();
            }
        });
        builder.setView(cityList);
        cityDialog = builder.create();
    }

    private void createDatePicker() {
        Calendar calendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar picked = Calendar.getInstance();
                picked.set(year, month, day);
                date_of_birth.setText(dateFormat.format(picked.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_with_button_save) {
            new UpdateUserInfo().execute();
        } else if (v.getId() == R.id.activity_profile_male) {
            genderText = getResources().getString(R.string.male);
        } else if (v.getId() == R.id.activity_profile_female) {
            genderText = getResources().getString(R.string.female);
        } else if (v.getId() == R.id.activity_profile_dof) {
            datePicker.show();
        } else if (v.getId() == R.id.activity_profile_city) {
            cityDialog.show();
        } else if (v.getId() == R.id.activity_profile_photo) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageURI = data.getData();
            doCrop();
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap bitmap = extras.getParcelable("data");
                photo.setImageBitmap(getCircleBitmap(bitmap));
            }
        }
    }

    private void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.setData(imageURI);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = Color.RED;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    private class UpdateUserInfo extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Обновляем данные...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

            BitmapDrawable bitmapDrawable = (BitmapDrawable) photo.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            photoText = Base64.encodeToString(b, Base64.DEFAULT);

            nameText = name.getText().toString();
            surnameText = surname.getText().toString();
            birthText = date_of_birth.getText().toString();
            phoneText = sp.getString("phone_number", "");
        }

        @Override
        protected Integer doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("phone_number", phoneText));
            params.add(new BasicNameValuePair("photo", photoText));
            params.add(new BasicNameValuePair("name", nameText));
            params.add(new BasicNameValuePair("surname", surnameText));
            params.add(new BasicNameValuePair("birth", birthText));
            params.add(new BasicNameValuePair("gender", genderText));
            params.add(new BasicNameValuePair("city_id", choosenCity.getId() + ""));
            params.add(new BasicNameValuePair("who", whoText + ""));

            JSONObject json = jsonParser.makeHttpRequest(URL.update_user_info, "POST", params);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("city_id", choosenCity.getId());
                    editor.putString("name", nameText);
                    editor.putString("surname", surnameText);
                    editor.putString("photo", photoText);
                    editor.putString("date_of_birth", birthText);
                    editor.putString("gender", genderText);
                    editor.commit();
                    return success;
                }
            } catch (Exception e) {
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            if (integer == 1) {
                Toast.makeText(ProfileActivity.this, "Данные успешно обнолены", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
