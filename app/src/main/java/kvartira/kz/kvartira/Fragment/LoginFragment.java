package kvartira.kz.kvartira.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import kvartira.kz.kvartira.Activity.BaseActivity;
import kvartira.kz.kvartira.Adapter.PhoneCodeAdapter;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 05.12.2015.
 */
public class LoginFragment extends BaseFragment implements OnClickListener {

    private EditText phoneNumber;
    private String phoneNumberText;
    private Spinner phoneCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumberText = getArguments().getString("phone_number");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        phoneNumber = (EditText) view.findViewById(R.id.fragment_login_phone_number);
        phoneNumber.setText(phoneNumberText);

        phoneCode = (Spinner) view.findViewById(R.id.fragment_login_phone_number_code);
        PhoneCodeAdapter adapter = new PhoneCodeAdapter(getActivity(), new String[]{"+7"});
        phoneCode.setAdapter(adapter);

        Button enter = (Button) view.findViewById(R.id.fragment_login_enter_button);
        enter.setOnClickListener(this);
    }

    public static LoginFragment returnInstance(String phoneNumber) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString("phone_number", phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_login_enter_button) {
            String phoneNumberText = phoneNumber.getText().toString();
            if (phoneNumberText.length() < 10) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.toast, null);
                ImageView toastImage = (ImageView) view.findViewById(R.id.toast_image);
                toastImage.setImageResource(R.drawable.alert);
                TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                toastText.setText(getActivity().getResources().getString(R.string.number_not_complete));
                Toast toast = new Toast(getActivity());
                toast.setView(view);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            } else {
                String phoneToSend = phoneCode.getSelectedItem().toString() + phoneNumberText;
                ((BaseActivity) getActivity()).replaceFragment(R.id.activity_login_container, VerificationFragment.newInstance(phoneToSend));
            }
        }
    }
}
