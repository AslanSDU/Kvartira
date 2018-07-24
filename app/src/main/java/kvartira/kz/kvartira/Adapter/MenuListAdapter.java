package kvartira.kz.kvartira.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import kvartira.kz.kvartira.Activity.ClientActivity;
import kvartira.kz.kvartira.Activity.RealtorActivity;
import kvartira.kz.kvartira.Model.MenuListItem;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 13.12.2015.
 */
public class MenuListAdapter extends ArrayAdapter<MenuListItem> {

    private int resource;
    private List<MenuListItem> objects;
    private LayoutInflater inflater;
    private SharedPreferences sp;

    public MenuListAdapter(Context context, int resource, List<MenuListItem> objects) {
        super(context, resource, objects);

        this.resource = resource;
        this.objects = objects;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = inflater.inflate(resource, null);

        TextView title = (TextView) v.findViewById(R.id.menu_list_item_title);
        ImageView icon = (ImageView) v.findViewById(R.id.menu_list_item_image);
        Switch switcher = (Switch) v.findViewById(R.id.menu_list_item_switch);
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int who = sp.getInt("who", 0);
                if (who == 0) {
                    ClientActivity.change();
                } else if (who == 1) {
                    RealtorActivity.change();
                }
            }
        });

        title.setText(objects.get(position).getTitle());
        icon.setImageResource(objects.get(position).getIcon());
        if (objects.get(position).getToggle()) {
            switcher.setVisibility(View.VISIBLE);
        } else {
            switcher.setVisibility(View.GONE);
        }
        return v;
    }
}
