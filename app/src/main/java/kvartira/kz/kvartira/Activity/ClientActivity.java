package kvartira.kz.kvartira.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kvartira.kz.kvartira.Adapter.MenuListAdapter;
import kvartira.kz.kvartira.Basic.DatabaseHelper;
import kvartira.kz.kvartira.Basic.JSONParser;
import kvartira.kz.kvartira.Basic.URL;
import kvartira.kz.kvartira.Fragment.BaseFragment;
import kvartira.kz.kvartira.Fragment.ClientAllOrdersFragment;
import kvartira.kz.kvartira.Fragment.ClientMainFragment;
import kvartira.kz.kvartira.Fragment.SendComplaintFragment;
import kvartira.kz.kvartira.Fragment.SendOffersFragment;
import kvartira.kz.kvartira.Fragment.TestFragment;
import kvartira.kz.kvartira.Model.MenuListItem;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 13.12.2015.
 */
public class ClientActivity extends BaseActivity implements OnClickListener {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private RelativeLayout drawerMenu;
    private ListView menuList;

    private List<MenuListItem> menuListItems;
    private List<BaseFragment> fragmentList;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private static SharedPreferences sp;
    private static DatabaseHelper databaseHelper;

    private static BaseActivity activity;
    private static ProgressDialog dialog;
    private static JSONParser jsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        activity = this;

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        jsonParser = new JSONParser();
        databaseHelper = new DatabaseHelper(this);

        RelativeLayout profileBox = (RelativeLayout) findViewById(R.id.activity_client_profile_box);
        profileBox.setOnClickListener(this);

        ImageView profilePhoto = (ImageView) findViewById(R.id.activity_client_profile_image);
        String photo = sp.getString("photo", "");
        byte[] b = Base64.decode(photo, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        profilePhoto.setImageBitmap(getCircleBitmap(bitmap));
        profilePhoto.setOnClickListener(this);

        TextView profileName = (TextView) findViewById(R.id.activity_client_profile_name);
        profileName.setText(sp.getString("name", "") + " " + sp.getString("surname", "") + " (Клиент)");
        profileName.setOnClickListener(this);
        TextView profileNumber = (TextView) findViewById(R.id.activity_client_profile_number);
        profileNumber.setText(sp.getString("phone_number", ""));
        profileNumber.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.activity_client_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_client_drawer_layout);
        drawerMenu = (RelativeLayout) findViewById(R.id.activity_client_menu);
        menuList = (ListView) findViewById(R.id.activity_client_menu_list);

        menuListItems = new ArrayList<MenuListItem>();
        int city_id = sp.getInt("city_id", 0);
        menuListItems.add(new MenuListItem("Город (" + databaseHelper.getCity(city_id) + ")", R.mipmap.ic_launcher, false));
        menuListItems.add(new MenuListItem("Отзывы и предложения", R.mipmap.ic_launcher, false));
        menuListItems.add(new MenuListItem("Режим риелтора", R.mipmap.ic_launcher, true));
        menuListItems.add(new MenuListItem("Пригласить друга", R.mipmap.ic_launcher, false));
        menuListItems.add(new MenuListItem("Сменить аккаунт", R.mipmap.ic_launcher, false));
        //menuListItems.add(new MenuListItem("Пожаловаться", R.mipmap.ic_launcher, false));
        //menuListItems.add(new MenuListItem("Настройки", R.mipmap.ic_launcher, false));
        //menuListItems.add(new MenuListItem("Помощь", R.mipmap.ic_launcher, false));

        MenuListAdapter adapter = new MenuListAdapter(getApplicationContext(), R.layout.menu_list_item, menuListItems);
        menuList.setAdapter(adapter);

        fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(new ClientMainFragment());
        fragmentList.add(new SendOffersFragment());
        fragmentList.add(new TestFragment());
        fragmentList.add(new TestFragment());
        fragmentList.add(new TestFragment());
        //fragmentList.add(new SendComplaintFragment());
        //fragmentList.add(new TestFragment());
        //fragmentList.add(new TestFragment());

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_client_container, fragmentList.get(0)).commit();

        toolbar.setTitle(menuListItems.get(0).getTitle());
        menuList.setItemChecked(0, true);
        drawerLayout.closeDrawer(drawerMenu);

        menuList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.invite));
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent, getString(R.string.share)));
                    menuList.setItemChecked(0, true);
                    drawerLayout.closeDrawer(drawerMenu);
                } else if (position == 4) {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putBoolean("first_time", true);
                    edit.putBoolean("registered", false);
                    edit.remove("who");
                    edit.remove("own_id");
                    edit.remove("city_id");
                    edit.remove("name");
                    edit.remove("surname");
                    edit.remove("photo");
                    edit.remove("phone_number");
                    edit.commit();
                    Intent intent = new Intent(ClientActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_client_container, fragmentList.get(position)).commit();

                    toolbar.setTitle(menuListItems.get(position).getTitle());
                    menuList.setItemChecked(position, true);
                    drawerLayout.closeDrawer(drawerMenu);
                }
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = Color.RED;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public static void change() {
        new ChangeRole().execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_client_profile_image ||
                v.getId() == R.id.activity_client_profile_name ||
                v.getId() == R.id.activity_client_profile_number ||
                v.getId() == R.id.activity_client_profile_box) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    private static class ChangeRole extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Идет обработка данных...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Integer doInBackground(Void... args) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            int id = sp.getInt("own_id", 0);
            params.add(new BasicNameValuePair("user_id", id + ""));
            params.add(new BasicNameValuePair("role", "1"));

            JSONObject json = jsonParser.makeHttpRequest(URL.switch_role, "POST", params);
            try {
                int success = json.getInt("success");
                return success;
            } catch (Exception e) {
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            SharedPreferences.Editor editor = sp.edit();
            if (integer == 1) {
                editor.putInt("who", 1);
                editor.commit();
                Intent intent = new Intent(activity, RealtorActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }
}
