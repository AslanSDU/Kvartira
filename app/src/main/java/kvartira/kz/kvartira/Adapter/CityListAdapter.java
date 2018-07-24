package kvartira.kz.kvartira.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kvartira.kz.kvartira.Model.City;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 15.03.2016.
 */
public class CityListAdapter extends ArrayAdapter<City> {

    private int resourse;
    private List<City> objects;
    private LayoutInflater inflater;

    public CityListAdapter(Context context, int resourse, List<City> objects) {
        super(context, resourse, objects);

        this.resourse = resourse;
        this.objects = objects;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(resourse, null);

        TextView city = (TextView) v.findViewById(R.id.city_list_item_text);
        city.setText(objects.get(position).getName());

        return v;
    }
}
