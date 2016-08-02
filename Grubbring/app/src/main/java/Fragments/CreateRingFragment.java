package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.R;

import Constants.FragmentTags;
import Fragments.MyRingsFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRingFragment extends Fragment {

    BaseActivity activity;

    public CreateRingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_ring, container, false);

        activity = (BaseActivity) getActivity();
        activity.myToolbar.getMenu().clear();
        activity.myToolbar.setTitle("Create Ring");
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
