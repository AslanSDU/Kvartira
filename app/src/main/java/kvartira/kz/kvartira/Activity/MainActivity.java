package kvartira.kz.kvartira.Activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import kvartira.kz.kvartira.Fragment.SplashFragment;
import kvartira.kz.kvartira.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SplashFragment fragment = new SplashFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_login_container, fragment).commit();
    }
}