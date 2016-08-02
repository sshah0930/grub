package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.R;

public class CreateActivityFragment extends Fragment {

    BaseActivity activity;

    public CreateActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_activity, container, false);

        activity = (BaseActivity) getActivity();
        activity.myToolbar.getMenu().clear();
        activity.myToolbar.setTitle("Create Activity");
        activity.childScreenLayout();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            activity.fragmentManager.popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

}
