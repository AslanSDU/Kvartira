package kvartira.kz.kvartira.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kvartira.kz.kvartira.Activity.BaseActivity;
import kvartira.kz.kvartira.Activity.ClientActivity;
import kvartira.kz.kvartira.Activity.RealtorActivity;
import kvartira.kz.kvartira.Basic.DatabaseHelper;
import kvartira.kz.kvartira.Basic.JSONParser;
import kvartira.kz.kvartira.Basic.URL;
import kvartira.kz.kvartira.Model.City;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 05.12.2015.
 */
public class VerificationFragment extends BaseFragment implements OnClickListener {

    private String phoneNumber;
    private String verificationCode;

    private JSONParser jsonParser;
    private ProgressDialog dialog;
    private EditText smsCode;
    private SharedPreferences sp;
    private DatabaseHelper dbHelper;

    private GoogleCloudMessaging gcm;
    private String gcmID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jsonParser = new JSONParser();
        phoneNumber = getArguments().getString("phone_number");
        dialog = new ProgressDialog(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new SendSMS().execute();

        TextView upperText = (TextView) view.findViewById(R.id.fragment_verification_your_number);
        String upperTextString = getActivity().getResources().getString(R.string.send_code_text_first_part) + " "
                + phoneNumber + " " + getActivity().getResources().getString(R.string.send_code_text_second_part);
        upperText.setText(upperTextString);

        smsCode = (EditText) view.findViewById(R.id.fragment_verification_sms_code);

        Button changePhoneNumber = (Button) view.findViewById(R.id.fragment_verification_change_phone_number_button);
        changePhoneNumber.setOnClickListener(this);

        Button replyCode = (Button) view.findViewById(R.id.fragment_verification_replay_code_button);
        replyCode.setOnClickListener(this);

        Button checkCode = (Button) view.findViewById(R.id.fragment_verification_check_phone_number_button);
        checkCode.setOnClickListener(this);
    }

    public static VerificationFragment newInstance(String phoneNumber) {
        VerificationFragment fragment = new VerificationFragment();
        Bundle args = new Bundle();
        args.putString("phone_number", phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_verification_change_phone_number_button) {
            ((BaseActivity) getActivity()).replaceFragment(R.id.activity_login_container, LoginFragment.returnInstance(phoneNumber.substring(2)));
        } else if (v.getId() == R.id.fragment_verification_check_phone_number_button) {
            if (smsCode.getText().toString().equals(verificationCode)) {
                boolean isFirstTime = sp.getBoolean("first_time", true);
                if (isFirstTime) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("first_time", false);
                    editor.commit();
                }
                new RegistrationGCM().execute();
            } else {
                Toast.makeText(getActivity(), "Код не совпадает", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.fragment_verification_replay_code_button) {
            new SendSMS().execute();
        }
    }

    private class SendSMS extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Sending verification code...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phone_number", phoneNumber));

            JSONObject json = jsonParser.makeHttpRequest(URL.send_verification_sms, "POST", params);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    String code = json.getString("code");
                    Log.d("VERIFICATION (Code)", code);
                    verificationCode = code;
                } else {
                    Log.d("VERIFICATION (Code)", "Fail");
                    verificationCode = "Error";
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Toast.makeText(getActivity(), verificationCode, Toast.LENGTH_SHORT).show();
        }
    }

    private class RegistrationGCM extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Saving phone number...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getActivity());
                }
                gcmID = gcm.register(URL.PROJECT_ID);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new SavePhoneNumber().execute();
        }
    }

    private class SavePhoneNumber extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phone_number", phoneNumber));
            params.add(new BasicNameValuePair("gcm_id", gcmID));

            JSONObject json = jsonParser.makeHttpRequest(URL.save_phone_number, "POST", params);
            Log.d("VERIFICATION (HELLO)", json.toString());
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    int registered = json.getInt("registered");
                    SharedPreferences.Editor edit = sp.edit();
                    if (registered == 1) {
                        int who = json.getInt("who");
                        edit.putBoolean("registered", true);
                        edit.putInt("who", who);
                        edit.putInt("own_id", json.getInt("id"));
                        edit.putInt("city_id", json.getInt("city_id"));
                        edit.putString("name", json.getString("name"));
                        edit.putString("surname", json.getString("surname"));
                        edit.putString("photo", json.getString("photo"));
                    } else {
                        edit.putBoolean("registered", false);
                    }
                    edit.commit();
                    JSONArray cities = json.getJSONArray("cities");
                    ArrayList<City> citiesList = new ArrayList<City>();
                    for (int i = 0; i < cities.length(); i++) {
                        JSONObject object = cities.getJSONObject(i);
                        City city = new City(object.getInt("id"), object.getString("name"));
                        citiesList.add(city);
                    }
                    dbHelper.addCities(citiesList);
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("phone_number", phoneNumber);
            editor.commit();

            boolean hasRegistered = sp.getBoolean("registered", false);
            if (hasRegistered) {
                int who = sp.getInt("who", 0);
                Intent intent = null;
                if (who == 0) {
                    intent = new Intent(getActivity(), ClientActivity.class);
                } else if (who == 1) {
                    intent = new Intent(getActivity(), RealtorActivity.class);
                }
                startActivity(intent);
                getActivity().finish();
            } else {
                ((BaseActivity) getActivity()).replaceFragment(R.id.activity_login_container, ChooseRoleFragment.newInstance());
            }
            dialog.dismiss();
        }
    }
}
