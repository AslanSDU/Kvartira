package kvartira.kz.kvartira.Activity;

import android.support.v7.app.AppCompatActivity;

import kvartira.kz.kvartira.Fragment.BaseFragment;

/**
 * Created by Aslan on 08.12.2015.
 */
public class BaseActivity extends AppCompatActivity {

    public void replaceFragment(int container, BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();
    }
}
