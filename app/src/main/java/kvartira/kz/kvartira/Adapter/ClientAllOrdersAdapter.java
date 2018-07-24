package kvartira.kz.kvartira.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

import kvartira.kz.kvartira.Model.Order;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 08.04.2016.
 */
public class ClientAllOrdersAdapter extends RecyclerView.Adapter<ClientAllOrdersAdapter.AllOrdersViewHolder> {

    private List<Order> orders;

    public class AllOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView address, rooms, price, notation, date, time;

        public AllOrdersViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.client_all_orders_item_address);
            rooms = (TextView) view.findViewById(R.id.client_all_orders_item_rooms);
            price = (TextView) view.findViewById(R.id.client_all_orders_item_price);
            notation = (TextView) view.findViewById(R.id.client_all_orders_item_notation);
            date = (TextView) view.findViewById(R.id.client_all_orders_item_date);
            time = (TextView) view.findViewById(R.id.client_all_orders_item_time);
        }
    }

    public ClientAllOrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public AllOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_all_orders_item, parent, false);
        return new AllOrdersViewHolder(item);
    }

    @Override
    public void onBindViewHolder(AllOrdersViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.address.setText(order.getAddress());
        holder.rooms.setText(order.getRoom() + " комнатная");
        holder.price.setText(order.getPrice() + " тг.");
        holder.notation.setText(order.getNotation());
        if (order.getDate().length() > 0 && order.getTime().length() > 0) {
            Date d = Date.valueOf(order.getDate());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String s = dateFormat.format(d);
            holder.date.setText(s);
            Time t = Time.valueOf(order.getTime());
            dateFormat = new SimpleDateFormat("HH:mm");
            s = dateFormat.format(t);
            holder.time.setText(s);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
