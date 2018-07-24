package kvartira.kz.kvartira.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.Button;

import kvartira.kz.kvartira.Activity.RegistrationActivity;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 08.12.2015.
 */
public class ChooseRoleFragment extends BaseFragment implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_role, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button client = (Button) view.findViewById(R.id.fragment_choose_role_client);
        client.setOnClickListener(this);
        Button realtor = (Button) view.findViewById(R.id.fragment_choose_role_realtor);
        realtor.setOnClickListener(this);
    }

    public static ChooseRoleFragment newInstance() {
        ChooseRoleFragment fragment = new ChooseRoleFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), RegistrationActivity.class);
        if (v.getId() == R.id.fragment_choose_role_client) {
            intent.putExtra("who", 0);
        } else if (v.getId() == R.id.fragment_choose_role_realtor) {
            intent.putExtra("who", 1);
        }
        startActivity(intent);
        getActivity().finish();
    }
}
