package kvartira.kz.kvartira.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import kvartira.kz.kvartira.Adapter.RealtorOrdersAdapter;
import kvartira.kz.kvartira.Basic.ClickListener;
import kvartira.kz.kvartira.Basic.DatabaseHelper;
import kvartira.kz.kvartira.Basic.DividerItemDecoration;
import kvartira.kz.kvartira.Basic.RecyclerTouchListener;
import kvartira.kz.kvartira.Model.Order;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 08.04.2016.
 */
public class RealtorOrdersFragment extends BaseFragment implements OnClickListener {

    private DatabaseHelper databaseHelper;
    private RealtorOrdersAdapter adapter;

    private ArrayList<Order> ordersList = new ArrayList<Order>();
    private CheckBox daily, longterm;

    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtor_orders, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, 100);
            }
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_realtor_orders_list);
        adapter = new RealtorOrdersAdapter(ordersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent calling = new Intent(Intent.ACTION_CALL);
                    calling.setData(Uri.parse("tel:" + ordersList.get(position).getPhoneNumber()));
                    startActivity(calling);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        boolean daily_rent = sp.getBoolean("daily_rent", true);
        daily = (CheckBox) view.findViewById(R.id.fragment_realtor_orders_daily);
        daily.setChecked(daily_rent);
        daily.setOnClickListener(this);
        boolean longterm_rent = sp.getBoolean("longterm_rent", true);
        longterm = (CheckBox) view.findViewById(R.id.fragment_realtor_orders_longterm);
        longterm.setChecked(longterm_rent);
        longterm.setOnClickListener(this);

        if (daily.isChecked() && longterm.isChecked()) {
            updateList(0);
        } else if (daily.isChecked() && !longterm.isChecked()) {
            updateList(1);
        } else if (!daily.isChecked() && longterm.isChecked()) {
            updateList(2);
        } else if (!daily.isChecked() && !longterm.isChecked()) {
            ordersList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_realtor_orders_daily) {
            if (daily.isChecked() && longterm.isChecked()) {
                updateList(0);
            } else if (daily.isChecked() && !longterm.isChecked()) {
                updateList(1);
            } else if (!daily.isChecked() && longterm.isChecked()) {
                updateList(2);
            } else if (!daily.isChecked() && !longterm.isChecked()) {
                ordersList.clear();
                adapter.notifyDataSetChanged();
            }
            SharedPreferences.Editor editor = sp.edit();
            if (daily.isChecked()) {
                editor.putBoolean("daily_rent", true);
            } else {
                editor.putBoolean("daily_rent", false);
            }
            editor.commit();
        } else if (v.getId() == R.id.fragment_realtor_orders_longterm) {
            if (daily.isChecked() && longterm.isChecked()) {
                updateList(0);
            } else if (daily.isChecked() && !longterm.isChecked()) {
                updateList(1);
            } else if (!daily.isChecked() && longterm.isChecked()) {
                updateList(2);
            } else if (!daily.isChecked() && !longterm.isChecked()) {
                ordersList.clear();
                adapter.notifyDataSetChanged();
            }
            SharedPreferences.Editor editor = sp.edit();
            if (longterm.isChecked()) {
                editor.putBoolean("longterm_rent", true);
            } else {
                editor.putBoolean("longterm_rent", false);
            }
            editor.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", "Success");
                } else {
                    Log.d("PERMISSION", "Denied");
                }
                return;
        }
    }

    public void updateList(int type) {
        ArrayList<Order> list = databaseHelper.getOrders(type);
        Log.d("LIST SIZE", list.size() + "");
        ordersList.clear();
        for (Order o : list) {
            ordersList.add(o);
        }
        adapter.notifyDataSetChanged();
    }
}
