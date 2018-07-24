package kvartira.kz.kvartira.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 17.12.2015.
 */
public class PhoneCodeAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] countries;

    public PhoneCodeAdapter(Context context, String[] countries) {
        super(context, R.layout.phone_code_item, countries);
        this.context = context;
        this.countries = countries;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.phone_code_item, null);

        TextView text = (TextView) v.findViewById(R.id.phone_code_item_code);
        text.setText(countries[position]);

        return v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.phone_code_item, null);

        TextView text = (TextView) v.findViewById(R.id.phone_code_item_code);
        text.setText(countries[position]);

        return v;
    }
}
