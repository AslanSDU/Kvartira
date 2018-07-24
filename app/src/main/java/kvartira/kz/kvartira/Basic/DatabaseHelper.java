package kvartira.kz.kvartira.Basic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import kvartira.kz.kvartira.Model.City;
import kvartira.kz.kvartira.Model.Order;

/**
 * Created by Aslan on 06.03.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shanyrak";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE cities(id INTEGER, name VARCHAR(30))");
            db.execSQL("CREATE TABLE ordering(id INTEGER, name VARCHAR(20), surname VARCHAR(20)," +
                    "photo TEXT, phone_number VARCHAR(20), city VARCHAR(30), address TEXT, room_number INTEGER," +
                    "price INTEGER, notation TEXT, residence INTEGER, date DATE, time TIME)");
            db.execSQL("CREATE TABLE myorder(id INTEGER, name VARCHAR(20), surname VARCHAR(20)," +
                    "photo TEXT, phone_number VARCHAR(20), city VARCHAR(30), address TEXT, room_number INTEGER," +
                    "price INTEGER, notation TEXT, residence INTEGER, date DATE, time TIME)");
            db.execSQL("CREATE TABLE flats(id INTEGER, name VARCHAR(20), surname VARCHAR(20)," +
                    "photo TEXT, phone_number VARCHAR(20), city VARCHAR(30), address TEXT, room_number INTEGER," +
                    "price INTEGER, residence INTEGER, floor INTEGER, floors INTEGER, map TEXT, type VARCHAR(30)," +
                    "description TEXT, data DATE, time TIME)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            //db.execSQL("DROP TABLE IF EXISTS cities");
            //db.execSQL("DROP TABLE IF EXISTS ordering");
            //db.execSQL("DROP TABLE IF EXISTS myorder");
            onCreate(db);
        }
    }

    public void addCities(ArrayList<City> city) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cities", null, null);

        for (City i : city) {
            ContentValues cv = new ContentValues();
            cv.put("id", i.getId());
            cv.put("name", i.getName());
            db.insert("cities", null, cv);
        }
    }

    public String getCity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM cities WHERE id=" + id, null);
        if (c != null) {
            c.moveToFirst();
        }
        String s = c.getString(c.getColumnIndex("name"));
        c.close();
        return s;
    }

    public ArrayList<City> getCities() {
        ArrayList<City> cities = new ArrayList<City>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM cities ORDER BY name ASC", null);
        if (c.moveToFirst()) {
            do {
                City city = new City(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")));
                cities.add(city);
            } while (c.moveToNext());
        }
        c.close();
        return cities;
    }

    public int getCitiesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM cities", null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public void addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DATABASE (INSERT)", "Hello");

        ContentValues cv = new ContentValues();
        cv.put("id", order.getId());
        cv.put("name", order.getName());
        cv.put("surname", order.getSurname());
        cv.put("photo", order.getPhoto());
        cv.put("phone_number", order.getPhoneNumber());
        cv.put("city", order.getCity());
        cv.put("address", order.getAddress());
        cv.put("room_number", order.getRoom());
        cv.put("price", order.getPrice());
        cv.put("notation", order.getNotation());
        cv.put("residence", order.getResidence());
        cv.put("date", order.getDate());
        cv.put("time", order.getTime());

        db.insert("ordering", null, cv);
    }

    public ArrayList<Order> getOrders(int type) {
        ArrayList<Order> orders = new ArrayList<Order>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;

        if (type == 0) {
            c = db.rawQuery("SELECT * FROM ordering ORDER BY id DESC", null);
        } else {
            c = db.rawQuery("SELECT * FROM ordering WHERE residence=" + type + " ORDER BY id DESC", null);
        }

        if (c.moveToFirst()) {
            do {
                Order order = new Order(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")),
                        c.getString(c.getColumnIndex("surname")), c.getString(c.getColumnIndex("photo")), c.getString(c.getColumnIndex("city")),
                        c.getString(c.getColumnIndex("phone_number")), c.getString(c.getColumnIndex("address")),
                        c.getInt(c.getColumnIndex("room_number")), c.getInt(c.getColumnIndex("price")), c.getString(c.getColumnIndex("notation")),
                        c.getInt(c.getColumnIndex("residence")), c.getString(c.getColumnIndex("date")), c.getString(c.getColumnIndex("time")));
                orders.add(order);
            } while (c.moveToNext());
        }
        c.close();
        return orders;
    }

    public int getOrdersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ordering", null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public void deleteOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ordering", "id = " + id, null);
    }

    public void deleteAllOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ordering", null, null);
    }

    public void addMyOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DATABASE (INSERT)", "Hello");

        ContentValues cv = new ContentValues();
        cv.put("id", order.getId());
        cv.put("name", order.getName());
        cv.put("surname", order.getSurname());
        cv.put("photo", order.getPhoto());
        cv.put("phone_number", order.getPhoneNumber());
        cv.put("city", order.getCity());
        cv.put("address", order.getAddress());
        cv.put("room_number", order.getRoom());
        cv.put("price", order.getPrice());
        cv.put("notation", order.getNotation());
        cv.put("residence", order.getResidence());
        cv.put("date", order.getDate());
        cv.put("time", order.getTime());

        db.insert("myorder", null, cv);
    }

    public ArrayList<Order> getMyOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();
        Log.d("DATABASE (SELECT)", "Hello");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM myorder ORDER BY id DESC", null);
        if (c.moveToFirst()) {
            do {
                Order order = new Order(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")),
                        c.getString(c.getColumnIndex("surname")), c.getString(c.getColumnIndex("photo")), c.getString(c.getColumnIndex("city")),
                        c.getString(c.getColumnIndex("phone_number")), c.getString(c.getColumnIndex("address")),
                        c.getInt(c.getColumnIndex("room_number")), c.getInt(c.getColumnIndex("price")), c.getString(c.getColumnIndex("notation")),
                        c.getInt(c.getColumnIndex("residence")), c.getString(c.getColumnIndex("date")), c.getString(c.getColumnIndex("time")));
                orders.add(order);
            } while (c.moveToNext());
        }
        c.close();
        return orders;
    }

    public int getMyOrdersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM myorder", null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public void deleteMyOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("myorder", "id = " + id, null);
    }

    public void addNewFlat(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DATABASE (INSERT)", "Hello");

        ContentValues cv = new ContentValues();
        cv.put("id", order.getId());
        cv.put("name", order.getName());
        cv.put("surname", order.getSurname());
        cv.put("photo", order.getPhoto());
        cv.put("phone_number", order.getPhoneNumber());
        cv.put("city", order.getCity());
        cv.put("address", order.getAddress());
        cv.put("room_number", order.getRoom());
        cv.put("price", order.getPrice());
        cv.put("notation", order.getNotation());
        cv.put("residence", order.getResidence());
        cv.put("date", order.getDate());
        cv.put("time", order.getTime());

        db.insert("myorder", null, cv);
    }
}
