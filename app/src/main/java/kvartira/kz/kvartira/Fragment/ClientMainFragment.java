package kvartira.kz.kvartira.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.*;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

import kvartira.kz.kvartira.Adapter.ViewPagerAdapter;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 13.12.2015.
 */
public class ClientMainFragment extends BaseFragment implements OnPageChangeListener, OnTabChangeListener {

    private TabHost tabHost;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private View v;
    private String[] tabNames = {"Снять", "Квартиры"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_client_main, container, false);

        this.initializeTabHost();

        this.initializeViewPager();

        return v;
    }

    private void initializeViewPager() {
        List<Fragment> fragments = new Vector<Fragment>();

        fragments.add(new ClientAllOrdersFragment());
        fragments.add(new TestFragment());

        adapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        viewPager = (ViewPager) v.findViewById(R.id.fragment_client_main_view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    private void initializeTabHost() {
        tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup();

        for (int i = 0; i < tabNames.length; i++) {
            View tabView = createTabView(tabHost.getContext(), tabNames[i]);
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabView);
            tabSpec.setContent(new FakeContent(getActivity()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);
    }

    private static View createTabView(Context context, String text) {
        View v = LayoutInflater.from(context).inflate(R.layout.tabs_background, null);
        TextView textView = (TextView) v.findViewById(R.id.tabs_background_text);
        textView.setText(text);
        return v;
    }

    @Override
    public void onTabChanged(String tabId) {
        int pos = tabHost.getCurrentTab();
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    private class FakeContent implements TabContentFactory {
        private final Context context;

        public FakeContent(Context context) {
            this.context = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(context);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }
    }
}
