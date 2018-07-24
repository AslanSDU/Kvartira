package kvartira.kz.kvartira.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class RealtorOrdersAdapter extends RecyclerView.Adapter<RealtorOrdersAdapter.OrdersViewHolder> {

    private List<Order> orders;

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
        private TextView name, address, rooms, price, notation, date, time;
        private ImageView avatar;

        public OrdersViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.realtor_orders_item_name);
            address = (TextView) view.findViewById(R.id.realtor_orders_item_address);
            rooms = (TextView) view.findViewById(R.id.realtor_orders_item_rooms);
            price = (TextView) view.findViewById(R.id.realtor_orders_item_price);
            notation = (TextView) view.findViewById(R.id.realtor_orders_item_notation);

            avatar = (ImageView) view.findViewById(R.id.realtor_orders_item_avatar);
            date = (TextView) view.findViewById(R.id.realtor_orders_item_date);
            time = (TextView) view.findViewById(R.id.realtor_orders_item_time);
        }
    }

    public RealtorOrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.realtor_orders_item, parent, false);
        return new OrdersViewHolder(item);
    }

    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.name.setText(order.getName() + " " + order.getSurname());
        holder.address.setText(order.getAddress());
        holder.rooms.setText(order.getRoom() + " комнатная");
        holder.price.setText(order.getPrice() + " тг.");
        holder.notation.setText(order.getNotation());
        //byte[] b = Base64.decode(order.getPhoto(), Base64.DEFAULT);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        //holder.avatar.setImageBitmap(bitmap);
        holder.avatar.setImageResource(R.drawable.no_photo);
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
