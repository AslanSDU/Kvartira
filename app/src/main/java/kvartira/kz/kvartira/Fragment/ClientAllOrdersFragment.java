package kvartira.kz.kvartira.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kvartira.kz.kvartira.Activity.ClientOrderActivity;
import kvartira.kz.kvartira.Adapter.ClientAllOrdersAdapter;
import kvartira.kz.kvartira.Basic.ClickListener;
import kvartira.kz.kvartira.Basic.DatabaseHelper;
import kvartira.kz.kvartira.Basic.DividerItemDecoration;
import kvartira.kz.kvartira.Basic.JSONParser;
import kvartira.kz.kvartira.Basic.RecyclerTouchListener;
import kvartira.kz.kvartira.Basic.URL;
import kvartira.kz.kvartira.Model.Order;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 22.03.2016.
 */
public class ClientAllOrdersFragment extends BaseFragment {

    private DatabaseHelper databaseHelper;
    private ClientAllOrdersAdapter adapter;

    private List<Order> list = new ArrayList<Order>();

    private ProgressDialog dialog;
    private JSONParser jsonParser;
    private int orderPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        jsonParser = new JSONParser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_all_orders, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView orders = (RecyclerView) view.findViewById(R.id.fragment_client_all_orders_list);
        adapter = new ClientAllOrdersAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        orders.setLayoutManager(layoutManager);
        orders.setItemAnimator(new DefaultItemAnimator());
        orders.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        orders.setAdapter(adapter);
        orders.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), orders, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DatePickerTheme);
                builder.setTitle("Отменить заказ");
                builder.setMessage("Вы уверены что хотите отменить заказ?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderPosition = position;
                        new Delete().execute();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        FloatingActionButton newOrder = (FloatingActionButton) view.findViewById(R.id.fragment_client_all_orders_new);
        newOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClientOrderActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        updateList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                updateList();
            }
        }
    }

    private void updateList() {
        ArrayList<Order> order = databaseHelper.getMyOrders();
        list.clear();
        for (Order o : order) {
            list.add(o);
        }
        adapter.notifyDataSetChanged();
    }

    private class Delete extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Отменяем ваш заказ");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("order_id", list.get(orderPosition).getId() + ""));

            JSONObject json = jsonParser.makeHttpRequest(URL.delete_order, "POST", params);

            try {
                int success = json.getInt("success");
                if (success == 1) {
                    databaseHelper.deleteMyOrder(list.get(orderPosition).getId());
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateList();
            dialog.dismiss();
        }
    }
}
