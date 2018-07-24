package kvartira.kz.kvartira.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 14.08.2016.
 */
public class SendOffersFragment extends BaseFragment implements OnClickListener {

    private EditText message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_send_offers, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        message = (EditText) view.findViewById(R.id.fragment_send_offers_message);

        Button send = (Button) view.findViewById(R.id.fragment_send_offers_send);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_send_offers_send) {

        }
    }

    private class Send extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
}
