package kvartira.kz.kvartira.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kvartira.kz.kvartira.Activity.RealtorNewFlatActivity;
import kvartira.kz.kvartira.Adapter.RealtorAllFlatsAdapter;
import kvartira.kz.kvartira.Basic.DividerItemDecoration;
import kvartira.kz.kvartira.Model.Order;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 05.02.2017.
 */
public class RealtorAllFlatsFragment extends BaseFragment {

    private RealtorAllFlatsAdapter adapter;

    private List<Order> list = new ArrayList<Order>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_realtor_all_flats, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView flats = (RecyclerView) view.findViewById(R.id.fragment_realtor_all_flats_list);
        adapter = new RealtorAllFlatsAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        flats.setLayoutManager(layoutManager);
        flats.setItemAnimator(new DefaultItemAnimator());
        flats.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        flats.setAdapter(adapter);

        FloatingActionButton addNewFlat = (FloatingActionButton) view.findViewById(R.id.fragment_realtor_all_flats_new);
        addNewFlat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RealtorNewFlatActivity.class);
                startActivityForResult(intent, 109);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 109) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }
}
