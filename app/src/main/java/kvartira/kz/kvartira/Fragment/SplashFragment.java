package kvartira.kz.kvartira.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kvartira.kz.kvartira.Activity.BaseActivity;
import kvartira.kz.kvartira.Activity.ClientActivity;
import kvartira.kz.kvartira.Activity.RealtorActivity;
import kvartira.kz.kvartira.Basic.DatabaseHelper;
import kvartira.kz.kvartira.Basic.JSONParser;
import kvartira.kz.kvartira.Basic.URL;
import kvartira.kz.kvartira.Model.City;
import kvartira.kz.kvartira.Model.Order;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 24.12.2015.
 */
public class SplashFragment extends BaseFragment {

    private SharedPreferences sp;
    private ProgressBar progressBar;
    private JSONParser jsonParser;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        jsonParser = new JSONParser();
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);

        progressBar = (ProgressBar) v.findViewById(R.id.fragment_splash_progress_bar);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        boolean isFirstTime = sp.getBoolean("first_time", true);
        if (isFirstTime) {
            ((BaseActivity) getActivity()).replaceFragment(R.id.activity_login_container, LoginFragment.returnInstance(""));
        } else {
            boolean hasRegistered = sp.getBoolean("registered", false);
            if (hasRegistered) {
                new Sync().execute();
            } else {
                ((BaseActivity) getActivity()).replaceFragment(R.id.activity_login_container, ChooseRoleFragment.newInstance());
            }
        }
    }

    private class Sync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phone_number", sp.getString("phone_number", "")));
            params.add(new BasicNameValuePair("city_count", databaseHelper.getCitiesCount() + ""));

            JSONObject json = jsonParser.makeHttpRequest(URL.sync, "POST", params);
            Log.d("SPLASH", json.toString());
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("own_id", json.getInt("id"));
                    edit.putInt("city_id", json.getInt("city_id"));

                    int city_change = json.getInt("city_change");
                    if (city_change == 1) {
                        JSONArray cities = json.getJSONArray("cities");
                        ArrayList<City> citiesList = new ArrayList<City>();
                        for (int i = 0; i < cities.length(); i++) {
                            JSONObject object = cities.getJSONObject(i);
                            City city = new City(object.getInt("id"), object.getString("name"));
                            citiesList.add(city);
                        }
                        databaseHelper.addCities(citiesList);
                    }

                    int order_check = json.getInt("order_check");
                    if (order_check == 1) {
                        JSONArray orders = json.getJSONArray("orders");
                        Log.d("ORDERS LENGTH", orders.length() + "");
                        databaseHelper.deleteAllOrders();
                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject object = orders.getJSONObject(i);
                            Log.d("DATE", object.getString("date"));
                            Order order = new Order(object.getInt("id"), object.getString("user_name"), object.getString("user_surname"),
                                    "", object.getString("city_name"), object.getString("user_phone"),
                                    object.getString("address"), object.getInt("rooms"), object.getInt("price"), object.getString("notation"),
                                    object.getInt("residence"), object.getString("date"), object.getString("time"));
                            databaseHelper.addOrder(order);
                        }
                    } else if (order_check == 0) {
                        databaseHelper.deleteAllOrders();
                    }
                    edit.commit();
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int who = sp.getInt("who", 0);
            Intent intent = null;
            if (who == 0) {
                intent = new Intent(getActivity(), ClientActivity.class);
            }
            if (who == 1) {
                intent = new Intent(getActivity(), RealtorActivity.class);
            }
            startActivity(intent);
            getActivity().finish();
        }
    }
}
