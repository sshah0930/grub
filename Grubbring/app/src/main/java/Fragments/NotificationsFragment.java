package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.R;

public class NotificationsFragment extends Fragment {

    BaseActivity activity;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        activity = (BaseActivity) getActivity();
        activity.myToolbar.getMenu().clear();
        activity.myToolbar.setTitle("Notifications");
        activity.coreScreenLayout();

        return view;
    }

}
